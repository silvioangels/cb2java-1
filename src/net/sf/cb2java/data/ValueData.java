package net.sf.cb2java.data;

import java.util.Collections;
import java.util.List;

import net.sf.cb2java.types.Element;

public abstract class ValueData extends Data
{    
    public ValueData(Element definition)
    {
        super(definition);
    }

    public boolean isLeaf()
    {
        return true;
    }
    
    public List getChildren()
    {
        return Collections.EMPTY_LIST;
    }
   
    public abstract String toString();
   
    public String toString(String indent)
    {
        return indent + toString();
    }
}