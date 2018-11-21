package result;

import java.io.File;
import java.util.ArrayList;

public class FileResult {
	
	
	public String filePath;
	public String fileName;
	
	public  ArrayList<String> methodNames = new  ArrayList<String>();
	public ArrayList< ArrayList<String>> testcases = new ArrayList< ArrayList<String>>();
	
	public FileResult(File file) {
		
		filePath = file.getPath();
		fileName = file.getName();
	}
	
}
