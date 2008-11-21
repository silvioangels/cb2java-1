package net.sf.cb2java.types;

import java.math.BigDecimal;

import net.sf.cb2java.Value;
import net.sf.cb2java.copybook.floating.Conversion;
import net.sf.cb2java.copybook.floating.IEEE754;
import net.sf.cb2java.copybook.floating.Conversion.Precision;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.FloatingData;

public class Floating extends Leaf
{
    private final Precision precision;
    private final Conversion conversion;
    
    public Floating(String name, int level, int occurs, Precision precision)
    {
        super(name, level, occurs);
        
        this.precision = precision;
        
        Conversion temp;
        
        try {
            Class clazz = Class.forName(getSettings().getFloatConversion());
            temp = (Conversion) clazz.newInstance();
        } catch (Exception e) {
            temp = new IEEE754();
        }
        
        conversion = temp;
    }
    
    public Floating(Precision precision)
    {
        this("", 0, 1, precision);
    }
    
    public Floating(Precision precision, Conversion conversion)
    {
        super("", 0, 1);
        
        this.precision = precision;
        this.conversion = conversion;
    }

    public Value getValue()
    {
        return super.getValue() == null ? getSettings().getValues().ZEROS : super.getValue();
    }

    public int getLength()
    {
        return precision.bytes;
    }

    public Data create()
    {
        return new FloatingData(this);
    }

    public Data parse(byte[] input)
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