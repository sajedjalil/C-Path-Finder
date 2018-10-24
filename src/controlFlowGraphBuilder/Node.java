package controlFlowGraphBuilder;

public class Node {
	
	public int leftChildID = -1; //null initially
	public int rightChildID = -1; //null initially
	
	public Node leftChild;
	public Node rightChild;
	
	public int id, level;
	public String content;
	public String conditions = "";
	public Boolean ifAssignmentStatement = false;
	
	private void setIfAssignmentStatement() {
		if( content.contains(" = ") ) ifAssignmentStatement = true;
	}
	
	public Node(int id, String content, int level) {
		this.id = id;
		this.content = content;
		this.level = level;
		//System.out.println(content+" "+id);
		getConditions();
		setIfAssignmentStatement();
	}
	
	
	private void getConditions() {
		
		String temp = "";
		int flag = 0;
		int len = content.length();
		
		if( content.startsWith("if") || content.startsWith("else if") ) {
			
			for(int i=0; i<len; i++) {
				
				
				if( content.charAt(i) == '(' ) {
					flag++;
					if(flag==1) continue;
				}
				else if( content.charAt(i) == ')' ) {
					flag--;
				}
				
				if( flag > 0 ) temp += content.charAt(i);
			}
		}
		
		//there exists a condition
		if( temp.trim().isEmpty() == false) {
			conditions = temp.trim();
			//System.out.println(conditions);
		}
	}
	

}
