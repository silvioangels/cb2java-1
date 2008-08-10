package net.sf.cb2java.copybook;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public abstract class Value
{
    public static final byte[] EMPTY_BYTES = {};
    
    public static final Side LEFT = new Side();
    public static final Side RIGHT = new Side();
    
    Values parent;
    
    Value(Values parent)
    {
        this.parent = parent;
    }
    
    byte[] get(int length)
    {
        return fill(length);
    }
    
    public abstract byte getByte();
    
    public String fillString(int length)
    {
        return fillString("", length, LEFT);
    }
        
    public String fillString(String s, int length, Side side)
    {
        try {
            return new String(fill(s.getBytes(parent.encoding), length, side), parent.encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
        
    public byte[] fill(int length)
    {
        return fill(EMPTY_BYTES, length, LEFT);
    }
    
    public byte[] fill(byte[] bytes, int length, Side side)
    {
        if (length <= 0) {
            return EMPTY_BYTES;
        }
        
        byte[] out = new byte[length];
        Arrays.fill(out, getByte());
        
        if (side == LEFT) {
            System.arraycopy(bytes, 0, out, length - bytes.length, bytes.length);
        } else {
            System.arraycopy(bytes, 0, out, 0, bytes.length);
        }
        
        return out;
    }
    
    private static class Side {}
}