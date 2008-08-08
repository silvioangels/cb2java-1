package net.sf.cb2java.copybook;

import net.sf.cb2java.copybook.data.CharData;
import net.sf.cb2java.copybook.data.Data;

/** 
 * Class used to represent alpha and alphanumeric
 * data types
 * 
 * @author James Watson
 */
public class Characters extends Leaf
{
    private final int length;
    
    public Characters(String name, int length, int level, int occurs)
    {
        super(name, level, occurs);
        
        this.length = length;
    }
    
    protected int getLength()
    {
        return length;
    }

    Data create()
    {
        return new CharData(this);
    }
    
    public Data parse(byte[] bytes)
    {
        CharData data = (CharData) create();
        
        data.setValue(getString(bytes));
        
        return data;
    }
    
    public void validate(Object data)
    {
        if (data == null) return;
        
        String s = data.toString();
        
        if (s.length() <= getLength()) throw new IllegalArgumentException("string value of " + data + " is longer than " + length);
    }

    public byte[] toBytes(Object data)
    {
        String output = (String) data;
        output = getValue().fillString(output == null ? "" : output, getLength(), Value.RIGHT);
        
        return getBytes(output);
    }

    protected Value getValue()
    {
        return value == null ? getCopybook().values.SPACES : value;
    }
}