package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class UnaryExpression extends Expression {
    public TokenNode operator;
    public Expression expr;

    public UnaryExpression(Tree parseTree) {
        super();

        operator = new TokenNode(parseTree.getChild(0));
        expr = Expression.translateExpression(parseTree.getChild(1));

        if (operator.token.getLexeme().equals("-") && expr instanceof Literal) {
            ((Literal) expr).isNegativeInteger = true;
        }

        addChild(operator);
        addChild(expr);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
