package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class BinaryOps extends Expression {
    public Expression first;
    public Expression second;
    public TokenNode operator;

    public BinaryOps(Tree parseTree) {
        super();

        first = Expression.translateExpression(parseTree.getChild(0));
        second = Expression.translateExpression(parseTree.getChild(2));
        operator = new TokenNode(parseTree.getChild(1));
        addChild(first);
        addChild(operator);
        addChild(second);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
