package parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

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
		parseMethodBody(m);
		//print();
		//System.out.println("________________________________________");
		
	}
	
	private void parseMethodBody(Method m) {
		//System.out.println(body);
		if(body.trim().isEmpty()) return; //this is a method without body
		
		body = beautifyMethodBody(body);

		m.body =  removeJunks(body);
		
		m.body = insertTab(m.body);
		
		//for(String s: m.body) System.out.println(s);
	}
	
	
	private ArrayList<String> insertTab(ArrayList<String> lines) {
		
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> target = new ArrayList<String>( Arrays.asList("if", "else", "for", "do", "while"));
		
		int level = 0;
		int lastFoundTarget = -3;
		
		for(int i=0; i<lines.size(); i++) {
			
			String words[] = lines.get(i).split(" +");
			
			
			if( words[0].contains("{") ) {
				level++;
			}
			
			if( lastFoundTarget == i-1 && !words[0].contains("{") ) {
				temp.add(  String.join("", Collections.nCopies(level+1, "\t")) + lines.get(i)  ); //insert tabs of level basis
			}
			else temp.add(  String.join("", Collections.nCopies(level, "\t")) + lines.get(i)  ); //insert tabs of level basis
			
			if( target.contains(words[0]) ) {
				lastFoundTarget = i;
			}
			else if( words[0].contains("}") ) {
				level--;
			}
			
			
			
		}
		
		//for(String s: temp) System.out.println(s);
		return temp;
		
	}
	
	
	private ArrayList<String> removeJunks(String bodyContents) {
		
		String temp [] = bodyContents.split(";");
		int len = temp.length;
		
		ArrayList<String> lines = new ArrayList<String>();
		
		
		for( int i=0; i<len; i++) {
			if( temp[i].trim().isEmpty() ) continue;
			else lines.add(temp[i].trim());
		}
		
		/** test */
		//for(String s: lines) System.out.println(s);
		
		
		return lines;
	}
	
	private String beautifyMethodBody(String s) {
		
		String temp = s;
		
		
		//temp = temp.replaceAll("\\(", "\\;(;");
		temp = temp.replaceAll("\\)", "\\);");
		temp = temp.replaceAll("\\{", "\\;{;");
		temp = temp.replaceAll("\\}", "\\;};");
		
		temp = temp.replaceAll("else", "else;");
		temp = temp.replaceAll("else;[ ]*if", "else if");
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
		
		ArrayList <String> target = new ArrayList<String>( Arrays.asList( "(", ")", "{", "}", ";", "," ) );
		
		String temp = "";
		
		for(int i = 1; i<s.length(); i++) {
			
			String c = "" + s.charAt(i);
			
			if( target.contains(c) ) temp += (" "+c+" ");
			else temp += c;
			//if( Character.isWhitespace(c)  || Character.isLetter(c) || Character.isDigit(c) ) temp += c;
			//else temp += (" "+c+" ");
		}
		//System.out.println(temp);
		return temp;
		//System.out.println(temp);
	}

}
