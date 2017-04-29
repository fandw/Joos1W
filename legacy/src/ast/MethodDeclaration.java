package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class MethodDeclaration extends ClassBodyDeclaration {
    // MethodHeader MethodBody

    public MethodHeader methodHeader;
    public MethodBody methodBody;

    public MethodDeclaration(Tree parseTree) {
        super();

        methodHeader = new MethodHeader(parseTree.getChild(0));
        addChild(methodHeader);
        methodBody = new MethodBody(parseTree.getChild(1));
        addChild(methodBody);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
