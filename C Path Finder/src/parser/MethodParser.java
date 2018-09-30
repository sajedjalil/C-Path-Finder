package parser;

import parser.components.Method;
import parser.components.Variable;

public class MethodParser {

	String beforeParameter = "";
	String parameter = "";
	String body = "";
	
	public MethodParser(Method m) {
		
		m.methodData = beautify(m.methodData);
		breakFragments(m.methodData);
		
		parseBeforeParameter(m);
		parseParameter(m);
		parseMethodBody();
		//print();
	}
	
	private void parseMethodBody() {
		//System.out.println(body);
		if(body.trim().isEmpty()) return; //this is a method without body
		
		body = beautifyMethodBody(body);
		
		String fragments[] = body.trim().split(";");
		for(String s:fragments) System.out.println(s.trim());
	}
	
	private String beautifyMethodBody(String s) {
		
		String temp = s;
		
		
		temp = temp.replaceAll("\\(", "\\;(;");
		temp = temp.replaceAll("\\)", "\\;);");
		temp = temp.replaceAll("\\{", "\\;{;");
		temp = temp.replaceAll("\\}", "\\;};");
		
		//System.out.println(temp);
		
		return temp;
	}
	
	
	private void parseBeforeParameter(Method m) {
		
		if( beforeParameter.trim().isEmpty() ) return; //if no string exists before parameter
		
		String words [] = beforeParameter.trim().split(" ");
		int len = words.length;
		
		m.methodName = words[len-1];
		
		String returnType = "";
		for(int i=0; i<len-1; i++) returnType += (words[i]+" ");
		m.methodReturnType = returnType.trim();
		
		//System.out.println(m.methodReturnType+" "+m.methodName);
	}
	
	private void parseParameter(Method m) {
		
		if( parameter.trim().isEmpty() ) return; //no parameter is present. So return the function
		
		String allParameter [] = parameter.split(",");
		
		for(String s: allParameter) {
			m.parameters.add( new Variable(s) );
		}
		
	}
	
	/*
	private void print() {
		System.out.println(beforeParameter);
		System.out.println(parameter);
		System.out.println(body);
	}
	*/
	
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
		
		for(i = len - 1 ; i>=flag; i--) {
			
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
