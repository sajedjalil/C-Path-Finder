package parser.components;

import java.io.File;
import java.util.ArrayList;

import inputFileLoader.CustomFileReader;
import parser.ComponentSeparator;
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
			//System.out.println(m.parameters.size());
		}
	}


}
