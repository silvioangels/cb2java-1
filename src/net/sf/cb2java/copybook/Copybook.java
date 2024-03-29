package net.sf.cb2java.copybook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.cb2java.Settings;
import net.sf.cb2java.Values;
import net.sf.cb2java.data.GroupData;
import net.sf.cb2java.data.Record;
import net.sf.cb2java.types.Element;
import net.sf.cb2java.types.Group;
import net.sf.cb2java.types.Numeric;
import net.sf.cb2java.types.Numeric.Position;

/**
 * Represents a copybook data definition in memory
 * 
 * <p>acts as a Group element but as a special parent
 * being the copybook itself
 * 
 * @author James Watson
 */
public class Copybook extends Group implements Settings
{
    private String encoding = Settings.DEFAULT.getEncoding();
    private boolean littleEndian = Settings.DEFAULT.getLittleEndian();
    private String floatConversion = Settings.DEFAULT.getFloatConversion();
    private Numeric.Position signPosition = Settings.DEFAULT.getSignPosition();
    
    private Map redefines = new HashMap();
    
    private final Values values;
    
    /**
     * constructor
     *
     * @param name the name of the copybook
     */
    Copybook(String name, Values values)
    {
        super(name, 0, 0);
        
        this.values = values;
        values.setEncoding(encoding);
    }
    
    public Values getValues()
    {
        return values;
    }
    
    /**
     * redefined an element.  This behavior is partial, untested and
     * not officially supported (yet)
     * 
     * @param main the name of the original element
     * @param alias the new name
     */
    void redefine(String main, Element alias)
    {
        redefines.put(main, alias);
    }
    
    /**
     * Gets an aliased element. This behavior is partial, untested and
     * not officially supported (yet)
     * 
     * @param name i'm not even sure anymore
     * @return who knows?
     */
    public Element getAliased(String name)
    {
        return (Element) redefines.get(name);
    }
    
    /**
     * creates a new empty application data instance
     * 
     * @return a new empty application data instance
     */
    public Record createNew()
    {
        return new Record(getName(), (GroupData) super.create());
    }
    
    /**
     * creates a new application data element with the given data
     * 
     * @param data the data to create the instance for
     * @return a new application data element with the given data
     * @throws IOException
     */
    public Record parseData(byte[] data) throws IOException
    {
        return new Record(getName(), (GroupData) parse(data));
    }
    
    public List parseData(InputStream stream) throws IOException
    {
        ByteBuffer buffer = new ByteBuffer(stream);        
        List list = new ArrayList();
        
        while (buffer.hasNext()) {
            list.add(new Record(getName(), (GroupData) parse(buffer.getNext())));
        }
        
        return list;
    }
    
    /**
     * Sets the encoding for the copybook instance, used for parsing
     * and writing of data
     * 
     * @param encoding the encoding for the system
     */
    public void setEncoding(String encoding)
    {
        this.encoding = encoding;
    }
    
    /**
     * retrieves the current encoding for text
     * 
     * @return the encoding for text
     */
    public String getEncoding()
    {
        return encoding;
    }
    
    public void setLittleEndian(boolean littleEndian)
    {
        this.littleEndian = littleEndian;
    }
    
    public boolean getLittleEndian()
    {
        return littleEndian;
    }
    
    public void setFloatConversion(String className)
    {
        this.floatConversion = className;
    }
    
    public String getFloatConversion()
    {
        return floatConversion;
    }
    
    public void getSignPosition(Position position)
    {
        this.signPosition = position;
    }
    
    public Position getSignPosition()
    {
        return signPosition;
    }
    
    /**
     * a helper class for buffering the data as it is processed
     * 
     * @author James Watson
     */
    public class ByteBuffer
    {
        // TODO allow strings, length delimiting
//        private static final char DELIMITER = '\n';
        private int position = 0;
        private byte[] internal = new byte[1024];
        private int size = 0;
        private final InputStream stream;
        
        public ByteBuffer(InputStream stream) throws IOException
        {
            this.stream = stream;
        }
        
        private boolean getMore()
        {
            try {
                byte[] array = new byte[1024];
                int read = 0;
                
                while ((read = stream.read(array)) >= 0) {
                    add(array, read);
                    
                    if (read >= getLength()) return true;
                }
                
                return read >= getLength();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        public void add(byte[] bytes, int length)
        {
            int space = internal.length - size;
            
            if (space < bytes.length) {
                byte[] temp = new byte[Math.min(internal.length * 2, internal.length + bytes.length)];
                System.arraycopy(internal, 0, temp, 0, size);
                internal = temp;
            }
            
            System.arraycopy(bytes, size, internal, size, length);
            
            size += length;
        }
        
        public boolean hasNext()
        {
            if (!(position < size)) {
                if (!getMore()) return false;
            }
            
            return true;
        }
        
        private int nextEnd()
        {
            if (!hasNext()) return -1;
            
            int next = position + getLength();
            
            return next > size ? size : next;
        }
        
        public byte[] getNext()
        {
            byte[] bytes = new byte[size];
            
            int end = nextEnd();
            
//            System.out.println("next end: " + end);
//            System.out.println("position: " + position);
            
            System.arraycopy(internal, position, bytes, 0, end - position);
            position = end;
            
            return bytes;
        }
    }
}