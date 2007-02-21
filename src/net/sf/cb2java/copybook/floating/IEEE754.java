package net.sf.cb2java.copybook.floating;

import java.math.BigDecimal;

public class IEEE754 implements Conversion
{
    public BigDecimal fromBytes(byte[] input, Precision p)
    {
        long bits = 0;
        
        for (int i = 0; i < p.bytes; i++) {
            bits = bits | (0xFF & input[i]);
            
//            String sLong = "0000000000000000000000000000000000000000000000000000000000000000" + Long.toBinaryString(bits);
            
//            System.out.println(sLong.substring(sLong.length() - 64));
            
            if (i < p.bytes - 1) bits = bits << 8;
            
//            sLong = "0000000000000000000000000000000000000000000000000000000000000000" + Long.toBinaryString(bits);
            
//            System.out.println(sLong.substring(sLong.length() - 64));
            
        }
        
//        System.out.println();
//        
//        for (int i = 0; i < input.length; i++) {
//            String sByte = "00000000" + Integer.toBinaryString(0xFF & input[i]);
//            System.out.print(sByte.substring(sByte.length() - 8));
//            System.out.print(' ');
//        }
//        
//        System.out.println();
        
        if (p == SINGLE) {
//            System.out.println(Integer.toBinaryString((int) bits));
            return new BigDecimal(Float.intBitsToFloat((int) bits));
        } else if (p == DOUBLE) {
//            System.out.println(Long.toBinaryString(bits));
            return new BigDecimal(Double.longBitsToDouble(bits));
            
        } else {
            /* this shouldn't happen unless Conversion class is changed */
            throw new IllegalStateException("unknown precision");
        }
    }

    public byte[] toBytes(BigDecimal data, Precision p)
    {
        long bits;
        
        if (p == SINGLE) {
            bits = Float.floatToRawIntBits(data.floatValue());
        } else if (p == DOUBLE) {
            bits = Double.doubleToRawLongBits(data.doubleValue());
        } else {
            /* this shouldn't happen unless Conversion class is changed */
            throw new IllegalStateException("unknown precision");
        }
        
//        System.out.println("bits: " + bits);
        
        byte[] bytes = new byte[p.bytes];
        
        for (int i = p.bytes; i > 0; i--) {
//            System.out.println("byte: " + (byte) (0xFF & bits));
            bytes[i - 1] = (byte) (0xFF & bits);
//            System.out.println("bits: " + bits);
            if (i > 1) bits = bits >> 8;
            
        }
        
//        for (int i = 0; i < bytes.length; i++) {
//            System.out.print(Integer.toBinaryString(bytes[i]));
//            System.out.print(' ');
//        }
//        
//        System.out.println();
        
        return bytes;
    }

    public void validate(BigDecimal data, Precision p)
    {
        BigDecimal test;
        
        
        if (p == SINGLE) {
            float f = data.floatValue();
            
            test = new BigDecimal(f);
        } else if (p == DOUBLE) {
            double d = data.doubleValue();
            
            test = new BigDecimal(d);
        } else {
            /* this shouldn't happen unless Conversion class is changed */
            throw new IllegalStateException("unknown precision");
        }
        
//        System.out.println(data);
//        System.out.println(test);
        
        
        if (test.compareTo(data) != 0) {
            throw new IllegalArgumentException("data cannot be converted to ieee754");
        }
    }
}
