package symbolicSolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controlFlowGraphBuilder.Node;
import parser.components.Variable;

class SMTSolver {
	
	Map<String, String> variableMap = new HashMap<String, String>();
	
	public SMTSolver() {
		
	}
	
	void putValue( String name, String symbol ) {
		
		
		if( !variableMap.containsKey(name) ) variableMap.put(name, symbol);
		//System.out.println(variableMap.size());
	}
	
	/* int k = 9;
	 * int i = 4;
	 * k = i - 9; 
	 */
	void parseAssignment(Node n, ArrayList <Variable> variables) {
		
		String content = n.content;
		String temp[] = content.split(" +");
		
		for(int i=0; i<temp.length; i++) {
			
			
		}
		
		
	}
}
