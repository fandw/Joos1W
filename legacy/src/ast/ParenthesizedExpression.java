package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class ParenthesizedExpression extends Expression {
    public Expression expr;

    public ParenthesizedExpression(Tree parseTree) {
        super();

        expr = Expression.translateExpression(parseTree.getChild(1));
        addChild(expr);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
