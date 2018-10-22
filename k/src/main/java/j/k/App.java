package j.k;

import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import imports.CLexer;
import imports.CParser;
import imports.CParser.CompilationUnitContext;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        new App();
    }
    
    
    public App() {
    	compile();
	}
    
    
    public void compile() {
		

        CLexer lexer;
		try {
			lexer = new CLexer(CharStreams.fromFileName("src/main/1.c"));
			CommonTokenStream tokens = new CommonTokenStream(lexer);

	        CParser parser = new CParser(tokens);
	        
	        ParseTree tree = parser.compilationUnit();
	        ParseTreeWalker walker = new ParseTreeWalker();
	        
	        MyListener listener = new MyListener();
	        walker.walk(listener, tree);
	        
	        
	        System.out.println(tree.toStringTree(parser));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        
    	

	}
}

