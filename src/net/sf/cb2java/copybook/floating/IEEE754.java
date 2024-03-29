package net.sf.cb2java.copybook.floating;

import java.math.BigDecimal;

public class IEEE754 implements Conversion
{   
    public BigDecimal fromBytes(byte[] input, Precision p)
    {
        long bits = 0;
                
        int shift = 0; 
        for (int i = 0; i < input.length; i++) { 
            bits |= ((long)(0xFF & input[i])) << shift; 
            shift += 8;
        }
                
        if (p == SINGLE) {
            return new BigDecimal(Float.intBitsToFloat((int) bits));
        } else if (p == DOUBLE) {
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
        
        byte[] bytes = new byte[p.bytes];
        
        for (int i = p.bytes; i > 0; i--) {
            bytes[i - 1] = (byte) (0xFF & bits);
            if (i > 1) bits = bits >> 8;            
        }
        
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
        
        if (test.compareTo(data) != 0) {
            throw new IllegalArgumentException("data cannot be converted to ieee754");
        }
    }
}
