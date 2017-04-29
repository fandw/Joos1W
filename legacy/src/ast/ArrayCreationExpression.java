package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class ArrayCreationExpression extends Expression {
    public Type type;
    public Expression expression;

    public ArrayCreationExpression(Tree parseTree) {
        super();

        if (parseTree.getRule().equals("ArrayCreationExpression NEW PrimitiveType LBRACK Expression RBRACK")) {
            type = new PrimitiveType(parseTree.getChild(1));
        }

        if (parseTree.getRule().equals("ArrayCreationExpression NEW Name LBRACK Expression RBRACK")) {
            type = new ClassType(new Name(parseTree.getChild(1)));
        }

        expression = Expression.translateExpression(parseTree.getChild(3));

        addChild(type);
        addChild(expression);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }

}
