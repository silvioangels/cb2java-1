package net.sf.cb2java.copybook;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.sf.cb2java.copybook.data.Data;
import net.sf.cb2java.copybook.data.DecimalData;
import net.sf.cb2java.copybook.data.IntegerData;

public class SignedSeparate extends Numeric
{
    SignedSeparate(String name, int level, int occurs, String pic)
    {
        super(name, level, occurs, pic);
    }
    
    public SignedSeparate(int length, int decimalPlaces, boolean signed)
    {
        super(length, decimalPlaces, signed);
    }
    
    static int getLength(String pic)
    {
        int length = 0;
        
        for (int i = 0; i < pic.length(); i++) {
            char c = pic.charAt(i);
            
            if (c == '(') {
                int pos = pic.indexOf(')', i);
                int times = Integer.parseInt(pic.substring(i + 1, pos));
                i = pos;
                length += times;
            }
        }
        
        return length;
    }
    
    static int getScale(String pic, int length)
    {
        int position = 0;
        pic = pic.toUpperCase();
        
        for (int i = 0; i < pic.length(); i++) {
            char c = pic.charAt(i);
            
            if (c == '(') {
                int pos = pic.indexOf(')', i);
                int times = Integer.parseInt(pic.substring(i + 1, pos));
                i = pos;
                position += times;
            } else if (c == 'V') {
                return length - position;
            }
        }
        
        return 0;
    }
    
    Data parse(byte[] bytes)
    {
        String s = getString(bytes);
        
        char sign;
        
        if (getSignPosition() == LEADING) {
            sign = s.charAt(0);
        } else if (getSignPosition() == TRAILING) {
            sign = s.charAt(s.length() - 1);
            s = sign + s.substring(0, s.length() - 1);
        } else {
            throw new RuntimeException("caused only by bug");
        }
        
        if (sign != '+' && sign != '-') throw new IllegalArgumentException(getName() + " is sign separate "
            + (getSignPosition() == LEADING ? "leading" : "trailing") + " but no sign was found on value " + s);
        
        if (sign == '+') s = s.substring(1);

        BigInteger big = new BigInteger(s);
        Data data = create();
        
        if (data instanceof DecimalData) {
            DecimalData dData = (DecimalData) data;
            
            dData.setValue(new BigDecimal(big, decimalPlaces()));
            
            return data;
        } else {
            IntegerData iData = (IntegerData) data;
            
            iData.setValue(big);
            
            return data;
        }
    }
    
    public byte[] toBytes(Object data)
    {
        String s;
        boolean positive;
        
        if (data == null) {
            positive = true;
            s = "";
        } else {
            BigInteger bigI = getUnscaled(data);
        
            positive = ZERO.unscaledValue().compareTo(bigI) < 0;
            
            s = bigI.toString();
        }
        
        char sign = positive ? '+' : '-';
        
        s = getValue().fillString(s, getLength() - 1, Value.LEFT);
        
        if (getSignPosition() == TRAILING) {
            s += sign;
        } else if (getSignPosition() == LEADING) {
            s = sign + s;
        } else {
            throw new RuntimeException("caused only by bug");
        }
        
        return getBytes(s);
    }

    public int digits()
    {
        return getLength() - 1;
    }
}