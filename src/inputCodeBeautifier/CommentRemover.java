package inputCodeBeautifier;

import java.util.ArrayList;

public class CommentRemover {
	
	public CommentRemover() {
		
	}
	
	public static ArrayList<String> removeComments( ArrayList<String> fileText){
		
		ArrayList<String> temp = new ArrayList<String>();
		
		int multipleLineCommentFlag = 0;
		int doubleQuoteFlag = 0;
		int multipleCommentLineStart = 0;
		
		
		for(int line=0; line<fileText.size(); line++) {
			
			//if(line == 114) System.out.println(fileText.get(line)+" "+doubleQuoteFlag);
 			String s = fileText.get(line);
			
			for(int i=0; i<s.length()-1; i++) {
				
				if( multipleLineCommentFlag==0 && doubleQuoteFlag==0 ) {
					
					if( s.charAt(i) == '/' && s.charAt(i+1)=='/') {  //remove single line comments
						s = s.replace( s.substring(i), " ");
						//temp.add( s );
						break;
					}
					else if( s.charAt(i) == '/' && s.charAt(i+1)=='*') {
						
						multipleCommentLineStart = i;
						multipleLineCommentFlag = 1;
					}
					else if( (int)s.charAt(i) == 34 ) doubleQuoteFlag = 1;
					
				}
				else if( multipleLineCommentFlag == 1) {
					if( s.charAt(i) == '*' && s.charAt(i+1)=='/') {
						multipleLineCommentFlag = 0;
						s = s.replace( s.substring(multipleCommentLineStart, i+2), " ");
						//temp.add( s );
						i = 0;
						//multipleCommentLines += (line-multipleCommentLineStart+1);
					}
				}
				else if( doubleQuoteFlag == 1) {
					if( (int)s.charAt(i) == 34 ) {
						doubleQuoteFlag = 0;
					}
				}
			}
			
			if(multipleLineCommentFlag == 1) {
				s = s.replace( s.substring(multipleCommentLineStart), " ");
				//temp.add( s );
				multipleCommentLineStart = 0;
			}
			
			if( s.length() > 0 && (int)s.charAt( s.length()-1 ) == 34 ) doubleQuoteFlag = (doubleQuoteFlag+1)%2; 
			
			temp.add(s);
		}
		
		return temp;
	}
}
