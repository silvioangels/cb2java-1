package net.sf.cb2java.copybook.data;

import java.util.Collections;
import java.util.List;

import net.sf.cb2java.copybook.Element;

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