package net.sf.cb2java.copybook;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;

import net.sf.cb2java.copybook.data.Data;
import net.sf.cb2java.copybook.data.DecimalData;
import net.sf.cb2java.copybook.data.IntegerData;

/**
 * 
 */
public abstract class Numeric extends Leaf
{
    public static final BigDecimal ZERO = new BigDecimal("0");
    
    public static final Position LEADING = new Position();
    public static final Position TRAILING = new Position();
 
    private Position position = Copybook.DEFAULT_DEFAULT_SIGN_POSITION;
    private final int length;
    private final int decimalPlaces;
    private final boolean signed;
    
    protected Numeric(String name, int level, int occurs, final String picture)
    {
        super(name, level, occurs);
        
        this.length = getLength(picture);
        this.decimalPlaces = getScale(picture, length);
        this.signed = isSigned(picture);
    }
    
    protected Numeric(String name, int level, int occurs, final int length, final int decimalPlaces, final boolean signed)
    {
        super(name, level, occurs);
        
        this.length = length;
        this.decimalPlaces = decimalPlaces;
        this.signed = signed;
    }
    
    protected Numeric(int length, int decimalPlaces, boolean signed, Position position)
    {
        this("", 0, 1, length, decimalPlaces, signed);
        if (position != null) setSignPosition(position);
    }
    
    void setSignPosition(Position position)
    {
        this.position = position;
    }
    
    public Position getSignPosition()
    {
        return position;
    }
    
    public static boolean isSigned(String picture)
    {
        return picture.charAt(0) == 'S';
    }
    
    public static int getLength(String pic)
    {
        int length = 0;
        
        for (int i = 0; i < pic.length(); i++) {
            char c = pic.charAt(i);
            
            if (c == '9' && (i == pic.length() - 1 || pic.charAt(i + 1) != '(')) {
                length++;
            } else if (c == '(') {
                int pos = pic.indexOf(')', i);
                int times = Integer.parseInt(pic.substring(i + 1, pos));
                i = pos;
                length += times;
            }
        }
        
        return length;
    }
    
    public static int getScale(String pic, int length)
    {
        int position = 0;
        pic = pic.toUpperCase();
        
        for (int i = 0; i < pic.length(); i++) {
            char c = pic.charAt(i);
            
            if (c == '(') {
                int pos = pic.indexOf(')', i);
                int times = Integer.parseInt(pic.substring(i + 1, pos));
                i = pos;
                position += times;
            } else if (c == 'V') {
                return length - position;
            }
        }
        
        return 0;
    }
    
    public final boolean signed()
    {
        return signed;
    }
    
    public abstract int digits();
//    {
//        return length();
//    }
    
    public int getLength()
    {
        return length;
    }
    
    public int decimalPlaces()
    {
        return decimalPlaces;
    }
    
    public DecimalFormat getFormatObject()
    {
        StringBuffer buffer = new StringBuffer("#");
                
        for (int i = 0; i < digits(); i++) {
            if (i + decimalPlaces() == digits()) {
                buffer.append('.');
            }
            
            buffer.append('0');
        }
        
        if (decimalPlaces() < 1) buffer.append('.');
        
        buffer.append('#');
                
        return new DecimalFormat(buffer.toString());
    }
    
//    public void validate(Object data)
//    {
//        validate(data, length());
//    }
    
    /**
     * validates the data with the given decimal (printed) length
     * constraint.  This is useful for types where the length in
     * the application data is not the same as the logical length
     * e.g. SignedSeparate.
     * 
     * @param data
     */
    public void validate(Object data)//, int length)
    {
        if (data == null) return;
        
        BigDecimal bigD;
        
        if (data instanceof BigInteger) {
            bigD = new BigDecimal((BigInteger) data);
        } else {
            bigD = (BigDecimal) data;
        }
        
        boolean negative = ZERO.compareTo(bigD) > 0;
        
        if (negative && !signed()) {
            throw (IllegalArgumentException) createEx(bigD, getName() 
                + " is not signed").fillInStackTrace();
        }
        
        int scale = bigD.scale();
        
        if (decimalPlaces() > 0) {
            if (scale > decimalPlaces()) {
                throw (IllegalArgumentException) createEx(bigD, "must have " 
                    + decimalPlaces() + " decimal places").fillInStackTrace();
            }
        }
        
        bigD = bigD.setScale(decimalPlaces());
            
        String s = bigD.unscaledValue().toString();
        
        if (negative) s = s.substring(1);
            
        if (s.length() > digits()) {
            throw (IllegalArgumentException) createEx(bigD, "must be no longer than " 
                + digits() + " digits").fillInStackTrace();
        }
    }
    
    protected Value getValue()
    {
        return value == null ? getSettings().getValues().ZEROS : value;
    }
    
    protected BigInteger getUnscaled(Object data)
    {
        if (data instanceof BigInteger) {
            return (BigInteger) data;
        } else {
            int places = decimalPlaces();
            BigDecimal bigD = (BigDecimal) data;
            if (bigD.scale() != places) {
                bigD = bigD.setScale(decimalPlaces());
            }
            
            return bigD.unscaledValue();
        }
    }
    
    protected Data create()
    {
        if (decimalPlaces() > 0) {
            return new DecimalData(this);
        } else {
            return new IntegerData(this);
        }
    }
    
    public IllegalArgumentException createEx(BigDecimal data, String reason)
    {
        return createEx(data, reason, null);
    }
    
    public IllegalArgumentException createEx(BigDecimal data, String reason, Throwable cause)
    {
        return new IllegalArgumentException(data + " is not valid for " + getName() + ". " + reason 
            + (cause == null ? "" : " " + cause.getMessage()));
    }
    
    public static class Position
    {
        private Position(){}
    }
}