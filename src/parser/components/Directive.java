package parser.components;

public class Directive extends Component {

	int startLine;
	int finishLine;
	
	public Directive( int startLine, int finishLine ) {
		
		this.startLine = startLine;
		this.finishLine = finishLine;
	}

}
