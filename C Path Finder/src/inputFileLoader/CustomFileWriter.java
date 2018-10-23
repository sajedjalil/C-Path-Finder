package inputFileLoader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CustomFileWriter {
	
	public static void writeAFile(File currentFile ,ArrayList<String> lines) {
		
		BufferedWriter bw = null;
		FileWriter fw = null;

		try {

			
			fw = new FileWriter(currentFile);
			bw = new BufferedWriter(fw);

			for(String s: lines) {
				bw.write(s);
				bw.newLine();
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
}
