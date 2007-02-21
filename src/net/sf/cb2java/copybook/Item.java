package net.sf.cb2java.copybook;

import java.util.ArrayList;
import java.util.List;

import net.sf.cb2java.copybook.floating.Conversion;

/**
 * our internal representation of a copybook "item" node
 */ 
class Item
{
    final boolean document;
    
    Values values;
    
    /**
     * @param analyzer
     */
    Item(Values values, final boolean document)
    {
        this.values = values;
        this.document = document;
    }
    
    Item(Values values)
    {
        this(values, false);
    }

    String name;
	int level;
    Item parent;
    int length;
    
    List children = new ArrayList();
    
    String redefines;
    int occurs = 1;
    int minOccurs;  // not supported
    String dependsOn; // not supported
    
    boolean isAlpha;
    boolean signSeparate;
    SignedSeparate.Position signPosition = SignedSeparate.LEADING;
    
    String picture;
    Value value;
    Usage usage;
    
    private Element element;
        
    void setParent(Item candidate)
    {
        if (level > candidate.level) {
            parent = candidate;
            parent.children.add(this);
        } else {
            setParent(candidate.parent);
        }
    }
    
    void createElement()
    {
        if (document) {
            createDocument();
            Copybook copybook = (Copybook) element;
            values.setCopybook(copybook);
            copybook.values = values;
        } else if (picture == null) {
            if (usage == Usage.COMPUTATIONAL_1) {
                createSingleFloat();
            } else if (usage == Usage.COMPUTATIONAL_2) {
                createDoubleFloat();
            } else {
                createGroup();
            }
        } else if (isAlpha) {
            createAlphaNumeric();
        } else {
            if (usage == Usage.BINARY) {
                createBinary();
            } else if (usage == Usage.COMPUTATIONAL) {
                createBinary();
            } else if (usage == Usage.PACKED_DECIMAL) {
                createPacked();
            } else if (usage == Usage.COMPUTATIONAL_3) {
                createPacked();
            } else if (usage == Usage.COMPUTATIONAL_4) {
                createBinary();
            } else if (usage == Usage.COMPUTATIONAL_5) {
                createNativeBinary();
            } else if (signSeparate) {
                createSignedSeparate();
            } else {
                createDecimal();
            }
        }
        
        if (value != null) {
            element.setValue(value);
        }
    }
    
    private void createDocument()
    {
        element = new Copybook(name);
    }
    
    private void createGroup()
    {
        element = new Group(name, level, occurs);
    }
    
    private void createBinary()
    {
        element = new Binary(name, level, occurs, picture);
    }
    
    private void createNativeBinary()
    {
        element = new Binary.Native(name, level, occurs, picture);
    }
    
    private void createPacked()
    {
        element = new Packed(name, level, occurs, picture);
    }
    
    private void createSignedSeparate()
    {
        element = new SignedSeparate(name, level, occurs, picture);
        ((SignedSeparate) element).setPosition(signPosition);
    }
    
    private void createDecimal()
    {
        element = new Decimal(name, level, occurs, picture);
    }
    
    private void createAlphaNumeric()
    {
        element = new AlphaNumeric(name, level, occurs, picture);
    }
    
    private void createSingleFloat()
    {
        element = new Floating(name, level, occurs, Conversion.SINGLE);
    }
    
    private void createDoubleFloat()
    {
        element = new Floating(name, level, occurs, Conversion.DOUBLE);
    }
    
    Element getElement()
    {
        if (element == null) createElement();
        
        return element;
    }
}