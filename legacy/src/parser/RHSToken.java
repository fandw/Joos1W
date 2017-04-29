package parser;

/**
 * Created by daiweifan on 2017-02-28.
 */
public class RHSToken {
    public final boolean optional;
    public Token token;
    public boolean print;

    RHSToken(Token token) {
        this.token = token;
        this.optional = false;
    }

    public RHSToken(Token token, boolean optional) {
        this.token = token;
        this.optional = optional;
    }

    public boolean equals(String t) {
        return token.getText().equals(t);
    }
}
