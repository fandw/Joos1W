package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by Daiwei on 2017-03-07.
 */
public class ConstructorDeclaration extends ClassBodyDeclaration {

    //ConstructorDeclaration Modifiersopt ConstructorDeclarator ConstructorBody

    public RecursedProductionList<Modifier> modifiers;
    public ConstructorDeclarator constructorDeclarator;
    public RecursedProductionList<Statement> constructorBody;

    public ConstructorDeclaration(Tree parseTree) {
        super();

        int modifierIndex = parseTree.getChildIndexByName("Modifiers");
        if (modifierIndex != -1) {
            modifiers = Modifier.traverseModifiers(parseTree.getChild(modifierIndex));
            addChild(modifiers);
        } else {
            modifiers = null;
        }

        constructorDeclarator = new ConstructorDeclarator(parseTree.getChildByName("ConstructorDeclarator"));
        addChild(constructorDeclarator);

        if (parseTree.getChildByName("ConstructorBody").getChildIndexByName("BlockStatements") != -1) {
            constructorBody =
                    BlockStatement.traverseBlockStatement(parseTree.getChildByName("ConstructorBody").getChild(1));
            addChild(constructorBody);
        } else {
            constructorBody = null;
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
