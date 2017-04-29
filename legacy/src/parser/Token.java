package parser;

/**
 * Created by daiweifan on 2017-02-28.
 */
public class Token {

    private String text;
    private boolean isTerminal;

    public Token(String s, boolean t) {
        text = s;
        isTerminal = t;
    }

    public String getText() {
        return text;
    }

    public void setText(String s) {
        text = s;
    }

    public boolean isTerminal() {
        return isTerminal;
    }

    public boolean equals(String s) {
        return text.equals(s);
    }
}
