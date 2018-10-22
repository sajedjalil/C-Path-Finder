package j.k;

import java.util.ArrayList;
import java.util.List;


import imports.CBaseListener;
import imports.CParser;

public class MyListener extends CBaseListener {

	private List<String> errors = new ArrayList<>();
	 
    // ... getter for errors
  
    @Override
    public void enterDeclarationSpecifier(CParser.DeclarationSpecifierContext ctx) {
        //TerminalNode node = ctx.Identifier();
        //System.out.println(ctx.toString());
    }
}
