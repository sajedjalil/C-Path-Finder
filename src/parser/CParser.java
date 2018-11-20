package parser;

import java.io.File;
import java.util.ArrayList;

import parser.components.*;

public class CParser {
	
	ArrayList<ObjectFile> files = new ArrayList<ObjectFile>(); // file infos
	
	public CParser(File file) {
		
		//ArrayList<String> lines = CustomFileReader.readAfile(new File("E:\\Results\\chon.c"));

		//new ComponentSeparator(lines);
		
		directorySearcher( file );
	}
	
	
	public static void main(String[] args) {
		new CParser(new File("results"));
	}
	
	
	private void directorySearcher(File currentDirectory) {
		
		for(File file: currentDirectory.listFiles()) {
			
			if(file.isDirectory()) {
				directorySearcher(file);
			}
			else if(file.isFile()) {
				
				//parse single file
				processSingleFile(file);
			}
		}
	}
	
	
	private void processSingleFile(File file) {
		
		//System.out.println(file.getPath());
		ObjectFile myFile = new ObjectFile(file);
		
		
		
		
		//System.out.println(myFile.methods);
	}
}
