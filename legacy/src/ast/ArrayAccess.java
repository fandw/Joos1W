package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class ArrayAccess extends Expression {
    public Name name;
    public Expression expr;

    public ArrayAccess(Tree parseTree) {
        super();

        // ArrayAccess Name LBRACK Expression RBRACK
        name = new Name(parseTree.getChild(0));
        expr = Expression.translateExpression(parseTree.getChild(2));
        addChild(name);
        addChild(expr);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
