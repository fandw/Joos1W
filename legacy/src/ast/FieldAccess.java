package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class FieldAccess extends Expression {
    public Expression primary;
    public TokenNode id;

    public FieldAccess(Tree parseTree) {
        super();

        //FieldAccess Primary DOT ID
        primary = Expression.translateExpression(parseTree.getChild(0));
        id = new TokenNode(parseTree.getChild(2));
        addChild(primary);
        addChild(id);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
