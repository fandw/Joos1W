package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class PackageDeclaration extends ASTNode {

    public Name name;

    public PackageDeclaration(Tree parseTree) {
        super();

        name = new Name(parseTree.getChild(1));
        addChild(name);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
