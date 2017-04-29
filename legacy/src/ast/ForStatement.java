package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by Daiwei on 2017-03-09.
 */
public class ForStatement extends Statement {
    public Statement forInit;
    public Expression forCondition;
    public Statement forUpdate;
    public Statement stmt;

    public ForStatement(Tree parseTree) {
        super();

        int forInitIndex = parseTree.getChildIndexByName("ForInit");
        if (forInitIndex != -1) {
            if (parseTree.getChild(forInitIndex).getRule().equals("ForInit StatementExpression")) {
                //ForInit StatementExpression
                forInit = new ExpressionStatement(parseTree.getChild(forInitIndex).getChild(0));
            } else {
                //ForInit LocalVariableDeclaration
                forInit = new LocalVariableDeclaration(parseTree.getChild(forInitIndex).getChild(0));
            }
            addChild(forInit);
        } else {
            forInit = null;
        }

        int forConditionIndex = parseTree.getChildIndexByName("Expression");
        if (forConditionIndex != -1) {
            forCondition = Expression.translateExpression(parseTree.getChild(forConditionIndex));
            addChild(forCondition);
        } else {
            forCondition = null;
        }

        int forUpdateIndex = parseTree.getChildIndexByName("ForUpdate");
        if (forUpdateIndex != -1) {
            forUpdate = new ExpressionStatement(parseTree.getChild(forUpdateIndex).getChild(0));
            addChild(forUpdate);
        } else {
            forUpdate = null;
        }

        int stmtIndex1 = parseTree.getChildIndexByName("Statement");
        int stmtIndex2 = parseTree.getChildIndexByName("StatementNoShortIf");
        if (stmtIndex1 != -1) {
            stmt = Statement.interpretStatement(parseTree.getChild(stmtIndex1));
        }

        if (stmtIndex2 != -1) {
            stmt = Statement.interpretStatement(parseTree.getChild(stmtIndex2));
        }

        // stmt should not be null!!
        addChild(stmt);

    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}