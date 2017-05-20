package common;

/**
 * Created by daiweifan on 2017-05-18.
 */
public class ProductionElement {
    protected String lexeme;

    public ProductionElement (String lexeme) {
        this.lexeme = lexeme;
    }

    public String getLexeme() {
        return lexeme;
    }

    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }
}
