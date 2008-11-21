package net.sf.cb2java.types;

import java.util.Collections;
import java.util.List;

/**
 * Base class for elements that are not group elements
 * 
 * @author Matt Watson
 */
abstract class Leaf extends Element
{
    protected Leaf(String name, int level, final int occurs)
    {
        super(name, level, occurs);
    }

    /**
     * returns an empty collection
     * 
     * @return an empty collection
     */
    public List getChildren()
    {
        return Collections.EMPTY_LIST;
    }
}