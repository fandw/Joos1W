package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class FieldDeclaration extends ClassBodyDeclaration {

    public RecursedProductionList<Modifier> modifiers;
    public Type type;
    public VariableDeclarator variableDeclarator;

    public FieldDeclaration(Tree parseTree) {
        super();

        // FieldDeclaration Modifiersopt Type VariableDeclarator ;
        int modifiersIndex = parseTree.getChildIndexByName("Modifiers");
        if (modifiersIndex != -1) {
            modifiers = Modifier.traverseModifiers(parseTree.getChild(modifiersIndex));
            addChild(modifiers);
        } else {
            modifiers = null;
        }

        type = Type.interpretType(parseTree.getChildByName("Type"));

        variableDeclarator = new VariableDeclarator(parseTree.getChildByName("VariableDeclarator"));

        addChild(type);
        addChild(variableDeclarator);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
