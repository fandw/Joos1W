package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class OtherArrayAccess extends Expression {
    public Expression primaryNoNewArray;
    public Expression expr;

    public OtherArrayAccess(Tree parseTree) {
        super();

        // ArrayAccess PrimaryNoNewArray LBRACK Expression LBRACK
        primaryNoNewArray = Expression.translateExpression(parseTree.getChild(0));
        expr = Expression.translateExpression(parseTree.getChild(2));
        addChild(primaryNoNewArray);
        addChild(expr);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
