
import java.util.*;

/** A sample main class demonstrating the use of the Tokenizer.Tokenizer.
 * This main class just outputs each line in the input, followed by
 * the tokens returned by the lexer for that line.
 *
 * @version 071011.0
 */
public class WLPPScan {
    public static final void main(String[] args) {
        new WLPPScan().run();
    }

    private Lexer lexer = new Lexer();

    private void run() {
        Scanner in = new Scanner(System.in);
        while(in.hasNextLine()) {
            String line = in.nextLine();

            // Scan the line into an array of tokens.
            Token[] tokens;
            tokens = lexer.scan(line);

            // Print the tokens produced by the scanner
            for( int i = 0; i < tokens.length; i++ ) {
                System.out.println(tokens[i].kind+" "+tokens[i].lexeme);
            }
        }
        System.out.flush();
    }
}

/** The various kinds of tokens. */
enum Kind {
    ID,         // Opcode or identifier (use of a label)
    NUM,
    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,
    RETURN,
    IF,
    ELSE,
    WHILE,
    PRINTLN,
    WAIN,
    BECOMES,
    INT,
    EQ,
    NE,
    LT,
    GT,
    LE,
    GE,
    PLUS,
    MINUS,
    STAR,
    SLASH,
    COMMENT,
    PCT,
    COMMA,
    SEMI,
    NEW,
    DELETE,
    LBRACK,
    RBRACK,
    AMP,
    NULL,
    WHITESPACE,
    ERROR
}

/** Representation of a token. */
class Token {
    public Kind kind;     // The kind of token.
    public String lexeme; // String representation of the actual token in the
                          // source code.

    public Token(Kind kind, String lexeme) {
        this.kind = kind;
        this.lexeme = lexeme;
    }
    /*public String toString() {
        return kind+" {"+lexeme+"}";
    }
    /** Returns an integer representation of the token. For tokens of kind
     * INT (decimal integer constant) and HEXINT (hexadecimal integer
     * constant), returns the integer constant. For tokens of kind
     * REGISTER, returns the register number.
     
    public int toInt() {
        if(kind == common.TokenType.INT) return parseLiteral(lexeme, 10, 32);
        else if(kind == common.TokenType.HEXINT) return parseLiteral(lexeme.substring(2), 16, 32);
        else if(kind == common.TokenType.REGISTER) return parseLiteral(lexeme.substring(1), 10, 5);
        else {
            System.err.println("ERROR in to-int conversion.");
            System.exit(1);
            return 0;
        }
    }
    private int parseLiteral(String s, int base, int bits) {
        BigInteger x = new BigInteger(s, base);
        if(x.signum() > 0) {
            if(x.bitLength() > bits) {
                System.err.println("ERROR in parsing: constant out of range: "+s);
                System.exit(1);
            }
        } else if(x.signum() < 0) {
            if(x.negate().bitLength() > bits-1
            && x.negate().subtract(new BigInteger("1")).bitLength() > bits-1) {
                System.err.println("ERROR in parsing: constant out of range: "+s);
                System.exit(1);
            }
        }
        return (int) (x.longValue() & ((1L << bits) - 1));
    }*/
}

/** Tokenizer.Tokenizer -- reads an input line, and partitions it into a list of tokens. */
class Lexer {
    public Lexer() {
        CharSet whitespace = new Chars("\t\n\r ");
        CharSet letters = new Chars(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
        CharSet lettersDigits = new Chars(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
        CharSet digits = new Chars("0123456789");
        CharSet oneToNine = new Chars("123456789");
        CharSet zero = new Chars("0");
        CharSet lparent = new Chars("(");
        CharSet rparent = new Chars(")");
        CharSet lbracer = new Chars("{");
        CharSet rbracer = new Chars("}");
        CharSet equal = new Chars("=");
        CharSet not = new Chars("!");
        CharSet lessthan = new Chars("<");
        CharSet greaterthan = new Chars(">");
        CharSet plus = new Chars("+");
        CharSet minus = new Chars("-");
        CharSet star = new Chars("*");
        CharSet slash = new Chars("/");
        CharSet pct = new Chars("%");
        CharSet comma = new Chars(",");
        CharSet semi = new Chars(";");
        CharSet lbracket = new Chars("[");
        CharSet rbracket = new Chars("]");
        CharSet and = new Chars("&");
        CharSet all = new AllChars(); 

        table = new Transition[] {
                new Transition(State.START, whitespace, State.WHITESPACE),
                new Transition(State.START, letters, State.ID),
                new Transition(State.START, zero, State.ZERO),
                new Transition(State.ZERO, lettersDigits, State.ERROR),
                new Transition(State.START, oneToNine, State.NUM),
                new Transition(State.START, lparent, State.LPAREN),
                new Transition(State.START, rparent, State.RPAREN),
                new Transition(State.START, lbracer, State.LBRACE),
                new Transition(State.START, rbracer, State.RBRACE),
                new Transition(State.START, equal, State.BECOMES),
                new Transition(State.START, not, State.NOT),
                new Transition(State.START, lessthan, State.LT),
                new Transition(State.START, greaterthan, State.GT),
                new Transition(State.START, plus, State.PLUS),
                new Transition(State.START, minus, State.MINUS),
                new Transition(State.START, star, State.STAR),
                new Transition(State.START, slash, State.SLASH),
                new Transition(State.START, pct, State.PCT),
                new Transition(State.START, comma, State.COMMA),
                new Transition(State.START, semi, State.SEMI),
                new Transition(State.START, lbracket, State.LBRACK),
                new Transition(State.START, rbracket, State.RBRACK),
                new Transition(State.START, and, State.AMP),
                new Transition(State.ID, lettersDigits, State.ID),
                new Transition(State.NUM, digits, State.NUM),
                new Transition(State.BECOMES, equal, State.EQ),
                new Transition(State.NOT, equal, State.NE),
                new Transition(State.LT, equal, State.LE),
                new Transition(State.GT, equal, State.GE),
                new Transition(State.SLASH, slash, State.COMMENT),
        };
    }
    /** Partitions the line passed in as input into an array of tokens.
     * The array of tokens is returned.
     */
    public Token[] scan( String input ) {
        List<Token> ret = new ArrayList<Token>();
        if(input.length() == 0) return new Token[0];
        int i = 0;
        int startIndex = 0;
        State state = State.START;
        while(true) {
            Transition t = null;
            if(i < input.length()) t = findTransition(state, input.charAt(i));
            if(t == null) {
                // no more transitions possible
                if(!state.isFinal()) {
                    System.err.println("ERROR in lexing after reading "+input.substring(0, i));
                    System.exit(1);
                }
                if(state.kind == Kind.ERROR){
                	System.err.println("ERROR in lexing after reading "+input.substring(0, i));
                    System.exit(1);
                }
                if(state.kind == Kind.COMMENT){
                	//ret.add(new TerminalToken(state.kind, input.substring(startIndex, input.length())));
                	break; 
                }
                if(state.kind == Kind.ID){
					if (input.substring(startIndex, i).equals("return")) {
						state = State.RETURN;
					} else if (input.substring(startIndex, i).equals("if")) {
						state = State.IF;
					} else if (input.substring(startIndex, i).equals("else")) {
						state = State.ELSE;
					} else if (input.substring(startIndex, i).equals("while")) {
						state = State.WHILE;
					} else if (input.substring(startIndex, i).equals("println")) {
						state = State.PRINTLN;	
					} else if (input.substring(startIndex, i).equals("wain")) {
						state = State.WAIN;
					} else if (input.substring(startIndex, i).equals("int")) {
						state = State.INT;
					} else if (input.substring(startIndex, i).equals("new")) {
						state = State.NEW;
					} else if (input.substring(startIndex, i).equals("delete")) {
						state = State.DELETE;
					} else if (input.substring(startIndex, i).equals("NULL")) {
						state = State.NULL;
					}
                }
                if( state.kind != Kind.WHITESPACE ) {
                    ret.add(new Token(state.kind, input.substring(startIndex, i)));
                }
                
                startIndex = i;
                state = State.START;
                if(i >= input.length()) break;
            } else {
                state = t.toState;
                i++;
            }
        }
        return ret.toArray(new Token[ret.size()]);
    }

    ///////////////////////////////////////////////////////////////
    // END OF PUBLIC METHODS
    ///////////////////////////////////////////////////////////////

    private Transition findTransition(State state, char c) {
        for( int j = 0; j < table.length; j++ ) {
            Transition t = table[j];
            if(t.fromState == state && t.chars.contains(c)) {
                return t;
            }
        }
        return null;
    }

    private static enum State {
    	
    	START(null),
    	ID(Kind.ID),
    	NUM(Kind.NUM),
    	ZERO(Kind.NUM),
    	LPAREN(Kind.LPAREN),
    	RPAREN(Kind.RPAREN),
    	LBRACE(Kind.LBRACE),
    	RBRACE(Kind.RBRACE),
    	R(null),
    	RE(null),
    	RET(null),
    	RETU(null),
    	RETUR(null),
    	RETURN(Kind.RETURN),
    	I(null),
    	IF(Kind.IF),
    	E(null),
    	EL(null),
    	ELS(null),
    	ELSE(Kind.ELSE),
    	W(null),
    	WH(null),
    	WHI(null),
    	WHIL(null),
    	WHILE(Kind.WHILE),
    	P(null),
    	PR(null),
    	PRI(null),
    	PRIN(null),
    	PRINT(null),
    	PRINTL(null),
    	PRINTLN(Kind.PRINTLN),
    	WA(null),
    	WAI(null),
    	WAIN(Kind.WAIN),
    	BECOMES(Kind.BECOMES),
    	IN(null),
    	INT(Kind.INT),
    	EQ(Kind.EQ),
    	NOT(null),
    	NE(Kind.NE),
    	LT(Kind.LT),
    	GT(Kind.GT),
    	LE(Kind.LE),
    	GE(Kind.GE),
    	PLUS(Kind.PLUS),
    	MINUS(Kind.MINUS),
    	STAR(Kind.STAR),
    	SLASH(Kind.SLASH),
    	COMMENT(Kind.COMMENT),
    	PCT(Kind.PCT),
    	COMMA(Kind.COMMA),
    	SEMI(Kind.SEMI),
    	NEW(Kind.NEW),
    	DELETE(Kind.DELETE),
    	LBRACK(Kind.LBRACK),
    	RBRACK(Kind.RBRACK),
    	AMP(Kind.AMP),
    	NULL(Kind.NULL),
    	WHITESPACE(Kind.WHITESPACE),
    	ERROR(Kind.ERROR);
        State(Kind kind) {
            this.kind = kind;
        }
        Kind kind;
        boolean isFinal() {
            return kind != null;
        }
    }

    private interface CharSet {
        public boolean contains(char newC);
    }
    private class Chars implements CharSet {
        private String chars;
        public Chars(String chars) { this.chars = chars; }
        public boolean contains(char newC) {
            return chars.indexOf(newC) >= 0;
        }
    }
    private class AllChars implements CharSet {
        public boolean contains(char newC) {
            return true;
        }
    }

    private class Transition {
        State fromState;
        CharSet chars;
        State toState;
        Transition(State fromState, CharSet chars, State toState) {
            this.fromState = fromState;
            this.chars = chars;
            this.toState = toState;
        }
    }
    private Transition[] table;
}
