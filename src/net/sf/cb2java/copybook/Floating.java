package net.sf.cb2java.copybook;

import java.math.BigDecimal;

import net.sf.cb2java.copybook.data.Data;
import net.sf.cb2java.copybook.data.FloatingData;
import net.sf.cb2java.copybook.floating.Conversion;
import net.sf.cb2java.copybook.floating.IEEE754;
import net.sf.cb2java.copybook.floating.Conversion.Precision;

public class Floating extends Leaf
{
    final Precision precision;
    final Conversion conversion;
    
    protected Floating(String name, int level, int occurs, Precision precision)
    {
        super(name, level, occurs);
        
        this.precision = precision;
        
        Conversion temp;
        
        try {
            Class clazz = Class.forName(getCopybook().getFloatConversion());
            temp = (Conversion) clazz.newInstance();
        } catch (Exception e) {
            temp = new IEEE754();
        }
        
        conversion = temp;
    }

    protected Value getValue()
    {
        return value == null ? getCopybook().values.ZEROS : value;
    }

    protected int getLength()
    {
        return precision.bytes;
    }

    Data create()
    {
        return new FloatingData(this);
    }

    Data parse(byte[] input)
    {
        FloatingData data = (FloatingData) create();
        
        data.setValue(conversion.fromBytes(input, precision));
        
        return data;
    }

    public void validate(Object data) throws IllegalArgumentException
    {
        if (!(data instanceof BigDecimal)) {
            throw new IllegalArgumentException("only BigDecimal is supported");
        }
        
        conversion.validate((BigDecimal) data, precision);
    }

    public byte[] toBytes(Object data)
    {
//        System.out.println("float:" + data);
        return conversion.toBytes((BigDecimal) data, precision);
    }
}