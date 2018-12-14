package parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import io.CustomFileWriter;
import parser.components.*;

public class CParser {
	
	public static String testCaseOutputDirectory = "testcases";
	
	public CParser(File file) {
		
		makeMainOutputDirectory();
		
		//directorySearcher( file );
		
		
	}
	
	public CParser(String changedFilePath) {
				
		
		File file = new File(changedFilePath);
		ObjectFile ob = processSingleFile(file);
		outputTestCase(ob);
	}
	
	
	private void makeMainOutputDirectory() {
		try {
			
			if( !Files.exists( Paths.get(testCaseOutputDirectory)) ) 
				Files.createDirectory(Paths.get(testCaseOutputDirectory));
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	/*
	public static void main(String[] args) {
		new CParser(new File("results"));
	}
	*/
	/*
	private void directorySearcher(File currentDirectory) {
		
		for(File file: currentDirectory.listFiles()) {
			
			if(file.isDirectory()) {
				
				directorySearcher(file);
			}
			else if(file.isFile()) {
				
				//parse single file
				ObjectFile ob = processSingleFile(file);
				outputTestCase(ob);
			}
		}
	}
	*/
	
	private void outputTestCase(ObjectFile objectFile) {
		
		ArrayList<String> results = new ArrayList<String>();
		
		results.add("File Name: "+objectFile.fileName);
		results.add("File Path: "+objectFile.filePath);
		results.add("\n\n\n");
		
		
		int methodNumber = 0;
		
		for( ArrayList<String> temp : objectFile.result.testcases) {
			
			//add method name
			results.add("Method Name: "+objectFile.result.methodNames.get(methodNumber++));
			
			for( int i=0; i<temp.size(); i++) {
				results.add("Testcase "+(i+1)+": ");
				results.add(temp.get(i));
			}
			
			results.add("\n");
		}
		
		String fileName = objectFile.fileName.replace(".c", ".txt");
		
		File dir = new File(testCaseOutputDirectory);
		dir.mkdirs();
		
		CustomFileWriter.writeAFile( new File(testCaseOutputDirectory+"\\"+fileName) , results);
		
	}
	
	
	private ObjectFile processSingleFile(File file) {
		
		//System.out.println(file.getPath());
		return new ObjectFile(file);
		//System.out.println( myFile.result.testcases );
	}
}
