package net.sf.cb2java.copybook;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import net.sf.cb2java.copybook.data.GroupData;
import net.sf.cb2java.copybook.data.Record;

/**
 * Represents a copybook data definition in memory
 * 
 * <p>acts as a Group element but as a special parent
 * being the copybook itself
 * 
 * @author James Watson
 */
public class Copybook extends Group
{
    public static final String DEFAULT_ENCODING;
    public static final boolean DEFAULT_LITTLE_ENDIAN;
    public static final String DEFAULT_FLOAT_CONVERSION;
    
    private String encoding = DEFAULT_ENCODING;
    private boolean littleEndian = DEFAULT_LITTLE_ENDIAN;
    private String floatConversion = DEFAULT_FLOAT_CONVERSION;
    
    private Map redefines = new HashMap();
    
    protected Values values;
    
    /* loads the default encoding for this class */
    static {
        Properties props = new Properties();
        
        String default_encoding = System.getProperty("cb2java.encoding", System.getProperty("file.encoding"));
        String default_little_endian = System.getProperty("cb2java.little-endian", "false");
        String default_float_conversion = System.getProperty("cb2java.float-conversion", "net.sf.cb2java.copybook.floating.IEEE754");
        
        try {
            props.load(ClassLoader.getSystemResourceAsStream("copybook.props"));
            
            try {
                default_encoding = props.getProperty("encoding", default_encoding);
            } catch (Exception e) {
                // TODO logging
            }
            
            try {
                default_little_endian = props.getProperty("little-endian", default_little_endian);
            } catch (Exception e) {
                // TODO logging
            }
            
            try {
                default_float_conversion = props.getProperty("float-conversion", default_little_endian);
            } catch (Exception e) {
                // TODO logging
            }
        } catch (Exception e) {
            // TODO logging
        }
        
        DEFAULT_ENCODING = default_encoding;
        DEFAULT_LITTLE_ENDIAN = Boolean.valueOf(default_little_endian).booleanValue();
        DEFAULT_FLOAT_CONVERSION = default_float_conversion;
    }
    
    /**
     * constructor
     *
     * @param name the name of the copybook
     */
    Copybook(String name)
    {
        super(name, 0, 0);
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
    
//    private static void display(byte[] bytes)
//    {
//        for (int i = 0; i < bytes.length; i++) {
//            byte b = bytes[i];
//            String s = "0" + Integer.toHexString(0xFF & b);
//            System.out.print(s.toUpperCase().substring(s.length() - 2));
//            System.out.print(' ');
//        }
//    }
    
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
    
    /**
     * creates a new application data element with the given data
     * 
     * @param reader the data to create the instance for
     * @return a new application data element with the given data
     * @throws IOException
     */
    public Record parseData(InputStream reader) throws IOException
    {
        ByteBuffer buffer = new ByteBuffer();
        byte[] array = new byte[1024];
        int read = 0;
        
        while ((read = reader.read(array)) >= 0) {
            buffer.add(array, read);
        }
        
        return new Record(getName(), (GroupData) parse(buffer.get()));
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
     * retrieves the current encoding for the copybook
     * 
     * @return
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
    
    /**
     * a helper class for buffering the data as it is processed
     * 
     * @author James Watson
     */
    public static class ByteBuffer
    {
        private byte[] internal = new byte[1024];
        private int size = 0;
        
        public void fill(InputStream stream) throws IOException
        {
            byte[] array = new byte[1024];
            int read = 0;
            
            while ((read = stream.read(array)) >= 0) {
                add(array, read);
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
        
        public byte[] get()
        {
            byte[] bytes = new byte[size];
            
            System.arraycopy(internal, 0, bytes, 0, size);
            
//            System.out.print(size + " bytes from buffer: ");
            
            return bytes;
        }
    }
}