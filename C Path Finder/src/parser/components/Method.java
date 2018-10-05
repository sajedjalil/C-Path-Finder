package parser.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import controlFlowGraphBuilder.Node;

public class Method extends Component {
	
	public int startLine;
	public int finishLine;
	
	public String methodData = "";
	public String methodName = "";
	public String methodReturnType = "";
	
	public ArrayList<Variable> parameters = new ArrayList<Variable>();
	public ArrayList<Variable> localVariables = new ArrayList<Variable>();
	public ArrayList<String> body = new ArrayList<String>();
	
	public ArrayList<Node> nodes = new ArrayList<Node>();
	public Node executionTree = null;
	public ArrayList< ArrayList<Integer> > paths = new ArrayList< ArrayList<Integer> >();
	
	private ArrayList <String> dataTypes = new ArrayList<String>( Arrays.asList("short", "long", "signed", "unsigned", "register",
			"int", "float", "double", "char") );
	
	
	
	public Method( int startLine, int finishLine, List<String> temp ) {
		
		this.startLine = startLine;
		this.finishLine = finishLine;
		
		for(String s:temp) methodData += s;
	}
	
	
	public void getLocalVariables() {
		
		for(Node n: nodes) {
			
			if( dataTypes.contains( n.content.split(" +")[0] ) ) {
				
				localVariables.add( new Variable(n.content) );
			}
		}
	}
	
	
	private int sequence[];
	
	
	public void printPaths() {
		sequence = new int[nodes.size()];	
		
		for(int i=0; i<sequence.length; i++) {
			sequence[i] = 0;
		}
		
		backtrack(executionTree);
	}
	
	private void backtrack(Node n) {
		
		sequence[ n.id ] = 1;
		//coveredNodes[ n.id ] = 1;
		
		if( n.leftChildID != -1 ) {
			
			backtrack(n.leftChild);
		}
		
		if( n.rightChildID != -1 ) {
			backtrack(n.rightChild);
		}
		
		if(n.leftChild == null && n.rightChild == null) {
			
				
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for(int i=0; i<sequence.length; i++) {
				if( sequence[i] == 1 ) temp.add(i); 
			}
			paths.add( temp );
			/*
			for(int i=0; i<sequence.length; i++) {
				if( sequence[i] == 1) System.out.print(i+" ");
			}
			System.out.println();
			*/
		}
		
		sequence[n.id] = 0;
	}
	
}
