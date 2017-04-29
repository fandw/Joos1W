package scanner;

import parser.Token;

/**
 * Created by daiweifan on 2017-02-28.
 */

public class TerminalToken extends Token {
    private Kind kind;     // The kind of token.
    private String lexeme;

    public TerminalToken(Kind kind, String lexeme) {
        super(kind.toString(), true);
        this.kind = kind;
        this.lexeme = lexeme;
    }

    public Kind getKind() {
        return this.kind;
    }

    public String getLexeme() {
        return this.lexeme;
    }

    public boolean isKind(Kind kind) {
        return this.kind == kind;
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof TerminalToken && ((TerminalToken) obj).getLexeme().equals(this.lexeme);
    }

    @Override
    public int hashCode() {
        return this.lexeme.hashCode();
    }
}
