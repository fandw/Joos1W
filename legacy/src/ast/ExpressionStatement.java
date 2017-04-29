package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class ExpressionStatement extends Statement {

    public Expression expr;

    public ExpressionStatement(Tree parseTree) {
        super();

        if (parseTree.getRule().equals("ExpressionStatement StatementExpression SEMI")) {
            parseTree = parseTree.getChild(0);
        }
        expr = Expression.translateExpression(parseTree.getChild(0));
        addChild(expr);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
