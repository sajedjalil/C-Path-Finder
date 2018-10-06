package symbolicSolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controlFlowGraphBuilder.Node;
import parser.components.Variable;

class SMTSolver {
	
	Map<String, Variable> variableMap = new HashMap<String, Variable>();
	ArrayList<String> contidtions = new ArrayList<String>();
	
	
	public SMTSolver() {
		
	}
	
	void putValue( String name, Variable v ) {
		
		
		if( !variableMap.containsKey(name) ) variableMap.put(name, v);
		//System.out.println(variableMap.size());
	}
	
	/* int k = 9;
	 * int i = 4;
	 * k = i - k; 
	 */
	void parseAssignment(Node n, ArrayList <Variable> variables) {
		
		String content = n.content;
		String temp[] = content.split(" +");
		
		for(int i=0; i<temp.length; i++) {
			
			
		}
		
		
	}
}
