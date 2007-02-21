package net.sf.cb2java.copybook.data;

import java.math.BigDecimal;

import net.sf.cb2java.copybook.Floating;

/**
 * Floating point representations are hardware specific
 * so the precision that is supported by the underlying type
 * may vary from platform to platform.  For most normal uses
 * Java floats or doubles should be safe to use but it's possible 
 * that there may be a loss of precision if the underlying platform
 * does not follow the IEEE 754 spec.
 * 
 * <p>Because not all floating types are the same in COBOL
 * BigDecimal is the 'natural' type for this class, however, numbers
 * with fraction portions that are not representable in binary will be
 * rounded on setting the data.
 * 
 * @author James Watson
 */
public class FloatingData extends ValueData
{
    /** All floats should be representable as BigDecimals */
    BigDecimal data;
    
    /**
     * constructor
     * 
     * @param definition the underlying definition for the
     * type in the copybook defintion
     */
    public FloatingData(final Floating definition)
    {
        super(definition);
    }

    /**
     * gets the big decimal representation of the value
     */
    public BigDecimal getBigDecimal()
    {
        return data == null ? new BigDecimal(0) : data;
    }
    
    public void setValueImpl(Object data)
    {
        setValue((BigDecimal) data, false);
    }

    public void setValue(BigDecimal data)
    {
        setValue(data, true);
    }
    
    private void setValue(BigDecimal data, boolean validate)
    {   
        if (validate) validate(data);
        this.data = data;
    }
    
    public String toString()
    {
        return getValue().toString();
    }

    /**
     * returns the internal data as a BigDecimal
     */
    public Object getValue()
    {
        return getBigDecimal();
    }
    
    /**
     * returns the internal data as a BigDecimal
     */
    protected float getFloat()
    {
        return getBigDecimal().floatValue();
    }
    
    /**
     * returns the internal data as a BigDecimal
     */
    protected double getDouble()
    {
        return getBigDecimal().doubleValue();
    }

    protected Object translate(String data)
    {
        return new BigDecimal(data);
    }
}