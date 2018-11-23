package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import parser.CParser;

public class MainController {
	
	@FXML Button openFileButton;
	@FXML Button runButton;
	@FXML Button openResultButton;
	
	@FXML Text openFilePath;
	@FXML ProgressBar runProgress;
	@FXML Text resultDirectory;
	
	@FXML
	private void selectFiles() {
		
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(Start.primaryStage);

		if(selectedDirectory == null){
		    System.out.println("null");
		}else{
		     
			openFilePath.setText(selectedDirectory.getAbsolutePath());
		     
		    openFileButton.setText("Change");
		    runButton.setText("Run");
		     
		    Start.inputPath = selectedDirectory.getAbsolutePath();
		    
		    runButton.setVisible(true);
		    runProgress.setVisible(true);
		}
	}
	
	
	@FXML
	private void runFiles() {
		
		Start.run();
		
		runButton.setText("Re-Run");
		
		openResultButton.setVisible(true);
		resultDirectory.setVisible(true);
	}
	
	
	@FXML
	private void changeOutputDirectory() {
		
		DirectoryChooser directoryChooser = new DirectoryChooser();
		File selectedDirectory = directoryChooser.showDialog(Start.primaryStage);

		if(selectedDirectory == null){
		    System.out.println("null");
		}else{
		     
			System.out.println(selectedDirectory.getAbsolutePath());
			CParser.testCaseOutputDirectory = selectedDirectory.getAbsolutePath();
			
			resultDirectory.setText(CParser.testCaseOutputDirectory);
		}
		
	}
	
	
	@FXML
	private void openOutputDirectory() {
		
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open( new File(CParser.testCaseOutputDirectory) );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
