package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class ClassInstanceCreationExpression extends Expression {
    public ClassType type;
    public RecursedProductionList<Expression> arguments;

    public ClassInstanceCreationExpression(Tree parseTree) {
        super();

        type = new ClassType(parseTree.getChild(1));
        addChild(type);

        if (parseTree.getChildIndexByName("ArgumentList") != -1) {
            //ClassInstanceCreationExpression new ClassType ( ArgumentList )
            arguments = new RecursedProductionList<Expression>(parseTree.getChild(3),
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
