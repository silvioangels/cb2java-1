package net.sf.cb2java.copybook;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.sf.cb2java.copybook.data.Data;
import net.sf.cb2java.copybook.data.DecimalData;
import net.sf.cb2java.copybook.data.IntegerData;

/**
 * class that represents decimal (zoned) data types
 *
 * @author James Watson
 */
public class Decimal extends Numeric
{
    Decimal(String name, int level, int occurs, String pic)
    {
        super(name, level, occurs, pic);
    }
    
    private char getChar(boolean positive, char first)
    {
        if (!signed()) {
            return first;
        } else if (positive) {
            switch(first) {
                case '0': return '{';
                case '1': return 'A';
                case '2': return 'B';
                case '3': return 'C';
                case '4': return 'D';
                case '5': return 'E';
                case '6': return 'F';
                case '7': return 'G';
                case '8': return 'H';
                case '9': return 'I';
            }
        } else {
            switch(first) {
                case '9': return 'R';
                case '8': return 'Q';
                case '7': return 'P';
                case '6': return 'O';
                case '5': return 'N';
                case '4': return 'M';
                case '3': return 'L';
                case '2': return 'K';
                case '1': return 'J';
                case '0': return '}';
            }
        }
        
        throw new IllegalArgumentException("invalid number: " + first);
    }
    
    private boolean isPositive(char first)
    {
        if (!signed()) {
            return true;
        } else {
            switch(first) {
                case '{':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                    return true;
                case '0':
                case '}':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R': 
                    return false;
            }
        }
        
        throw new IllegalArgumentException("invalid char: " + first);
    }
    
    private char getNumber(char first)
    {
        if (!signed()) {
            return first;
        } else {
            switch(first) {
                case 'R': 
                case 'I': 
                    return '9';
                case 'Q':
                case 'H': 
                    return '8';
                case 'P': 
                case 'G': 
                    return '7';
                case 'O': 
                case 'F': 
                    return '6';
                case 'N': 
                case 'E': 
                    return '5';
                case 'M': 
                case 'D': 
                    return '4';
                case 'L': 
                case 'C': 
                    return '3';
                case 'K': 
                case 'B': 
                    return '2';
                case 'J': 
                case 'A': 
                    return '1';
                case '0': 
                case '}': 
                case '{': 
                    return '0';
            }
        }
        
        throw new IllegalArgumentException("invalid char: " + first);
    }
    
    Data parse(byte[] bytes)
    {
        String input = getString(bytes).trim();
        String s;
        
        if (getSignPosition() == LEADING) {
            char c = input.charAt(0); 
            s = (isPositive(c) ? "" : "-") + getNumber(c) + input.toString().substring(1); 
        } else {
            int last = input.length() - 1; 
            char c = input.charAt(last); 
            s = (isPositive(c) ? "" : "-") + input.toString().substring(0, last-1) + getNumber(c);
        }
        
        BigInteger big = new BigInteger(s);
        Data data = create();
        
        if (data instanceof DecimalData) {
            DecimalData dData = (DecimalData) data;
            BigDecimal bigD = new BigDecimal(big, decimalPlaces());
            
            dData.setValue(bigD);
            
            return data;
        } else {
            IntegerData iData = (IntegerData) data;
            
            iData.setValue(big);
            
            return data;
        }
    }
    
    public byte[] toBytes(Object data)
    {
        if (data == null) {
            return getValue().fill(getLength());
        } 

        BigInteger bigI = getUnscaled(data);
        boolean positive;
        
        if (BigDecimal.ZERO.unscaledValue().compareTo(bigI) > 0) {
            bigI = bigI.abs();
            positive = false;
        } else {
            positive = true;
        }
        
        String s = bigI.toString();
        
        s = getValue().fillString(s, getLength(), Value.LEFT);
        
        return getBytes(getChar(positive, s.charAt(0)) + s.substring(1));
    }

    public int digits()
    {
        return getLength();
    }
}