package net.sf.cb2java.copybook;

public interface Settings
{
    String getEncoding();
    
    Values getValues();
    
    boolean getLittleEndian();
    
    String getFloatConversion();
}
