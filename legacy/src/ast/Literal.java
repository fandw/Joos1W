package ast;

import environment.ASTVisitor;
import parser.Tree;
import scanner.Kind;
import scanner.TerminalToken;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class Literal extends Expression {
    public TokenNode token;
    public boolean isNegativeInteger;

    public Literal(Tree parseTree) {
        super();

        token = new TokenNode(parseTree.getChild(0));
        addChild(token);
    }

    public Literal(Integer i) {
        super();

        token = new TokenNode(new TerminalToken(Kind.DecimalIntegerLiteral, Integer.toString(i)));
    }

    public Literal(boolean b) {
        super();

        token = new TokenNode(new TerminalToken(Kind.BooleanLiteral, Boolean.toString(b)));
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equalsTrue() {
        return isBooleanLiteral() && getBoolean();
    }

    @Override
    public boolean equalsFalse() {
        return isBooleanLiteral() && !getBoolean();
    }

    @Override
    public boolean isIntegerLiteral() {
        return token.token.getKind() == Kind.DecimalIntegerLiteral;
    }

    @Override
    public Integer getInteger() {
        if (isIntegerLiteral()) {
            return Integer.parseInt((isNegativeInteger ? "-" : "") + token.token.getLexeme());
        } else {
            return null;
        }
    }

    @Override
    public boolean isBooleanLiteral() {
        return token.token.getKind() == Kind.BooleanLiteral;
    }

    @Override
    public Boolean getBoolean() {
        if (isBooleanLiteral()) {
            return Boolean.parseBoolean(token.token.getLexeme());
        } else {
            return null;
        }
    }
}
