package common;

/**
 * Created by daiweifan on 2017-04-28.
 */
public enum TokenType {
    BOF,
    EOF,
    SPECIAL,

    // Identifier
    ID,

    // Keywords
    ABSTRACT,
    IF,
    PRIVATE,
    THIS,
    BOOLEAN,
    IMPLEMENTS,
    PROTECTED,
    IMPORT,
    PUBLIC,
    BYTE,
    ELSE,
    INSTANCEOF,
    RETURN,
    EXTENDS,
    INT,
    SHORT,
    FINAL,
    INTERFACE,
    STATIC,
    VOID,
    CHAR,
    CLASS,
    NATIVE,
    WHILE,
    FOR,
    NEW,
    PACKAGE,

    // Literals
    IntegerLiteral,
    BooleanLiteral,
    CharacterLiteral,
    StringLiteral,
    NullLiteral,

    // Separators
    LPAREN,
    RPAREN,
    LBRACE,
    RBRACE,
    LBRACK,
    RBRACK,
    SEMI,
    COMMA,
    DOT,

    // Operators
    BECOMES,
    GT,
    LT,
    NOT,
    QUESTION,
    COLON,
    EQ,
    LE,
    GE,
    NE,
    AND,
    OR,
    PLUSPLUS,
    MINUSMINUS,
    PLUS,
    MINUS,
    STAR,
    SLASH,
    EAND,
    EOR,
    MOD,
    PLUSASSIGN,
    MINUSASSIGN,
    MULTASSIGN,
    DIVASSIGN,
    MODASSIGN;
}
