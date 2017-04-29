package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class ClassBodyDeclaration extends ASTNode {

    public ClassBodyDeclaration() {
        super();
    }

    private static String[] trickleDown = new String[] {
            "ClassBodyDeclaration FieldDeclaration",
            "ClassBodyDeclaration MethodDeclaration",
            "ClassBodyDeclaration ConstructorDeclaration"
    };

    public static ClassBodyDeclaration interpretClassBody(Tree parseTree) {

        for (String r : trickleDown) {
            if (parseTree.getRule().equals(r)) {
                return interpretClassBody(parseTree.getChild(0));
            }
        }

        if (parseTree.getRule().startsWith("FieldDeclaration")) {
            return new FieldDeclaration(parseTree);
        }

        if (parseTree.getRule().startsWith("MethodDeclaration")) {
            return new MethodDeclaration(parseTree);
        }

        if (parseTree.getRule().startsWith("ConstructorDeclaration")) {
            return new ConstructorDeclaration(parseTree);
        }

        return null;
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
