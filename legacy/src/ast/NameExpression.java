package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-03.
 */
public class NameExpression extends Expression {

    public Name name;

    public NameExpression(Tree parseTree) {
        super();

        name = new Name(parseTree);
        addChild(name);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
