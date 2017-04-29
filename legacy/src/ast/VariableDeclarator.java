package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class VariableDeclarator extends ASTNode {

    public TokenNode id;
    public Expression expr;

    public VariableDeclarator(Tree parseTree) {
        super();

        id = new TokenNode(parseTree.getChildByName("ID"));
        addChild(id);

        int exprIndex = parseTree.getChildIndexByName("Expression");
        if (exprIndex != -1) {
            expr = Expression.translateExpression(parseTree.getChild(exprIndex));
            addChild(expr);
        } else {
            expr = null;
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
