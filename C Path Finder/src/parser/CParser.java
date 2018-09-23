package parser;

import java.io.File;
import java.util.ArrayList;

import inputFileLoader.CustomFileReader;

public class CParser {

	public CParser() {
		
		ArrayList<String> lines = CustomFileReader.readAfile(new File("E:\\Results\\chon.c"));

		new ComponentSeparator(lines);
	}
	
	
	public static void main(String[] args) {
		new CParser();
	}
}
