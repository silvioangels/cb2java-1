package net.sf.cb2java.copybook.floating;

import java.math.BigDecimal;

public interface Conversion
{
    static final Precision SINGLE = new Precision(4);
    static final Precision DOUBLE = new Precision(8);
    
    public BigDecimal fromBytes(byte[] input, Precision precision);
   
    public byte[] toBytes(BigDecimal data, Precision precision);
    
    public void validate(BigDecimal data, Precision precision);
    
    public static class Precision
    {
        public final int bytes;
        
        private Precision(int bytes)
        {
            this.bytes = bytes;
        }
    }
}