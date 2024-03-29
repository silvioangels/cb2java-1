package net.sf.cb2java.copybook;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Properties;

/**
 * Very simple COBOL pre-processor that chops the left and right margins. 
 * Column start and end positions are configurable using a properties file. 
 * Linefeeds are retained as these are required by the main parser.
 * COBOL files typically contain some junk characters and 
 * comment indicators in the "margins" and this routine removes those. 
 * 
 * @author Peter Thomas
 */
public class CobolPreprocessor {

      public static String preProcess(Reader reader) {
        int columnStart = 6;
        int columnEnd = 72;
        
        try {
//            FileInputStream propsStream = new FileInputStream("cb2xml.properties");
            Properties props = new Properties();
//            props.load(propsStream);
//            propsStream.close();
            String columnStartProperty = props.getProperty("column.start");
            String columnEndProperty = props.getProperty("column.end");
            if (columnStartProperty != null) {
                columnStart = Integer.parseInt(columnStartProperty);
            }
            if (columnEndProperty != null) {
                columnEnd = Integer.parseInt(columnEndProperty);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        StringBuffer sb = new StringBuffer();

        try {
          BufferedReader buffer = new BufferedReader(reader);
          String s = null;
          while ((s = buffer.readLine()) != null) {         
            if (s.length() > columnStart) {
                int thisColumnStart = columnStart;
                if (s.charAt(columnStart) == '/') {
                    sb.append('*');
                    thisColumnStart++;
                }               
                if (s.length() < columnEnd) {
                    sb.append(s.substring(thisColumnStart));
                } else {
                    sb.append(s.substring(thisColumnStart, columnEnd));
                }   
            }
            sb.append("\n");
          }
        }
        catch (Exception e) {
          e.printStackTrace();
          return null;
        }
        
        return sb.toString();
      }
}