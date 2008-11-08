package net.sf.cb2java.types;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.cb2java.Value;
import net.sf.cb2java.data.Data;
import net.sf.cb2java.data.GroupData;

/**
 * 
 */
public class Group extends Element
{
    final List children = new ArrayList();
    private final List wrapper = Collections.unmodifiableList(children);
    
    public Group(final String name, final int level, final int occurs)
    {
        super(name, level, occurs);
    }
    
    public void addChild(Element element)
    {
        children.add(element);
        element.setParent(this);
    }
    
    public List getChildren()
    {
        return wrapper;
    }

    protected int getLength()
    {
        int length = 0;
        
        for (Iterator i = children.iterator(); i.hasNext();) {
            Element element = (Element) i.next();
            for (int j = 0; j < element.getOccurs(); j++) {
                length += element.getLength();
            }
        }
        
        return length;
    }
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        
        buffer.append(super.toString());
        
        for (Iterator i = children.iterator(); i.hasNext();) {
            buffer.append(i.next());
        }
        
        return buffer.toString();
    }

    public void setValue(String value)
    {
        throw new IllegalArgumentException("cannot set a value to group '" + getName() + "'");
    }
    
    public Data create()
    {
        ArrayList dataChildren = new ArrayList();
        
        for (Iterator i = children.iterator(); i.hasNext();) {
            Element element = (Element) i.next();
            for (int j = 0; j < element.getOccurs(); j++) {
                dataChildren.add(element.create());
            }
        }
        
        return new GroupData(this, dataChildren);
    }
    
    public Data parse(final byte[] bytes)
    {
        ArrayList dataChildren = new ArrayList();
        
        int pos = 0;
        
        for (Iterator i = children.iterator(); i.hasNext();) {
            final Element element = (Element) i.next();
            for (int j = 0; j < element.getOccurs(); j++) {
                final int p = pos;
                final int end = pos + element.getLength();
    //            System.out.println(pos + " " + end + " " + input.length());
                dataChildren.add(element.parse(sub(bytes, pos, end)));
//                dataChildren.add(new DataHolder() {
//                    public Data evaluate()
//                    {
//                       return element.parse(sub(bytes, p, end));
//                    }
//                });
                
                pos = end;
            }
        }
        
        return new GroupData(this, dataChildren);
    }

    byte[] sub(byte[] in, int start, int end)
    {
        byte[] out = new byte[end - start];
        
//        System.out.println("sub: " + in.length + ", " + start + ", " + end);
        
        System.arraycopy(in, start, out, 0, out.length);
        
        return out;
    }
    
    public byte[] toBytes(Object data)
    {
        throw new IllegalArgumentException("cannot read bytes from a group");
    }

    public void validate(Object data)
    {
        throw new IllegalArgumentException("groups do not accept data");
    }

    public void setValue(Value value)
    {
        throw new RuntimeException("groups cannot have a value");
    }
    
    protected Value getValue()
    {
        throw new RuntimeException("groups cannot have a value");
    }
}