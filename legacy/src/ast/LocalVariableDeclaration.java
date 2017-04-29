package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by Daiwei on 2017-03-09.
 */
public class LocalVariableDeclaration extends BlockStatement {

    public Type type;
    public TokenNode id;
    public Expression expr;

    public LocalVariableDeclaration(Tree parseTree) {
        super();

        type = Type.interpretType(parseTree.getChild(0));
        addChild(type);

        id = new TokenNode(parseTree.getChildByName("ID"));
        addChild(id);

        expr = Expression.translateExpression(parseTree.getChildByName("Expression"));
        addChild(expr);

    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
