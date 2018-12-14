package database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.filechooser.FileSystemView;

import main.Start;
import parser.CParser;
//-Djava.library.path=”z3\bin;${env_var:PATH}”
public class DatabaseLoader {
	
	public static String defaultDatabaseDirectory = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()
			+"//C Path Finder//db//";
	public static String databaseName = "cache.db";
	
	public ArrayList<String> changedFiles;
	
	public DatabaseLoader() {
		
		makeDefaultDirectory();
		
		createDatabase();
		createProjectTable();
		createFileTable();
		
		changedFiles = new ArrayList<String>();
		/*
		insertINtoProjectTable("lala");
		insertINtoProjectTable("kala");
		
		selectAll();
		
		
		insertINtoFileTable("a", "a", "a", "1");
		insertINtoFileTable("b", "b", "b", "1");
		selectAllFile(); */
	}
	
		
	
	private void createFileTable() {
		
        String sql = "CREATE TABLE IF NOT EXISTS file (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	fileName text NOT NULL,\n"
                + "	filePath text NOT NULL,\n"
                + "	lastRun text NOT NULL,\n"
                + "	outputPath text NOT NULL,\n"
                + "	projectID integer,\n"
                + " FOREIGN KEY (projectID)"
                + "    REFERENCES project(id)"
                + ");";
        
        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            
            System.out.println("Table file created");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
	}
	
	public void selectAllFile(){
        String sql = "SELECT * FROM file where projectID='"+Start.currentProjectId+"'";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                				rs.getString("fileName") +  "\t" +
                				rs.getString("filePath") +  "\t" +
                				rs.getString("lastRun") +  "\t" +
                				rs.getString("outputPath") +  "\t" +
                                rs.getInt("projectID") + "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	public void insertINtoFileTable(String fileName, String filePath, 
			String lastRun, String projectID, String outputPath) {
		
		String sql = "select filePath from file where filePath = '"+filePath+"'";
		System.out.println(sql);
		try (Connection conn = this.connect();
	             Statement stmt  = conn.createStatement();
	             ResultSet rs    = stmt.executeQuery(sql)){
	           	
	            while (rs.next()) {
	            	//System.out.println(rs.getInt(""));
	            	sql = "update file set lastRun = '"+lastRun+"', outputPath ='"+
	            			outputPath+"' where filePath = '"+filePath+"'";
	            	PreparedStatement pstmt = conn.prepareStatement(sql);
	            	pstmt.executeUpdate();
	            	return;
	            }
	    } catch (SQLException e) {
	            System.out.println(e.getMessage());
	    }
        
        sql = "INSERT INTO file (fileName, filePath, lastRun, projectID, outputPath)\n" + 
        		"VALUES ( '"+fileName+"', '"+filePath+"', '"+lastRun+"', '"
        		+projectID+"', '"+outputPath+"' );";
        
        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            
            System.out.println("Inserted: "+fileName);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
	}
	
	public void selectAll(){
        String sql = "SELECT * FROM project";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getString("path") + "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	private void createDatabase() {
	
        try (Connection conn = this.connect()) {
            if (conn != null) {
                
            	conn.getMetaData(); // creates database
                System.out.println("A new database "+databaseName+" has been created.");
                conn.close();
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	private void createProjectTable() {
		
        String sql = "CREATE TABLE IF NOT EXISTS project (\n"
                + "	id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "	path text NOT NULL\n"
                + ");";
        
        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            
            System.out.println("Table project created");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
	}
	
	public int getProjectId(String path) {
		
		String sql = "select id from project where path = '"+path+"'";
		
		try (Connection conn = this.connect();
	             Statement stmt  = conn.createStatement();
	             ResultSet rs    = stmt.executeQuery(sql)){
				
	            while (rs.next()) {
	            	return rs.getInt("id");
	            }
	    } catch (SQLException e) {
	            System.out.println("Project path not found.");
	    }
		
		return -1;
	}
	
	public void insertINtoProjectTable(String path) {
		
		String sql = "select path from project where path = '"+path+"'";
		System.out.println(sql);
		try (Connection conn = this.connect();
	             Statement stmt  = conn.createStatement();
	             ResultSet rs    = stmt.executeQuery(sql)){
	           	
	            while (rs.next()) {
	                return;
	            }
	    } catch (SQLException e) {
	            System.out.println(e.getMessage());
	    }
        
        sql = "INSERT INTO project (path)\n" + 
        		"VALUES ( '"+path+"' );";
        
        try (Connection conn = this.connect();
            Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            
            System.out.println("Inserted: "+path);
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
	}
	
	private void makeDefaultDirectory() {
		
		File dir = new File(defaultDatabaseDirectory);
		dir.mkdirs();
		
	}
	
	private Connection connect() {
        String url = "jdbc:sqlite:"+ defaultDatabaseDirectory + databaseName;
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
	}
	
	public void getFileData(File file) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		
		System.out.println("After Format : " + sdf.format(file.lastModified()));
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
		System.out.println("Current : " + dtf.format(LocalDateTime.now()));
	}
	
	public ArrayList<String> getChangedFiles() {
		
		return changedFiles;
	}
	
	public Boolean ifFileToBeRun(File file, String lastRun) {
		
		if( lastRun.equals("") ) return true; //no previous value in database
		
		Date lastModified = new Date(file.lastModified());
		System.out.println(lastModified);
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		String lr = simpleDateFormat.format(new Date());
		
		try {
			Date parsedDate = simpleDateFormat.parse(lr);
			System.out.println(parsedDate);
			
			return lastModified.after(parsedDate);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		return true;
	}
	
	public void directorySearcher(File currentDirectory) {
		
		for(File file: currentDirectory.listFiles()) {
			
			if(file.isDirectory()) {
				
				directorySearcher(file);
			}
			else if(file.isFile() ) {
				//System.out.println(file.getPath());
				//System.out.println( getLastRunFromDB(file) );
				//changedFiles.add(file);
				
				Boolean ifRun = ifFileToBeRun( file , getLastRunFromDB(file));
				if( ifRun == true ) changedFiles.add( file.getPath() );
				System.out.println(ifRun);
			}
		}
	}
	
	public String getLastRunFromDB(File file){
        
		String sql = "select lastRun from file where filePath = '"+file.getPath()+"'";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // loop through the result set
            while (rs.next()) {
                return rs.getString("lastRun");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return "";
    }
	
	
	public void loadIntoFileTable() {
		
		Date currentTime = new Date();
		System.out.println(currentTime.toString());
		
		for(String path: changedFiles) {
			File file = new File(path);
			insertINtoFileTable(file.getName(), file.getPath(),
					currentTime.toString() , Integer.toString( Start.currentProjectId )
					, CParser.testCaseOutputDirectory);
		} 
	}
}
