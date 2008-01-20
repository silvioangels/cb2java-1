package net.sf.cb2java.copybook;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import net.sf.cb2java.copybook.data.Data;

/**
 * base class for types represented in the copybook
 * 
 * @author James Watson
 */
public abstract class Element 
{
    /** the name of the element from the copybook */
    private final String name;
    /** the level of the element from the copybook */
    private final int level;
    /** how many times the element occurs in the application data */
    private final int occurs;
    /** the absolute position of the where this item starts in data */
    int position;
    /** the instance that represents the data that defines this element */
    private Copybook copybook;
    /** the default value of this element */
    Value value;
    
    /**
     * constructor
     * 
     * @param name the element name
     * @param level the level of the element
     * @param occurs how many times the element occurs
     */
    protected Element(final String name, final int level, final int occurs)
    {
        this.name = name;
        this.level = level;
        this.occurs = occurs;
    }
    
    /**
     * the value for this element
     * 
     * @return the value set for this element or the default
     */
    protected abstract Value getValue();
    
    /**
     * gets the children of this element or null if there are none
     * 
     * @return the children of this element or null if there are none
     */
    public abstract List getChildren();
    
    /**
     * returns the length of one instance of this element
     * 
     * @return the length of one instance of this element
     */
    protected abstract int getLength();
    
    /**
     * creates a new empty Data instance for this element
     * 
     * @return a new empty Data instance for this element
     */
    abstract Data create();
    
    /**
     * creates a new empty Data instance from the data supplied
     * 
     * @param input the input data
     * @return a new empty Data instance from the data supplied
     */
    abstract Data parse(byte[] input);
    
    /**
     * validates the data based on this element definition
     * 
     * @param data the data to validate
     * @throws IllegalArgumentException if the data is invalid
     */
    public abstract void validate(Object data) throws IllegalArgumentException;
    
    /**
     * converts the supplied data to bytes
     * 
     * @param data the data to convert to bytes
     * @return the bytes for the data
     */
    public abstract byte[] toBytes(Object data);
    
    /**
     * returns the name of this element
     * 
     * @return the name of this element
     */
    public final String getName()
    {
        return name;
    }
    
    /**
     * returns the level of this element
     * 
     * @return the level of this element
     */
    public final int getLevel()
    {
        return level;
    }
    
    /**
     * returns the position of this element
     * 
     * @return the position of this element
     */
    public final int getPosition()
    {
        return position;
    }
    
    /**
     * returns the number of times this item appears in data
     * 
     * @return the number of times this item appears in data
     */
    public int getOccurs()
    {
        return occurs;
    }
    
    /**
     * sets the value for this element
     * 
     * @param value
     */
    void setValue(Value value)
    {
        this.value = value;
    }
    
    /**
     * writes the data as bytes to the given stream
     * 
     * @param stream the outputstream
     * @param data the data to write as bytes
     * @throws IOException
     */
    public final void write(OutputStream stream, Object data) throws IOException
    {
        validate(data);
        stream.write(toBytes(data));
    }
    
    /**
     * helper method for converting the given bytes to a string with
     * the parent copybook's encoding
     * 
     * @param data the data to convert to a String
     * @return the String value
     */
    public final String getString(byte[] data)
    {
        try {
            return new String(data, getCopybook().getEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } 
    }
    
    /**
     * converts a String to a byte array based on the current encoding
     * 
     * @param s
     * @return
     */
    public final byte[] getBytes(String s)
    {
        try {
            return s.getBytes(getCopybook().getEncoding());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    
    public String toString() 
    {
        return new String(getCopybook().values.SPACES.fill(level)) + name + ": '" 
            + this.getClass() + " " + getLength() + "'\n";
    }

    void setCopybook(Copybook copybook) {
        if (copybook != null) throw new IllegalStateException("copybook alread initialized");
        
        this.copybook = copybook;
    }

    public Copybook getCopybook() {
        return copybook;
    }
}