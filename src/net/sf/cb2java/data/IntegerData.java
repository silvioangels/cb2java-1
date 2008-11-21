package net.sf.cb2java.data;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.sf.cb2java.types.Numeric;

/**
 * class that represents numeric data 
 * with no fraction portion
 * 
 * @author James Watson
 */
public class IntegerData extends NumericData
{
    public BigInteger data;
    
    public IntegerData(Numeric definition)
    {
        super(definition);
    }
    
    public int getInt()
    {
        return data == null ? 0 : data.intValue();
    }
    
    public long getLong()
    {
        return data == null ? 0 : data.longValue();
    }
    
    public BigInteger getBigInteger()
    {
        return data == null ? new BigInteger("0") : data;
    }
    
    public void setValue(long data)
    {
        BigInteger temp = BigInteger.valueOf(data);
        setValue(temp, true);
    }
    
    protected void setValueImpl(Object data)
    {
        setValue(((BigDecimal) data).toBigInteger(), false);
    }
    
    public void setValue(BigInteger data)
    {
        setValue(data, true);
    }

    public void setValue(BigInteger data, boolean validate)
    {
        if (validate) validate(data);
        this.data = data;
    }
    
    public Object getValue()
    {
        return getBigInteger();
    }
}