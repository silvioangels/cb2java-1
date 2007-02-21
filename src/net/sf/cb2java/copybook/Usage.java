package net.sf.cb2java.copybook;

public class Usage
{
    private Usage() {}
    
    public static final Usage BINARY = new Usage();
    
    public static final Usage COMPUTATIONAL = new Usage(); // binary 
    
    public static final Usage COMPUTATIONAL_1 = new Usage(); // single precision float
    
    public static final Usage COMPUTATIONAL_2 = new Usage(); // double precision float
    
    public static final Usage COMPUTATIONAL_3 = new Usage(); // packed
    
    public static final Usage COMPUTATIONAL_4 = new Usage(); // binary
    
    public static final Usage COMPUTATIONAL_5 = new Usage(); // binary or comp-5
    
//    public static final Usage DISPLAY = new Usage(); // uh, who cares?
//    
    public static final Usage DISPLAY_1 = new Usage();
    
    public static final Usage INDEX = new Usage(); // 4 bytes
    
    public static final Usage NATIONAL = new Usage();  // not supported
    
    public static final Usage OBJECT_REFERENCE = new Usage(); // ???
    
    public static final Usage PACKED_DECIMAL = new Usage(); 
    
    public static final Usage POINTER = new Usage(); // ??? binary?  how many bytes?
    
    public static final Usage PROCEDURE_POINTER = new Usage(); // ???
    
    public static final Usage FUNCTION_POINTER = new Usage(); // ???
}