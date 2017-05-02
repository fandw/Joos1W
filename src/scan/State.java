package scan;

import common.TokenType;

/**
 * Created by daiweifan on 2017-05-01.
 */
enum State {
    START(),
    WHITESPACE(TokenType.SPECIAL),
    SCOMMENT(),
    RCOMMEN(),
    INCOMMENT(),
    COMMENT(TokenType.SPECIAL),

    ID(TokenType.ID),
    KEYWORD(),
    IntFloatState1(TokenType.IntegerLiteral),
    IntFloatState2(TokenType.IntegerLiteral),

    CharacterLiteral1(),
    CharacterLiteral2(),
    CharacterLiteral3(),
    CharacterLiteral4(TokenType.CharacterLiteral),
    CharacterLiteral5(),
    CharacterLiteral6(),

    StringLiteral1(),
    StringLiteral2(),
    StringLiteral3(TokenType.StringLiteral),
    StringLiteral4(),
    StringLiteral5(),

    BooleanLiteral(TokenType.BooleanLiteral),
    NullLiteral(TokenType.NullLiteral),

    LPAREN(TokenType.LPAREN),
    RPAREN(TokenType.RPAREN),
    LBRACE(TokenType.LBRACE),
    RBRACE(TokenType.RBRACE),
    LBRACK(TokenType.LBRACK),
    RBRACK(TokenType.RBRACK),
    SEMI(TokenType.SEMI),
    COMMA(TokenType.COMMA),

    BECOMES(TokenType.BECOMES),
    GT(TokenType.GT),
    LT(TokenType.LT),
    NOT(TokenType.NOT),
    QUESTION(TokenType.QUESTION),
    COLON(TokenType.COLON),
    EQ(TokenType.EQ),
    LE(TokenType.LE),
    GE(TokenType.GE),
    NE(TokenType.NE),
    AND(TokenType.AND),
    OR(TokenType.OR),
    PLUSPLUS(TokenType.PLUSPLUS),
    MINUSMINUS(TokenType.MINUSMINUS),
    PLUS(TokenType.PLUS),
    MINUS(TokenType.MINUS),
    STAR(TokenType.STAR),
    SLASH(TokenType.SLASH),
    EAND(TokenType.EAND),
    EOR(TokenType.EOR),
    MOD(TokenType.MOD),
    PLUSASSIGN(TokenType.PLUSASSIGN),
    MINUSASSIGN(TokenType.MINUSASSIGN),
    MULTASSIGN(TokenType.MULTASSIGN),
    DIVASSIGN(TokenType.DIVASSIGN),
    MODASSIGN(TokenType.MODASSIGN),
    DOT(TokenType.DOT);

    private TokenType validType;

    State() {
        this.validType = null;
    }

    State(TokenType tokenType) {
        this.validType = tokenType;
    }

    State(boolean specialFinalState) {
        this.validType = TokenType.SPECIAL;
    }

    TokenType getValidType() {
        return validType;
    }

    void setKeywordTokenType(String keyword) {
        if (!Keyword.getKeywordSet().containsKey(keyword)) return;
        validType = Keyword.getKeywordSet().get(keyword);
    }
}
