package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class ReturnStatement extends Statement {
    public Expression expr;

    public ReturnStatement(Tree parseTree) {
        super();

        if (parseTree.getRule().hasRHS("Expression") != -1) {
            expr = Expression.translateExpression(parseTree.getChild(1));
            addChild(expr);
        } else {
            expr = null;
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
