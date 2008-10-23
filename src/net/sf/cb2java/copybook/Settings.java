package net.sf.cb2java.copybook;

import java.util.Properties;

import net.sf.cb2java.copybook.Numeric.Position;

public interface Settings
{
    public static Settings DEFAULT = new Default();
    
    String getEncoding();
    
    Values getValues();
    
    boolean getLittleEndian();
    
    String getFloatConversion();
    
    Numeric.Position getSignPosition();
    
    public static class Default implements Settings
    {
        private static final String DEFAULT_ENCODING;
        private static final boolean DEFAULT_LITTLE_ENDIAN;
        private static final String DEFAULT_FLOAT_CONVERSION;
        private static final Numeric.Position DEFAULT_DEFAULT_SIGN_POSITION;
        private static final Values DEFAULT_VALUES = new Values();
        
        static {
            Properties props = new Properties();
            
            try {
                props.load(ClassLoader.getSystemResourceAsStream("copybook.props"));
            } catch (Exception e) {
                // TODO logging
            }  
            
            DEFAULT_ENCODING = getSetting("encoding", System.getProperty("file.encoding"), props);
            DEFAULT_LITTLE_ENDIAN = "false".equals(getSetting("little-endian", "false", props));
            DEFAULT_FLOAT_CONVERSION = getSetting("float-conversion", 
                "net.sf.cb2java.copybook.floating.IEEE754", props);
            DEFAULT_DEFAULT_SIGN_POSITION = "leading".equalsIgnoreCase(
                getSetting("default-sign-position", "trailing", props)) ? Numeric.LEADING : Numeric.TRAILING;
        }
        
        private static final String getSetting(String name, String defaultValue, Properties props)
        {
            try {
                String value = System.getProperty("cb2java." + name, defaultValue);
                
                try {
                    try {
                        value = props.getProperty("encoding", value);
                    } catch (Exception e) {
                        // TODO logging
                    }
                } catch (Exception e) {
                    // TODO logging
                }
                
                return value;
            } catch (Exception e) {
                return defaultValue;
            }
        }
        
        public String getEncoding()
        {
            return DEFAULT_ENCODING;
        }

        public String getFloatConversion()
        {
            return DEFAULT_FLOAT_CONVERSION;
        }

        public boolean getLittleEndian()
        {
            return DEFAULT_LITTLE_ENDIAN;
        }

        public Values getValues()
        {
            return DEFAULT_VALUES;
        }

        public Position getSignPosition()
        {
            return DEFAULT_DEFAULT_SIGN_POSITION;
        }
    }
}
