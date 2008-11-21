package net.sf.cb2java.data;

import java.math.BigDecimal;
//import java.text.ParseException;

import net.sf.cb2java.types.Numeric;

public abstract class NumericData extends ValueData
{
    protected NumericData(final Numeric definition)
    {
        super(definition);
    }
    
    public String toString()
    {
        return ((Numeric) getDefinition()).getFormatObject().format(getValue());
    }
    
    public Object translate(String data)
    {
        return new BigDecimal(data);
    }
}