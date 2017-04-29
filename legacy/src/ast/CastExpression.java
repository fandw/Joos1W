package ast;

import environment.ASTVisitor;
import exception.ASTException;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class CastExpression extends Expression {
    public Type type;
    public Expression expr;

    public CastExpression(Tree parseTree) {
        super();

        if (parseTree.getRule().equals("CastExpression LPAREN PrimitiveType RPAREN UnaryExpression")) {
            type = new PrimitiveType(parseTree.getChild(1));

        }

        if (parseTree.getRule().equals("CastExpression LPAREN Expression RPAREN UnaryExpressionNotPlusMinus")) {
            Expression cast = Expression.translateExpression(parseTree.getChild(1));
            if (!(cast instanceof NameExpression)) {
                throw new ASTException("Cast header is an expression but not a type!");
            }
            type = new ClassType(((NameExpression) cast).name);
        }

        if (parseTree.getRule().equals("CastExpression LPAREN ArrayType RPAREN UnaryExpressionNotPlusMinus")) {
            type = new ArrayType(parseTree.getChild(1));
        }

        expr = Expression.translateExpression(parseTree.getChild(3));
        addChild(type);
        addChild(expr);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
