package common;

import util.Location;

/**
 * Created by daiweifan on 2017-04-29.
 */
public class Token {
    private TokenType type;
    private String lexeme;
    private Location location;

    public Token(TokenType type, String lexeme, int line, int column, String filePath) {
        this.type = type;
        this.lexeme = lexeme;
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

}
