package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.filechooser.FileSystemView;

import codeBeautifier.CodeBeautifier;
import database.DatabaseLoader;
import io.InputFileCopyMachine;

public class Start extends Application{

	
	public static Stage primaryStage;
	
	
	public static String inputPath; 
	public static String outputPath = "//C Path Finder//results";
	
	private static DatabaseLoader dbloader = new DatabaseLoader();
	public static int currentProjectId; 
	
	public static void main(String[] args) {
		
		loadNecessaryStuffs();
		
		launch(args);
	}
	
	private static void loadNecessaryStuffs() {
		
		String myDocuments = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
		outputPath = myDocuments + outputPath;
		
		
		
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws IOException{
		
		Start.primaryStage = primaryStage;
		Start.primaryStage.setTitle("C Path Finder");
		Start.primaryStage.getIcons().add( new Image("file:resources//icon16.png") );
	
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
	
	public static ArrayList<String> runInit(){
		
		dbloader.changedFiles = new ArrayList<String>();
		
		database();
		
		new InputFileCopyMachine(inputPath, outputPath);
		
		//for memorized execution
		dbloader.directorySearcher( new File(outputPath) );
		
		new CodeBeautifier( new File(outputPath));
		
		dbloader.loadIntoFileTable();
	
		return dbloader.getChangedFiles();
	}
	
	public static void deleteTempFiles() {
		try {
			Files.walk( Paths.get(outputPath) ).sorted(Comparator.reverseOrder())
		    .map(Path::toFile)
		    .forEach(File::delete);
			}catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	private static void database() {
		
		dbloader.insertINtoProjectTable(inputPath);
		currentProjectId = dbloader.getProjectId(inputPath);
		//dbloader.ifFileToBeRun(new File("E:\\GitHub\\C-Path-Finder\\test\\1.c"), "Wed Nov 21 13:17:12 BDT 2018");
		//dbloader.insertINtoFileTable("1.c", "results\\1.c", "Wed Nov 21 13:17:12 BDT 2018", "1");
		//dbloader.insertINtoFileTable("2.c", "results\\2.c", "Wed Nov 21 13:17:12 BDT 2018", "1");
		//dbloader.insertINtoFileTable("3.c", "results\\3.c", "Wed Nov 21 13:17:12 BDT 2018", "1");
		//dbloader.selectAllFile();
		
		//System.out.println( dbloader.getLastRunFromDB( new File("results\\1.c") ) );;
		
		
		
	}
}
