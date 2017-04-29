package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daiweifan on 2017-03-01.
 */
public class InterfaceDeclaration extends TypeDeclaration {

    public RecursedProductionList<InterfaceType> extendsInterfaces;
    public RecursedProductionList<MethodHeader> interfaceBody;

    public boolean hierarchyChecked = false;

    public InterfaceDeclaration(Tree parseTree) {
        super();

        // InterfaceDeclaration Modifiersopt interface identifier ExtendsInterfacesopt { InterfaceMemberDeclarationsopt }

        int modifiersIndex = parseTree.getChildIndexByName("Modifiers");
        if (modifiersIndex != -1) {
            modifiers = Modifier.traverseModifiers(parseTree.getChild(modifiersIndex));
        } else {
            modifiers = null;
        }

        id = new TokenNode(parseTree.getChildByName("ID"));

        int superIndex = parseTree.getChildIndexByName("Super");
        if (superIndex != -1) {
            supers = new ClassType(parseTree.getChild(superIndex));
            addChild(supers);
        } else {
            supers = null;
        }

        int interfaceIndex = parseTree.getChildIndexByName("ExtendsInterfaces");
        if (interfaceIndex != -1) {
            extendsInterfaces = InterfaceType.translateInterfaces(parseTree.getChild(interfaceIndex),
                                                                  "ExtendsInterfaces ExtendsInterfaces COMMA InterfaceType",
                                                                  true);
            addChild(extendsInterfaces);
        } else {
            extendsInterfaces = null;
        }

        int bodyIndex = parseTree.getChildIndexByName("InterfaceMemberDeclarations");
        if (bodyIndex != -1) {
            interfaceBody = new RecursedProductionList<MethodHeader>(parseTree.getChild(bodyIndex),
                                                                     "InterfaceMemberDeclarations InterfaceMemberDeclarations InterfaceMemberDeclaration",
                                                                     false, false) {
                @Override
                public MethodHeader spawn(Tree parseTree) {
                    return new MethodHeader(parseTree.getChild(0));
                }
            };
            addChild(interfaceBody);
        } else {
            interfaceBody = null;
        }

    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
