package net.sf.cb2java.types;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.sf.cb2java.Value;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.DecimalData;
import net.sf.cb2java.data.IntegerData;

/**
 * class that represents decimal (zoned) data types
 *
 * @author James Watson
 */
public class Decimal extends Numeric
{
    public Decimal(String name, int level, int occurs, String picture)
    {
        super(name, level, occurs, picture);
    }
    
    public Decimal(String name, String picture)
    {
        super(name, 0, 1, picture);
    }
    
    public Decimal(String picture)
    {
        super("", 0, 1, picture);
    }
    
    public Decimal(String name, int length, int decimalPlaces, boolean signed)
    {
        super(name, length, decimalPlaces, signed, null);
    }
    
    public Decimal(int length, int decimalPlaces, boolean signed)
    {
        super("", length, decimalPlaces, signed, null);
    }
    
    public Decimal(String name, int length, int decimalPlaces, boolean signed, Position position)
    {
        super(name, length, decimalPlaces, signed, position);
    }
    
    public Decimal(int length, int decimalPlaces, boolean signed, Position position)
    {
        super("", length, decimalPlaces, signed, position);
    }
    
    /**
     * returns the character for the given char representing a digit
     * in order to create an overpunched digit in a zoned number
     * 
     * @param positive whether the value is positive i.e. false is negative
     * @param overpunch the char to overpunch
     * @return the overpunched char
     */
    private char getChar(boolean positive, char overpunch)
    {
        if (!signed()) {
            return overpunch;
        } else if (positive) {
            switch(overpunch) {
                case '0': return '{';
                case '1': return 'A';
                case '2': return 'B';
                case '3': return 'C';
                case '4': return 'D';
                case '5': return 'E';
                case '6': return 'F';
                case '7': return 'G';
                case '8': return 'H';
                case '9': return 'I';
            }
        } else {
            switch(overpunch) {
                case '9': return 'R';
                case '8': return 'Q';
                case '7': return 'P';
                case '6': return 'O';
                case '5': return 'N';
                case '4': return 'M';
                case '3': return 'L';
                case '2': return 'K';
                case '1': return 'J';
                case '0': return '}';
            }
        }
        
        throw new IllegalArgumentException("invalid number: " + overpunch);
    }
    
    /**
     * whether the given overpunched char is positive
     * 
     * @param overpunched the char to check
     * @return whether the given overpunched char is positive
     */
    private boolean isPositive(char overpunched)
    {
        if (!signed()) {
            return true;
        } else {
            switch(overpunched) {
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '{':
                case 'A':
                case 'B':
                case 'C':
                case 'D':
                case 'E':
                case 'F':
                case 'G':
                case 'H':
                case 'I':
                    return true;
                case '0':
                case '}':
                case 'J':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R': 
                    return false;
            }
        }
        
        throw new IllegalArgumentException("invalid char: " + overpunched);
    }
    
    /**
     * The digit char for an overpunched char
     * 
     * @param overpunched
     * @return the digit char
     */
    private char getNumber(char overpunched)
    {
        if (!signed()) {
            return overpunched;
        } else {
            switch(overpunched) {
                case '9':
                case 'R': 
                case 'I': 
                    return '9';
                case '8':
                case 'Q':
                case 'H': 
                    return '8';
                case '7':
                case 'P': 
                case 'G': 
                    return '7';
                case '6':
                case 'O': 
                case 'F': 
                    return '6';
                case '5':
                case 'N': 
                case 'E': 
                    return '5';
                case '4':
                case 'M': 
                case 'D': 
                    return '4';
                case '3':
                case 'L': 
                case 'C': 
                    return '3';
                case '2':
                case 'K': 
                case 'B': 
                    return '2';
                case '1':
                case 'J': 
                case 'A': 
                    return '1';
                case '0': 
                case '}': 
                case '{': 
                    return '0';
            }
        }
        
        throw new IllegalArgumentException("invalid char: " + overpunched);
    }
    
    public Data parse(byte[] bytes)
    {
        String input = getString(bytes).trim();
        String s;
        
        if (input.length() < 1) {
            s = null;
        }else if (getSignPosition() == LEADING) {
            char c = input.charAt(0); 
            s = (isPositive(c) ? "" : "-") + getNumber(c) 
                + (input.length() > 1 ? input.substring(1) : ""); 
        } else {
            int last = input.length() - 1; 
            char c = input.charAt(last); 
            s = (isPositive(c) ? "" : "-") 
                + (input.length() > 1 ? input.substring(0, last-1) : "") + getNumber(c);
        }
        
        BigInteger big = s == null ? null : new BigInteger(s);
        Data data = create();
        
        if (data instanceof DecimalData) {
            DecimalData dData = (DecimalData) data;
            BigDecimal bigD = big == null ? null : new BigDecimal(big, decimalPlaces());
            
            dData.setValue(bigD);
            
            return data;
        } else {
            IntegerData iData = (IntegerData) data;
            
            iData.setValue(big);
            
            return data;
        }
    }
    
    public byte[] toBytes(Object data)
    {
        if (data == null) {
            return getValue().fill(getLength());
        } 

        BigInteger bigI = getUnscaled(data);
        boolean positive;
        
        if (ZERO.unscaledValue().compareTo(bigI) > 0) {
            bigI = bigI.abs();
            positive = false;
        } else {
            positive = true;
        }
        
        byte[] output = getValue().fill(getBytes(bigI.toString()), getLength(), Value.LEFT);
        
        if (getSignPosition() == LEADING) {
            output[0] = (byte) getChar(positive, (char) output[0]);
        } else {
            int last = output.length - 1;
            output[last] = (byte) getChar(positive, (char) output[last]);
        }
        
        return output;
        
//        s = getValue().fillString(s, getLength(), Value.LEFT);
//        
//        if (getSignPosition() == LEADING) {
//            return getBytes(getChar(positive, s.charAt(0)) + s.substring(1));
//        } else {
//            int last = s.length() - 1;
//            return getBytes(s.substring(0, last) + getChar(positive, s.charAt(last)));
//        }
    }

    public int digits()
    {
        return getLength();
    }
}