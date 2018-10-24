package symbolicSolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.microsoft.z3.ArithExpr;
import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

import parser.components.Variable;

class SMTSolver {
	
	Map<String, Variable> variableMap = new HashMap<String, Variable>();
	ArrayList<String> contidtions = new ArrayList<String>();
	
	Map<String, IntExpr> intVariables = new HashMap<String, IntExpr>();
	
	HashMap<String, String> cfg = new HashMap<String, String>();
    Context ctx = new Context(cfg);
	
    private ArrayList<Character> operators = new ArrayList<Character>( Arrays.asList('+', '-', '*', '/', '%') );
    private ArrayList<String> conditonals = new ArrayList<String>( 
    		Arrays.asList("<", "<=", ">", ">=", "==", "!=") );
    private ArrayList<String> conjuctions = new ArrayList<String>( 
    		Arrays.asList("&&", "||") );
    
	public SMTSolver() {
		cfg.put("model", "true");
	}
	
	void putValue( String name, Variable v ) {
		
		
		if( !variableMap.containsKey(name) ) variableMap.put(name, v);
		//System.out.println(variableMap.size());
	}
	
	
	void analyze(String line) {
		
		BoolExpr t = makeConjunctionExpression(line);
		Model model;
		try {
			model = check(ctx, t , Status.SATISFIABLE);
			if(model != null ) System.out.println(model);
			else System.out.println("Unsatisfiable");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	Model check(Context ctx, BoolExpr f, Status sat) throws Exception
    {
        Solver s = ctx.mkSolver();
        s.add(f);
        if (s.check() != sat)  return null;
        
        if (sat == Status.SATISFIABLE)
            return s.getModel();
        else
            return null;
    }

	
	String removeOuterMostBracketContent( String line ) {
		
		int level = 0;
		
		String temp = line;
		
		if( temp.length() < 1  ||  temp.charAt(0) != '(' ) return line; // (a+b)
		
		for(int i=0; i<temp.length(); i++) {
			if( temp.charAt(i) == '(' ) level++;
			else if( temp.charAt(i) == ')') {
				level--;
				if( i != temp.length()-1 && level == 0) return line; // (a+b)*(b+4)
			}
		}
		
		//System.out.println(line.substring(1, line.length()-1));
		return line.substring(1, line.length()-1).trim();
	}
	
	BoolExpr makeConjunctionExpression( String line ) {
		
		
		//System.out.println(line);
		line = removeOuterMostBracketContent(line.trim()); //remove excess outer brackets
		//System.out.println(line);
		
		int counter = 0;
		String sign = null;
		BoolExpr temp1 = null, temp2 = null;
		
		String first = "", second ="";
		
		String words[] = line.split(" +");
		int flag = words.length;
		
		for(int i=0; i<words.length; i++) {
			
			if( words[i].equals("(") ) counter++;
			else if( words[i].equals(")") ) counter--;
			else if( counter == 0 && conjuctions.contains(words[i]) ) {
				sign = words[i];
				flag = i;
			}
		}
		
		for(int i=0; i<flag; i++) first += (words[i]+" ");
		for(int i=flag+1; i<words.length; i++) second += (words[i]+" ");
		
		//System.out.println(first+ " " + second);
		temp1 = makeBooleanExpression( first.trim() );
		temp2 = makeBooleanExpression( second.trim() );
		
		return returnConjunctionExpression(sign, temp1, temp2);
		
	}
	
	
	private BoolExpr returnConjunctionExpression(String sign, BoolExpr temp1, BoolExpr temp2) {
		
		if( sign.equals("&&") ) return ctx.mkAnd(temp1, temp2);
		else if( sign.equals("||") ) return ctx.mkOr(temp1, temp2);
		else return null;
	}
	
	
	BoolExpr makeBooleanExpression( String line ) {
		
		//System.out.println(line);
		line = removeOuterMostBracketContent(line.trim()); //remove excess outer brackets
		//System.out.println(line);
		
		
		int counter = 0;
		String sign = null;
		ArithExpr temp1 = null, temp2 = null;
		
		String first = "", second ="";
		
		String words[] = line.split(" +");
		int flag = words.length;
		
		for(int i=0; i<words.length; i++) {
			
			if( words[i].equals("(") ) counter++;
			else if( words[i].equals(")") ) counter--;
			else if( counter == 0 && conditonals.contains(words[i]) ) {
				sign = words[i];
				flag = i;
			}
		}
		
		for(int i=0; i<flag; i++) first += (words[i]+" ");
		for(int i=flag+1; i<words.length; i++) second += (words[i]+" ");
		
		//System.out.println(first+ " " + second);
		temp1 = makeArithmeticExpression( first.trim() );
		temp2 = makeArithmeticExpression( second.trim() );
		
		return returnBoolExpression(sign, temp1, temp2);
		
	}
	
	private BoolExpr returnBoolExpression(String sign, ArithExpr temp1, ArithExpr temp2) {
		
		if( sign.equals("<") ) return ctx.mkLt(temp1, temp2);
		else if( sign.equals("<=") ) return ctx.mkGt(temp1, temp2);
		else if( sign.equals(">") ) return ctx.mkGt(temp1, temp2);
		else if( sign.equals(">=") ) return ctx.mkLt(temp1, temp2);
		else if( sign.equals("==") ) return ctx.mkEq(temp1, temp2);
		else if( sign.equals("!=") ) return ctx.mkNot( ctx.mkEq(temp1, temp2) );
		else return null;
	}
	
	ArithExpr makeArithmeticExpression( String line ) {
		
		//System.out.println(line);
		line = removeOuterMostBracketContent(line.trim()); //remove excess outer brackets
		//System.out.println(line);
		
		
		int counter = 0;
		Character sign = null;
		ArithExpr temp1 = null, temp2 = null;
		
		if( line.split(" +").length == 1 ) return makeInteger( line );
		
		for(int i=0; i<line.length(); i++) {
			
			if( line.charAt(i) == '(' ) counter++;
			else if( line.charAt(i) == ')' ) counter--;
			else if( counter == 0 && operators.contains(line.charAt(i)) ) { // sign found
				sign = line.charAt(i);
				
				temp1 = makeArithmeticExpression( line.substring(0, i-1));
				temp2 = makeArithmeticExpression( line.substring(i+1) );
				
				return buildArithmeticExpression(sign, temp1, temp2);
			}
		}
		
		//make the arithmetic expression
		
		return null;
	}
	
	private ArithExpr buildArithmeticExpression(Character sign, ArithExpr temp1, ArithExpr temp2) {
		
		if( sign == '+' ) return ctx.mkAdd(temp1, temp2);
		else if( sign == '-' ) return ctx.mkSub(temp1, temp2);
		else if( sign == '*' ) return ctx.mkMul(temp1, temp2);
		else if( sign == '/' ) return ctx.mkDiv(temp1, temp2);
		else if( sign == '%' ) return ctx.mkMod((IntExpr)temp1, (IntExpr)temp2); 
		else return null;
	}
	
	private IntExpr makeInteger(String word) {
		
		word = word.trim();
		
		if( word.length() == 0 ) return null;
		else if( Character.isDigit(word.charAt(0)) ) return makeIntConstant(word);
		else return makeIntVariable(word);
	}
	
	private IntExpr makeIntVariable(String variable) {
		
		if( !intVariables.containsKey(variable) ) {
			
			IntExpr temp = ctx.mkIntConst(variable);
			intVariables.put(variable, temp );
		}
		
		return intVariables.get(variable);
	}
	
	private IntExpr makeIntConstant(String variable) {
		
		if( !intVariables.containsKey(variable) ) {
			
			IntExpr temp = ctx.mkInt(variable);
			intVariables.put(variable, temp );
		}
		return intVariables.get(variable);
	}
}
