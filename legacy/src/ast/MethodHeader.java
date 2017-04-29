package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by Daiwei on 2017-03-09.
 */
public class MethodHeader extends ASTNode {
    public RecursedProductionList<Modifier> modifiers;
    public Type returnType;
    public TokenNode id;
    public RecursedProductionList<FormalParameter> formalParameters;

    public MethodHeader(Tree parseTree) {
        super();

        int modifierIndex = parseTree.getChildIndexByName("Modifiers");
        if (modifierIndex != -1) {
            modifiers = Modifier.traverseModifiers(parseTree.getChild(modifierIndex));
            addChild(modifiers);
        } else {
            modifiers = null;
        }

        int typeIndex = parseTree.getChildIndexByName("Type");
        if (typeIndex != -1) {
            returnType = Type.interpretType(parseTree.getChild(typeIndex));
            addChild(returnType);
        } else {
            returnType = null;
        }

        id = new TokenNode(parseTree.getChildByName("ID"));

        int formalsIndex = parseTree.getChildIndexByName("FormalParameterList");
        if (formalsIndex != -1) {
            formalParameters = FormalParameter.recurseFormalParameterList(parseTree.getChild(formalsIndex));
            addChild(formalParameters);
        } else {
            formalParameters = null;
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
