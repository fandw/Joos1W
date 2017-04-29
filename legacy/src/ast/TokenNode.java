package ast;

import environment.ASTVisitor;
import parser.Tree;
import scanner.Kind;
import scanner.TerminalToken;

/**
 * Created by daiweifan on 2017-02-28.
 */
public class TokenNode extends ASTNode {
    // This Node always contains a terminal token
    public TerminalToken token;

    public TokenNode(TerminalToken token) {
        super();
        this.token = token;
    }

    public TokenNode(Tree parseTree) {
        super();
        token = parseTree.getRule().getTtoken();
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof TokenNode && ((TokenNode) obj).token.equals(token);
    }

    @Override
    public int hashCode() {
        return this.token.hashCode();
    }

    @Override
    public String toString() {
        return this.token.getLexeme();
    }

    public boolean isKind(Kind kind) {
        return this.token.isKind(kind);
    }
}
