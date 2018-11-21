package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import parser.CParser;

import java.io.File;
import java.io.IOException;

import inputCodeBeautifier.CodeBeautifier;
import inputFileLoader.InputFileCopyMachine;

public class Start extends Application{

	
	public static Stage primaryStage;
	
	
	public static String inputPath; 
	public static String outputPath = "results"; 
	
	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	@Override
	public void start(Stage primaryStage) throws IOException{
		
		Start.primaryStage = primaryStage;
		Start.primaryStage.setTitle("C Path Finder");
		
		
		showMainView();
	}
		
	
	private void showMainView() {
		
		try {
			AnchorPane root = FXMLLoader.load(getClass().getResource("MainView.fxml"));
			Scene scene = new Scene(root,550,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String openSystemPathChooser() {
		
		return null;
	}
	
	public static void initialize() {
		long startTime = System.nanoTime();
		/** */
		new InputFileCopyMachine(inputPath, outputPath);
		
		long endTime   = System.nanoTime();
		System.out.println(endTime-startTime);
		/** */
		new CodeBeautifier( new File(outputPath));
		
		endTime = System.nanoTime();
		System.out.println(endTime-startTime);
		
		new CParser(new File(outputPath));
	}
}
