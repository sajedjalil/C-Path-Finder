package parser.components;

import java.io.File;
import java.util.ArrayList;

import parser.ComponentSeparator;
import result.FileResult;
import symbolicSolver.SymbolicSolver;
import parser.*;
import controlFlowGraphBuilder.*;
import io.CustomFileReader;

public class ObjectFile {

	public ArrayList<Directive> directives = new ArrayList<Directive>();
	public ArrayList<Method> methods = new ArrayList<Method>();
	public ArrayList<UserDefinedData> userDefinedData = new ArrayList<UserDefinedData>();
	
	public String fileName = "";
	public String filePath = "";
	
	public ArrayList <String> fileData =  new ArrayList<String>();
	
	public FileResult result;
	
	
	
	public ObjectFile(File file) {
		
		init(file);
		
		separateMainComponents();
		parseMethods();
	}
	
	
	private void init(File file) {
		//read file contents
		fileData = CustomFileReader.readAfile( file );
				
		fileName = file.getName();
		filePath = file.getPath();
		
		result = new FileResult(file);
	}
	
	private void separateMainComponents() {
		ComponentSeparator cs = new ComponentSeparator(this);
		cs.getComponent(this);
		
	}
	
	
	private void parseMethods() {
		
		for(Method m: methods) {
			
			new MethodParser(m); //parse method
			new CFGBuilder(m); // build control flow graph
			
			m.findPaths();
			
			SymbolicSolver ss = new SymbolicSolver(m);
			result.methodNames.add(m.methodName);
			result.testcases.add( ss.testcases );
			
		}
	}


}
