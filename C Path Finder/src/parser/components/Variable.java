package parser.components;

import java.util.ArrayList;
import java.util.Arrays;

public class Variable {
	
	private String dataType = "";
	private String name = "";
	private String actualValue = "";
	private String smbolicValue = "";
	
	public String getDataType() {
		return dataType;
	}
	
	public String getName() {
		return name;
	}
	
	public Variable( String line ) {
		
		parseLine(line);
	}
	
	
	private void parseLine(String line) {
		
		/** trim to remove extra spaces from first and last
		 * split on the basis of one or multiple space between
		 */
		ArrayList<String> words =  new ArrayList<>( Arrays.asList( line.trim().split(" +") ) );
		int size = words.size();
		
		if( words.contains("=")) {
			
		}
		else {
			
			//System.out.println(words.size());
			for(int i=0; i<size-1; i++) dataType += (words.get(i)+" "); //variable type
			dataType = dataType.trim(); // trim the last space
			
			name = words.get(size-1); //last element is name;
			
		}
	}
}
