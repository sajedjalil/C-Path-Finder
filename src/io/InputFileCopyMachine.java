
package io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class InputFileCopyMachine {
	
	public InputFileCopyMachine(String sourcePath, String destinationPath) {
		
		makeMainOutputDirectory(destinationPath);
		copyDirectory(sourcePath, destinationPath);
	}
	
	
	
	private void makeMainOutputDirectory(String destinationPath) {
		
		File dir = new File(destinationPath);
		dir.mkdirs();
	}
	
	
	
	
	private void copyDirectory(String sourcePath, String destinationPath) {
		
		
		File source = new File(sourcePath);
		
		
		for(File a : source.listFiles()) {
			
			Path s = Paths.get(a.getPath());
			Path d = Paths.get(destinationPath+"\\"+a.getName());
			
			
			if( a.isDirectory() ) {
				
				try {
					
					if(!Files.exists(d))
						Files.copy(s, d, StandardCopyOption.REPLACE_EXISTING );
					
					copyDirectory(a.getPath(), destinationPath+"\\"+a.getName());
					
				} catch (IOException e) { e.printStackTrace(); }
				
				
			}
			else if( a.isFile() && (a.getName().endsWith(".c") || a.getName().endsWith(".h")) ) {
				
				try {
					Files.copy(s, d, StandardCopyOption.REPLACE_EXISTING );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
}
