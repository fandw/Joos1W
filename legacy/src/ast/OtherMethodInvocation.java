package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class OtherMethodInvocation extends Expression {

    public Expression primary;
    public TokenNode id;
    public RecursedProductionList<Expression> arguments;

    public OtherMethodInvocation(Tree parseTree) {
        super();

        // MethodInvocation Primary DOT ID LPAREN ArgumentList RPAREN
        primary = Expression.translateExpression(parseTree.getChild(0));
        id = new TokenNode(parseTree.getChild(2));
        addChild(primary);
        addChild(id);

        if (parseTree.getChildIndexByName("ArgumentList") != -1) {
            //ClassInstanceCreationExpression new ClassType ( ArgumentList )
            arguments = new RecursedProductionList<Expression>(parseTree.getChild(4),
                                                               "ArgumentList ArgumentList COMMA Expression", true,
                                                               false) {
                @Override
                public Expression spawn(Tree parseTree) {
                    return Expression.translateExpression(parseTree);
                }
            };
            addChild(arguments);
        } else {
            arguments = null;
        }

    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
