package ast;

import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public abstract class Statement extends ASTNode {

    public Statement() {
        super();
    }

    public static String[] trickleDown = new String[] {
            "Statement StatementWithoutTrailingSubstatement",
            "Statement IfThenStatement",
            "Statement IfThenElseStatement",
            "Statement WhileStatement",
            "Statement ForStatement",
            "StatementWithoutTrailingSubstatement Block",
            "StatementWithoutTrailingSubstatement EmptyStatement",
            "StatementWithoutTrailingSubstatement ExpressionStatement",
            "StatementWithoutTrailingSubstatement ReturnStatement",

            "StatementNoShortIf StatementWithoutTrailingSubstatement",
            "StatementNoShortIf IfThenElseStatementNoShortIf",
            "StatementNoShortIf WhileStatementNoShortIf",
            "StatementNoShortIf ForStatementNoShortIf",
            };
    public boolean reachable;
    public boolean completable;

    public static Statement interpretStatement(Tree parseTree) {
        for (String rule : trickleDown) {
            if (parseTree.getRule().equals(rule)) {
                return interpretStatement(parseTree.getChild(0));
            }
        }

        if (parseTree.getRule().startsWith("IfThenStatement") ||
            parseTree.getRule().startsWith("IfThenElseStatement") ||
            parseTree.getRule().startsWith("IfThenElseStatementNoShortIf")) {
            return new IfStatement(parseTree);
        }
        if (parseTree.getRule().startsWith("WhileStatement") ||
            parseTree.getRule().startsWith("WhileStatementNoShortIf")) {
            return new WhileStatement(parseTree);
        }
        if (parseTree.getRule().startsWith("ForStatement") || parseTree.getRule().startsWith("ForStatementNoShortIf")) {
            return new ForStatement(parseTree);
        }
        if (parseTree.getRule().startsWith("Block")) {
            return new Block(parseTree);
        }
        if (parseTree.getRule().startsWith("EmptyStatement")) {
            return new EmptyStatement();
        }
        if (parseTree.getRule().startsWith("ReturnStatement")) {
            return new ReturnStatement(parseTree);
        }
        if (parseTree.getRule().startsWith("ExpressionStatement")) {
            return new ExpressionStatement(parseTree);
        }

        return null;
    }
}
