package scan;

import java.util.HashMap;

/**
 * Created by daiweifan on 2017-05-01.
 */
class TransitionSet {

    private static TransitionSet transitionSet;
    private HashMap<State, HashMap<CharSet, State>> collection = new HashMap<>();

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

    State find_to_state(State state, char c) {
        if (collection.containsKey(state)) {
            for (CharSet set : collection.get(state).keySet()) {
                if (set.contains(c)) {
                    return collection.get(state).get(set);
                }
            }
        }
        return null;
    }

    private TransitionSet() {}

    static TransitionSet get_transition_set() {
        if (transitionSet != null) return transitionSet;
        transitionSet = new TransitionSet();

        CharSet whitespace = new Chars("\t\n\r ");
        CharSet letters = new CharsIDStart();
        CharSet lettersDigits = new CharsIDPart();
        CharSet escapables = new Chars("tbnrf'\"\\");
        CharSet digits = new Chars("0123456789");
        CharSet nonZeroDigits = new Chars("123456789");
        CharSet octalDigits = new Chars("01234567");
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
        CharSet allCharsForChar = new CharsExcept("\\'");
        CharSet allCharsForString = new CharsExcept("\\\"");
        CharSet zeroToThree = new Chars("0123");
        CharSet gtThreeOctal = new Chars("4567");

        transitionSet.add(new Transition(State.START, whitespace, State.WHITESPACE));
        transitionSet.add(new Transition(State.START, letters, State.ID));

        // Integer or Float literal transitions
        transitionSet.add(new Transition(State.START, zero, State.IntFloatState1));
        transitionSet.add(new Transition(State.START, nonZeroDigits, State.IntFloatState2));
        transitionSet.add(new Transition(State.IntFloatState2, digits, State.IntFloatState2));

        // Char literal transitions
        transitionSet.add(new Transition(State.START, squote, State.CharacterLiteral1));
        transitionSet.add(new Transition(State.CharacterLiteral1, allCharsForChar, State.CharacterLiteral2));
        transitionSet.add(new Transition(State.CharacterLiteral1, escape, State.CharacterLiteral3));
        transitionSet.add(new Transition(State.CharacterLiteral2, squote, State.CharacterLiteral4));
        transitionSet.add(new Transition(State.CharacterLiteral3, escapables, State.CharacterLiteral2));
        transitionSet.add(new Transition(State.CharacterLiteral3, zeroToThree, State.CharacterLiteral5));
        transitionSet.add(new Transition(State.CharacterLiteral3, gtThreeOctal, State.CharacterLiteral6));
        transitionSet.add(new Transition(State.CharacterLiteral5, octalDigits, State.CharacterLiteral6));
        transitionSet.add(new Transition(State.CharacterLiteral5, squote, State.CharacterLiteral4));
        transitionSet.add(new Transition(State.CharacterLiteral6, octalDigits, State.CharacterLiteral2));
        transitionSet.add(new Transition(State.CharacterLiteral6, squote, State.CharacterLiteral4));

        // String literal transitions
        transitionSet.add(new Transition(State.START, dquote, State.StringLiteral1));
        transitionSet.add(new Transition(State.StringLiteral1, allCharsForString, State.StringLiteral1));
        transitionSet.add(new Transition(State.StringLiteral1, escape, State.StringLiteral2));
        transitionSet.add(new Transition(State.StringLiteral1, dquote, State.StringLiteral3));
        transitionSet.add(new Transition(State.StringLiteral2, escapables, State.StringLiteral1));
        transitionSet.add(new Transition(State.StringLiteral2, zeroToThree, State.StringLiteral4));
        transitionSet.add(new Transition(State.StringLiteral2, gtThreeOctal, State.StringLiteral5));
        transitionSet.add(new Transition(State.StringLiteral4, octalDigits, State.StringLiteral5));
        transitionSet.add(new Transition(State.StringLiteral4, allCharsForString, State.StringLiteral1));
        transitionSet.add(new Transition(State.StringLiteral4, dquote, State.StringLiteral3));
        transitionSet.add(new Transition(State.StringLiteral4, escape, State.StringLiteral2));
        transitionSet.add(new Transition(State.StringLiteral5, octalDigits, State.StringLiteral1));
        transitionSet.add(new Transition(State.StringLiteral5, allCharsForString, State.StringLiteral1));
        transitionSet.add(new Transition(State.StringLiteral5, dquote, State.StringLiteral3));
        transitionSet.add(new Transition(State.StringLiteral5, escape, State.StringLiteral2));

        transitionSet.add(new Transition(State.START, lparent, State.LPAREN));
        transitionSet.add(new Transition(State.START, rparent, State.RPAREN));
        transitionSet.add(new Transition(State.START, lbracer, State.LBRACE));
        transitionSet.add(new Transition(State.START, rbracer, State.RBRACE));
        transitionSet.add(new Transition(State.START, equal, State.BECOMES));
        transitionSet.add(new Transition(State.START, not, State.NOT));
        transitionSet.add(new Transition(State.START, lessthan, State.LT));
        transitionSet.add(new Transition(State.START, greaterthan, State.GT));
        transitionSet.add(new Transition(State.START, plus, State.PLUS));
        transitionSet.add(new Transition(State.START, minus, State.MINUS));
        transitionSet.add(new Transition(State.START, star, State.STAR));
        transitionSet.add(new Transition(State.START, slash, State.SLASH));
        transitionSet.add(new Transition(State.START, mod, State.MOD));
        transitionSet.add(new Transition(State.START, comma, State.COMMA));
        transitionSet.add(new Transition(State.START, semi, State.SEMI));
        transitionSet.add(new Transition(State.START, lbracket, State.LBRACK));
        transitionSet.add(new Transition(State.START, rbracket, State.RBRACK));
        transitionSet.add(new Transition(State.START, emp, State.EAND));
        transitionSet.add(new Transition(State.START, question, State.QUESTION));
        transitionSet.add(new Transition(State.START, colon, State.COLON));
        transitionSet.add(new Transition(State.START, or, State.EOR));
        transitionSet.add(new Transition(State.START, dot, State.DOT));
        transitionSet.add(new Transition(State.PLUS, equal, State.PLUSASSIGN));
        transitionSet.add(new Transition(State.MINUS, equal, State.MINUSASSIGN));
        transitionSet.add(new Transition(State.STAR, equal, State.MULTASSIGN));
        transitionSet.add(new Transition(State.SLASH, equal, State.DIVASSIGN));
        transitionSet.add(new Transition(State.MOD, equal, State.MODASSIGN));
        transitionSet.add(new Transition(State.ID, lettersDigits, State.ID));
        transitionSet.add(new Transition(State.BECOMES, equal, State.EQ));
        transitionSet.add(new Transition(State.NOT, equal, State.NE));
        transitionSet.add(new Transition(State.LT, equal, State.LE));
        transitionSet.add(new Transition(State.GT, equal, State.GE));
        transitionSet.add(new Transition(State.SLASH, slash, State.SCOMMENT));
        transitionSet.add(new Transition(State.SLASH, star, State.INCOMMENT));
        transitionSet.add(new Transition(State.PLUS, plus, State.PLUSPLUS));
        transitionSet.add(new Transition(State.MINUS, minus, State.MINUSMINUS));
        transitionSet.add(new Transition(State.EAND, emp, State.AND));
        transitionSet.add(new Transition(State.EOR, or, State.OR));
        transitionSet.add(new Transition(State.INCOMMENT, star, State.RCOMMEN));
        transitionSet.add(new Transition(State.RCOMMEN, slash, State.COMMENT));
        transitionSet.add(new Transition(State.RCOMMEN, star, State.RCOMMEN));

        return transitionSet;
    }
}
