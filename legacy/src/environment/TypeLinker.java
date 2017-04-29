package environment;

import ast.*;
import environment.symbol.*;
import exception.TypeLinkException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TypeLinker implements ASTVisitor {

    public PackageSymbol packages;

    public TypeLinker(PackageSymbol packages) throws TypeLinkException {
        this.packages = packages;
        checkPackageSymbol(this.packages, true);
    }

    private static void checkPackageSymbol(PackageSymbol symbol, boolean atRoot) throws TypeLinkException {
        for (TokenNode packageId : symbol.packages.keySet()) {
            if (!atRoot && symbol.types.containsKey(packageId)) {
                throw new TypeLinkException(
                        "No package names or prefixes may resolve to types, except for types in the default package.");
            }
            checkPackageSymbol(symbol.packages.get(packageId), false);
        }
    }

    private TypeSymbol resolveTypeByName(FileSymbolTable symbolTable, List<TokenNode> ids, boolean suppressException) throws TypeLinkException {
        TypeSymbol result = null;

        // simple name, search in symbol table
        if (ids.size() == 1) {
            TokenNode id = ids.get(0);
            for (Symbol s : symbolTable.symbols) {

                // Take advantage of sorted symbol table
                if (!(s instanceof TypeSymbol)) {
                    break;
                }

                if (s.equalsById(id)) {
                    if (result != null &&
                        ((TypeSymbol) s).typeLinkingPriority == TypeSymbol.TypeLinkingPriority.ImportOnDemand) {
                        throw new TypeLinkException(
                                "All simple type names must resolve to a unique class or interface.");
                    }
                    result = (TypeSymbol) s;
                    if (result.typeLinkingPriority != TypeSymbol.TypeLinkingPriority.ImportOnDemand) {
                        break;
                    }
                }
            }

        // fully qualified name, search in packages
        } else {
            List<TokenNode> packageIds = ids.subList(0, ids.size() - 1);

            // Try recursively resolving all prefixes of the name; if it does resolve, we have a problem
            TypeSymbol tryPrefixResolution = resolveTypeByName(symbolTable, packageIds, true);
            if (tryPrefixResolution != null) {
                throw new TypeLinkException(
                        "When a fully qualified name resolves to a type, no strict prefix of the fully qualified name can resolve to a type in the same environment.");
            }

            TokenNode typeId = ids.get(ids.size() - 1);
            PackageSymbol packageSymbol = this.packages.getPackageSymbol(packageIds.listIterator());
            result = packageSymbol == null ? null : packageSymbol.getType(typeId);
        }

        if (!suppressException && result == null) {
            throw new TypeLinkException(
                    "All type names must resolve to some class or interface declared in some file listed on the Joos command line.");
        }

        return result;
    }

    public void visit(ArrayAccess node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ArrayCreationExpression node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ArrayType node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(Assignment node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(AST ast) throws TypeLinkException {
        visit(ast.compilationUnit);
    }

    public void visit(BinaryOps node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(Block node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(BlockStatement node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(CastExpression node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ClassBodyDeclaration node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ClassDeclaration node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ClassInstanceCreationExpression node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ClassType node) throws TypeLinkException {
        node.symbol = resolveTypeByName(node.symbolTable.fileSymbolTable, node.name.ids, false);
        node.visitChildren(this);
    }

    public void visit(CompilationUnit node) throws TypeLinkException {

        if (node.symbolTable == null || !(node.symbolTable instanceof FileSymbolTable)) {
            throw new TypeLinkException("Something went terribly wrong with the file's symbol table.");
        }

        // Sort FileSymbolTable for better search performance
        Collections.sort(node.symbolTable.symbols, new Comparator<Symbol>() {
            @Override
            public int compare(Symbol o1, Symbol o2) {
                if (o1 instanceof TypeSymbol && o2 instanceof TypeSymbol) {
                    return ((TypeSymbol) o1).typeLinkingPriority.value - ((TypeSymbol) o2).typeLinkingPriority.value;
                } else if (o1 instanceof TypeSymbol) {
                    return -1;
                } else if (o2 instanceof TypeSymbol) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        TypeSymbol selfSymbol = (TypeSymbol) node.symbolTable.symbols.get(0);

        // Check FileSymbolTable against rules
        List<TokenNode> singleTypeImportIds = new ArrayList<>();
        for (Symbol s : node.symbolTable.symbols) {
            if (s instanceof TypeSymbol &&
                ((TypeSymbol) s).typeLinkingPriority == TypeSymbol.TypeLinkingPriority.SingleTypeImport) {
                TokenNode id = s.getId();
                if (node.symbolTable.getType().equalsById(id) && !s.equals(selfSymbol)) {
                    throw new TypeLinkException(
                            "No single-type-import declaration clashes with the class or interface declared in the same file.");
                }
                if (singleTypeImportIds.contains(id) && !s.equals(node.symbolTable.getSymbol(id))) {
                    throw new TypeLinkException("No two single-type-import declarations clash with each other.");
                }
                singleTypeImportIds.add(id);
            }
        }

        node.visitChildren(this);
    }

    public void visit(ConstructorDeclaration node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ConstructorDeclarator node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(EmptyStatement node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ExpressionStatement node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(FieldAccess node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(FieldDeclaration node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(FormalParameter node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ForStatement node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(IfStatement node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ImportDeclaration node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(InstanceOf node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(InterfaceDeclaration node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(InterfaceType node) throws TypeLinkException {
        TypeSymbol symbol = resolveTypeByName(node.symbolTable.fileSymbolTable, node.name.ids, false);
        if (!(symbol instanceof InterfaceSymbol)) {
            throw new TypeLinkException("Expecting a InterfaceSymbol, got something else.");
        }
        node.symbol = (InterfaceSymbol) symbol;
        node.visitChildren(this);
    }

    public void visit(Literal node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(LocalMethodInvocation node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(LocalVariableDeclaration node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(MethodBody node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(MethodDeclaration node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(MethodHeader node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(Modifier node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(Name node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(NameExpression node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(OtherArrayAccess node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(OtherMethodInvocation node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(PackageDeclaration node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ParenthesizedExpression node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(PrimitiveType node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ReturnStatement node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(ThisExpression node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(TokenNode node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(UnaryExpression node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(VariableDeclarator node) throws TypeLinkException {
        node.visitChildren(this);
    }

    public void visit(WhileStatement node) throws TypeLinkException {
        node.visitChildren(this);
    }
}
