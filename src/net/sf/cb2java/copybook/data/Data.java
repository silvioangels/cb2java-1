package net.sf.cb2java.copybook.data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import net.sf.cb2java.copybook.Element;

/**
 * The base type for all Data elements
 * 
 * @author James Watson
 */
public abstract class Data
{
    /**
     * the string used to indent data in
     * toString methods
     */
    static final String INDENT = "  ";
    
    private final Element definition;
    
    /**
     * constructor
     * 
     * @param definition the underlying type definition
     */
    public Data(final Element definition)
    {
        this.definition = definition;
    }
    
    /**
     * returns the name of the type that defines this element
     */
    public String getName()
    {
        return getDefinition().getName();
    }
    
    public abstract boolean isLeaf();
    
    /**
     * returns all the children of this item, if there are any
     */
    public abstract List getChildren();
    
    /**
     * gives a string representation of this element
     * with the given indention
     * 
     * @param indent the string used to indent
     */
    public abstract String toString(String indent);
    
    /**
     * the underlying type definition of the element
     */
    public Element getDefinition()
    {
        return definition;
    }
    
    /**
     * returns the Object as it's 'natural' Java type
     */
    public abstract Object getValue();
    
    /**
     * Sets the internal value of this item.  If the data is not
     * compatible with this type, an Exception will be thrown.  See
     * the documentation of the specific subtypes for a list of 
     * acceptable types and other rules for valid input.
     * 
     * @param data the data to set.  Must be compatible with the
     * the instance.
     */
    public final void setValue(Object data)
    {
        //TODO setting?
//        validate(data);
        
        setValueImpl(data);
    }
    
    public final void setValue(String data)
    {
        setValue(translate(data));
    }
    
    protected abstract Object translate(String data);
    
    /**
     * called by setData after validate is called
     * 
     * @param data the data to set
     */
    protected abstract void setValueImpl(Object data);
    
    /**
     * 
     * 
     * @param stream
     * @throws IOException
     */
    public void write(OutputStream stream) throws IOException
    {
        getDefinition().write(stream, getValue());
    }
    
    /**
     * Validates the data passed in using the underlying definition
     * 
     * @param data the data to validate
     */
    public final void validate(Object data)
    {
        getDefinition().validate(data);
    }
}