package net.sf.cb2java.copybook.data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.cb2java.copybook.Group;

public class GroupData extends Data
{
    final Group definition;
    final List children;
    final List wrapper;
    
    public GroupData(final Group definition, final List children)
    {
        super(definition);
        this.definition = definition;
        this.children = children;
        wrapper = Collections.unmodifiableList(children);
    }
    
    public boolean isLeaf()
    {
        return false;
    }
    
    /**
     * returns a immutable collection of children
     */
    public List getChildren()
    {
        return wrapper;
    }
    
    /**
     * returns the first child with the specified name
     * irrespective of case
     * 
     * @param name the name of the child to look for
     * @return the first child with the given name
     * @throws IllegalArgumentException if no child is found
     */
    public Data getChild(String name)
    {
        for (Iterator i = children.iterator(); i.hasNext();)
        {
            Data child = (Data) i.next();
            
            if (child.getName().equalsIgnoreCase(name)) return child;
        }
        
        return null;
    }
    
    public String toString()
    {
        return toString("");
    }
    
    public String toString(String indent)
    {
        StringBuffer buffer = new StringBuffer(indent);
        
        buffer.append(getName());
        
        for (Iterator i = children.iterator(); i.hasNext();) {
            buffer.append('\n');
            buffer.append(((Data) i.next()).toString(indent + INDENT));
        }
        
        return buffer.toString();
    }
    
    public void write(OutputStream stream) throws IOException
    {
        for (Iterator i = children.iterator(); i.hasNext();) {
            Data child = (Data) i.next();
            child.write(stream);
        }
    }

    /**
     * returns the children of this item
     */
    public Object getValue()
    {
        return getChildren();
    }

    protected Object translate(String data)
    {
        throw new UnsupportedOperationException("cannot convert string to group");
    }
    
    /**
     * not supported
     */
    protected void setValueImpl(Object data)
    {
        throw new IllegalArgumentException("operation not yet supported for groups");
    }
}