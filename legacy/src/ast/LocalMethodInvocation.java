package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class LocalMethodInvocation extends Expression {
    public Name name;
    public RecursedProductionList<Expression> arguments;

    public LocalMethodInvocation(Tree parseTree) {
        super();

        //MethodInvocation Name ( ArgumentListopt )

        name = new Name(parseTree.getChild(0));

        if (parseTree.getChildIndexByName("ArgumentList") != -1) {
            //ClassInstanceCreationExpression new ClassType ( ArgumentList )
            arguments = new RecursedProductionList<Expression>(parseTree.getChild(2),
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

        addChild(name);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
