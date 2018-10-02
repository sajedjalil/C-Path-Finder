package controlFlowGraphBuilder;


public class Node {
	
	int leftChildID = -1; //null initially
	int rightChildID = -1; //null initially
	
	public Node leftChild;
	public Node rightChild;
	
	public Node controller[];
	
	public int id, level;
	public String content;
	
	public Node(int id, String content, int level) {
		this.id = id;
		this.content = content;
		this.level = level;
		//System.out.println(content+" "+id);
	}

}
