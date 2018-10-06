package symbolicSolver;

import java.util.ArrayList;
import java.util.Arrays;

import controlFlowGraphBuilder.Node;
import parser.components.*;

public class SymbolicSolver {
	
	String currentSymbol = "zz";
	ArrayList <String> dataTypes = new ArrayList<String>( Arrays.asList("short", "long", "signed", "unsigned", "register",
			"int", "float", "double", "char") );
	
	
	private String getCurrentSymbol() {
		
		currentSymbol = getNextSymbol(currentSymbol); 
		
		return currentSymbol;
	}
	
	private String getNextSymbol(String current) {
		
		if( current.equals("")) return "a";
		
		current = new StringBuffer(current).reverse().toString(); // bcz become zcb
		int len = current.length();
		
		String temp = "";
		int extra = 1;
		
		
		for(int i=0; i<len; i++) {
			
			char c = current.charAt(i);
			
			if( extra == 0 ) temp += c;
			else {
				
				if( c == 'z' ) {
					temp += 'a';
					extra = 1;
				}
				else {
					extra = 0;
					temp += (++c);
				}
			}
		}
		
		if( extra == 1) temp += 'a';
		
		temp = new StringBuffer(temp).reverse().toString();
		
		return temp;
	}
	
	
	public SymbolicSolver (Method m) {
		
		assignSymbol(m);
		analyzPaths(m);
	}
	
	
	private void assignSymbol(Method m) {
		
		assignParameterSymbol(m.parameters);
	}
	
	
	private void assignParameterSymbol(ArrayList<Variable> parameters) {
		
		for(Variable v: parameters) {
			
			v.setSymbolicValue( getCurrentSymbol() );
			
		}
	}
	
	
	private void analyzPaths(Method m) {
		
		for(int i=0; i<1/*m.paths.size()*/; i++) {
			
			analyzeSinglePath(i, m);
		}
	}
	
	private void analyzeSinglePath( int serial, Method m) {
		
		SMTSolver solver = new SMTSolver();
		loadParameters(m, solver);
		
		for(int a=0; a<m.paths.get(serial).size(); a++) {
			
			int i = m.paths.get(serial).get(a);
			//System.out.println(m.nodes.get(i).content);
			//has condition operator
			if( !m.nodes.get(i).conditions.equals("") ) {
				
				//condition: true
				int nextID = m.paths.get(serial).get(a+1);
				
				if( m.nodes.get(i).rightChildID == m.nodes.get(nextID).id )
					solver.contidtions.add( returnSymbolicCondition( m.nodes.get(i).conditions, solver ) );
				else {
					solver.contidtions.add( "!( "+returnSymbolicCondition( m.nodes.get(i).conditions, solver )  +" )");
				}
			}
			else if( m.nodes.get(i).ifAssignmentStatement == true || dataTypes.contains( m.nodes.get(i).content.split(" +")[0])) {
				
				handleAssignments( m.nodes.get(i), solver);
				//solver.parseAssignment(m.nodes.get(i), m.localVariables);
			}
		}
		
		for(String s: solver.contidtions) System.out.println(s);
		
		//if( solver.variableMap.containsKey("b"))
			//System.out.println( solver.variableMap.get("b").getSymbolicValue());
	}
	
	private String returnSymbolicCondition(String content, SMTSolver solver) {
		
		//System.out.println(content);
		String words[] = content.trim().split(" +");
		String temp = "";
		
		for(String s: words) {
			
			if( solver.variableMap.containsKey(s) ) temp += solver.variableMap.get(s).getSymbolicValue()+" ";
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
		    v.setSymbolicValue( getCurrentSymbol() );
		   
			getNewSymbolicValue(n.content, v, solver);
			
			solver.variableMap.put(v.getName(), v);
			//System.out.println(v.getActualValue()+ " "+v.getName());
		}
		else if( solver.variableMap.containsKey(words[0]) ) {
			
			//System.out.println(words[0]+" "+solver.variableMap.get(words[0]).getSymbolicValue());
			getNewSymbolicValue( n.content, solver.variableMap.get(words[0]), solver);
			//System.out.println(words[0]+" "+solver.variableMap.get(words[0]).getSymbolicValue());
		}
		
	}
	
	private void getNewSymbolicValue(String content, Variable v, SMTSolver solver) {
		
		if( !content.contains("=") ) return;
		
		String words[] = content.trim().split("=")[1].split(" +");
		String temp = "";
		
		for(String s: words) {
			//System.out.println(s);
			if( solver.variableMap.containsKey(s) ) {
				
				temp += solver.variableMap.get(s).getSymbolicValue() +" ";
			}
			else temp += s+" ";
		}
		
		v.setSymbolicValue(temp.trim());
		
	}
	
	private void loadParameters(Method m, SMTSolver solver) {
		
		for(int i=0; i<m.parameters.size(); i++) {
			solver.putValue( m.parameters.get(i).getName(),  m.parameters.get(i));
		}
	}
}
