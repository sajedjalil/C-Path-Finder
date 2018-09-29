package parser;

import parser.components.Method;

public class MethodParser {

	String beforeParameter = "";
	String parameter = "";
	String body = "";
	
	public MethodParser(Method m) {
		
		m.methodData = beautify(m.methodData);
		breakFragments(m.methodData);
		
		print();
	}
	
	private void print() {
		System.out.println(beforeParameter);
		System.out.println(parameter);
		System.out.println(body);
	}
	
	private void breakFragments(String s) {
		
		
		
		int i = 0;
		int len = s.length();
		int flag = 0;
		
		//part before parameter - return type, method name
		for( ; i<len; i++) {
			
			if( s.charAt(i) == '(' ) {
				beforeParameter = s.substring(0, i-1);
				flag = i+1;
				break;
			}
		}
		
		//parameter part
		for( ; i<len; i++) {
			
			if( s.charAt(i) == ')' ) {
				parameter = s.substring(flag, i-1);
				flag = i+1;
				break;
			}
		}
		
		//method body start
		for( ; i<len; i++) {
			
			if( s.charAt(i) == '{' ) {
				flag = i+1;
				break;
			}
		}
		
		for(i = len - 1 ; i>0; i--) {
			
			if( s.charAt(i) == '}' ) {
				body = s.substring(flag, i);
				flag = i+1;
				break;
			}
		}
		
		//System.out.println(body);
	}
	
	private String beautify(String s) {
		
		String temp = "";
		
		for(int i = 0; i<s.length(); i++) {
			
			char c = s.charAt(i);
			
			if( Character.isWhitespace(c)  || Character.isLetter(c) || Character.isDigit(c) ) temp += c;
			else temp += (" "+c+" ");
		}
		
		return temp;
		//System.out.println(temp);
	}

}
