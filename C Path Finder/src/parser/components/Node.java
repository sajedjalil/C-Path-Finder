package parser.components;

public class Node {
	
	public Node childs[];
	public Node controller[];
	
	public int id;
	public String content;
	
	public Node(int id, String content) {
		this.id = id;
		this.content = content;
		
		System.out.println(content+" "+id);
	}

}
