package net.sf.cb2java.copybook;

import java.util.Collections;
import java.util.List;

abstract class Leaf extends Element
{
    protected Leaf(String name, int level, final int occurs)
    {
        super(name, level, occurs);
    }

    public List getChildren()
    {
        return Collections.EMPTY_LIST;
    }
}