package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class ThisExpression extends Expression {
    public TokenNode token;

    public ThisExpression(Tree parseTree) {
        super();

        token = new TokenNode(parseTree);
        addChild(token);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
