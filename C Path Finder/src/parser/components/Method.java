package parser.components;

import java.util.List;

public class Method extends Component {
	
	public int startLine;
	public int finishLine;
	
	public String methodData = "";
	
	
	
	public Method( int startLine, int finishLine, List<String> temp ) {
		
		this.startLine = startLine;
		this.finishLine = finishLine;
		
		for(String s:temp) methodData += s;
	}
	
	
}
