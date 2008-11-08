package net.sf.cb2java.data;

import java.math.BigDecimal;

import net.sf.cb2java.types.Numeric;

public class DecimalData extends NumericData
{
    int roundingMode = BigDecimal.ROUND_HALF_UP;
    public BigDecimal data;
    
    public DecimalData(final Numeric definition)
    {
        super(definition);
    }
    
    public void setRoundingMode(int mode)
    {
        this.roundingMode = mode;
    }
    
    public float getFloat()
    {
        return data == null ? 0 : data.floatValue();
    }
    
    public double getDouble()
    {
        return data == null ? 0 : data.doubleValue();
    }
    
    public BigDecimal getBigDecimal()
    {
        return data == null ? new BigDecimal(0) : data;
    }
    
    /**
     * sets the value of this data object with the given floating
     * point number.  Rounds the value to the decimalPlaces specified
     * in the definiton using this object's rounding more.
     * 
     * @param data the value to set this Object to
     */
    public void setValue(double data)
    {
        BigDecimal temp = new BigDecimal(data);
        temp = temp.setScale(((Numeric) getDefinition()).decimalPlaces(), roundingMode);
        
        setValue(temp, true);
    }
    
    protected void setValueImpl(Object data)
    {   
        setValue((BigDecimal) data, false);
    }
    
    public void setValue(BigDecimal data)
    {   
        setValue(data, true);
    }
    
    public void setValue(BigDecimal data, boolean validate)
    {   
        if (validate) validate(data);
        this.data = data;
    }
    
    public Object getValue()
    {
        return getBigDecimal();
    }
}