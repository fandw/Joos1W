package environment;

import ast.*;
import environment.symbol.*;
import exception.EnvironmentBuildException;
import scanner.Kind;
import scanner.TerminalToken;
import utils.ModifiersChecker;

import java.util.*;

/**
 * Created by daiweifan on 2017-03-10.
 */
public class EnvironmentBuilder implements ASTVisitor {

    private static List<TokenNode> javaLangPackageIds = new ArrayList<>(
            Arrays.asList(new TokenNode(new TerminalToken(Kind.ID, "java")),
                          new TokenNode(new TerminalToken(Kind.ID, "lang"))));
    public PackageSymbol rootPackageSymbol;
    private PackageSymbol javaLangPackageSymbol = null;
    public ClassSymbol javaLangObjectSymbol = null;

    public EnvironmentBuilder(AST[] asts) throws EnvironmentBuildException {
        this.rootPackageSymbol = new PackageSymbol();

        for (AST ast : asts) {
            TypeSymbol typeSymbol;
            if (ast.compilationUnit.typeDeclaration instanceof ClassDeclaration) {
                typeSymbol = new ClassSymbol((ClassDeclaration) ast.compilationUnit.typeDeclaration);
            } else if (ast.compilationUnit.typeDeclaration instanceof InterfaceDeclaration) {
                typeSymbol = new InterfaceSymbol((InterfaceDeclaration) ast.compilationUnit.typeDeclaration);
            } else {
                throw new EnvironmentBuildException("AST does not have a valid TypeDeclaration");
            }

            Iterator<TokenNode> packageIds;
            if (ast.compilationUnit.packageDeclaration == null) {
                packageIds = Collections.emptyIterator();
            } else {
                packageIds = ast.compilationUnit.packageDeclaration.name.ids.listIterator();
            }

            if (!this.rootPackageSymbol.addType(typeSymbol, packageIds)) {
                throw new EnvironmentBuildException("No two classes or interfaces have the same canonical name.");
            }

            if (typeSymbol.getId().token.getLexeme().equals("Object") &&
                this.rootPackageSymbol.getPackageSymbol(javaLangPackageIds.listIterator())
                             .getType(new TokenNode(new TerminalToken(Kind.ID, "Object"))) == typeSymbol) {
                if (!(typeSymbol instanceof ClassSymbol)) {
                    throw new EnvironmentBuildException("Detected bad java.lang.Object class.");
                }
                this.javaLangObjectSymbol = (ClassSymbol) typeSymbol;
            }
        }

        this.javaLangPackageSymbol = this.rootPackageSymbol.getPackageSymbol(javaLangPackageIds.listIterator());
    }

    public void visit(ArrayAccess node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(ArrayCreationExpression node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(ArrayType node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(Assignment node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(AST ast) throws EnvironmentBuildException {
        visit(ast.compilationUnit);
    }

    public void visit(BinaryOps node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(Block node) throws EnvironmentBuildException {
        node.symbolTable = new NodeSymbolTable(node.parent.getSymbolTable());
        node.visitChildren(this);
    }

    public void visit(BlockStatement node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(CastExpression node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(ClassBodyDeclaration node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(ClassDeclaration node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        ClassSymbol symbol = new ClassSymbol(node);
        symbol.typeLinkingPriority = TypeSymbol.TypeLinkingPriority.EnclosingClassOrInterface;
        node.symbolTable.addSymbol(symbol);
        node.visitChildren(this);
    }

    public void visit(ClassInstanceCreationExpression node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(ClassType node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(CompilationUnit node) throws EnvironmentBuildException {
        TypeSymbol typeSymbol;
        if (node.typeDeclaration instanceof ClassDeclaration) {
            typeSymbol = new ClassSymbol((ClassDeclaration) node.typeDeclaration);
        } else if (node.typeDeclaration instanceof InterfaceDeclaration) {
            typeSymbol = new InterfaceSymbol((InterfaceDeclaration) node.typeDeclaration);
        } else {
            throw new EnvironmentBuildException("CompilationUnit does not have a valid TypeDeclaration");
        }
        node.symbolTable = new FileSymbolTable(typeSymbol, this.rootPackageSymbol, node.packageDeclaration);

        // Always import java.lang.*
        packageImport(node.symbolTable.fileSymbolTable, javaLangPackageSymbol);

        node.visitChildren(this);
    }

    public void visit(ConstructorDeclaration node) throws EnvironmentBuildException {
        node.symbolTable = new NodeSymbolTable(node.parent.getSymbolTable());
        node.symbolTable.fileSymbolTable.addSymbol(new ConstructorSymbol(node));
        node.visitChildren(this);
    }

    public void visit(ConstructorDeclarator node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(EmptyStatement node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(ExpressionStatement node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(FieldAccess node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(FieldDeclaration node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        TokenNode id = node.variableDeclarator.id;
        Symbol existingSymbol = node.symbolTable.getSymbol(id);
        if (existingSymbol != null && existingSymbol instanceof FieldSymbol) {
            throw new EnvironmentBuildException("No two fields declared in the same class may have the same name.");
        }
        node.symbolTable.addSymbol(new FieldSymbol(node));
        node.visitChildren(this);
    }

    public void visit(FormalParameter node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        TokenNode id = node.id;
        if (node.symbolTable.getLocalSymbol(id) != null) {
            throw new EnvironmentBuildException("No two local variables with overlapping scope have the same name.");
        }
        node.symbolTable.addSymbol(new FormalParameterSymbol(node));
        node.visitChildren(this);
    }

    public void visit(ForStatement node) throws EnvironmentBuildException {
        node.symbolTable = new NodeSymbolTable(node.parent.getSymbolTable());
        node.visitChildren(this);
    }

    public void visit(IfStatement node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    private static void packageImport(FileSymbolTable symbolTable, PackageSymbol packageSymbol) {
        for (TypeSymbol symbol : packageSymbol.getAllTypes()) {
            if (symbol instanceof ClassSymbol) {
                symbolTable
                        .addSymbol(new ClassSymbol((ClassSymbol) symbol, TypeSymbol.TypeLinkingPriority.ImportOnDemand));
            } else if (symbol instanceof InterfaceSymbol) {
                symbolTable.addSymbol(new InterfaceSymbol((InterfaceSymbol) symbol,
                                                                 TypeSymbol.TypeLinkingPriority.ImportOnDemand));
            } else {
                throw new EnvironmentBuildException("Invalid TypeSymbol detected");
            }
        }
    }

    private static void singleImport(FileSymbolTable symbolTable, PackageSymbol packageSymbol, TokenNode singleImportId) {
        TypeSymbol symbol = packageSymbol.getType(singleImportId);
        if (symbol == null) {
            throw new EnvironmentBuildException("Trying to import nonexistent type.");
        }
        if (symbol instanceof ClassSymbol) {
            symbolTable.addSymbol(new ClassSymbol((ClassSymbol) symbol, TypeSymbol.TypeLinkingPriority.SingleTypeImport));
        } else if (symbol instanceof InterfaceSymbol) {
            symbolTable.addSymbol(new InterfaceSymbol((InterfaceSymbol) symbol,
                                                             TypeSymbol.TypeLinkingPriority.SingleTypeImport));
        } else {
            throw new EnvironmentBuildException("Invalid TypeSymbol detected");
        }
    }

    public void visit(ImportDeclaration node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();

        Iterator<TokenNode> ids = node.isPackageImport ? node.name.ids.listIterator() :
                                  node.name.ids.subList(0, node.name.ids.size() - 1).listIterator();
        PackageSymbol packageSymbol = this.rootPackageSymbol.getPackageSymbol(ids);

        if (packageSymbol == null) {
            throw new EnvironmentBuildException("Trying to import from nonexistent package.");
        }

        if (packageSymbol != javaLangPackageSymbol) {
            if (node.isPackageImport) {
                packageImport(node.symbolTable.fileSymbolTable, packageSymbol);
            } else {
                TokenNode singleImportId = node.name.ids.get(node.name.ids.size() - 1);
                singleImport(node.symbolTable.fileSymbolTable, packageSymbol, singleImportId);
            }
        }

        node.visitChildren(this);
    }

    public void visit(InstanceOf node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(InterfaceDeclaration node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        InterfaceSymbol symbol = new InterfaceSymbol(node);
        symbol.typeLinkingPriority = TypeSymbol.TypeLinkingPriority.EnclosingClassOrInterface;
        node.symbolTable.addSymbol(symbol);
        node.visitChildren(this);
    }

    public void visit(InterfaceType node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(Literal node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(LocalMethodInvocation node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(LocalVariableDeclaration node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        TokenNode id = node.id;
        if (node.symbolTable.getLocalSymbol(id) != null) {
            throw new EnvironmentBuildException("No two local variables with overlapping scope have the same name.");
        }
        node.symbolTable.addSymbol(new LocalVariableSymbol(node));
        node.visitChildren(this);
    }

    public void visit(MethodBody node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(MethodDeclaration node) throws EnvironmentBuildException {
        node.symbolTable = new NodeSymbolTable(node.parent.getSymbolTable());
        node.visitChildren(this);
    }

    public void visit(MethodHeader node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        MethodSymbol methodSymbol = new MethodSymbol(node);
        methodSymbol.isAbstract = node.symbolTable.getType() instanceof InterfaceSymbol || (new ModifiersChecker(node.modifiers)).isAbstract;
        node.symbolTable.fileSymbolTable.addSymbol(methodSymbol);
        node.visitChildren(this);
    }

    public void visit(Modifier node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(Name node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(NameExpression node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(OtherArrayAccess node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(OtherMethodInvocation node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(PackageDeclaration node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(ParenthesizedExpression node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(PrimitiveType node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(ReturnStatement node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(ThisExpression node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(TokenNode node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(UnaryExpression node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(VariableDeclarator node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }

    public void visit(WhileStatement node) throws EnvironmentBuildException {
        node.symbolTable = node.parent.getSymbolTable();
        node.visitChildren(this);
    }
}
