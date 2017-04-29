package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

public class ImportDeclaration extends ASTNode {
    public Name name;
    public boolean isPackageImport;

    public ImportDeclaration(Tree parseTree) {
        super();

        this.name = new Name(parseTree.getChild(1));
        isPackageImport = parseTree.getRule().hasRHS("STAR") != -1;
        addChild(name);
    }

    public static RecursedProductionList<ImportDeclaration> translateImports(Tree parseTree) {
        return new RecursedProductionList<ImportDeclaration>(parseTree,
                                                             "ImportDeclarations ImportDeclarations ImportDeclaration",
                                                             false, false) {
            @Override
            public ImportDeclaration spawn(Tree parseTree) {
                return new ImportDeclaration(parseTree);
            }
        };
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
