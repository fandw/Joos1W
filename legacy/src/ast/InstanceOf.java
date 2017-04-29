package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class InstanceOf extends Expression {

    public Expression relational;
    public Type reference;

    public InstanceOf(Tree parseTree) {
        super();

        //RelationalExpression RelationalExpression INSTANCEOF ReferenceType
        relational = Expression.translateExpression(parseTree.getChild(0));
        reference = Type.interpretType(parseTree.getChild(2));
        addChild(relational);
        addChild(reference);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
