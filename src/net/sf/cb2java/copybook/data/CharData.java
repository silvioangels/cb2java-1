package net.sf.cb2java.copybook.data;

import net.sf.cb2java.copybook.AlphaNumeric;

/**
 * Represents data for alpanumeric data types
 * 
 * @author James Watson
 */
public class CharData extends ValueData
{
    final AlphaNumeric definition;
    String data;
    
    public CharData(final AlphaNumeric definition)
    {
        super(definition);
        this.definition = definition;
    }
    
    public String getString()
    {
        return data == null ? "" : data.trim();
    }
    
    public Object getValue()
    {
        return getString();
    }
    
    protected void setValueImpl(Object data)
    {
        setValue((String) data, false);
    }
    
    /**
     * sets the data as a String
     * @param data
     */
//    public void setValue(String data)
//    {
//        setValue(data, true);
//    }
    
    private void setValue(String data, boolean validate)
    {
        if (validate) validate(data);
        this.data = data;
    }
    
    public String toString()
    {
        return getString();
    }

    protected Object translate(String data)
    {
        return data;
    }
}