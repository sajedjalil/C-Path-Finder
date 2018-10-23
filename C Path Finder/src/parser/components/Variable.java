package parser.components;

import java.util.ArrayList;
import java.util.Arrays;

public class Variable {
	
	private String dataType = "";
	private String name = "";
	private String actualValue = "";
	public String line = "";
	
	public String getDataType() {
		return dataType;
	}
	
	public String getName() {
		return name;
	}
	
	public String getActualValue() {
		return actualValue;
	}
	
	public void setActualValue(String actualValue) {
		this.actualValue = actualValue;
	}
	
	public Variable( String line ) {
		this.line = line;
		parseLine(line);
	}
	
	
	private void parseLine(String line) {
		
		/** trim to remove extra spaces from first and last
		 * split on the basis of one or multiple space between
		 */
		ArrayList<String> words =  new ArrayList<>( Arrays.asList( line.trim().split(" +") ) );
		int size = words.size();
		
		if( words.contains("=")) {
			
			
			String beforeEqualSign[] = line.split("=")[0].trim().split(" +");
			String afterEqualSign = line.split("=")[1].trim();
			
			//System.out.println(words.size());
			for(int i=0; i<beforeEqualSign.length-1; i++) dataType += (beforeEqualSign[i]+" "); //variable type
			dataType = dataType.trim(); // trim the last space
			name = beforeEqualSign[beforeEqualSign.length-1]; //last element is name;
			
			actualValue = afterEqualSign;
			
		}
		else {
			
			//System.out.println(words.size());
			for(int i=0; i<size-1; i++) dataType += (words.get(i)+" "); //variable type
			dataType = dataType.trim(); // trim the last space
			
			name = words.get(size-1); //last element is name;
			
		}
	}
}
