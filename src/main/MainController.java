package main;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javafx.application.Platform;
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
	@FXML Text status;
	@FXML Text resultDirectory;
	@FXML Text copyright;
	
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
		    
		}
	}
	
	
	@FXML
	private void runFiles() {
		
		runProgress.setProgress(0.00);
		status.setText("Processing Source Files");
		
		runProgress.setVisible(true);
	    status.setVisible(true);
		
		
		
		
		
		
		new Thread(){
            public void run() {
            	
            	ArrayList<String> changedFiles = Start.runInit();
            	Platform.runLater(() -> runProgress.setProgress( 0.1));
            	
            	
        		final double step = 0.9 / changedFiles.size(); 
        		
        		for(int i=0; i<changedFiles.size(); i++) {
        			
        			final int temp = i;
        			
        			final ExecutorService service = Executors.newSingleThreadExecutor();

        	        try {
        	            final Future<Object> f = service.submit(() -> {
        	            	new CParser(changedFiles.get(temp));
        	                Thread.sleep(100); // Simulate some delay
        	                return "42";
        	            });

        	            System.out.println(f.get(10, TimeUnit.SECONDS));
        	        } catch (final TimeoutException e) {
        	            System.err.println("Calculation took to long");
        	        } catch (final Exception e) {
        	            throw new RuntimeException(e);
        	        } finally {
        	            service.shutdown();
        	        }
        			
        			
        			final double adder = (i+1)*step;
        			
        			Platform.runLater(() -> runProgress.setProgress( 0.1 + adder ));
        			status.setText("Analyzing: "+changedFiles.get(i));
                   
        		}
        		
        		Platform.runLater(() -> runProgress.setProgress( 1.0 ));
        		status.setText("Done");
        		Start.deleteTempFiles();
        		Platform.runLater(() -> runButton.setText("Re-Run") );
        		
        		openResultButton.setVisible(true);
        		resultDirectory.setVisible(true);
        		
            }
        }.start();
		
		
		
		
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
	
	@FXML
	private void exitProgram() {
		
		System.exit(0);
		
	}
	
	@FXML
	private void openAbout() {
		
		if (Desktop.isDesktopSupported()) {
		    try {
		        File myFile = new File("Documentation\\About.txt");
		        Desktop.getDesktop().open(myFile);
		    } catch (IOException ex) {
		        // no application registered for PDFs
		    }
		}
	}
	
	
	@FXML
	private void openTutorial() {
		
		if (Desktop.isDesktopSupported()) {
		    try {
		        File myFile = new File("Documentation\\User Manual.pdf");
		        Desktop.getDesktop().open(myFile);
		    } catch (IOException ex) {
		        // no application registered for PDFs
		    }
		}
	}
}
