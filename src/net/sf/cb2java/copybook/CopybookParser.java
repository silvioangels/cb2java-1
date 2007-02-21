package net.sf.cb2java.copybook;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;

import net.sf.cb2xml.DebugLexer;
import net.sf.cb2xml.sablecc.lexer.Lexer;
import net.sf.cb2xml.sablecc.node.Start;
import net.sf.cb2xml.sablecc.parser.Parser;

/**
 * 
 * This class is the starting point for parsing copybooks
 * 
 * <p>To parse or create data, you need to first parse the
 * copybook.  The returned CopyBook instance will allow for 
 * working with data.
 * 
 * @author James Watson
 */
public class CopybookParser
{
    /** turns debugging on/off I want to replace this with a logging solution */
    private static boolean debug = false;
    
    /**
     * Parses a copybook defintion and returns a Copybook instance
     * 
     * @param name the name of the copybook.  For future use.
     * @param stream the copybook definiton's source stream
     * 
     * @return a copybook instance containg the parse tree for the definition
     */
    public static Copybook parse(String name, InputStream stream)
    {        
        return parse(name, new InputStreamReader(stream));
    }
    
    /**
     * Parses a copybook defintion and returns a Copybook instance
     * 
     * @param name the name of the copybook.  For future use.
     * @param reader the copybook definiton's source reader
     * 
     * @return a copybook instance containg the parse tree for the definition
     */
    public static Copybook parse(String name, Reader reader)
    {        
        Copybook document = null;
        Lexer lexer = null;
        String preProcessed = null;
        try {
            preProcessed = CobolPreprocessor.preProcess(reader);
            StringReader sr = new StringReader(preProcessed);
            PushbackReader pbr = new PushbackReader(sr, 1000);
            
            if (debug) {
                lexer = new DebugLexer(pbr);
            } else {
                lexer = new Lexer(pbr);
            }
            
            Parser parser = new Parser(lexer);
            Start ast = parser.parse();
            CopybookAnalyzer copyBookAnalyzer = new CopybookAnalyzer(name, parser);
            ast.apply(copyBookAnalyzer);
            document = copyBookAnalyzer.getDocument();
        } catch (Exception e) {
            throw new RuntimeException("fatal parse error\n"
                + (lexer instanceof DebugLexer 
                ? "=== buffer dump start ===\n"
                + ((DebugLexer) lexer).getBuffer()
                + "\n=== buffer dump end ===" : ""), e);
        }
        
        return document;
    }
}