package symbolicSolver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.filechooser.FileSystemView;

import controlFlowGraphBuilder.Node;
import parser.components.*;

public class SymbolicSolver {
	
	ArrayList <String> dataTypes = new ArrayList<String>( Arrays.asList("short", "long", "signed", "unsigned", "register",
			"int", "float", "double", "char") );
	
	public ArrayList<String> testcases = new ArrayList<String>();
	String myDocument = FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + 
			"//C Path Finder//logs//";
	
	Logger logger = Logger.getLogger("MyLog");  
    FileHandler fh; 
	
	public SymbolicSolver (Method m) {
		
		init(m.methodName);
		
		//analyzing paths
		analyzPaths(m);
	}
	
	private void init(String name) {
		
		//setting the logger
		File dir = new File(myDocument);
		dir.mkdirs();
		
		logger.setUseParentHandlers(false);
		try {   
	        fh = new FileHandler(myDocument + name +".txt");  
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter);  

	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }		 
	}
	
	private void analyzPaths(Method m) {
		
		//log
		String log = "";
		for(Node s: m.nodes) {
			log += (s.id+" "+s.content + "\n");
		}
		logger.info(log);
		
		for(int i=0; i<m.paths.size(); i++) {
			//System.out.println("Case: " + (i+1));
			
			analyzeSinglePath(i, m);
			
		}
	}
	
	
	
	private void analyzeSinglePath( int serial, Method m) {
		
		String log = "";
		for(int i: m.paths.get(serial)) log += ( i + " ");
		logger.info( log);
		
		//for(int i: m.paths.get(serial)) System.out.print(i+" ");
		//System.out.println();
		//if(serial > 4) return;
		
		
		SMTSolver solver = new SMTSolver();
		loadParameters(m, solver);
		
		for(int a=0; a<m.paths.get(serial).size(); a++) {
			
			int i = m.paths.get(serial).get(a);
			//System.out.println(m.nodes.get(i).content);
			//has condition operator
			if( !m.nodes.get(i).conditions.equals("") ) {
				
				
				int nextID = m.paths.get(serial).get(a+1);
				
				//condition: true
				if( m.nodes.get(i).rightChildID == m.nodes.get(nextID).id )
					solver.contidtions.add( "( "+ returnSymbolicCondition( m.nodes.get(i).conditions, solver ) +" )" );
				else {
					solver.contidtions.add( "! ( "+returnSymbolicCondition( m.nodes.get(i).conditions, solver )  +" )");
				}
			}
			else if( m.nodes.get(i).ifAssignmentStatement == true || dataTypes.contains( m.nodes.get(i).content.split(" +")[0])) {
				
				handleAssignments( m.nodes.get(i), solver);
				//solver.parseAssignment(m.nodes.get(i), m.localVariables);
			}
		}
		
		
		//for(String s: solver.contidtions) System.out.println(s);
		String testcase = solver.analyze();
		testcases.add(testcase);
		//if( solver.variableMap.containsKey("b"))
			//System.out.println( solver.variableMap.get("b").getSymbolicValue());
	}
	
	private String returnSymbolicCondition(String content, SMTSolver solver) {
		
		//System.out.println(content);
		String words[] = content.trim().split(" +");
		String temp = "";
		
		for(String s: words) {
			
			if( solver.variableMap.containsKey(s) ) temp += getVariableValue(solver.variableMap.get(s)) + " ";
			else temp += s+" ";
		}
		
		//System.out.println(temp);
		return temp.trim();
	}
	
	
	/* int k = 9;
	 * int i = 4;
	 * k = i - k; 
	 */
	private void handleAssignments(Node n, SMTSolver solver) {
		
		//System.out.println(n.content);
		String words[] = n.content.trim().split(" +");
		
		if( dataTypes.contains(words[0]) ) {
			//add new variable
			Variable v = new Variable(n.content.trim());
		    
		   
			getUpdatedValue(n.content, v, solver);
			
			solver.variableMap.put(v.getName(), v);
			//System.out.println(v.getActualValue()+ " "+v.getName());
		}
		else if( solver.variableMap.containsKey(words[0]) ) {
			
			//System.out.println(words[0]+" "+solver.variableMap.get(words[0]).getSymbolicValue());
			getUpdatedValue( n.content, solver.variableMap.get(words[0]), solver);
			//System.out.println(words[0]+" "+solver.variableMap.get(words[0]).getSymbolicValue());
		}
		
	}
	
	private void getUpdatedValue(String content, Variable v, SMTSolver solver) {
		
		if( !content.contains("=") ) return; // cases like: int a;
		if( content.trim().split("=").length <2 ) return;
		
		String words[] = content.trim().split("=")[1].split(" +");
		String temp = "";
		
		for(String s: words) {
			//System.out.println(s);
			if( solver.variableMap.containsKey(s) ) {
				
				temp += (getVariableValue(solver.variableMap.get(s))  + " ");
			}
			else temp += s+" ";
		}
		
		//add brackets
		if( temp.trim().split(" +").length > 1 ) temp = "( "+temp+" )";
		v.setActualValue(temp.trim());
		
	}
	
	private void loadParameters(Method m, SMTSolver solver) {
		
		for(int i=0; i<m.parameters.size(); i++) {
			
			Variable v = new Variable(m.parameters.get(i).line );
			solver.putValue( m.parameters.get(i).getName(),  v);
		}
	}
	
	private String getVariableValue(Variable v) {
		
		if( v.getActualValue().equals("") ) return v.getName();
		else return v.getActualValue();
	}
}
