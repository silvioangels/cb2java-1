package net.sf.cb2java.copybook;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.sf.cb2java.copybook.data.Data;
import net.sf.cb2java.copybook.data.DecimalData;
import net.sf.cb2java.copybook.data.IntegerData;

public class Packed extends Numeric
{
    private static final BigInteger TEN = BigInteger.valueOf(10);
    BigInteger ZERO = BigInteger.valueOf(0);
    final int digits;
    final int length;
    
    protected Packed(String name, int level, int occurs, String picture)
    {
        super(name, level, occurs, picture);
        digits = super.getLength();
        length = getLength(digits);
    }

    public Packed(int length, int decimalPlaces, boolean signed)
    {
        super(length, decimalPlaces, signed);
        digits = super.getLength();
        this.length = getLength(digits);
    }
    
    protected static final int getLength(int digits)
    {   
        return (digits / 2) + 1;
    }
    
    protected int getLength()
    {
        return length;
    }

    public int digits()
    {
        return digits;
    }
    
    Data parse(byte[] input)
    {
//        for (int i = 0; i < input.length; i++) {
//            System.out.print(input[i]);
//            System.out.print(' ');
//        }
        
//        System.out.println();
        
        byte lastByte = input[input.length -1]; 
        boolean negative = signed() && (lastByte & 0x0F) == 0x0D;
        
        BigInteger bigI = BigInteger.ZERO;
        
//        (input.length * 2)
        /*
         * the length of the number portion of the packed item is
         * the number of bytes * 2 minus the sign nibble
         */
        int numberLength = (length * 2) - 1;
        
        for (int i = 0; i < numberLength; i++) {
//            System.out.println(input[i / 2]);
            byte current = input[i / 2];
            
            /* 
             * if the index is even, use the left nibble
             * odd, use the right nibble 
             */
            if (i % 2 == 0) {
                current = (byte) ((current & 0xF0) >>> 4);
            } else {
                current = (byte) (current & 0x0F);
            }
            
            /* 
             * magnitude is the one more than the reverse of 
             * the index so if there are 9 digits, the magnitude
             * at i = 0, is 8, the magnitude at i = 8 is 0
             */
//              int magnitude = ((length * 2) - i) - 2;
//              System.out.println(magnitude + " - " + (numberLength - (i + 1)));
            int magnitude = numberLength - (i + 1);
            
//            System.out.println(i + " " + current + " " + magnitude + " " + Math.pow(10, magnitude) + " " 
//                    + (current * (long) Math.pow(10, magnitude)));
            
            BigInteger temp = TEN.pow(magnitude);
            
            bigI = bigI.add(temp.multiply(BigInteger.valueOf(current)));
        }
        
        if (negative) {
            bigI = bigI.negate();
        }
        
        // delete
//        bigI = BigInteger.ZERO;
        
//        System.out.println(bigI);
        
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

    public byte[] toBytes(Object data)
    {
        BigInteger bigI;
        
        if (data == null) {
            bigI = BigInteger.valueOf(0);
        } else {
            bigI = getUnscaled(data);
        }
        
//        System.out.println(data);
        
        byte signNibble;
        
        if (signed()) {
            signNibble = (byte) (bigI.compareTo(BigInteger.ZERO) < 0 ? 0x0D : 0x0C);
        } else {
            signNibble = 0x0F;
        }
        
        byte[] bytes = new byte[getLength()];
        
        int numberLength = (length * 2) - 1;
        
        for (int i = numberLength; i > 0; i--) {
//        for (int i = 0; i < (length * 2) - 1; i++) {
            int value = bigI.mod(TEN).intValue();
            int index = (i - 1) / 2;
            
//            System.out.println(i + " " + value);
            
            if (i % 2 == 0) {
                bytes[index] = (byte) (bytes[index] | value);
            } else {
                bytes[index] = (byte) (bytes[index] | (value << 4));
            }
            
            bigI = bigI.divide(TEN);
        }
        
        int signByte = bytes.length - 1;
        
        bytes[signByte] = (byte) (bytes[signByte] | signNibble);
        
//        for (int i = 0; i < bytes.length; i++) {
//            System.out.print(bytes[i]);
//            System.out.print(' ');
//        }
//        
//        System.out.println();
        
        return bytes;
    }
}