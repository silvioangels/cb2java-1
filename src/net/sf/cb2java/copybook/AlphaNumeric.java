package net.sf.cb2java.copybook;

import java.util.regex.Pattern;

import net.sf.cb2java.copybook.data.CharData;
import net.sf.cb2java.copybook.data.Data;

/** 
 * Class used to represent alpha and alphanumeric
 * data types
 * 
 * @author James Watson
 */
public class AlphaNumeric extends Leaf
{
    private final String originalPattern;
    private final Pattern pattern;
    private int length;
    
    public AlphaNumeric(String name, int level, int occurs, String pattern)
    {
        super(name, level, occurs);
        
        this.originalPattern = pattern;
        pattern = pattern.toUpperCase();
        
        StringBuffer buffer = new StringBuffer();
        boolean open = false;
        
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            
            if (c == '(') {
                int pos = pattern.indexOf(')', i);
                int times = Integer.parseInt(pattern.substring(i + 1, pos));
                buffer.append('{').append(times).append('}');
                i = pos + 1;
                length += times;
                open = false;
            } else {
                if (open) length++;
                buffer.append(forChar(c));
                open = true;
            }
        }
        
        if (open) length++;
        
        this.pattern = Pattern.compile(buffer.toString());
    }
    
    private String forChar(char c)
    {
        switch (c) {
        case 'A':
            return "[a-zA-Z\u0000 ]";
        case 'X':
            return ".";//[a-zA-Z0-9\u0000 ]
        case '9':
            return "[0-9\u0000 ]";
        default:
            throw new IllegalArgumentException();
        }
    }
    
    protected int getLength()
    {
        return length;
    }
    
    public Pattern getPattern()
    {
        return pattern;
    }

    Data create()
    {
        return new CharData(this);
    }
    
    Data parse(byte[] bytes)
    {
        CharData data = (CharData) create();
        
        data.setValue(getString(bytes));
        
        return data;
    }
    
    public void validate(Object data)
    {
        if (data == null) return;
        
        String s = (String) data;
        
        if (!pattern.matcher(getValue().fillString(s, getLength(), Value.LEFT)).matches()) {
            System.out.print(pattern.toString());
            throw new IllegalArgumentException(data + " does not match pattern '" + originalPattern
                + "' specified for " + getName());
        }
    }

    public byte[] toBytes(Object data)
    {
        String output = (String) data;
        output = getValue().fillString(output == null ? "" : output, getLength(), Value.LEFT);
        
        return getBytes(output);
    }

    protected Value getValue()
    {
        return value == null ? getCopybook().values.LOW_VALUES : value;
    }
}