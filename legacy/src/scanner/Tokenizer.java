package scanner;

import exception.InvalidSyntaxException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Tokenizer {

    private static boolean inComment = false;
    public ArrayList<TerminalToken> tokenList = new ArrayList<>();
    private TransitionCollection table = new TransitionCollection();

    public Tokenizer() {

        CharSet whitespace = new Chars("\t\n\r ");
        CharSet letters = new CharsIDStart();
        CharSet lettersDigits = new CharsIDPart();
        CharSet escapables = new Chars("tbnrf'\"\\");
        CharSet digits = new Chars("0123456789");
        CharSet nonZeroDigits = new Chars("123456789");
        CharSet hexDigits = new Chars("0123456789abcdefABCDEF");
        CharSet octalDigits = new Chars("01234567");
        CharSet nonOctalDecimalDigits = new Chars("89");
        CharSet hexX = new Chars("xX");
        CharSet longTypeSuffix = new Chars("lL");
        CharSet exponentIndicator = new Chars("eE");
        CharSet sign = new Chars("+-");
        CharSet floatTypeSuffix = new Chars("fFdD");
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
        CharSet mod = new Chars("%");
        CharSet comma = new Chars(",");
        CharSet semi = new Chars(";");
        CharSet lbracket = new Chars("[");
        CharSet rbracket = new Chars("]");
        CharSet emp = new Chars("&");
        CharSet or = new Chars("|");
        CharSet dot = new Chars(".");
        CharSet escape = new Chars("\\");
        CharSet squote = new Chars("\'");
        CharSet dquote = new Chars("\"");
        CharSet question = new Chars("?");
        CharSet colon = new Chars(":");
        CharSet tilda = new Chars("~");
        CharSet caret = new Chars("^");
        CharSet allCharsForChar = new AllCharsExcept("\\'");
        CharSet allCharsForString = new AllCharsExcept("\\\"");
        CharSet zeroToThree = new Chars("0123");
        CharSet gtThreeOctal = new Chars("4567");

        table.add(new Transition(State.START, whitespace, State.WHITESPACE));
        table.add(new Transition(State.START, letters, State.ID));

        // Integer or Float literal transitions
        table.add(new Transition(State.START, zero, State.IntFloatState1));
        table.add(new Transition(State.START, nonZeroDigits, State.IntFloatState2));
        table.add(new Transition(State.START, dot, State.IntFloatState3));
        table.add(new Transition(State.IntFloatState1, octalDigits, State.IntFloatState4));
        table.add(new Transition(State.IntFloatState1, nonOctalDecimalDigits, State.IntFloatState5));
        table.add(new Transition(State.IntFloatState1, dot, State.IntFloatState6));
        table.add(new Transition(State.IntFloatState1, hexX, State.IntFloatState7));
        table.add(new Transition(State.IntFloatState1, exponentIndicator, State.IntFloatState8));
        table.add(new Transition(State.IntFloatState1, longTypeSuffix, State.IntFloatState9));
        table.add(new Transition(State.IntFloatState1, floatTypeSuffix, State.IntFloatState10));
        table.add(new Transition(State.IntFloatState2, digits, State.IntFloatState2));
        table.add(new Transition(State.IntFloatState2, dot, State.IntFloatState6));
        table.add(new Transition(State.IntFloatState2, exponentIndicator, State.IntFloatState8));
        table.add(new Transition(State.IntFloatState2, longTypeSuffix, State.IntFloatState9));
        table.add(new Transition(State.IntFloatState2, floatTypeSuffix, State.IntFloatState10));
        table.add(new Transition(State.IntFloatState3, digits, State.IntFloatState6));
        table.add(new Transition(State.IntFloatState4, octalDigits, State.IntFloatState4));
        table.add(new Transition(State.IntFloatState4, nonOctalDecimalDigits, State.IntFloatState5));
        table.add(new Transition(State.IntFloatState4, dot, State.IntFloatState6));
        table.add(new Transition(State.IntFloatState4, longTypeSuffix, State.IntFloatState9));
        table.add(new Transition(State.IntFloatState4, floatTypeSuffix, State.IntFloatState10));
        table.add(new Transition(State.IntFloatState5, digits, State.IntFloatState5));
        table.add(new Transition(State.IntFloatState5, dot, State.IntFloatState6));
        table.add(new Transition(State.IntFloatState5, exponentIndicator, State.IntFloatState8));
        table.add(new Transition(State.IntFloatState5, floatTypeSuffix, State.IntFloatState10));
        table.add(new Transition(State.IntFloatState6, digits, State.IntFloatState6));
        table.add(new Transition(State.IntFloatState6, exponentIndicator, State.IntFloatState8));
        table.add(new Transition(State.IntFloatState6, floatTypeSuffix, State.IntFloatState10));
        table.add(new Transition(State.IntFloatState7, hexDigits, State.IntFloatState11));
        table.add(new Transition(State.IntFloatState8, digits, State.IntFloatState12));
        table.add(new Transition(State.IntFloatState8, sign, State.IntFloatState13));
        table.add(new Transition(State.IntFloatState11, hexDigits, State.IntFloatState11));
        table.add(new Transition(State.IntFloatState11, longTypeSuffix, State.IntFloatState9));
        table.add(new Transition(State.IntFloatState12, digits, State.IntFloatState12));
        table.add(new Transition(State.IntFloatState12, floatTypeSuffix, State.IntFloatState10));
        table.add(new Transition(State.IntFloatState13, digits, State.IntFloatState12));
        table.add(new Transition(State.IntFloatState13, floatTypeSuffix, State.IntFloatState10));

        // Char literal transitions
        table.add(new Transition(State.START, squote, State.CharacterLiteral1));
        table.add(new Transition(State.CharacterLiteral1, allCharsForChar, State.CharacterLiteral2));
        table.add(new Transition(State.CharacterLiteral1, escape, State.CharacterLiteral3));
        table.add(new Transition(State.CharacterLiteral2, squote, State.CharacterLiteral4));
        table.add(new Transition(State.CharacterLiteral3, escapables, State.CharacterLiteral2));
        table.add(new Transition(State.CharacterLiteral3, zeroToThree, State.CharacterLiteral5));
        table.add(new Transition(State.CharacterLiteral3, gtThreeOctal, State.CharacterLiteral6));
        table.add(new Transition(State.CharacterLiteral5, octalDigits, State.CharacterLiteral6));
        table.add(new Transition(State.CharacterLiteral5, squote, State.CharacterLiteral4));
        table.add(new Transition(State.CharacterLiteral6, octalDigits, State.CharacterLiteral2));
        table.add(new Transition(State.CharacterLiteral6, squote, State.CharacterLiteral4));

        // String literal transitions
        table.add(new Transition(State.START, dquote, State.StringLiteral1));
        table.add(new Transition(State.StringLiteral1, allCharsForString, State.StringLiteral1));
        table.add(new Transition(State.StringLiteral1, escape, State.StringLiteral2));
        table.add(new Transition(State.StringLiteral1, dquote, State.StringLiteral3));
        table.add(new Transition(State.StringLiteral2, escapables, State.StringLiteral1));
        table.add(new Transition(State.StringLiteral2, zeroToThree, State.StringLiteral4));
        table.add(new Transition(State.StringLiteral2, gtThreeOctal, State.StringLiteral5));
        table.add(new Transition(State.StringLiteral4, octalDigits, State.StringLiteral5));
        table.add(new Transition(State.StringLiteral4, allCharsForString, State.StringLiteral1));
        table.add(new Transition(State.StringLiteral4, dquote, State.StringLiteral3));
        table.add(new Transition(State.StringLiteral4, escape, State.StringLiteral2));
        table.add(new Transition(State.StringLiteral5, octalDigits, State.StringLiteral1));
        table.add(new Transition(State.StringLiteral5, allCharsForString, State.StringLiteral1));
        table.add(new Transition(State.StringLiteral5, dquote, State.StringLiteral3));
        table.add(new Transition(State.StringLiteral5, escape, State.StringLiteral2));

        table.add(new Transition(State.START, lparent, State.LPAREN));
        table.add(new Transition(State.START, rparent, State.RPAREN));
        table.add(new Transition(State.START, lbracer, State.LBRACE));
        table.add(new Transition(State.START, rbracer, State.RBRACE));
        table.add(new Transition(State.START, equal, State.BECOMES));
        table.add(new Transition(State.START, not, State.NOT));
        table.add(new Transition(State.START, lessthan, State.LT));
        table.add(new Transition(State.START, greaterthan, State.GT));
        table.add(new Transition(State.START, plus, State.PLUS));
        table.add(new Transition(State.START, minus, State.MINUS));
        table.add(new Transition(State.START, star, State.STAR));
        table.add(new Transition(State.START, slash, State.SLASH));
        table.add(new Transition(State.START, mod, State.MOD));
        table.add(new Transition(State.START, comma, State.COMMA));
        table.add(new Transition(State.START, semi, State.SEMI));
        table.add(new Transition(State.START, lbracket, State.LBRACK));
        table.add(new Transition(State.START, rbracket, State.RBRACK));
        table.add(new Transition(State.START, emp, State.BAND));
        table.add(new Transition(State.START, question, State.QUESTION));
        table.add(new Transition(State.START, colon, State.COLON));
        table.add(new Transition(State.START, tilda, State.FLIP));
        table.add(new Transition(State.START, caret, State.BXOR));
        table.add(new Transition(State.START, or, State.BOR));
        table.add(new Transition(State.LT, lessthan, State.BLSHIFT));
        table.add(new Transition(State.GT, greaterthan, State.BRSHIFT));
        table.add(new Transition(State.BRSHIFT, greaterthan, State.BZEROFILL));
        table.add(new Transition(State.PLUS, equal, State.PLUSASSIGN));
        table.add(new Transition(State.MINUS, equal, State.MINUSASSIGN));
        table.add(new Transition(State.STAR, equal, State.MULTASSIGN));
        table.add(new Transition(State.SLASH, equal, State.DIVIDEASSIGN));
        table.add(new Transition(State.BAND, equal, State.BANDASSIGN));
        table.add(new Transition(State.BOR, equal, State.BORASSIGN));
        table.add(new Transition(State.BXOR, equal, State.BXORASSIGN));
        table.add(new Transition(State.MOD, equal, State.MODASSIGN));
        table.add(new Transition(State.BLSHIFT, equal, State.BLSHIFTASSIGN));
        table.add(new Transition(State.BRSHIFT, equal, State.BRSHIFTASSIGN));
        table.add(new Transition(State.BZEROFILL, equal, State.BZEROFILLASSIGN));
        table.add(new Transition(State.ID, lettersDigits, State.ID));
        table.add(new Transition(State.BECOMES, equal, State.EQ));
        table.add(new Transition(State.NOT, equal, State.NE));
        table.add(new Transition(State.LT, equal, State.LE));
        table.add(new Transition(State.GT, equal, State.GE));
        table.add(new Transition(State.SLASH, slash, State.SCOMMENT));
        table.add(new Transition(State.SLASH, star, State.INCOMMENT));
        table.add(new Transition(State.PLUS, plus, State.PLUSPLUS));
        table.add(new Transition(State.MINUS, minus, State.MINUSMINUS));
        table.add(new Transition(State.BAND, emp, State.AND));
        table.add(new Transition(State.BOR, or, State.OR));
        table.add(new Transition(State.INCOMMENT, star, State.RCOMMEN));
        table.add(new Transition(State.RCOMMEN, slash, State.COMMENT));
        table.add(new Transition(State.RCOMMEN, star, State.RCOMMEN));
    }

    private ArrayList<TerminalToken> scan(String input) throws InvalidSyntaxException {
        ArrayList<TerminalToken> ret = new ArrayList<>();
        if (input.length() == 0) return ret;
        int i = 0;
        int startIndex = 0;
        State state = inComment ? State.INCOMMENT : State.START;
        while (true) {
            State t = null;
            if (i < input.length()) t = table.findToState(state, input.charAt(i));
            if (t == null) {
                if (state == State.RCOMMEN) {
                    inComment = true;
                    state = State.INCOMMENT;
                    i++;
                    if (i >= input.length()) break;
                    continue;
                }
                if (state == State.INCOMMENT) {
                    inComment = true;
                    i++;
                    if (i >= input.length()) break;
                    continue;
                }
                // no more transitions possible
                if (!state.isFinal()) {
                    throw new InvalidSyntaxException("ERROR in lexing after reading " + input.substring(0, i));
                }
                if (state.kind == Kind.ERROR) {
                    throw new InvalidSyntaxException("ERROR in lexing after reading " + input.substring(0, i));
                }
                if (state.kind == Kind.SCOMMENT) {
                    break;
                } else if (state.kind == Kind.ID) {
                    if (input.substring(startIndex, i).equals("true") ||
                        input.substring(startIndex, i).equals("false")) {
                        state = State.BooleanLiteral;
                    } else if (input.substring(startIndex, i).equals("null")) {
                        state = State.NullLiteral;
                    } else if (Kind.keywords.containsKey(input.substring(startIndex, i))) {
                        state = State.Keyword;
                        state.kind = Kind.keywords.get(input.substring(startIndex, i));
                    }
                }
                if (state.kind != Kind.WHITESPACE && state.kind != Kind.RCOMMENT) {
                    ret.add(new TerminalToken(state.kind, input.substring(startIndex, i)));
                }
                if (state.kind == Kind.RCOMMENT) {
                    inComment = false;
                }

                startIndex = i;
                state = State.START;
                if (i >= input.length()) break;
            } else {
                state = t;
                i++;
            }
        }
        return ret;
    }

    public void run() throws InvalidSyntaxException {
        Scanner in = new Scanner(System.in);
        tokenList.add(new TerminalToken(Kind.BOF, "bof"));
        while (in.hasNextLine()) {
            String line = in.nextLine();
            ArrayList<TerminalToken> tokens = this.scan(line);
            tokenList.addAll(tokens);
        }
        tokenList.add(new TerminalToken(Kind.EOF, "eof"));
        System.out.flush();
    }

    private enum State {
        START(null),
        WHITESPACE(Kind.WHITESPACE),
        SCOMMENT(Kind.SCOMMENT),
        RCOMMEN(null),
        INCOMMENT(null),
        COMMENT(Kind.RCOMMENT),

        ID(Kind.ID),
        IntFloatState1(Kind.DecimalIntegerLiteral),
        IntFloatState2(Kind.DecimalIntegerLiteral),
        IntFloatState3(Kind.DOT),
        IntFloatState4(Kind.OctalIntegerLiteral),
        IntFloatState5(null),
        IntFloatState6(Kind.FloatingPointLiteral),
        IntFloatState7(null),
        IntFloatState8(null),
        IntFloatState9(Kind.LongIntegerLiteral),
        IntFloatState10(Kind.FloatingPointLiteral),
        IntFloatState11(Kind.HexIntegerLiteral),
        IntFloatState12(Kind.FloatingPointLiteral),
        IntFloatState13(null),
        CharacterLiteral1(null),
        CharacterLiteral2(null),
        CharacterLiteral3(null),
        CharacterLiteral4(Kind.CharacterLiteral),
        CharacterLiteral5(null),
        CharacterLiteral6(null),

        StringLiteral1(null),
        StringLiteral2(null),
        StringLiteral3(Kind.StringLiteral),
        StringLiteral4(null),
        StringLiteral5(null),

        BooleanLiteral(Kind.BooleanLiteral),
        NullLiteral(Kind.NullLiteral),

        Keyword(Kind.Keyword),

        LPAREN(Kind.LPAREN),
        RPAREN(Kind.RPAREN),
        LBRACE(Kind.LBRACE),
        RBRACE(Kind.RBRACE),
        LBRACK(Kind.LBRACK),
        RBRACK(Kind.RBRACK),
        SEMI(Kind.SEMI),
        COMMA(Kind.COMMA),

        BECOMES(Kind.BECOMES),
        GT(Kind.GT),
        LT(Kind.LT),
        NOT(Kind.NOT),
        FLIP(Kind.FLIP),
        QUESTION(Kind.QUESTION),
        COLON(Kind.COLON),
        EQ(Kind.EQ),
        LE(Kind.LE),
        GE(Kind.GE),
        NE(Kind.NE),
        AND(Kind.AND),
        OR(Kind.OR),
        PLUSPLUS(Kind.PLUSPLUS),
        MINUSMINUS(Kind.MINUSMINUS),
        PLUS(Kind.PLUS),
        MINUS(Kind.MINUS),
        STAR(Kind.STAR),
        SLASH(Kind.SLASH),
        BAND(Kind.BAND),
        BOR(Kind.BOR),
        BXOR(Kind.BXOR),
        MOD(Kind.MOD),
        BLSHIFT(Kind.BLSHIFT),
        BRSHIFT(Kind.BRSHIFT),
        BZEROFILL(Kind.BZEROFILL),
        PLUSASSIGN(Kind.PLUSASSIGN),
        MINUSASSIGN(Kind.MINUSASSIGN),
        MULTASSIGN(Kind.MULTASSIGN),
        DIVIDEASSIGN(Kind.DIVIDEASSIGN),
        BANDASSIGN(Kind.BANDASSIGN),
        BORASSIGN(Kind.BORASSIGN),
        BXORASSIGN(Kind.BXORASSIGN),
        MODASSIGN(Kind.MODASSIGN),
        BLSHIFTASSIGN(Kind.BLSHIFTASSIGN),
        BRSHIFTASSIGN(Kind.BRSHIFTASSIGN),
        BZEROFILLASSIGN(Kind.BZEROFILLASSIGN),;

        Kind kind;

        State(Kind kind) {
            this.kind = kind;
        }

        boolean isFinal() {
            return kind != null;
        }
    }

    private interface CharSet {
        boolean contains(char newC);
    }

    public class Chars implements CharSet {
        private String chars;

        Chars(String chars) {
            this.chars = chars;
        }

        public boolean contains(char newC) {
            return chars.indexOf(newC) >= 0;
        }
    }

    public class AllCharsExcept implements CharSet {
        private String chars;

        AllCharsExcept(String chars) {
            this.chars = chars;
        }

        public boolean contains(char newC) {
            return chars.indexOf(newC) < 0;
        }
    }

    public class CharsIDPart implements CharSet {
        public boolean contains(char newC) {
            return Character.isJavaIdentifierPart(newC);
        }
    }

    public class CharsIDStart implements CharSet {
        public boolean contains(char newC) {
            return Character.isJavaIdentifierStart(newC);
        }
    }

    class TransitionCollection {
        HashMap<State, HashMap<CharSet, State>> collection = new HashMap<>();

        private void add(Transition t) {
            if (collection.containsKey(t.fromState)) {
                if (!collection.get(t.fromState).containsKey(t.chars)) {
                    collection.get(t.fromState).put(t.chars, t.toState);
                }
            } else {
                HashMap<CharSet, State> m = new HashMap<>();
                m.put(t.chars, t.toState);
                collection.put(t.fromState, m);
            }
        }

        private State findToState(State state, char c) {
            if (this.collection.containsKey(state)) {
                for (CharSet set : this.collection.get(state).keySet()) {
                    if (set.contains(c)) {
                        return this.collection.get(state).get(set);
                    }
                }
            }
            return null;
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
}
