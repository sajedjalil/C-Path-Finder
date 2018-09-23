package parser;

import java.util.ArrayList;

public class ComponentSeparator {
	
	private ArrayList<String[]> save = new ArrayList<String[]>();
	private ArrayList<String> lines;
	
	public ComponentSeparator( ArrayList<String> lines) {
		
		preSave(lines);
		
		getComponent();
	}
	
	
	private void getComponent() {
		
		for(int i=0; i<save.size(); i++) {
			
			if( checkifPreprocessor(i) == true ) {
				
				int k = preprocessorRange(i);
				System.out.println("Preprocessor: "+ (i+1) +  " " + (i+k+1) );
				i += k;
			}
			else if( checkifMethod(i) == true ){
				
				
				int k = getMethodRange(i);
				System.out.println("Methods: " + (i+1) +  " " + (i+k+1) );
				i+=k;
				
			}
			else {
				
				int k = getOtherRange(i);
				System.out.println("Others: "+(i+1) + " " + (k+i+1));
				i+=k;
			}
			
		}
		
	}
	
	
	
	private int getOtherRange(int lineNumber) {
		
		int counter = lineNumber;
		int singleBrace = 0;
		
		for( ; lineNumber < lines.size(); lineNumber++) {
			
			String temp = lines.get(lineNumber);
			
			for(int i=0; i<temp.length(); i++) {
				
				if(temp.charAt(i) == ';' && singleBrace == 0) return lineNumber-counter;
				else if( temp.charAt(i) == '{' ) singleBrace++;
				else if( temp.charAt(i) == '}' ) singleBrace--;
			}
			
		}
		
		return 0;
	}
	
	
	
	private int getMethodRange(int lineNumber) {

		int braceCounter = 0;
		
		
		
		for(int i=lineNumber; i < lines.size(); i++ ) {
			
			String temp = lines.get(i);
			
			for( int j=0; j<temp.length(); j++) {
				
				if( temp.charAt(j) == '{' ) {
					braceCounter++;
				}
				else if( temp.charAt(j) == '}' ) {
					
					braceCounter--;
					if(braceCounter == 0) {
						return i-lineNumber;
					}
				}
				else if(braceCounter == 0 && temp.charAt(j) == ';') {
					//method signature declaration tracker
					return i-lineNumber;
				}
				
			}
			
		}
		
		return 0;
		
	}
		
	private Boolean checkifMethod(int lineNumber) {
		
		while( lineNumber < lines.size() ) {
			
			//System.out.println(lineNumber);
			String temp = lines.get(lineNumber);
			int len = temp.length();
			
			
			for(int i=0; i<len; i++) {
				
				if(temp.charAt(i) == '(') return true;
				else if(temp.charAt(i) == ';' ) return false;
				else if( temp.charAt(i) == '{' ) return false;
			}
			
			lineNumber++;
		}
		
		return false;
		
	}
	
	private Boolean ifConditionalPreprocessor(int lineNumber) {
		
		String specialConcerned [] = {"#if", "#ifdef", "#ifndef"};
		
		String firstTerm = "";
		if( save.get(lineNumber)[0].length() == 1 ) firstTerm = save.get(lineNumber)[0] + save.get(lineNumber)[1];
		else firstTerm = save.get(lineNumber)[0];
		
		for(int i=0; i<specialConcerned.length; i++) {

			if(firstTerm.equals(specialConcerned[i])) {
				return true;
			}
		}
		
		return false;
	}
	
	private Boolean ifEndConditionalPreprocessor(int lineNumber) {
		
		String specialConcerned = "#endif";
		
		String firstTerm = "";
		if( save.get(lineNumber)[0].length() == 1 ) firstTerm = save.get(lineNumber)[0] + save.get(lineNumber)[1];
		else firstTerm = save.get(lineNumber)[0];
		
		if(firstTerm.equals(specialConcerned)) return true;
		
		return false;
	}
	
	private int getConditionalPreprocessorRange(int lineNumber) {
		
		int conditionLevelCounter = 0; 
		
		while( lineNumber < save.size() ) {

			if( save.get(lineNumber)[0].startsWith("#") ) {
				
				if( ifConditionalPreprocessor(lineNumber) == true ) {
					conditionLevelCounter++;
				}
				else if( ifEndConditionalPreprocessor(lineNumber) == true ) {
					conditionLevelCounter--;
					
					if(conditionLevelCounter == 0) {
						return lineNumber;
					}
				}
			}
			
			
			lineNumber++;
		}
		
		return lineNumber;
	}
	
	private int preprocessorRange(int lineNumber) {
		
		// if it contains #if #ifdef #ifndif
		if(ifConditionalPreprocessor(lineNumber) == true) {
			//System.out.println(lineNumber);
			return getConditionalPreprocessorRange(lineNumber) - lineNumber;
		}
		
		
		//others - #include #define #pragma #error
		int counter = 0;
		
		while( save.get(lineNumber)[ save.get(lineNumber).length -1 ] .endsWith("\\") ) {
			lineNumber++;
			counter++;
		}
		
		return counter;
	}
	
	private Boolean checkifPreprocessor(int lineNumber) {
		
		if( save.get(lineNumber)[0].startsWith("#") ) return true;
		else return false;
	}
		
	
	
	private void preSave(ArrayList<String> lines) {
		this.lines = lines;
		
		for(String s: lines) {
			String temp[] = s.split(" ");
			save.add(temp);
		}
	}
	
	
	
	
	
}
