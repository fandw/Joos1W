package common;

import util.Location;

/**
 * Created by daiweifan on 2017-04-29.
 */
public class Token extends ProductionElement {
    private TokenType type;
    private Location location;

    public Token(TokenType type, String lexeme, int line, int column, String filePath) {
        super(lexeme);
        this.type = type;
        this.location = new Location(line, column, filePath);
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public Location getLocation() {
        return location;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }

}
