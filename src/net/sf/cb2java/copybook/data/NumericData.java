package net.sf.cb2java.copybook.data;

import java.math.BigDecimal;
//import java.text.ParseException;

import net.sf.cb2java.copybook.Numeric;

public abstract class NumericData extends ValueData
{
    NumericData(final Numeric definition)
    {
        super(definition);
    }
    
    public String toString()
    {
        return ((Numeric) getDefinition()).getFormatObject().format(getValue());
    }
    
    protected Object translate(String data)
    {
            return new BigDecimal(data);
    }
}