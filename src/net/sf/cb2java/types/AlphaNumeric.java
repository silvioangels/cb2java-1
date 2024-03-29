package net.sf.cb2java.types;

import java.util.regex.Pattern;

import net.sf.cb2java.Value;

/** 
 * Class used to represent alpha and alphanumeric
 * data types
 * 
 * @author James Watson
 */
public class AlphaNumeric extends Characters
{
    /** the original pattern used for debugging */
    private final String originalPattern;
    /** the regex pattern for validation */
    private final Pattern pattern;
    /** the byte length of this element */
    private final int length;
    
    public AlphaNumeric(String name, int level, int occurs, String pattern)
    {
        super(name, 0, level, occurs);
        
        this.originalPattern = pattern;
        pattern = pattern.toUpperCase();
        
        StringBuffer buffer = new StringBuffer();
        
        length = parsePattern(pattern, buffer);
        
        this.pattern = Pattern.compile(buffer.toString());
    }
    
    public AlphaNumeric(String pattern)
    {
        this("", 0, 1, pattern);
    }
    
    public int getLength()
    {
        return length;
    }
    
    private static int parsePattern(String pattern, StringBuffer buffer)
    {
        boolean open = false;
        int length = 0;
        
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
        
        return length;
    }
    
    private static String forChar(char c)
    {
        switch (c) {
        case 'A':
            return "[a-zA-Z\u0000 ]";
        case 'X':
            return ".";//[a-zA-Z0-9\u0000 ]
        case '9':
            return "[0-9\u0000 ]";
        default:
            throw new IllegalArgumentException("character [" + c + "] not allowed.");
        }
    }
    
    /**
     * returns the pattern used to validate the element
     * 
     * @return the pattern used to validate the element
     */
    public Pattern getPattern()
    {
        return pattern;
    }
    
    public void validate(Object data)
    {
        if (data == null) return;
        
        String s = (String) data;
        
        if (!pattern.matcher(getValue().fillString(s, getLength(), Value.RIGHT)).matches()) {
            System.out.print(pattern.toString());
            throw new IllegalArgumentException(data + " does not match pattern '" + originalPattern
                + "' specified for " + getName());
        }
    }
}