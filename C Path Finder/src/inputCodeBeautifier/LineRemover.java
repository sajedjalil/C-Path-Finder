package inputCodeBeautifier;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

class LineRemover {
	
	
	public static void removeIncludeStatementLine(File inputFile) {
		 
		List<String> out;
		try {
			out = Files.lines(inputFile.toPath())
			                        .filter(line -> ifIncludeStatementJunk(line) )
			                        .collect(Collectors.toList());
			
			
			
			Files.write(inputFile.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	private static Boolean ifIncludeStatementJunk(String line) {
		
		if( line.trim().length() == 0) return false;
		
		String [] temp = line.split(" ");
		
		if( temp.length > 0 ) {
			if( temp[0].startsWith("#include")) return false; //removes #include lines
		}
		
		return true;
	}
	
	
	
	public static void removeUnnecessaryLine(File inputFile) {
		 
		List<String> out;
		try {
			out = Files.lines(inputFile.toPath())
			                        .filter(line -> ifOtherJunk(line) )
			                        .collect(Collectors.toList());
			
			
			
			Files.write(inputFile.toPath(), out, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	
	private static Boolean ifOtherJunk(String line) {
		
		if( line.trim().length() == 0) return false;
		
		String [] temp = line.split(" ");
		
		if( temp.length > 0 ) {
			if( temp[0].startsWith("#") && !temp[0].startsWith("#define")) return false; //removes #include lines
			else if( temp[0].startsWith("#") && !temp[0].startsWith("#typedef")) return false;
		}
		
		return true;
	}
}
