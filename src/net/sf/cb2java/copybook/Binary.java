package net.sf.cb2java.copybook;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.sf.cb2java.copybook.data.Data;
import net.sf.cb2java.copybook.data.DecimalData;
import net.sf.cb2java.copybook.data.IntegerData;

/**
 * class that represents binary data types 
 * 
 * @author James Watson
 */
public class Binary extends Numeric
{
    final int digits;
    final int length;
    
    protected Binary(String name, int level, int occurs, String picture)
    {
        super(name, level, occurs, picture);
        digits = super.getLength();
        length = getLength(digits);
    }
    
    public Binary(String name, String picture)
    {
        super(name, 0, 1, picture);
        digits = super.getLength();
        length = getLength(digits);
    }
    
    public Binary(String picture)
    {
        super("", 0, 1, picture);
        digits = super.getLength();
        length = getLength(digits);
    }
    
    public Binary(String name, int length, int decimalPlaces, boolean signed)
    {
        super(name, length, decimalPlaces, signed, null);
        digits = super.getLength();
        this.length = getLength(digits);
    }
    
    public Binary(int length, int decimalPlaces, boolean signed)
    {
        super("", length, decimalPlaces, signed, null);
        digits = super.getLength();
        this.length = getLength(digits);
    }
    
    protected static final int getLength(int digits)
    {   
        if (1 <= digits && digits <= 4) {
            return 2;
        } else if (5 <= digits && digits <= 9) {
            return 4;
        } else if (10 <= digits && digits <= 18) {
            return 8;
        } else {
            throw new IllegalArgumentException("invalid numeric length");
        }
    }
    
    public int getLength()
    {
        return length;
    }

    public int digits()
    {
        return digits;
    }
        
    public Data parse(byte[] input)
    {
//        display(input);
        
        BigInteger bigI = new BigInteger(input);
        
        Data data = create();
        
        if (data instanceof DecimalData) {
            DecimalData dData = (DecimalData) data;
            
            BigDecimal bigD = new BigDecimal(bigI, decimalPlaces());
            
            dData.setValue(bigD);
            
            return data;
        } else {
            IntegerData iData = (IntegerData) data;
            
            iData.setValue(bigI);
            
            return data;
        }
    }

//    private static void display(byte[] bytes)
//    {
//        for (int i = 0; i < bytes.length; i++) {
//            byte b = bytes[i];
//            String s = "0" + Integer.toHexString(0xFF & b);
//            System.out.print(s.toUpperCase().substring(s.length() - 2));
//            System.out.print(' ');
//        }
//    }
    
    public byte[] toBytes(Object data)
    {
        BigInteger bigI;
        
        if (data == null) {
            bigI = BigInteger.ZERO;
        } else {
            bigI = getUnscaled(data);
        }
        
//        display(copybook.values.LOW_VALUES.fill(bigI.toByteArray(), length(), Value.LEFT));
        
        return getSettings().getValues().LOW_VALUES.fill(bigI.toByteArray(), getLength(), Value.LEFT);
    }
    
    public static byte[] reverse(byte[] input)
    {
        final int length = input.length;
        byte[] output = new byte[length];
        
        for (int i = 0; i < length; i++) {
            output[length - (i + 1)] = input[i];
        }
        
        return output;
    }
    
    public static class Native extends Binary
    {
        protected Native(String name, int level, int occurs, String picture)
        {
            super(name, level, occurs, picture);
        }
        
        public Native(String name, String picture)
        {
            super(name, 0, 1, picture);
        }
        
        public Native(String picture)
        {
            super("", 0, 1, picture);
        }

        public Native(String name, int length, int decimalPlaces, boolean signed)
        {
            super(name, length, decimalPlaces, signed);
        }
        
        public Native(int length, int decimalPlaces, boolean signed)
        {
            super("", length, decimalPlaces, signed);
        }
        
        public byte[] toBytes(Object data)
        {
            byte[] bytes = super.toBytes(data);
            
            return getSettings().getLittleEndian() ? reverse(bytes) : bytes;
        }
        
        public Data parse(byte[] input)
        {
            return super.parse(getSettings().getLittleEndian() ? reverse(input) : input);
        }
    }
}