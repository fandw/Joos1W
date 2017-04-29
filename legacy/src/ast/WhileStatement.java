package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by Daiwei on 2017-03-09.
 */
public class WhileStatement extends Statement {
    public Expression expr;
    public Statement stmt;

    public WhileStatement(Tree parseTree) {
        super();

        expr = Expression.translateExpression(parseTree.getChild(2));
        addChild(expr);
        stmt = Statement.interpretStatement(parseTree.getChild(4));
        addChild(stmt);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
