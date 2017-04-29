package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class Assignment extends Expression {
    public Expression leftHandSide;
    public Expression expression;

    public Assignment(Tree parseTree) {
        super();

        leftHandSide = Expression.translateExpression(parseTree.getChild(0).getChild(0));
        expression = Expression.translateExpression(parseTree.getChild(2));
        addChild(leftHandSide);
        addChild(expression);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
