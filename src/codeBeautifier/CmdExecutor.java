package codeBeautifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CmdExecutor {
	
	public CmdExecutor(String directory, String inputFileName, String outputFileName) {
		
		
		removeCommentsGCC( new File(directory), inputFileName, outputFileName);
	}
	
	public CmdExecutor(String directory, String inputFilePath) {
		this(directory, inputFilePath, inputFilePath);
	}
	
	private void removeCommentsGCC(File directory, String inputFileName, String outputFileName) {
		
		inputFileName = "\"" +inputFileName + "\"";
		
		//System.out.println(directory.getPath() + " "+ inputFileName);
        try {  

            Process p = Runtime.getRuntime().exec("cmd /C gcc -fpreprocessed -dD -E "
            		+ inputFileName, null, directory); 
            
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            
            ArrayList<String> temp = new ArrayList<String>();
            
            String line = null;  
            while ((line = in.readLine()) != null) temp.add(line);
            in.close();
            
            BufferedWriter out = new BufferedWriter(new FileWriter(directory+"\\"+outputFileName));
            
            for(String s: temp) {
            	out.write(s);
            	out.newLine();
            }
            
            temp.clear();
            out.close();
            
        } catch (IOException e ) {  
            e.printStackTrace();  
        }
        
	}
	
}
