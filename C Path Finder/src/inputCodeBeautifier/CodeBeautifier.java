package inputCodeBeautifier;

import java.io.File;
import java.util.ArrayList;

import inputFileLoader.CustomFileReader;
import inputFileLoader.CustomFileWriter;

public class CodeBeautifier {

	public CodeBeautifier(File sourceFilesDirectory) {
		
		directorySearcher(sourceFilesDirectory);
	}
	
	public void beautify(File currentFile) {
		
		ArrayList<String> lines = CustomFileReader.readAfile(currentFile);
		
		/**Faster Approach
		 * Manual parsing 
		 */
		lines = CommentRemover.removeComments(lines);
		lines = BlankLineRemover.removeBlankLines(lines);
		
		CustomFileWriter.writeAFile(currentFile, lines);
		
		/**
		 * Slower almost 15 times
		 * GCC implementation invoke
		 */
		//LineRemover.removeIncludeStatementLine(a);
		//new CmdExecutor(currentDirectory.getPath(), a.getName(), a.getName());
		//LineRemover.removeUnnecessaryLine(a);
	}
	
	private void directorySearcher(File currentDirectory) {
		
		for(File file: currentDirectory.listFiles()) {
			
			if(file.isDirectory()) {
				directorySearcher(file);
			}
			else if(file.isFile()) {
				
				beautify(file);
			}
		}
	}
	
	
	
}
