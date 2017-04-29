package ast;

import ast.util.RecursedProductionList;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public abstract class TypeDeclaration extends ASTNode {
    public RecursedProductionList<Modifier> modifiers;
    public TokenNode id;
    public ClassType supers;

    public TypeDeclaration() {
        super();
    }

    public static TypeDeclaration interpret(Tree parseTree) {
        if (parseTree.getRule().equals("TypeDeclaration ClassDeclaration")) {
            return new ClassDeclaration(parseTree.getChild(0));
        }
        if (parseTree.getRule().equals("TypeDeclaration InterfaceDeclaration")) {
            return new InterfaceDeclaration(parseTree.getChild(0));
        }
        return null;
    }
}
