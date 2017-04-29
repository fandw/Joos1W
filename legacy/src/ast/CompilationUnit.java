package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-28.
 */
public class CompilationUnit extends ASTNode {

    public PackageDeclaration packageDeclaration;
    public RecursedProductionList<ImportDeclaration> importDeclarations;
    public TypeDeclaration typeDeclaration;

    public CompilationUnit(Tree parseTree) {
        super();

        // CompilationUnit PackageDeclarationopt ImportDeclarationsopt TypeDeclarationsopt
        int packageIndex = parseTree.getChildIndexByName("PackageDeclaration");
        if (packageIndex != -1) {
            packageDeclaration = new PackageDeclaration(parseTree.getChild(packageIndex));
            addChild(packageDeclaration);
        } else {
            packageDeclaration = null;
        }

        int importIndex = parseTree.getChildIndexByName("ImportDeclarations");
        if (importIndex != -1) {
            importDeclarations = ImportDeclaration.translateImports(parseTree.getChild(importIndex));
            addChild(importDeclarations);
        } else {
            importDeclarations = null;
        }

        int typeIndex = parseTree.getChildIndexByName("TypeDeclaration");
        if (typeIndex != -1) {
            typeDeclaration = TypeDeclaration.interpret(parseTree.getChild(typeIndex));
            addChild(typeDeclaration);
        } else {
            typeDeclaration = null;
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
