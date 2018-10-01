package controlFlowGraphBuilder;

import java.util.ArrayList;
import java.util.Arrays;

import parser.components.*;

public class CFGBulider {
	
	ArrayList <String> methodBodyWords = new ArrayList<String>();
	ArrayList <String> dataTypes = new ArrayList<String>( Arrays.asList("int", "float", "double", "char") ); 
	
	private ArrayList<Node> nodes = new ArrayList<Node>();
	
	
	public CFGBulider(Method m) {
		
		methodBodyWords = m.body;
		//for(String s: methodBodyWords) System.out.println(s);
		beautify();
		//for(String s: methodBodyWords) System.out.println(s);
		buildNodes();
	}
	
	private void beautify() {
		provideNecessarySpace();
		primaryDataTypeFixer();
	}
	
	private void primaryDataTypeFixer() {
		
		ArrayList <String> temp = new ArrayList<String>();
		
		for(String s: methodBodyWords) {
			
			String words [] = s.split(" +");
			
			//System.out.println(words[0]);
			if( dataTypes.contains(words[0]) ) {
				
				String parts[] = s.split(",");

				for(int i=0; i<parts.length; i++) {
					if( i== 0 ) temp.add(parts[i]);
					else temp.add(words[0]+" "+parts[i].trim());
				}
			}
			else temp.add(s);
		}
		
		//for(String s: temp) System.out.println(s);
		methodBodyWords = temp;
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
		
		for(String s: methodBodyWords) {
			
			nodes.add( new Node( id++, s) );
		}
		
		
	}
}
