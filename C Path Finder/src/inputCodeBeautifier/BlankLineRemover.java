package inputCodeBeautifier;

import java.util.ArrayList;

public class BlankLineRemover {
	
public static ArrayList<String> removeBlankLines( ArrayList<String> fileText){
		
		ArrayList<String> temp = new ArrayList<String>();
		
		for(String s: fileText) {
			if( s.trim().length() > 0 ) {
				temp.add(s);
			}
		}
		
		return temp;
	}
}
