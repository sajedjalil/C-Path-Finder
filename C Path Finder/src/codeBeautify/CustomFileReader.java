package codeBeautify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CustomFileReader {
	
	public static ArrayList<String> readAfile( File currentFile){
		
		ArrayList<String> temp = new ArrayList<String>();
		
		BufferedReader br = null;
		FileReader fr = null;

		try {

			
			fr = new FileReader(currentFile);
			br = new BufferedReader(fr);

			String line;

			while ((line = br.readLine()) != null) {
				temp.add(line);
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
		
		return temp;
	}
}
