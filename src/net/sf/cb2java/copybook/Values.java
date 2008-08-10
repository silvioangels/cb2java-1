package net.sf.cb2java.copybook;

import java.io.UnsupportedEncodingException;

class Values
{
//    Copybook copybook;
    protected String encoding;
    
    Values()
    {
        /* */
    }
    
    void setEncoding(String encoding)
    {
        this.encoding = encoding;
        
        try {
            ((StringBasedValue) SPACES).bite = " ".getBytes(encoding)[0];
            ((StringBasedValue) QUOTES).bite = "\"".getBytes(encoding)[0];
            ((StringBasedValue) ZEROS).bite = "0".getBytes(encoding)[0];
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public class Literal extends Value
    {
        private final String value;
        
        Literal(final String value)
        {
            super(Values.this);
            this.value = value;
        }

        public byte[] get(int length)
        {
            String s = value.length() > length ? value.substring(0, length) : value;
            
            try {
                return s.getBytes(encoding);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        public byte getByte()
        {
            return 0;
        }
    }
    
    private class StringBasedValue extends Value
    {
        StringBasedValue(Values parent)
        {
            super(parent);
        }

        byte bite;
        
        public byte getByte()
        {
            return bite;
        }
    }
    
    public final Value SPACES = new StringBasedValue(Values.this);
    
    public final Value LOW_VALUES = new Value(Values.this) {

        public byte getByte()
        {
            return 0;
        }
    };
    
    public final Value HIGH_VALUES = new Value(Values.this) {

        public byte getByte()
        {
            return -1;
        }
    };
    
    public final Value ZEROS = new StringBasedValue(Values.this);
    
    public final Value QUOTES = new StringBasedValue(Values.this);
    
    public final Value NULLS = LOW_VALUES;
}