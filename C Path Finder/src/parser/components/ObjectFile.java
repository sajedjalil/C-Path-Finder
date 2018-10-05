package parser.components;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import inputFileLoader.CustomFileReader;
import parser.ComponentSeparator;
import symbolicSolver.SymbolicSolver;
import parser.*;
import controlFlowGraphBuilder.*;

public class ObjectFile {

	public ArrayList<Directive> directives = new ArrayList<Directive>();
	public ArrayList<Method> methods = new ArrayList<Method>();
	public ArrayList<UserDefinedData> userDefinedData = new ArrayList<UserDefinedData>();
	
	public String fileName = "";
	public String filePath = "";
	
	public ArrayList <String> fileData =  new ArrayList<String>();
	
	
	
	
	
	public ObjectFile(File file) {
		
		//read file contents
		fileData = CustomFileReader.readAfile( file );
		
		fileName = file.getName();
		filePath = file.getPath();
		
		separateMainComponents();
		parseMethods();
	}
	
	
	private void separateMainComponents() {
		ComponentSeparator cs = new ComponentSeparator(this);
		cs.getComponent(this);
		
	}
	
	
	private void parseMethods() {
		
		for(Method m: methods) {
			
			new MethodParser(m); //parse method
			new CFGBulider(m);
			
			m.getLocalVariables();
			m.printPaths();
			
			/*
			for(int i=0; i<m.paths.size(); i++) {
				for(int j:m.paths.get(i)) System.out.print(j+" ");
				System.out.println();
			}  */
			//System.out.println(m.paths.size());
			//System.out.println(m.body);
			//for(Variable v: m.localVariables) System.out.println(v.getName()+" "+v.getActualValue()+" "+v.getDataType());
			new SymbolicSolver(m);
			//for(Variable v: m.localVariables) System.out.println(v.getName()+" "+v.getDataType()+" "+v.getSymbolicValue());
			//for(Variable v: m.parameters) System.out.println(v.getName()+" "+v.getActualValue()+" "+v.getSymbolicValue());
			
			
			//m.printPaths();
		}
	}


}
