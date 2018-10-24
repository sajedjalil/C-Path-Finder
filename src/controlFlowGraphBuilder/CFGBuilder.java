package controlFlowGraphBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import controlFlowGraphBuilder.Node;
import parser.components.*;

public class CFGBuilder {
	
	ArrayList <String> methodBodyWords = new ArrayList<String>();
	ArrayList <String> dataTypes = new ArrayList<String>( Arrays.asList("short", "long", "signed", "unsigned", "register",
			"int", "float", "double", "char") );
	private ArrayList<Node> nodes = new ArrayList<Node>();
	public Node root;
	
	private int visited[];
	
	public CFGBuilder(Method m) {
		
		methodBodyWords = m.body;
		//for(String s: methodBodyWords) System.out.println(s);
		beautify();
		//for(String s: methodBodyWords) System.out.println(s);
		buildNodes();
		
		//for(Node n: nodes) System.out.println(n.level);
		buildEdges(0);
		
		buildTree();
		/*
		for(Node n: nodes) {
			System.out.println(n.id+" "+n.content);
			if( n.leftChild != null) System.out.print(n.leftChild.id);
			if( n.rightChild != null) System.out.println("*"+n.rightChild.id);
		}
		*/
		//printPaths();
		m.nodes = nodes;
		m.executionTree = root;
	}
	
	private void beautify() {
		provideNecessarySpace();
		primaryDataTypeFixer();
	}
	
	private void primaryDataTypeFixer() {
		
		ArrayList <String> temp = new ArrayList<String>();
		
		for(String s: methodBodyWords) {
			
			String words [] = s.split(" +");
			
			//System.out.println(s);
			if( dataTypes.contains(words[0]) ) {
				
				//modify s to detect method if a method call exits in variable declaration  
				s = keepMethodCallsUnchanged(s);
				//System.out.println(s);
				String parts[] = s.split(";");
				
				//set variable type
				String variableType = "";
				if( parts.length > 0 ) {
					
					String k[] = parts[0].split(" +");
					
					for(int i=0; i<k.length-1; i++) variableType += (" "+k[i]);
					
					variableType = variableType.trim();
				}
				
				
				for(int i=0; i<parts.length; i++) {
					if( i== 0 ) temp.add(parts[i]);
					else temp.add(variableType+" "+parts[i].trim());
				}
			}
			else temp.add(s);
		}
		
		//for(String s: temp) System.out.println(s);
		methodBodyWords = temp;
	}
	
	private String keepMethodCallsUnchanged(String s) {
		
		int len = s.length();
		int firstBraceDepth = 0;
		String temp = "";
		
		for(int i=0; i<len; i++) {
			
			char c = s.charAt(i);
			
			if( s.charAt(i) == '(' ) firstBraceDepth++;
			else if( s.charAt(i) == '(' ) firstBraceDepth--;
			else if( s.charAt(i) == ',' && firstBraceDepth == 0) c = ';';
			
			temp += c;
		}
		
		return temp;
	}
	
	private void provideNecessarySpace() {
		
		ArrayList <String> target = new ArrayList<String>( Arrays.asList( "+", "-", "*", "/", "=", ">", "<", "%", "&", "|", "~", "^" ) );
		ArrayList <String> clear = new ArrayList<String>();
		
		
		
		for(int i=0; i< methodBodyWords.size(); i++) {
			
			String s = methodBodyWords.get(i)+" ";
			int len = s.length();
			String temp = "";
			int flag = 0;
			
			for(int j=0; j<len-1; j++) {
				
				if( s.charAt(j) == '\"' ) {
					flag = (flag + 1) % 2;
					temp += s.charAt(j);
				}
				else if( flag == 0 ) {
					if( target.contains( ""+s.charAt(j) ) && target.contains( ""+s.charAt(j+1) ) ) {
						temp += (" "+s.charAt(j)+s.charAt(j+1)+" ");
						j++;
					}
					else if( target.contains( ""+s.charAt(j)) ) temp+= (" "+s.charAt(j)+" ");
					else temp += s.charAt(j);
				}
				else temp += s.charAt(j);
				
			}
			
			clear.add(temp);
		}
		
		
		//for(String s: clear) System.out.println(s);
		methodBodyWords = clear;
	}
	
	private void buildNodes() {
		
		int id = 0;
		
		nodes.add( new Node(id++, "", 0)); //source blank node
		
		for(String s: methodBodyWords) {
			
			int level = findTabDepth(s);
			//System.out.println(s);
			nodes.add( new Node( id++, s.trim(), level) );
		}
		
		nodes.add( new Node(id++, "", 0)); //destination blank node
		
	}
	
	
	
	private void buildEdges(int prevPosition) {
		
		int len = nodes.size();
		
		for(int i= prevPosition+1 ; i<len; i++) {
			
			//start of if bloc
			if( nodes.get(i).content.trim().split(" +")[0].trim().equals("if") ) {
				
				int finishNode = getIfBlockFinishNode( i , nodes.get(i).level);
				ArrayList<Integer> positions = getNeighbourNodesOfIf( i, finishNode, nodes.get(i).level);
				positions.add(finishNode);
				
				//map all fragments last as Right Child into finish
				for(int j:positions) nodes.get(j-1).rightChildID = finishNode;
				
				
				
				//map all condition to its next condition if exists
				
				int currentBranchposition = i;
				
				for(int j:positions) {
					
					Boolean elseIfStatement = nodes.get(currentBranchposition).content.startsWith("else if");
					Boolean elseStatement= nodes.get(currentBranchposition).content.startsWith("else");
					
					if( !elseIfStatement && elseStatement) break; // else type doesn't make a branch
					
					nodes.get(currentBranchposition).leftChildID = j;
					currentBranchposition = j;
				}
				
				
				
			}
			
			if( nodes.get(prevPosition).rightChildID == -1 ) nodes.get(prevPosition).rightChildID = i;
			prevPosition = i;
		}
		
	}
	
	private ArrayList<Integer> getNeighbourNodesOfIf(int startNodePosition, int finishNodePosition, int level) {
		
		ArrayList<Integer> temp = new ArrayList<Integer>();
		
		for(int i = startNodePosition+1 ; i<=finishNodePosition; i++) {
			
			Node n = nodes.get(i);
			//System.out.println(n.level);
			if( n.level == level && n.content.startsWith("else") ) temp.add(i);
		}
		
		//System.out.println(temp);
		return temp;
	}
	
	private int getIfBlockFinishNode(int startNodePosition, int level) {
		
		int nodeLen = nodes.size();
		int finish = startNodePosition;
		
		for(int i = startNodePosition+1 ; i<nodeLen; i++) {
			
			Node n = nodes.get(i);
			//System.out.println(n.level);
			if( n.level <= level && !n.content.contains("else") ) return i;
		}
		
		return finish;
	}
	
	
	private int findTabDepth(String s) {
		
		int len = s.length();
		int tabCounter = 0;
		
		for(int i=0; i<len; i++) {
			if( s.charAt(i) == '\t' ) tabCounter++;
			else break;
		}
		
		return tabCounter;
	}
	
	private void buildTree() {
		
		visited = new int[nodes.size()];
		for(int i=0; i<visited.length; i++) visited[i] = 0;
		
		for(Node n: nodes) {
			
			if( root == null) root = n;
			
			if( visited[n.id] == 0) dfs(n);
		}
		
	}
	
	private void dfs(Node current) {
		
		visited[current.id] = 1;
		
		if( current.leftChildID != -1 ) {
			current.leftChild = nodes.get( current.leftChildID);
			
			if( visited[current.leftChildID] == 0 ) dfs( nodes.get( current.leftChildID) );
		}
		
		if( current.rightChildID != -1 ) {
			current.rightChild = nodes.get( current.rightChildID);
			
			if( visited[current.rightChildID] == 0 ) dfs( nodes.get( current.rightChildID) );
		}
		
		
	}
	/*
	private int sequence[];
	
	private void printPaths() {
		sequence = new int[nodes.size()];
		for(int i=0; i<sequence.length; i++) sequence[i] = 0;
		
		backtrack(root);
	}
	
	private void backtrack(Node n) {
		
		sequence[ n.id ] = 1;
		
		if( n.leftChildID != -1 ) {
			
			backtrack(n.leftChild);
		}
		
		if( n.rightChildID != -1 ) {
			backtrack(n.rightChild);
		}
		
		if(n.leftChild == null && n.rightChild == null) {
			for(int i=0; i<sequence.length; i++) {
				if( sequence[i] == 1) System.out.print(i+" ");
			}
			System.out.println();
		}
		
		sequence[n.id] = 0;
	} */
}
