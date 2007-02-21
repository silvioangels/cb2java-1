package net.sf.cb2java.copybook;

import java.io.UnsupportedEncodingException;

class Values
{
    Copybook copybook;
    
    Values()
    {
        super();
    }
    
    void setCopybook(Copybook copybook)
    {
        this.copybook = copybook;
        try {
            ((StringBasedValue) SPACES).b = " ".getBytes(copybook.getEncoding())[0];
            ((StringBasedValue) QUOTES).b = "\"".getBytes(copybook.getEncoding())[0];
            ((StringBasedValue) ZEROS).b = "0".getBytes(copybook.getEncoding())[0];
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
            return copybook.getBytes(value.length() > length ? value.substring(0, length) : value);
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

        byte b;
        
        public byte getByte()
        {
            return b;
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