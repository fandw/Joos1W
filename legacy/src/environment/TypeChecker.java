package environment;

import ast.*;
import ast.util.ASTNodeInterface;
import ast.util.RecursedProductionList;
import environment.symbol.*;
import exception.TypeCheckException;

/**
 * Created by Da on 2017/03/16.
 */
public class TypeChecker implements ASTVisitor {

    public TypeChecker() {
    }

    public void visit(ArrayAccess node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ArrayCreationExpression node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ArrayType node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(Assignment node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(AST ast) throws TypeCheckException {
        visit(ast.compilationUnit);
    }

    public void visit(BinaryOps node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(Block node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(BlockStatement node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(CastExpression node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ClassBodyDeclaration node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ClassDeclaration node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ClassInstanceCreationExpression node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ClassType node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(CompilationUnit node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ConstructorDeclaration node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ConstructorDeclarator node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(EmptyStatement node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ExpressionStatement node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(FieldAccess node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(FieldDeclaration node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(FormalParameter node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ForStatement node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(IfStatement node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ImportDeclaration node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(InstanceOf node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(InterfaceDeclaration node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(InterfaceType node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(Literal node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(LocalMethodInvocation node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(LocalVariableDeclaration node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(MethodBody node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(MethodDeclaration node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(MethodHeader node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(Modifier node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(Name node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(NameExpression node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(OtherArrayAccess node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(OtherMethodInvocation node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(PackageDeclaration node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ParenthesizedExpression node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(PrimitiveType node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ReturnStatement node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(ThisExpression node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(TokenNode node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(UnaryExpression node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(VariableDeclarator node) throws TypeCheckException {
        node.visitChildren(this);
    }

    public void visit(WhileStatement node) throws TypeCheckException {
        node.visitChildren(this);
    }

    /*
        If the AmbiguousName is a simple name, consisting of a single Identifier:
            If the Identifier appears within the scope (§6.3) of a local variable declaration (§14.4) or parameter declaration (§8.4.1, §8.8.1, §14.19) or field declaration (§8.3) with that name, then the AmbiguousName is reclassified as an ExpressionName.
            Otherwise, if the Identifier appears within the scope (§6.3) of a local class declaration (§14.3) or member type declaration (§8.5, §9.5) with that name, then the AmbiguousName is reclassified as a TypeName.
            Otherwise, if a type of that name is declared in the compilation unit (§7.3) containing the Identifier, either by a single-type-import declaration (§7.5.1) or by a top level class (§8) or interface type declaration (§9), then the AmbiguousName is reclassified as a TypeName.
            Otherwise, if a type of that name is declared in another compilation unit (§7.3) of the package (§7.1) of the compilation unit containing the Identifier, then the AmbiguousName is reclassified as a TypeName.
            Otherwise, if a type of that name is declared by exactly one type-import-on-demand declaration (§7.5.2) of the compilation unit containing the Identifier, then the AmbiguousName is reclassified as a TypeName.
            Otherwise, the AmbiguousName is reclassified as a PackageName. A later step determines whether or not a package of that name actually exists.
        If the AmbiguousName is a qualified name, consisting of a name, a ".", and an Identifier, then the name to the left of the "." is first reclassified, for it is itself an AmbiguousName. There is then a choice:
            If the name to the left of the "." is reclassified as a PackageName, then if there is a package whose name is the name to the left of the "." and that package contains a declaration of a type whose name is the same as the Identifier, then this AmbiguousName is reclassified as a TypeName. Otherwise, this AmbiguousName is reclassified as a PackageName. A later step determines whether or not a package of that name actually exists.
            If the name to the left of the "." is reclassified as a TypeName, then if the Identifier is the name of a method or field of the class or interface denoted by TypeName, this AmbiguousName is reclassified as an ExpressionName. Otherwise, if the Identifier is the name of a member type of the class or interface denoted by TypeName, this AmbiguousName is reclassified as a TypeName. Otherwise, a compile-time error results.
            If the name to the left of the "." is reclassified as an ExpressionName, then let T be the type of the expression denoted by ExpressionName. If the Identifier is the name of a method or field of the class or interface denoted by T, this AmbiguousName is reclassified as an ExpressionName. Otherwise, if the Identifier is the name of a member type (§8.5, §9.5) of the class or interface denoted by T, then this AmbiguousName is reclassified as a TypeName. Otherwise, a compile-time error results.
     */
    public Symbol disambiguation(ASTNodeInterface node, RecursedProductionList<TokenNode> ids, int idPosition, NodeSymbolTable symbolTable) {
        TokenNode id = ids.get(idPosition);

        if (idPosition == 0) {
            // Is simple name
            //If the Identifier appears within the scope (§6.3) of
            // a local variable declaration (§14.4)
            // or parameter declaration (§8.4.1, §8.8.1, §14.19)
            Symbol localVariableSymbol = symbolTable.getLocalSymbol(id);
            if (localVariableSymbol != null) {
                return localVariableSymbol;
            }
            // or field declaration (§8.3) with that name
            FieldSymbol fieldSymbol = symbolTable.getFieldSymbol(id);
            if (fieldSymbol != null) {
                return fieldSymbol;
            }

            TypeSymbol typeSymbol = symbolTable.getTypeSymbol(id);
            if (typeSymbol != null) {
                return typeSymbol;
            }

            //Otherwise, the AmbiguousName is reclassified as a PackageName.
            PackageSymbol packageSymbol = symbolTable.fileSymbolTable.packages.getPackageSymbol(ids.iterator());
            if (packageSymbol != null) {
                return packageSymbol;
            }
            throw new TypeCheckException("Could not disambiguate this name");
        } else {
            // Is qualified name
            RecursedProductionList<TokenNode> sublist = (RecursedProductionList<TokenNode>) ids.subList(0, idPosition - 1);
            Symbol leftSide = disambiguation(node, sublist, idPosition - 1, symbolTable);
            if (leftSide instanceof PackageSymbol) {
                TypeSymbol typeSymbol = ((PackageSymbol) leftSide).getType(id);
                if (typeSymbol != null) {
                    return typeSymbol;
                }
                PackageSymbol packageSymbol = symbolTable.fileSymbolTable.packages.getPackageSymbol(sublist.iterator());
                if (packageSymbol != null) {
                    return packageSymbol;
                }
                throw new TypeCheckException("Could not disambiguate this name");
            } else if (leftSide instanceof TypeSymbol) {

            } else if (leftSide instanceof LocalVariableSymbol || leftSide instanceof FieldSymbol) {

            }


            throw new TypeCheckException("Could not disambiguate this name");
        }

    }
}
