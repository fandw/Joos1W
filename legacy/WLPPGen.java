import java.util.*;
import java.util.Map.Entry;

public class WLPPGen{
    // Main program
	static HashMap<String, Type> symbolTable;
	static HashMap<String, Boolean> usedSymbols;
	static HashMap<String, Integer> variableOffsets;
	static ArrayList<String> mipsCode;

	static boolean arrayPass = false;
	
    public static final void main(String args[]) {
        new WLPPGen().go();
    }

    public void go() {
    	
    	symbolTable = new HashMap<String, Type>();
    	usedSymbols = new HashMap<String, Boolean>();
    	variableOffsets = new HashMap<String, Integer>();
    	mipsCode = new ArrayList<String>();
    	arrayPass = false;
    	
    	Scanner in = new Scanner(System.in);
        Tree parseTree = readParse(in, "S");
        
        Validate validate = new Validate();
        validate.genSymbols(parseTree);
        
        reduceSymbolTable();
        
        validate.printSymbolTable();
        
        Generator generator = new Generator();
        generator.go(parseTree);
        
    }
    
    public static void reduceSymbolTable(){
    	for(Entry<String, Boolean> entry : usedSymbols.entrySet()){
    		if(entry.getValue().booleanValue()==false){
    			int offset = variableOffsets.get(entry.getKey());
    			symbolTable.remove(entry.getKey());
    			for(Entry<String, Integer> single : variableOffsets.entrySet()){
    				if(single.getValue()>offset){
    					variableOffsets.put(single.getKey(), single.getValue()-1);
    				}
    			}
    			variableOffsets.remove(entry.getKey());
    		}
    	}
    }
    
    enum Type{
    	INT,
    	INT_STAR;
    }

    // Data structure for storing the parse tree.
    public static class Tree {
        List<String> rule;
        Type type;
        ArrayList<Tree> children = new ArrayList<Tree>();

        // Does this node's rule match otherRule?
        boolean matches(String otherRule) {
            return tokenize(otherRule).equals(rule);
        }
    }
    
    public static List<String> tokenize(String line) {
        List<String> ret = new ArrayList<String>();
        Scanner sc = new Scanner(line);
        while (sc.hasNext()) {
            ret.add(sc.next());
        }
        return ret;
    }
    
	public static Set<String> terminals = new HashSet<String>(Arrays.asList(
			"BOF", "BECOMES", "COMMA", "ELSE", "EOF", "EQ", "GE", "GT", "ID",
			"IF", "INT", "LBRACE", "LE", "LPAREN", "LT", "MINUS", "NE", "NUM",
			"PCT", "PLUS", "PRINTLN", "RBRACE", "RETURN", "RPAREN", "SEMI",
			"SLASH", "STAR", "WAIN", "WHILE", "AMP", "LBRACK", "RBRACK", "NEW",
			"DELETE", "NULL"));

	public static Tree readParse(Scanner in, String lhs) {
		String line = in.nextLine();
		List<String> tokens = tokenize(line);
		Tree ret = new Tree();
		ret.rule = tokens;

		if (!terminals.contains(lhs)) {
			Scanner sc = new Scanner(line);
			sc.next(); // discard lhs
			while (sc.hasNext()) {
				String s = sc.next();
				ret.children.add(readParse(in, s));
			}
		}
		return ret;
	}
    
	public static class Validate {
			    
	    Type declareType = null;
	    int variableCounter = 0;
	    
	   
	    // Compute symbols defined in t
	    void genSymbols(Tree t) {
	    	Tree procedure = t.children.get(1);//t has 3 children, get the important one
	    	if(readSDcl(procedure.children.get(3)) == Type.INT_STAR){//get the first parameter
	    		arrayPass = true;
	    	}
	    	Type param2Type = readSDcl(procedure.children.get(5));//get the second parameter
	    	if (param2Type != Type.INT) {
	            bail("The second parameter must be INT");
	        }
	    	readDcls(procedure.children.get(8));//read the declarations at the beginning of wain
	    	checkIDs(procedure.children.get(9));//check undeclared identifiers
	    	checkIDs(procedure.children.get(11));
	    	checkStatements(procedure.children.get(9));
	    	Type returnType = checkExprType(procedure.children.get(11));
	    	if(returnType!=Type.INT){
	    		bail("Must return an INT");
	    	}
	    	return;
	    }
	    
	    Type readSDcl(Tree t){//read a single declaration
	    	//determine type
	    	Type type = Type.INT;
	    	if(t.children.get(0).matches("type INT STAR")){
	    		type = Type.INT_STAR;
	    	}
	    	String id = t.children.get(1).rule.get(1);
	    	if(symbolTable.containsKey(id)){
	    		bail("Duplicated identifier: " + id);
	    	}
	    	symbolTable.put(id, type);
	    	usedSymbols.put(id, false);
	    	variableOffsets.put(id, variableCounter++);
	    	return type;
	    }
	    
	    void readDcls(Tree t){
	    	if(t.children.size()>0){//means you didn't get a tree with rule "dcls -> nothing"
	    		readDcls(t.children.get(0));//recurse until you get rule "dcls -> nothing"
	    		Type dclType = readSDcl(t.children.get(1));
	    		if(t.matches("dcls dcls dcl BECOMES NUM SEMI")){
	    			if(dclType!=Type.INT){
	    				bail("Trying to initialize pointer with a NUM");
	    			}
	    		}else if(t.matches("dcls dcls dcl BECOMES NULL SEMI")){
	    			if(dclType!=Type.INT_STAR){
	    				bail("Trying to initialize INT with NULL");
	    			}
	    		}
	    	}
	    }
	    
	    void checkIDs(Tree t){
	    	if(t.rule.get(0).equals("ID")){
	    		String id = t.rule.get(1);
	    		if(!symbolTable.containsKey(id)){
	    			bail("Identifier "+id+" has not been declared.");
	    		}else{
	    			usedSymbols.put(id, true);
	    		}
	    	}else{
	    		for(int i=0;i<t.children.size();i++){
	        		checkIDs(t.children.get(i));
	    		}
	    	}
	    }
	    
	    void checkStatements(Tree t){
	    	if(t.children.size()>0){
	    		checkStatements(t.children.get(0));//similar to Dcls, recurse until you get rule "statements -> nothing"
	    		checkSStatement(t.children.get(1));
	    	}
	    }
	    
	    void checkSStatement(Tree t){//checks a single statement
	    	/*
	    	  	statement ¡ú lvalue BECOMES expr SEMI
				statement ¡ú IF LPAREN test RPAREN LBRACE statements RBRACE ELSE LBRACE statements RBRACE 
				statement ¡ú WHILE LPAREN test RPAREN LBRACE statements RBRACE 
				statement ¡ú PRINTLN LPAREN expr RPAREN SEMI
				statement ¡ú DELETE LBRACK RBRACK expr SEMI
	    	 */
	    	if(t.matches("statement lvalue BECOMES expr SEMI")){
	    		Type lvalueType = checkLValueType(t.children.get(0));
	    		Type exprType = checkExprType(t.children.get(2));
	    		if(lvalueType!=exprType){
	    			bail("Type mismatch in rule \""+t.rule+"\"");
	    		}
	    	}else if(t.matches("statement IF LPAREN test RPAREN LBRACE statements RBRACE ELSE LBRACE statements RBRACE")){
	    		checkTest(t.children.get(2));
	    		checkStatements(t.children.get(5));
	    		checkStatements(t.children.get(9));
	    	}else if(t.matches("statement WHILE LPAREN test RPAREN LBRACE statements RBRACE")){
	    		checkTest(t.children.get(2));
	    		checkStatements(t.children.get(5));
	    	}else if(t.matches("statement PRINTLN LPAREN expr RPAREN SEMI")){
	    		Type exprType = checkExprType(t.children.get(2));
	    		if(exprType!=Type.INT){
	    			bail("Trying to println a pointer");
	    		}
	    	}else if(t.matches("statement DELETE LBRACK RBRACK expr SEMI")){
	    		Type exprType = checkExprType(t.children.get(3));
	    		if(exprType!=Type.INT_STAR){
	    			bail("Trying to delete a non-pointer");
	    		}
	    	}
	    	assert(false);
	    }
	    
	    Type checkLValueType(Tree t){
	    	t.type = realCheckLValueType(t);
	    	return t.type;
	    }
	    
	    Type realCheckLValueType(Tree t){
	    	/*
	    	lvalue ¡ú ID  
			lvalue ¡ú STAR factor
			lvalue ¡ú LPAREN lvalue RPAREN 
	    	*/
	    	if(t.matches("lvalue ID")){
	    		return symbolTable.get(t.children.get(0).rule.get(1));
	    	}else if(t.matches("lvalue STAR factor")){
	    		//this means lvalue is INT but you should check if factor is a pointer 
	    		Type factorType = checkFactorType(t.children.get(1));
	    		if(factorType!=Type.INT_STAR){
	    			bail("Factor type should be pointer in rule \"lvalue STAR factor\"");
	    		}
	    		return Type.INT;
	    	}else if(t.matches("lvalue LPAREN lvalue RPAREN")){
	    		return checkLValueType(t.children.get(1));
	    	}
	    	assert(false);
	    	return Type.INT;
	    }
	    
	    
	    
	    Type checkExprType(Tree t){
	    	
	    	t.type = realCheckExprType(t);
	    	return t.type;
	    }
	    
	    Type realCheckExprType(Tree t){
	    	/*
	    	expr ¡ú term 
			expr ¡ú expr PLUS term 
			expr ¡ú expr MINUS term 
	    	*/
	    	if(t.matches("expr term")){
	    		return checkTermType(t.children.get(0));
	    	}else if(t.matches("expr expr PLUS term")){
	    		Type exprType = checkExprType(t.children.get(0));
	    		Type termType = checkTermType(t.children.get(2));
	    		if(exprType==Type.INT_STAR){
	    			if(termType==Type.INT_STAR){
	    				bail("A pointer cannot be added to another pointer.");
	    			}else{
	    				return Type.INT_STAR;
	    			}
	    		}else{
	    			if(termType==Type.INT_STAR){
	    				return Type.INT_STAR;
	    			}else{
	    				return Type.INT;
	    			}
	    		}
	    	}else if(t.matches("expr expr MINUS term")){
	    		Type exprType = checkExprType(t.children.get(0));
	    		Type termType = checkTermType(t.children.get(2));
	    		if(exprType==Type.INT_STAR){
	    			if(termType==Type.INT_STAR){
	    				return Type.INT;
	    			}else{
	    				return Type.INT_STAR;
	    			}
	    		}else{
	    			if(termType==Type.INT_STAR){
	    				bail("Cannot subtract a pointer from an int.");
	    			}else{
	    				return Type.INT;
	    			}
	    		}
	    	}
	    	assert(false);
	    	return Type.INT;
	    }
	    
	    Type checkFactorType(Tree t){
	    	t.type = realCheckFactorType(t);
	    	return t.type;
	    }
	    Type realCheckFactorType(Tree t){
	    	/*
	    	factor ¡ú ID  
	    	factor ¡ú NUM  
	    	factor ¡ú NULL  
	    	factor ¡ú LPAREN expr RPAREN  
	    	factor ¡ú AMP lvalue
	    	factor ¡ú STAR factor
	    	factor ¡ú NEW INT LBRACK expr RBRACK
	    	*/
	    	if(t.matches("factor ID")){
	    		return symbolTable.get(t.children.get(0).rule.get(1));
	    	}else if(t.matches("factor NUM")){
	    		return Type.INT;
	    	}else if(t.matches("factor NULL")){
	    		return Type.INT_STAR;
	    	}else if(t.matches("factor LPAREN expr RPAREN")){
	    		return checkExprType(t.children.get(1));
	    	}else if(t.matches("factor AMP lvalue")){
	    		//this means INT_STAR but you should check if lvalue is an int
	    		Type lvalueType = checkLValueType(t.children.get(1));
	    		if(lvalueType!=Type.INT){
	    			bail("lvalue type should be int in rule \"factor AMP lvalue\"");
	    		}
	    		return Type.INT_STAR;
	    	}else if(t.matches("factor STAR factor")){
	    		//this means INT but you should check if lvalue is a pointer
	    		Type factorType = checkFactorType(t.children.get(1));
	    		if(factorType!=Type.INT_STAR){
	    			bail("factor type should be INT_STAR in rule \"factor STAR factor\"");
	    		}
	    		return Type.INT;
	    	}else if(t.matches("factor NEW INT LBRACK expr RBRACK")){
	    		Type exprType = checkExprType(t.children.get(3));
	            if (exprType != Type.INT) {
	                bail("expr type should be INT in rule \"factor NEW INT LBRACK expr RBRACK\"");
	            }
	            return Type.INT_STAR;
	    	}
	    	assert(false);
	    	return Type.INT;
	    }
	    
	    Type checkTermType(Tree t){
	    	t.type = realCheckTermType(t);
	    	return t.type;
	    }
	    Type realCheckTermType(Tree t){
	    	/*
	    	term ¡ú factor 
	    	term ¡ú term STAR factor 
	    	term ¡ú term SLASH factor 
	    	term ¡ú term PCT factor 
	    	*/
	    	if(t.matches("term factor")){
	    		return checkFactorType(t.children.get(0));
			} else {
				Type termType = checkTermType(t.children.get(0));
				Type factorType = checkFactorType(t.children.get(2));
				if(termType!=Type.INT || factorType!=Type.INT){
					bail("Operands on both side of \"*\", \"/\", \"%\" should be of type INT");
				}
				return Type.INT;
			}
	    }
	    
	    void checkTest(Tree t){
	    	Type e1 = checkExprType(t.children.get(0));
	        Type e2 = checkExprType(t.children.get(2));
	        if (e1 != e2) {
	            bail("Type mismatch in test");
	        }
	    }
	    
	    public void traverse(Tree t, int d) {
	        //for(int i = 0; i < d; i++) System.out.print(" ");
	        System.out.println(t.rule); // print root
	        for(Tree c : t.children) {  // print all subtrees
	            traverse(c, d+1);
	        }
	    }
	    
	    // Print an error message and exit the program.
	    void bail(String msg) {
	        System.err.println("ERROR: " + msg);
	        System.exit(0);
	    }

	    // Generate the code for the parse tree t.
	    String genCode(Tree t) {
	        return null;
	    }
	    
	    void printSymbolTable(){
	    	Iterator<String> iterator = symbolTable.keySet().iterator();  
	        
	        while (iterator.hasNext()) {  
	           String key = iterator.next().toString();  
	           Type value = symbolTable.get(key);  
	           String type="";
	           if(value==Type.INT){
	        	   type = "int";
	           }else{
	        	   type = "int*";
	           }
	           System.err.println(key + " " + type);  
	        }
	    }

	}
	
	public static class Generator {
		genMIPS mips = new genMIPS();
		boolean printFlag = false;
		boolean allocFlag = false;
		int loopLabelIndex = 0;
		int ifLabelIndex = 0;
		int tempRegCounter = 5;
		
		void go(Tree t){
			int offset = 3;
			
			System.out.println(".import print");
			System.out.println(".import init");
			System.out.println(".import new");
			System.out.println(".import delete");
			
			mips.add(29, 30, 0);
			//procedure ¡ú INT WAIN LPAREN dcl COMMA dcl RPAREN LBRACE dcls statements RETURN expr SEMI RBRACE 
			Tree procedure = t.children.get(1);

            //Push parameters to stack
			if(symbolTable.containsKey(procedure.children.get(3).children.get(1).rule.get(1))){
				mips.push(1);
				offset++;
			}
			if(symbolTable.containsKey(procedure.children.get(5).children.get(1).rule.get(1))){
				mips.push(2);
				offset++;
			}
            
            mips.loadValue(28, 4);
            
            callInit();
            
            //Push decalarations to stack
            push_declarations(procedure.children.get(8));

            //Statements
            genStatements(procedure.children.get(9));

            //Return expression
            genExpr(procedure.children.get(11));

            
            mips.jr(31);
            
            if(!allocFlag){
            	int num = 11;
            	if(!arrayPass){
            		num = 13;
            	}
            	for(int i=0;i<num;i++){
            		mipsCode.remove(offset);
            	}
            }
            
            mips.printMIPSCode();
            /*
            if(printFlag || allocFlag){
            	mips.addPrintSubroutine();
            }
            if(allocFlag){
            	mips.addAllocSubroutine();
            }
            */
		}
		
		void callInit(){
			mips.push(31);// 1
            mips.stackAbove();//3
            if(!arrayPass){
            	mips.loadValue(2, 0);//2
            }
            mips.loadLabel(1, "init");//2
            mips.jalr(1);//1
            mips.backToGround();//3
            mips.pop(31);//1
		}
		
		/*
		dcl
		dcls
		statements
		statement
		test
		expr
		term
		factor
		lvalue 
		 */
		void push_declarations(Tree t){
			 if (t.children.size() == 0) {
	                return;
	         }
			 //dcls ¡ú dcls dcl BECOMES NUM SEMI
			 //dcls ¡ú dcls dcl BECOMES NULL SEMI
			 
	         push_declarations(t.children.get(0));
	         
	         mips.printComment("Variable: " +  t.children.get(1).children.get(1).rule.get(1));
	            
	         if (t.matches("dcls dcls dcl BECOMES NUM SEMI") && symbolTable.containsKey(t.children.get(1).children.get(1).rule.get(1))) {
	        	 mips.loadValue(3, Integer.parseInt(t.children.get(3).rule.get(1)));
	             mips.push(3);
	         }else if (t.matches("dcls dcls dcl BECOMES NULL SEMI") && symbolTable.containsKey(t.children.get(1).children.get(1).rule.get(1))) {
	             mips.push(0);
	         }
		}
		
		void genStatements(Tree t){
			if(t.children.size()==0){
				return;
			}
			if(t.matches("statements statements statement")){
				genStatements(t.children.get(0));
				genStatement(t.children.get(1));
			}
		}
		
		void genStatement(Tree t){
			/*
			statement ¡ú lvalue BECOMES expr SEMI
			statement ¡ú IF LPAREN test RPAREN LBRACE statements RBRACE ELSE LBRACE statements RBRACE 
			statement ¡ú WHILE LPAREN test RPAREN LBRACE statements RBRACE 
			statement ¡ú PRINTLN LPAREN expr RPAREN SEMI
			statement ¡ú DELETE LBRACK RBRACK expr SEMI
			 */
			if (t.matches("statement PRINTLN LPAREN expr RPAREN SEMI")) {
                printFlag = true;
                mips.push(31);
                mips.stackAbove();
                genExpr(t.children.get(2));
                mips.add(1, 3, 0);
                mips.loadLabel(2, "print");
                mips.jalr(2);
                mips.backToGround();
                mips.pop(31);
            }else if(t.matches("statement lvalue BECOMES expr SEMI")){
            	genLValue(t.children.get(0));
            	mips.add(5, 3, 0);
            	genExpr(t.children.get(2));
            	mips.sw(3, 0, 5);
            }else if(t.matches("statement WHILE LPAREN test RPAREN LBRACE statements RBRACE")){
            	String beginLoop = "BeginLoop"+ loopLabelIndex;
            	String endLoop = "EndLoop" + loopLabelIndex++;
            	
            	mips.label(beginLoop);

                if (genTest(t.children.get(2))) {
                    // branch on zero
                    mips.beq(3, 0, endLoop);
                }
                else {
                    // branch on one
                    mips.bne(3, 0, endLoop);
                }
            	genStatements(t.children.get(5));
            	mips.beq(0, 0, beginLoop);
            	mips.label(endLoop);
            }else if(t.matches("statement IF LPAREN test RPAREN LBRACE statements RBRACE ELSE LBRACE statements RBRACE")){
            	String elseLabel = "Else"+ifLabelIndex;
            	String endifLabel = "Endif"+ifLabelIndex++;
            	
            	if (genTest(t.children.get(2))) {
                    // branch on zero
                    mips.beq(3, 0, elseLabel);
                }
                else {
                    // branch on one
                    mips.bne(3, 0, elseLabel);
                }
            	genStatements(t.children.get(5));
            	mips.beq(0, 0, endifLabel);
            	mips.label(elseLabel);
            	genStatements(t.children.get(9));
            	mips.label(endifLabel);
            	
            }else if(t.matches("statement DELETE LBRACK RBRACK expr SEMI")){
            	allocFlag = true;
                mips.push(31);
                mips.stackAbove();
                genExpr(t.children.get(3));
                mips.add(1, 3, 0);
                mips.loadLabel(2, "delete");
                mips.jalr(2);
                mips.backToGround();
                mips.pop(31);
            }
		}
		
		boolean genTest(Tree t){//algorithm helped by Shida Li
			genExpr(t.children.get(0));
			mips.add(5, 3, 0);
            genExpr(t.children.get(2));

            String testType = t.rule.get(2);
            boolean LT = testType.equals("LT");
            boolean GT = testType.equals("GT");
            boolean LE = testType.equals("LE");
            boolean GE = testType.equals("GE");
            boolean EQ = testType.equals("EQ");
            boolean NE = testType.equals("NE");

            if (LT || GE) {
            	if(t.children.get(0).type==Type.INT_STAR){
            		mips.sltu(3, 5, 3);
            	}else{
            		mips.slt(3, 5, 3);
            	}
                return LT;
            }
            if (GT || LE) {
            	if(t.children.get(0).type==Type.INT_STAR){
            		mips.sltu(3, 3, 5);
            	}else{
                    mips.slt(3, 3, 5);
            	}
                return GT;
            }
            if (EQ || NE) {
                mips.sub(3, 5, 3);
                return NE;
            }
            
            assert(false);
            return false;
		}
		
		void genExpr(Tree t){
			/*
			expr ¡ú term 
			expr ¡ú expr PLUS term 
			expr ¡ú expr MINUS term 
			 */
			if(t.matches("expr term")){
				genTerm(t.children.get(0));
			}else if(t.matches("expr expr PLUS term")){
				genExpr(t.children.get(0));
				if(t.children.get(2).type == Type.INT_STAR){
					mips.mult(3, 28);
					mips.mflo(3);
				}
				mips.add(5, 3, 0);
				genTerm(t.children.get(2));
				if(t.children.get(0).type == Type.INT_STAR){
					mips.mult(3, 28);
					mips.mflo(3);
				}
				mips.add(3, 5, 3);
			}else if(t.matches("expr expr MINUS term")){
				genExpr(t.children.get(0));
				mips.add(5, 3, 0);
				genTerm(t.children.get(2));
				if(t.children.get(0).type == Type.INT_STAR && t.children.get(2).type == Type.INT){
					mips.mult(3, 28);
					mips.mflo(3);
				}
				mips.sub(3, 5, 3);
				if(t.children.get(0).type == Type.INT_STAR && t.children.get(2).type == Type.INT_STAR){
					mips.div(3, 28);
					mips.mflo(3);
				}
			}
		}
		
		void genTerm(Tree t){
			/*
			term ¡ú factor 
			term ¡ú term STAR factor 
			term ¡ú term SLASH factor 
			term ¡ú term PCT factor 
			*/
			if(t.matches("term factor")){
				genFactor(t.children.get(0));
			}else if(t.matches("term term STAR factor")){
				genTerm(t.children.get(0));
				mips.add(5, 3, 0);
				genFactor(t.children.get(2));
				mips.mult(5, 3);
				mips.mflo(3);
			}else if(t.matches("term term SLASH factor")){
				genTerm(t.children.get(0));
				mips.add(5, 3, 0);
				genFactor(t.children.get(2));
				mips.div(5, 3);
				mips.mflo(3);
			}else if(t.matches("term term PCT factor")){
				genTerm(t.children.get(0));
				mips.add(5, 3, 0);
				genFactor(t.children.get(2));
				mips.div(5, 3);
				mips.mfhi(3);
			}
		}
		
		void genFactor(Tree t){
			/*
			factor ¡ú ID  
			factor ¡ú NUM  
			factor ¡ú NULL  
			factor ¡ú LPAREN expr RPAREN  
			factor ¡ú AMP lvalue
			factor ¡ú STAR factor
			 */
			if(t.matches("factor ID")){
				int index = variableOffsets.get(t.children.get(0).rule.get(1));
				mips.lw(3, (index+1)*(-4), 29);//when loading variables, always use the ground-zero SP
			}else if(t.matches("factor LPAREN expr RPAREN")){
				genExpr(t.children.get(1));
			}else if(t.matches("factor NUM")){
				mips.loadValue(3, Integer.parseInt(t.children.get(0).rule.get(1)));
			}else if(t.matches("factor NULL")){
				mips.add(3, 0, 0);
			}else if(t.matches("factor AMP lvalue")){
				genLValue(t.children.get(1));
			}else if(t.matches("factor STAR factor")){
				genFactor(t.children.get(1));
				mips.lw(3, 0, 3);
			}else if(t.matches("factor NEW INT LBRACK expr RBRACK")){
				allocFlag = true;
                mips.push(31);
                mips.stackAbove();
                genExpr(t.children.get(3));
                mips.add(1, 3, 0);
                mips.loadLabel(2, "new");
                mips.jalr(2);
                mips.backToGround();
                mips.pop(31);
			}
		}
		
		void genLValue(Tree t){
			/*
			lvalue ¡ú ID  
			lvalue ¡ú STAR factor
			lvalue ¡ú LPAREN lvalue RPAREN
			 */
			if(t.matches("lvalue ID")){
				int index = variableOffsets.get(t.children.get(0).rule.get(1));
				mips.loadValue(3, (index+1)*(-4));
				mips.add(3, 3, 29);
			}else if(t.matches("lvalue LPAREN lvalue RPAREN")){
				genLValue(t.children.get(1));
			}else if(t.matches("lvalue STAR factor")){
				genFactor(t.children.get(1));
				//mips.lw(3, 0, 3);
			}
		}
	}
	
	public static class genMIPS{//MIPS operations and output
		
        private int stackOffset = 0;
        private int realHeight = 0;
        private int groundMark = 0;
        
        //Macros

        void push(int reg) {
            stackOffset += 4;
            /*if (isTooLong(stackOffset)) {
                sync_stack();
            }*/
            sw(reg, -stackOffset, 30);
        }
        
        void pop(int reg) {
            
            lw(reg, -stackOffset, 30);
            stackOffset -= 4;
        }
        
        
        void loadValue(int reg, int value){
        	lis(reg);
        	word(value);
        }
        
        void loadLabel(int reg, String label){
        	lis(reg);
        	word(label);
        }
        
        void stackAbove(){
        	groundMark = stackOffset;
            realHeight += stackOffset;
            raiseSP(-stackOffset);//the ground is raised
            stackOffset = 0;//reset to ground, but this is a fake ground
        }
		
        void backToGround(){
        	raiseSP(groundMark);//the ground is lowered
            stackOffset = groundMark;
            realHeight -= stackOffset;
            groundMark = 0;//ground is back to 0, this is the real ground
        }
        
        void raiseSP(int value){
        	loadValue(4, value);
        	add(30, 4, 30);
        }
        
		void storeMIPS(String line){
			mipsCode.add(line);
		}
		void printMIPSCode(){
			for(String line : mipsCode){
				System.out.println(line);
			}
		}
		void storeMIPS(String line, int index){
			mipsCode.add(index, line);
		}
		void printMIPS(String line){
			System.out.println(line);
		}
		void oneReg(String cm, int a){
			storeMIPS(cm + " $" + a);
		}
		void twoReg(String cm, int a, int b){
			storeMIPS(cm + " $" + a + ", $" + b);
		}
		void threeReg(String cm, int a, int b, int c){
			storeMIPS(cm + " $" + a + ", $" + b + ", $" + c);
		}
		void regRegImm_int(String cm, int reg1, int reg2, int imm){
			storeMIPS(cm + " $" + reg1 + ", $" + reg2 + ", " + imm);
		}
		void regRegImm_String(String cm, int reg1, int reg2, String label){ //two regregimm since the last parameter can be an integer or String
			storeMIPS(cm + " $" + reg1 + ", $" + reg2 + ", " + label);
		}
		void regImmReg(String cm, int reg1, int imm, int reg2){
			storeMIPS(cm + " $" + reg1 + ", " + imm + "($" + reg2 +")");
		}
		void printComment(String line){
			storeMIPS("; "+line);
		}
		//MIPS commands
		
		//label:
		void label(String label) {
            storeMIPS(label + ":");
        }

        //.word label
        void word(String label) {
            storeMIPS(".word " + label);
        }

        //.word i
        void word(int i) {
            storeMIPS(".word " + i);
        }

        //.word 0xi
        void wordHex(int i) {
            storeMIPS(".word 0x" + Integer.toHexString(i).toUpperCase());
        }

        //add $d, $s, $t
        void add(int d, int s, int t) {
            threeReg("add", d, s, t);
        }

        //sub $d, $s, $t
        void sub(int d, int s, int t) {
            threeReg("sub", d, s, t);
        }

        //mult $s, $t
        void mult(int s, int t) {
            twoReg("mult", s, t);
        }

        //multu $s, $t
        void multu(int s, int t) {
            twoReg("multu", s, t);
        }

        //div $s, $t
        void div(int s, int t) {
            twoReg("div", s, t);
        }

        //divu $s, $t
        void divu(int s, int t) {
            twoReg("divu", s, t);
        }

        //mfhi $d
        void mfhi(int d) {
            oneReg("mfhi", d);
        }

        //mflo $d
        void mflo(int d) {
            oneReg("mflo", d);
        }

        //lis $d
        void lis(int d) {
            oneReg("lis", d);
        }

        //lw $t, i($s) 
        void lw(int t, int i, int s) {
            regImmReg("lw", t, i, s);
        }

        //sw $t, i($s) 
        void sw(int t, int i, int s) {
            regImmReg("sw", t, i, s);
        }

        //slt $d, $s, $t
        void slt(int d, int s, int t) {
            threeReg("slt", d, s, t);
        }

        //sltu $d, $left, $right
        void sltu(int d, int s, int t) {
            threeReg("sltu", d, s, t);
        }

        //beq $s, $t, i
        void beq(int s, int t, int i) {
            regRegImm_int("beq", s, t, i);
        }

        //beq $s, $t, label
        void beq(int s, int t, String label) {
            regRegImm_String("beq", s, t, label);
        }

        //bne $s, $t, i
        void bne(int s, int t, int i) {
            regRegImm_int("bne", s, t, i);
        }

        //bne $s, $t, label
        void bne(int s, int t, String label) {
            regRegImm_String("bne", s, t, label);
        }
        
        //jalr $s
        void jalr(int s) {
            oneReg("jalr", s);
        }
        
        //jr $s
        void jr(int s) {
            oneReg("jr", s);
        }
        
        public void addPrintSubroutine() {
            printMIPS("print:");
            printMIPS("sw $1, -4($30)");
            printMIPS("sw $2, -8($30)");
            printMIPS("sw $3, -12($30)");
            printMIPS("sw $4, -16($30)");
            printMIPS("sw $5, -20($30)");
            printMIPS("sw $6, -24($30)");
            printMIPS("sw $7, -28($30)");
            printMIPS("sw $8, -32($30)");
            printMIPS("sw $9, -36($30)");
            printMIPS("sw $10, -40($30)");
            printMIPS("lis $3");
            printMIPS(".word -40");
            printMIPS("add $30, $30, $3");
            printMIPS("lis $3");
            printMIPS(".word 0xffff000c");
            printMIPS("lis $4");
            printMIPS(".word 10");
            printMIPS("lis $5");
            printMIPS(".word 4");
            printMIPS("add $6, $1, $0");
            printMIPS("slt $7, $1, $0");
            printMIPS("beq $7, $0, IfDone");
            printMIPS("lis $8");
            printMIPS(".word 0x0000002d");
            printMIPS("sw $8, 0($3)");
            printMIPS("sub $6, $0, $6");
            printMIPS("IfDone:");
            printMIPS("add $9, $30, $0");
            printMIPS("Loop:");
            printMIPS("divu $6, $4");
            printMIPS("mfhi $10");
            printMIPS("sw $10, -4($9)");
            printMIPS("mflo $6");
            printMIPS("sub $9, $9, $5");
            printMIPS("slt $10, $0, $6");
            printMIPS("bne $10, $0, Loop");
            printMIPS("lis $7");
            printMIPS(".word 48");
            printMIPS("Loop2:");
            printMIPS("lw $8, 0($9)");
            printMIPS("add $8, $8, $7");
            printMIPS("sw $8, 0($3)");
            printMIPS("add $9, $9, $5");
            printMIPS("bne $9, $30, Loop2");
            printMIPS("sw $4, 0($3)");
            printMIPS("; restore saved registers");
            printMIPS("lis $3");
            printMIPS(".word 40");
            printMIPS("add $30, $30, $3");
            printMIPS("lw $1, -4($30)");
            printMIPS("lw $2, -8($30)");
            printMIPS("lw $3, -12($30)");
            printMIPS("lw $4, -16($30)");
            printMIPS("lw $5, -20($30)");
            printMIPS("lw $6, -24($30)");
            printMIPS("lw $7, -28($30)");
            printMIPS("lw $8, -32($30)");
            printMIPS("lw $9, -36($30)");
            printMIPS("lw $10, -40($30)");
            printMIPS("jr $31");
        }
        
		public void addAllocSubroutine() {

			printMIPS("");
			printMIPS("init:");
			printMIPS("sw $1, -4($30)");
			printMIPS("sw $2, -8($30)");
			printMIPS("sw $3, -12($30)");
			printMIPS("sw $4, -16($30)");
			printMIPS("sw $5, -20($30)");
			printMIPS("sw $6, -24($30)");
			printMIPS("sw $7, -28($30)");
			printMIPS("sw $8, -32($30)");
			printMIPS("");
			printMIPS("lis $4");
			printMIPS(".word 32");
			printMIPS("sub $30, $30, $4");
			printMIPS("");
			printMIPS("lis $1");
			printMIPS(".word end");
			printMIPS("lis $3");
			printMIPS(".word 1024       ; space for free list (way more than necessary)");
			printMIPS("");
			printMIPS("lis $6");
			printMIPS(".word 16         ; size of bookkeeping region at end of program");
			printMIPS("");
			printMIPS("lis $7");
			printMIPS(".word 4096       ; size of heap");
			printMIPS("");
			printMIPS("lis $8");
			printMIPS(".word 1");
			printMIPS("add $2, $2, $2   ; Convert array length to words (*4)");
			printMIPS("add $2, $2, $2");
			printMIPS("add $2, $2, $6   ; Size of \"OS\" added by loader");
			printMIPS("");
			printMIPS("add $5, $1, $6   ; end of program + length of bookkeeping");
			printMIPS("add $5, $5, $2   ; + length of incoming array");
			printMIPS("add $5, $5, $3   ; + length of free list");
			printMIPS("");
			printMIPS("sw $5, 0($1)     ; store address of heap at Mem[end]");
			printMIPS("add $5, $5, $7   ; store end of heap at Mem[end+4]");
			printMIPS("sw $5, 4($1)");
			printMIPS("sw $8, 8($1)     ; store initial size of free list (1) at Mem[end+8]");
			printMIPS("");
			printMIPS("add $5, $1, $6");
			printMIPS("add $5, $5, $2");
			printMIPS("sw $5, 12($1)   ; store location of free list at Mem[end+12]");
			printMIPS("sw $8, 0($5)    ; store initial contents of free list (1) at Mem[end+12]");
			printMIPS("sw $0, 4($5)    ; zero-terminate the free list");
			printMIPS("");
			printMIPS("add $30, $30, $4");
			printMIPS("");
			printMIPS("lw $1, -4($30)");
			printMIPS("lw $2, -8($30)");
			printMIPS("lw $3, -12($30)");
			printMIPS("lw $4, -16($30)");
			printMIPS("lw $5, -20($30)");
			printMIPS("lw $6, -24($30)");
			printMIPS("lw $7, -28($30)");
			printMIPS("lw $8, -32($30)");
			printMIPS("jr $31");
			printMIPS("");
			printMIPS(";; new -- allocates memory (in 16-byte blocks)");
			printMIPS(";; $1 -- requested size in words");
			printMIPS(";; $3 -- address of allocated memory (0 if none available)  OUTPUT");
			printMIPS("new:");
			printMIPS("sw $1, -4($30)");
			printMIPS("sw $2, -8($30)");
			printMIPS("sw $4, -12($30)");
			printMIPS("sw $5, -16($30)");
			printMIPS("sw $6, -20($30)");
			printMIPS("sw $7, -24($30)");
			printMIPS("sw $8, -28($30)");
			printMIPS("sw $9, -32($30)");
			printMIPS("sw $10, -36($30)");
			printMIPS("sw $11, -40($30)");
			printMIPS("sw $12, -44($30)");
			printMIPS("");
			printMIPS("lis $10");
			printMIPS(".word 44");
			printMIPS("sub $30, $30, $10");
			printMIPS("");
			printMIPS(";; Make sure requested size > 0 ; if not, bail out.");
			printMIPS("slt $3, $0, $1");
			printMIPS("beq $3, $0, cleanupN");
			printMIPS("");
			printMIPS("lis $11   ; $11 = 1");
			printMIPS(".word 1");
			printMIPS("");
			printMIPS("add $1, $1, $11 ; One extra word to store deallocation info");
			printMIPS("add $1, $1, $1  ; Convert $1 from words to bytes");
			printMIPS("add $1, $1, $1");
			printMIPS("");
			printMIPS("add $2, $11, $11  ; $2 = 2");
			printMIPS("add $4, $0, $0  ; $4 = counter, to accumulate ceil(log($1))");
			printMIPS("");
			printMIPS(";; Repeatedly dividing $1 by 2 and counting the divisions gives");
			printMIPS(";; floor (log($1)).  To get ceil(log($1)), evaluate floor(log($1-1))+1");
			printMIPS("sub $1, $1, $11  ; So subtract 1 from $1");
			printMIPS("");
			printMIPS("topN:  ; Repeatedly divide $1 by 2, and count iterations");
			printMIPS("beq $1, $0, endloopN");
			printMIPS("div $1, $2      ; $1 /= 2");
			printMIPS("mflo $1");
			printMIPS("add $4, $4, $11  ; $4++");
			printMIPS("");
			printMIPS("beq $0, $0, topN");
			printMIPS("endloopN:");
			printMIPS("");
			printMIPS("add $1, $1, $11  ; Now add 1 to $1 to restore its value after previous sub");
			printMIPS("add $4, $4, $11  ; And add 1 to $4 to complete ceil calculation (see above)");
			printMIPS("");
			printMIPS(";; An address' allocation code will consist of $14-$4 bits");
			printMIPS("lis $5     ; $5 = 14");
			printMIPS(".word 14");
			printMIPS("");
			printMIPS("sub $4, $5, $4  ; $4 <- 14 - $4");
			printMIPS("");
			printMIPS(";; Cap the number of bits in an allocation code at 9 (so we don't allocate");
			printMIPS(";; blocks smaller than 4 words at a time).");
			printMIPS("lis $5");
			printMIPS(".word 9");
			printMIPS("");
			printMIPS("slt $6, $5, $4");
			printMIPS("beq $6, $0, doNotFixN");
			printMIPS("add $4, $5, $0");
			printMIPS("");
			printMIPS("doNotFixN:");
			printMIPS("; Make sure requested size is not too big, i.e., $4>0");
			printMIPS("slt $3, $0, $4");
			printMIPS("beq $3, $0, cleanupN");
			printMIPS("");
			printMIPS("; Now search for a word in the free list with that many bits or fewer");
			printMIPS("; (Fewer bits = larger block size)");
			printMIPS("; Compute largest possible $4-bit number, store in $7");
			printMIPS("add $6, $4, $0    ; countdown from $4 to 0");
			printMIPS("add $7, $11, $0   ; accumulates result by doubling $4 times");
			printMIPS("top2N:");
			printMIPS("add $7, $7, $7    ; double $7");
			printMIPS("sub $6, $6, $11   ; $6--");
			printMIPS("bne $6, $0, top2N");
			printMIPS("");
			printMIPS("sub $7, $7, $11  ; At the end of the loop, $7 = 2^$4 - 1");
			printMIPS("");
			printMIPS("; Find largest word in freelist <= $7");
			printMIPS("lis $8");
			printMIPS(".word findWord");
			printMIPS("sw $31, -4($30)");
			printMIPS("lis $31");
			printMIPS(".word 4");
			printMIPS("sub $30, $30, $31");
			printMIPS("jalr $8          ; call findWord");
			printMIPS("lis $31");
			printMIPS(".word 4");
			printMIPS("add $30, $30, $31");
			printMIPS("lw $31, -4($30)");
			printMIPS("");
			printMIPS("; If no match found, cleanup and abort");
			printMIPS("");
			printMIPS("beq $3, $0, cleanupN  ; if allocation fails, clean up and return 0");
			printMIPS("");
			printMIPS("; Compute minimum code for exact match  (($7+1)/2)");
			printMIPS("add $7, $7, $11");
			printMIPS("div $7, $2");
			printMIPS("mflo $7");
			printMIPS("; If exact match found, remove it from the free list");
			printMIPS("exactN:");
			printMIPS("slt $6, $3, $7");
			printMIPS("bne $6, $0, largerN");
			printMIPS("");
			printMIPS("beq $0, $0, convertN");
			printMIPS("");
			printMIPS("; If larger match found, split into smaller buddies");
			printMIPS("largerN:  ;; buddies are 2$3 and 2$3+1");
			printMIPS("add $3, $3, $3 ;; double $3");
			printMIPS("; add 2$3+1 to free list; evaluate 2$3 as possible candidate");
			printMIPS("lis $6   ;; $6 = address of address of free list");
			printMIPS(".word free");
			printMIPS("lw $8, -4($6)  ;; $8 = length of free list");
			printMIPS("lw $6, 0($6)   ;; $6 = address of free list");
			printMIPS("add $8, $8, $8 ;; convert to words (*4)");
			printMIPS("add $8, $8, $8");
			printMIPS("add $6, $6, $8 ;; address of next spot in free list");
			printMIPS("add $8, $3, $11 ;; $8 = buddy");
			printMIPS("sw $8, 0($6)   ;; add to end of list");
			printMIPS("sw $0, 4($6)");
			printMIPS(";; increment length of free list");
			printMIPS("lis $6");
			printMIPS(".word free");
			printMIPS("lw $8, -4($6)");
			printMIPS("add $8, $8, $11");
			printMIPS("sw $8, -4($6)");
			printMIPS("");
			printMIPS("; now go back to exact with new value of $3, and re-evaluate");
			printMIPS("beq $0, $0, exactN");
			printMIPS("");
			printMIPS("; Convert number to address");
			printMIPS("convertN:");
			printMIPS("add $12, $3, $0  ; retain original freelist word");
			printMIPS("add $7, $0, $0 ;; offset into heap");
			printMIPS("lis $8");
			printMIPS(".word end");
			printMIPS("lw $9, 4($8)  ;; end of heap");
			printMIPS("lw $8, 0($8)  ;; beginning of heap");
			printMIPS("sub $9, $9, $8 ;; size of heap (bytes)");
			printMIPS("top5N:");
			printMIPS("beq $3, $11, doneconvertN");
			printMIPS("div $3, $2");
			printMIPS("mflo $3    ;; $3/2");
			printMIPS("mfhi $10   ;; $3%2");
			printMIPS("beq $10, $0, evenN");
			printMIPS("add $7, $7, $9   ;; add size of heap to offset");
			printMIPS("evenN:");
			printMIPS("div $7, $2       ;; divide offset by 2");
			printMIPS("mflo $7");
			printMIPS("beq $0, $0, top5N");
			printMIPS("");
			printMIPS("doneconvertN:");
			printMIPS("add $3, $8, $7  ;; add start of heap to offset to get address");
			printMIPS("lis $4");
			printMIPS(".word 4");
			printMIPS("add $3, $3, $4  ;; advance one byte for deallocation info");
			printMIPS("sw $12, -4($3)  ;; store deallocation info");
			printMIPS("");
			printMIPS("cleanupN:");
			printMIPS("lis $10");
			printMIPS(".word 44");
			printMIPS("add $30, $30, $10");
			printMIPS("");
			printMIPS("lw $1, -4($30)");
			printMIPS("lw $2, -8($30)");
			printMIPS("lw $4, -12($30)");
			printMIPS("lw $5, -16($30)");
			printMIPS("lw $6, -20($30)");
			printMIPS("lw $7, -24($30)");
			printMIPS("lw $8, -28($30)");
			printMIPS("lw $9, -32($30)");
			printMIPS("lw $10, -36($30)");
			printMIPS("lw $11, -40($30)");
			printMIPS("lw $12, -44($30)");
			printMIPS("jr $31");
			printMIPS("");
			printMIPS(";; delete -- frees allocated memory");
			printMIPS(";; $1 -- address to be deleted");
			printMIPS("delete:");
			printMIPS("sw $1, -4($30)");
			printMIPS("sw $2, -8($30)");
			printMIPS("sw $3, -12($30)");
			printMIPS("sw $4, -16($30)");
			printMIPS("sw $5, -20($30)");
			printMIPS("sw $6, -24($30)");
			printMIPS("sw $11, -28($30)");
			printMIPS("sw $12, -32($30)");
			printMIPS("sw $14, -36($30)");
			printMIPS("");
			printMIPS("lis $6");
			printMIPS(".word 36");
			printMIPS("sub $30, $30, $6");
			printMIPS("");
			printMIPS("lis $11");
			printMIPS(".word 1");
			printMIPS("");
			printMIPS("lis $12");
			printMIPS(".word 2");
			printMIPS("");
			printMIPS("lis $14");
			printMIPS(".word 4");
			printMIPS("");
			printMIPS("lw $2, -4($1) ;; buddy code for the allocated block");
			printMIPS("");
			printMIPS("nextBuddyD:");
			printMIPS("beq $2, $11, notFoundD  ;; if there is no buddy (i.e. buddy code=1), bail out");
			printMIPS(";; compute buddy's buddy code  (i.e, add 1 if code is even, sub 1 if odd)");
			printMIPS("add $3, $2, $0");
			printMIPS("div $3, $12   ; $4 = $3 % 2");
			printMIPS("mfhi $4");
			printMIPS("");
			printMIPS("beq $4, $0, evenD");
			printMIPS("sub $3, $3, $11");
			printMIPS("beq $0, $0, doneParityD");
			printMIPS("evenD:");
			printMIPS("add $3, $3, $11");
			printMIPS("doneParityD:");
			printMIPS("");
			printMIPS(";; Now search free list for the buddy; if found, remove, and divide the");
			printMIPS(";; buddy code by 2; if not found, add current buddy code to the free list.");
			printMIPS("lis $5");
			printMIPS(".word findAndRemove");
			printMIPS("sw $31, -4($30)");
			printMIPS("sub $30, $30, $14");
			printMIPS("add $1, $3, $0");
			printMIPS("jalr $5");
			printMIPS("add $30, $30, $14");
			printMIPS("lw $31, -4($30)");
			printMIPS("");
			printMIPS(";; If the procedure succeeded in finding the buddy, $3 will be 1; else it");
			printMIPS(";; will be 0.");
			printMIPS("beq $3, $0, notFoundD");
			printMIPS("div $2, $12");
			printMIPS("mflo $2");
			printMIPS("beq $0, $0, nextBuddyD");
			printMIPS("");
			printMIPS("notFoundD:");
			printMIPS("lis $4   ;; address of address of free list");
			printMIPS(".word free");
			printMIPS("lw $5, -4($4) ; length of the free list");
			printMIPS("lw $4, 0($4)  ;; address of the free list");
			printMIPS("");
			printMIPS("add $5, $5, $5  ; convert to offset");
			printMIPS("add $5, $5, $5");
			printMIPS("add $5, $4, $5  ; address of next spot in free list");
			printMIPS("sw $2, 0($5)    ; put code back into free list");
			printMIPS("sw $0, 4($5)    ; keep free list 0-terminated");
			printMIPS("");
			printMIPS("; update size of free list");
			printMIPS("lis $4");
			printMIPS(".word free");
			printMIPS("lw $5, -4($4)");
			printMIPS("add $5, $5, $11");
			printMIPS("sw $5, -4($4)");
			printMIPS("");
			printMIPS("lis $6");
			printMIPS(".word 36");
			printMIPS("add $30, $30, $6");
			printMIPS("");
			printMIPS("lw $1, -4($30)");
			printMIPS("lw $2, -8($30)");
			printMIPS("lw $3, -12($30)");
			printMIPS("lw $4, -16($30)");
			printMIPS("lw $5, -20($30)");
			printMIPS("lw $6, -24($30)");
			printMIPS("lw $11, -28($30)");
			printMIPS("lw $12, -32($30)");
			printMIPS("lw $14, -36($30)");
			printMIPS("jr $31");
			printMIPS("");
			printMIPS(";; findWord -- find and remove largest word from free list <= given limit");
			printMIPS(";;             return 0 if not possible");
			printMIPS(";; Registers:");
			printMIPS(";;   $7 -- limit");
			printMIPS(";;   $3 -- output");
			printMIPS("findWord:");
			printMIPS("sw $1, -4($30)");
			printMIPS("sw $2, -8($30)");
			printMIPS("sw $4, -12($30)");
			printMIPS("sw $5, -16($30)");
			printMIPS("sw $6, -20($30)");
			printMIPS("sw $7, -24($30)");
			printMIPS("sw $8, -28($30)");
			printMIPS("sw $9, -32($30)");
			printMIPS("sw $10, -36($30)");
			printMIPS("lis $1");
			printMIPS(".word 36");
			printMIPS("sub $30, $30, $1");
			printMIPS("");
			printMIPS(";; $1 = start of free list");
			printMIPS(";; $2 = length of free list");
			printMIPS("lis $1  ;; address of address of the free list");
			printMIPS(".word free");
			printMIPS("lw $2, -4($1)");
			printMIPS("lw $1, 0($1) ;; address of the free list");
			printMIPS("lis $4   ; $4 = 4 (for looping increments over memory)");
			printMIPS(".word 4");
			printMIPS("lis $9   ; $9 = 1 (for loop decrements)");
			printMIPS(".word 1");
			printMIPS("");
			printMIPS("add $3, $0, $0  ;; initialize output to 0 (not found)");
			printMIPS("add $10, $0, $0 ;; for address of max word");
			printMIPS("beq $2, $0, cleanupFW  ;; skip if no free memory");
			printMIPS("add $5, $2, $0  ;; loop countdown to 0");
			printMIPS("topFW:");
			printMIPS("lw $6, 0($1)");
			printMIPS("slt $8, $7, $6  ;; limit < current item (i.e. item ineligible?)");
			printMIPS("bne $8, $0, ineligibleFW");
			printMIPS("slt $8, $3, $6  ;; max < current item?");
			printMIPS("beq $8, $0, ineligibleFW  ; if not, skip to ineligible");
			printMIPS("add $3, $6, $0  ;; replace max with current");
			printMIPS("add $10, $1, $0 ;; address of current");
			printMIPS("ineligibleFW:");
			printMIPS("add $1, $1, $4  ;; increment address");
			printMIPS("sub $5, $5, $9  ;; decrement loop counter");
			printMIPS("bne $5, $0, topFW     ;; if items left, continue looping");
			printMIPS("");
			printMIPS(";; if candidate not found, bail out (if not found, $3 will still be 0)");
			printMIPS("beq $3, $0, cleanupFW");
			printMIPS("");
			printMIPS(";; now loop from $10 to end, moving up array elements");
			printMIPS("top2FW:");
			printMIPS("lw $6, 4($10)  ;; grab next element in array");
			printMIPS("sw $6, 0($10)  ;; store in current position");
			printMIPS("add $10, $10, $4 ;; increment address");
			printMIPS("bne $6, $0, top2FW  ;; continue while elements nonzero");
			printMIPS("");
			printMIPS(";; decrement length of free list");
			printMIPS("lis $2");
			printMIPS(".word end");
			printMIPS("lw $4, 8($2)");
			printMIPS("sub $4, $4, $9  ; $9 still 1");
			printMIPS("sw $4, 8($2)");
			printMIPS("");
			printMIPS("cleanupFW:");
			printMIPS("");
			printMIPS("lis $1");
			printMIPS(".word 36");
			printMIPS("add $30, $30, $1");
			printMIPS("lw $1, -4($30)");
			printMIPS("lw $2, -8($30)");
			printMIPS("lw $4, -12($30)");
			printMIPS("lw $5, -16($30)");
			printMIPS("lw $6, -20($30)");
			printMIPS("lw $7, -24($30)");
			printMIPS("lw $8, -28($30)");
			printMIPS("lw $9, -32($30)");
			printMIPS("lw $10, -36($30)");
			printMIPS("jr $31");
			printMIPS("");
			printMIPS(";; findAndRemove -- find and remove given word from free list");
			printMIPS(";;             return 1 for success, 0 for failure");
			printMIPS(";; Registers:");
			printMIPS(";;   $1 -- word to remove");
			printMIPS(";;   $3 -- output (1 = success, 0 = failure)");
			printMIPS("findAndRemove:");
			printMIPS("sw $1, -4($30)");
			printMIPS("sw $2, -8($30)");
			printMIPS("sw $4, -12($30)");
			printMIPS("sw $5, -16($30)");
			printMIPS("sw $6, -20($30)");
			printMIPS("sw $7, -24($30)");
			printMIPS("sw $8, -28($30)");
			printMIPS("sw $9, -32($30)");
			printMIPS("sw $11, -36($30)");
			printMIPS("sw $14, -40($30)");
			printMIPS("");
			printMIPS("lis $9");
			printMIPS(".word 40");
			printMIPS("sub $30, $30, $9");
			printMIPS("");
			printMIPS("lis $11");
			printMIPS(".word 1");
			printMIPS("");
			printMIPS("lis $14");
			printMIPS(".word 4");
			printMIPS("");
			printMIPS("lis $2     ;; address of address of the free list");
			printMIPS(".word free");
			printMIPS("lw $4, -4($2) ;; length of the free list");
			printMIPS("lw $2, 0($2)  ;; address of the free list");
			printMIPS("");
			printMIPS("");
			printMIPS("add $3, $0, $0 ; success code");
			printMIPS("add $6, $0, $0 ; address of found code");
			printMIPS("add $7, $0, $0 ; loop counter");
			printMIPS("");
			printMIPS("topFaR:  ; loop through free list, looking for the code");
			printMIPS("beq $4, $0, cleanupFaR");
			printMIPS("lw $5, 0($2) ; next code in list");
			printMIPS("bne $5, $1, notEqualFaR  ;; compare with input");
			printMIPS("add $6, $6, $2  ; if code found, save its address");
			printMIPS("beq $0, $0, removeFaR");
			printMIPS("");
			printMIPS("notEqualFaR:  ; current item not the one we're looking for; update counters");
			printMIPS("add $2, $2, $14");
			printMIPS("add $7, $7, $11");
			printMIPS("bne $7, $4, topFaR");
			printMIPS("");
			printMIPS("removeFaR:");
			printMIPS("beq $6, $0, cleanupFaR  ;; if code not found, bail out");
			printMIPS("");
			printMIPS("top2FaR:  ; now loop through the rest of the free list, moving each item one");
			printMIPS("; slot up");
			printMIPS("lw $8, 4($2)");
			printMIPS("sw $8, 0($2)");
			printMIPS("add $2, $2, $14  ; add 4 to current address");
			printMIPS("add $7, $7, $11  ; add 1 to loop counter");
			printMIPS("bne $7, $4, top2FaR");
			printMIPS("add $3, $11, $0  ;; set success code");
			printMIPS("");
			printMIPS(";; decrement size");
			printMIPS("lis $2");
			printMIPS(".word free");
			printMIPS("lw $5, -4($2)");
			printMIPS("sub $5, $5, $11");
			printMIPS("sw $5, -4($2)");
			printMIPS("");
			printMIPS("cleanupFaR:");
			printMIPS("lis $9");
			printMIPS(".word 40");
			printMIPS("add $30, $30, $9");
			printMIPS("");
			printMIPS("lw $1, -4($30)");
			printMIPS("lw $2, -8($30)");
			printMIPS("lw $4, -12($30)");
			printMIPS("lw $5, -16($30)");
			printMIPS("lw $6, -20($30)");
			printMIPS("lw $7, -24($30)");
			printMIPS("lw $8, -28($30)");
			printMIPS("lw $9, -32($30)");
			printMIPS("lw $11, -36($30)");
			printMIPS("lw $14, -40($30)");
			printMIPS("jr $31");
			printMIPS("");
			printMIPS(";; printFreeList -- prints the contents of the free list, for testing and");
			printMIPS(";;  debugging purposes.  Requires a print routine for $1 to be linked in.");
			printMIPS(";;  Registers:");
			printMIPS(";;    Input -- none");
			printMIPS(";;    Output -- none");
			printMIPS("printFreeList:");
			printMIPS("sw $1, -4($30)");
			printMIPS("sw $2, -8($30)");
			printMIPS("sw $3, -12($30)");
			printMIPS("sw $4, -16($30)");
			printMIPS("sw $5, -20($30)");
			printMIPS("sw $6, -24($30)");
			printMIPS("sw $7, -28($30)");
			printMIPS("sw $8, -32($30)");
			printMIPS("lis $6");
			printMIPS(".word 32");
			printMIPS("sub $30, $30, $6");
			printMIPS("");
			printMIPS("lis $3   ; address of address of the start of the free list");
			printMIPS(".word free");
			printMIPS("lis $4");
			printMIPS(".word 4");
			printMIPS("lis $5   ; external print procedure");
			printMIPS(".word print");
			printMIPS("lis $6");
			printMIPS(".word 1");
			printMIPS("");
			printMIPS("lw $2, -4($3) ; $2 = length of free list; countdown to 0 for looping");
			printMIPS("lw $3, 0($3) ; $3 = address of the start of the free list");
			printMIPS("");
			printMIPS(";; loop through the free list, and print each element");
			printMIPS("topPFL:");
			printMIPS("beq $2, $0, endPFL  ;; skip if free list empty");
			printMIPS("");
			printMIPS("lw $1, 0($3)     ; store in $1 the item to be printed");
			printMIPS("sw $31, -4($30)");
			printMIPS("sub $30, $30, $4");
			printMIPS("jalr $5          ; call external print procedure");
			printMIPS("add $30, $30, $4");
			printMIPS("lw $31, -4($30)");
			printMIPS("add $3, $3, $4   ; update current address and loop counter");
			printMIPS("sub $2, $2, $6");
			printMIPS("bne $2, $0, topPFL");
			printMIPS("");
			printMIPS("endPFL:");
			printMIPS(";; add an extra newline at the end, so that if this procedure is called");
			printMIPS(";; multiple times, we can distinguish where one call ends and the next");
			printMIPS(";; begins");
			printMIPS("lis $6");
			printMIPS(".word 0xffff000c");
			printMIPS("lis $5");
			printMIPS(".word 10");
			printMIPS("sw $5, 0($6)");
			printMIPS("");
			printMIPS("lis $6");
			printMIPS(".word 32");
			printMIPS("add $30, $30, $6");
			printMIPS("lw $1, -4($30)");
			printMIPS("lw $2, -8($30)");
			printMIPS("lw $3, -12($30)");
			printMIPS("lw $4, -16($30)");
			printMIPS("lw $5, -20($30)");
			printMIPS("lw $6, -24($30)");
			printMIPS("lw $7, -28($30)");
			printMIPS("lw $8, -32($30)");
			printMIPS("jr $31");
			printMIPS("end:");
			printMIPS(".word 0 ;; beginnning of heap");
			printMIPS(".word 0 ;; end of heap");
			printMIPS(".word 0 ;; length of free list");
			printMIPS("free: .word 0 ;; beginning of free list");
			printMIPS("");

		}
	}
	
}