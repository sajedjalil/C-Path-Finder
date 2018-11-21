package main;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

public class MainController {
	
	@FXML Button openFileButton;
	@FXML Button runButton;
	@FXML Button openResultButton;
	
	@FXML Text openFilePath;
	@FXML ProgressBar runProgress;
	
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
		}
	}
	
	
	@FXML
	private void runFiles() {
		
		Start.initialize();
		
		runButton.setText("Re-Run");
		
	}
}
