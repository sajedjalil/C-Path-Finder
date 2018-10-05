package symbolicSolver;

import java.util.ArrayList;

import parser.components.*;
import controlFlowGraphBuilder.*;

public class SymbolicSolver {
	
	String currentSymbol = "zz";
	
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
		assignLocalVariableSymbol(m.localVariables);
	}
	
	
	private void assignParameterSymbol(ArrayList<Variable> parameters) {
		
		for(Variable v: parameters) {
			
			v.setSymbolicValue( getCurrentSymbol() );
			
		}
	}
	
	private void assignLocalVariableSymbol(ArrayList<Variable> localVariables) {
		
		for(Variable v: localVariables) {
			
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
		
		for(int i=0; i<m.paths.get(serial).size(); i++) {
			
			//has condition operator
			if( m.nodes.get(i).conditions.equals("") == false ) {
				
			}
			else if( m.nodes.get(i).ifAssignmentStatement == true ) {
				solver.parseAssignment(m.nodes.get(i), m.localVariables);
			}
		}
	}
	
	private void loadParameters(Method m, SMTSolver solver) {
		
		for(int i=0; i<m.parameters.size(); i++) {
			solver.putValue( m.parameters.get(i).getName(),  m.parameters.get(i).getSymbolicValue());
		}
	}
}
