package z3Solver;

import java.util.ArrayList;
import java.util.HashMap;

import com.microsoft.z3.BoolExpr;
import com.microsoft.z3.Context;
import com.microsoft.z3.IntExpr;
import com.microsoft.z3.Model;
import com.microsoft.z3.Solver;
import com.microsoft.z3.Status;

public class InputGenerator {
	

	private HashMap<String, IntExpr> integers = new HashMap<String, IntExpr>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new InputGenerator();
	}
	
	
	
	public InputGenerator() {
		
		System.out.println( getVariableNames("!( 4 + aab + aab == 5 )".trim()));
		

        
	}
	
	
    Model check(Context ctx, BoolExpr f, Status sat) throws TestFailedException
    {
        Solver s = ctx.mkSolver();
        s.add(f);
        if (s.check() != sat)
            throw new TestFailedException();
        if (sat == Status.SATISFIABLE)
            return s.getModel();
        else
            return null;
    }
	
	private ArrayList<String> getVariableNames(String line) {
		
		ArrayList<String> variableNames = new ArrayList<String>();	
		String words[] = line.split(" +");
		
		for( String temp: words) {
			
			if( Character.isAlphabetic( temp.charAt(0) )  ) variableNames.add(temp);
		}
		
		return variableNames;
	}
	
	private void addNewIntIntoMap( String line) {
		
	}
	
	
	@SuppressWarnings("serial")
    class TestFailedException extends Exception
    {
        public TestFailedException()
        {
            super("Check FAILED");
        }
    };

}
