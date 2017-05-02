package scan;

import common.TokenType;

import java.security.Key;
import java.util.HashMap;

/**
 * Created by daiweifan on 2017-04-29.
 */
class Keyword {
    private static final HashMap<String, TokenType> keywordSet = new HashMap<>();

    static {
        keywordSet.put("abstract", TokenType.ABSTRACT);
        keywordSet.put("if", TokenType.IF);
        keywordSet.put("private", TokenType.PRIVATE);
        keywordSet.put("this", TokenType.THIS);
        keywordSet.put("boolean", TokenType.BOOLEAN);
        keywordSet.put("implements", TokenType.IMPLEMENTS);
        keywordSet.put("protected", TokenType.PROTECTED);
        keywordSet.put("import", TokenType.IMPORT);
        keywordSet.put("public", TokenType.PUBLIC);
        keywordSet.put("byte", TokenType.BYTE);
        keywordSet.put("else", TokenType.ELSE);
        keywordSet.put("instanceof", TokenType.INSTANCEOF);
        keywordSet.put("return", TokenType.RETURN);
        keywordSet.put("extends", TokenType.EXTENDS);
        keywordSet.put("int", TokenType.INT);
        keywordSet.put("short", TokenType.SHORT);
        keywordSet.put("final", TokenType.FINAL);
        keywordSet.put("interface", TokenType.INTERFACE);
        keywordSet.put("static", TokenType.STATIC);
        keywordSet.put("void", TokenType.VOID);
        keywordSet.put("char", TokenType.CHAR);
        keywordSet.put("class", TokenType.CLASS);
        keywordSet.put("while", TokenType.WHILE);
        keywordSet.put("for", TokenType.FOR);
        keywordSet.put("new", TokenType.NEW);
        keywordSet.put("package", TokenType.PACKAGE);
        keywordSet.put("native", TokenType.NATIVE);
    }

    private Keyword() {}

    public static HashMap<String, TokenType> getKeywordSet() {
        return keywordSet;
    }
}
