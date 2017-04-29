package scanner;

import java.util.HashMap;

/**
 * Created by daiweifan on 2017-02-28.
 */
public enum Kind {
    BOF,
    EOF,
    // Identifier
    ID,
    // Literal
    // Integer Literal
    DecimalIntegerLiteral,
    HexIntegerLiteral,
    OctalIntegerLiteral,
    LongIntegerLiteral,
    // Floating Point Literal
    FloatingPointLiteral,
    // Boolean Literal
    BooleanLiteral,
    // Character Literal
    CharacterLiteral,
    // String Literal
    StringLiteral,
    // Null Literal
    NullLiteral,

    // Keywords
    Keyword,
    ABSTRACT("abstract"),
    DEFAULT("default"),
    IF("if"),
    PRIVATE("private"),
    THIS("this"),
    BOOLEAN("boolean"),
    DO("do"),
    IMPLEMENTS("implements"),
    PROTECTED("protected"),
    THROW("throw"),
    BREAK("break"),
    DOUBLE("double"),
    IMPORT("import"),
    PUBLIC("public"),
    THROWS("throws"),
    BYTE("byte"),
    ELSE("else"),
    INSTANCEOF("instanceof"),
    RETURN("return"),
    TRANSIENT("transient"),
    CASE("case"),
    EXTENDS("extends"),
    INT("int"),
    SHORT("short"),
    TRY("try"),
    CATCH("catch"),
    FINAL("final"),
    INTERFACE("interface"),
    STATIC("static"),
    VOID("void"),
    CHAR("char"),
    FINALLY("finally"),
    LONG("long"),
    STRICTFP("strictfp"),
    VOLATILE("volatile"),
    CLASS("class"),
    FLOAT("float"),
    NATIVE("native"),
    SUPER("super"),
    WHILE("while"),
    CONST("const"),
    FOR("for"),
    NEW("new"),
    SWITCH("switch"),
    CONTINUE("continue"),
    GOTO("goto"),
    PACKAGE("package"),
    SYNCHRONIZED("synchronized"),

    // Separators
    LPAREN("("),
    RPAREN(")"),
    LBRACE("{"),
    RBRACE("}"),
    LBRACK("["),
    RBRACK("]"),
    SEMI(";"),
    COMMA(","),
    DOT("."),

    // Operators
    BECOMES("="),
    GT(">"),
    LT("<"),
    NOT("!"),
    FLIP("~"),
    QUESTION("?"),
    COLON(":"),
    EQ("=="),
    LE("<="),
    GE(">="),
    NE("!="),
    AND("&&"),
    OR("||"),
    PLUSPLUS("++"),
    MINUSMINUS("--"),
    PLUS("+"),
    MINUS("-"),
    STAR("*"),
    SLASH("/"),
    BAND("&"),
    BOR("|"),
    BXOR("^"),
    MOD("%"),
    BLSHIFT("<<"),
    BRSHIFT(">>"),
    BZEROFILL(">>>"),
    PLUSASSIGN("+="),
    MINUSASSIGN("-="),
    MULTASSIGN("*="),
    DIVIDEASSIGN("/="),
    BANDASSIGN("&="),
    BORASSIGN("|="),
    BXORASSIGN("^="),
    MODASSIGN("%="),
    BLSHIFTASSIGN("<<="),
    BRSHIFTASSIGN(">>="),
    BZEROFILLASSIGN(">>>="),

    RCOMMENT,
    SCOMMENT,
    WHITESPACE,
    ERROR;

    public static HashMap<String, Kind> keywords = new java.util.HashMap<>();

    static {
        for (Kind k : values()) {
            keywords.put(k.text, k);
        }
    }

    String text;

    Kind(String s) {
        this.text = s;

    }

    Kind() {
        this.text = null;
    }
}
