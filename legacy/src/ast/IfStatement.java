package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by Daiwei on 2017-03-09.
 */
public class IfStatement extends Statement {

    //    IfThenStatement if ( Expression ) Statement
    //    IfThenElseStatement if ( Expression ) StatementNoShortIf else Statement
    //    IfThenElseStatementNoShortIf if ( Expression ) StatementNoShortIf else StatementNoShortIf

    public Expression expr;
    public Statement thenStatement;
    public Statement elseStatement;

    public IfStatement(Tree parseTree) {
        super();

        expr = Expression.translateExpression(parseTree.getChildByName("Expression"));
        addChild(expr);

        thenStatement = Statement.interpretStatement(parseTree.getChild(4));
        addChild(thenStatement);

        if (parseTree.getRule().hasRHS("ELSE") != -1) {
            elseStatement = Statement.interpretStatement(parseTree.getChild(6));
            addChild(elseStatement);
        } else {
            elseStatement = null;
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
