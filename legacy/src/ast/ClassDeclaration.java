package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class ClassDeclaration extends TypeDeclaration {

    public RecursedProductionList<InterfaceType> interfaces;
    public RecursedProductionList<ClassBodyDeclaration> body;

    public boolean hierarchyChecked = false;

    public ClassDeclaration(Tree parseTree) {
        super();

        //ClassDeclaration Modifiersopt class identifier Superopt Interfacesopt { ClassBodyDeclarationsopt }

        int modifiersIndex = parseTree.getChildIndexByName("Modifiers");
        if (modifiersIndex != -1) {
            modifiers = Modifier.traverseModifiers(parseTree.getChild(modifiersIndex));
            addChild(modifiers);
        } else {
            modifiers = null;
        }

        id = new TokenNode(parseTree.getChildByName("ID"));
        addChild(id);

        int superIndex = parseTree.getChildIndexByName("Super");
        if (superIndex != -1) {
            supers = new ClassType(parseTree.getChild(superIndex).getChild(1));
            addChild(supers);
        } else {
            supers = null;
        }

        int interfaceIndex = parseTree.getChildIndexByName("Interfaces");
        if (interfaceIndex != -1) {
            interfaces = InterfaceType.translateInterfaces(parseTree.getChild(interfaceIndex).getChild(1),
                                                           "InterfaceTypeList InterfaceTypeList COMMA InterfaceType",
                                                           false);
            addChild(interfaces);
        } else {
            interfaces = null;
        }

        int bodyIndex = parseTree.getChildIndexByName("ClassBodyDeclarations");
        if (bodyIndex != -1) {
            body = new RecursedProductionList<ClassBodyDeclaration>(parseTree.getChild(bodyIndex),
                                                                    "ClassBodyDeclarations ClassBodyDeclarations ClassBodyDeclaration",
                                                                    false, false) {
                @Override
                public ClassBodyDeclaration spawn(Tree parseTree) {
                    return ClassBodyDeclaration.interpretClassBody(parseTree);
                }
            };
            addChild(body);
        } else {
            body = null;
        }

    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
