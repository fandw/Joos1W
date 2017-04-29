package environment;

import ast.*;
import ast.util.RecursedProductionList;
import environment.symbol.*;
import exception.HierarchyCheckException;
import utils.ModifiersChecker;

import java.util.ArrayList;
import java.util.List;

import static utils.Helpers.safe;

/**
 * Created by Da on 2017/03/16.
 */
public class HierarchyChecker implements ASTVisitor {

    private ClassSymbol javaLangObjectSymbol;

    public HierarchyChecker(ClassSymbol javaLangObjectSymbol) throws HierarchyCheckException {
        this.javaLangObjectSymbol = javaLangObjectSymbol;
        hierarchyCheck(javaLangObjectSymbol.getDeclaration());
    }

    private static void startCheckForCycles(TypeDeclaration self) throws HierarchyCheckException {
        checkForCycles(self, new ArrayList<TypeDeclaration>());
    }

    private static void checkForCycles(TypeDeclaration target, List<TypeDeclaration> reached)
            throws HierarchyCheckException {

        if (reached.contains(target)) {
            throw new HierarchyCheckException("The hierarchy must be acyclic.");
        }

        reached = new ArrayList<>(reached);
        reached.add(target);

        if (target instanceof ClassDeclaration) {
            if (target.supers != null) {
                checkForCycles(((ClassSymbol) target.supers.symbol).getDeclaration(), reached);
            }
            for (InterfaceType interfaceType : safe(((ClassDeclaration) target).interfaces)) {
                checkForCycles(interfaceType.symbol.getDeclaration(), reached);
            }
        } else if (target instanceof InterfaceDeclaration) {
            for (InterfaceType interfaceType : safe(((InterfaceDeclaration) target).extendsInterfaces)) {
                checkForCycles(interfaceType.symbol.getDeclaration(), reached);
            }
        }
    }

    private static boolean compareFormalParameters(RecursedProductionList<FormalParameter> l1,
                                                   RecursedProductionList<FormalParameter> l2) {
        if (l1 == l2) {
            return true;
        }

        if (l1 == null || l2 == null) {
            return false;
        }

        if (l1.size() != l2.size()) {
            return false;
        }

        for (int i = 0; i < l1.size(); i++) {
            if (!l1.get(i).equalsByType(l2.get(i))) {
                return false;
            }
        }
        return true;
    }

    private static void inheritField(FileSymbolTable symbolTable, FieldSymbol toInherit) {
        if (symbolTable.symbols.contains(toInherit)) {
            return;
        }

        boolean isReplaced = false;

        for (Symbol s : symbolTable.symbols) {
            if (s instanceof FieldSymbol && s.equalsById(toInherit.getId())) {
                isReplaced = true;
                break;
            }
        }

        if (!isReplaced) {
            symbolTable.addSymbol(new FieldSymbol(toInherit, true));
        }
    }

    private static void inheritConstructor(FileSymbolTable symbolTable, ConstructorSymbol toInherit) {
        if (symbolTable.symbols.contains(toInherit)) {
            return;
        }

        symbolTable.addSymbol(new ConstructorSymbol(toInherit, true));
    }

    private static void inheritMethod(FileSymbolTable symbolTable, MethodSymbol toInherit)
            throws HierarchyCheckException {
        if (symbolTable.symbols.contains(toInherit)) {
            return;
        }

        boolean isReplacedByLocal = false;

        for (Symbol s : symbolTable.symbols) {

            if (!(s instanceof MethodSymbol)) {
                continue;
            }

            MethodSymbol local = (MethodSymbol) s;
            MethodHeader localHeader = local.getDeclaration();
            MethodHeader toInheritHeader = toInherit.getDeclaration();

            if (local.equalsById(toInherit.getId()) &&
                compareFormalParameters(local.getDeclaration().formalParameters,
                                        toInherit.getDeclaration().formalParameters)) {

                ModifiersChecker inheritedModifiers = new ModifiersChecker(toInheritHeader.modifiers);
                ModifiersChecker localModifiers = new ModifiersChecker(localHeader.modifiers);

                if (toInheritHeader.returnType == null && localHeader.returnType != null ||
                    toInheritHeader.returnType != null && !toInheritHeader.returnType.equals(localHeader.returnType)) {
                    throw new HierarchyCheckException(
                            "A method must not replace a method with a different return type.");
                }

                if (localModifiers.isStatic != inheritedModifiers.isStatic) {
                    throw new HierarchyCheckException(
                            "A non-static method must not replace a static method, and vice-versa.");
                }

                if (localModifiers.isProtected && inheritedModifiers.isPublic ||
                    local.isAbstract && !toInherit.isAbstract && localModifiers.isPublic &&
                    inheritedModifiers.isProtected) {
                    throw new HierarchyCheckException("A protected method must not replace a public method.");
                }

                if (inheritedModifiers.isFinal) {
                    throw new HierarchyCheckException("A method must not replace a final method.");
                }

                isReplacedByLocal = true;
                break;
            }
        }

        if (!isReplacedByLocal) {
            symbolTable.addSymbol(new MethodSymbol(toInherit, true));
        }
    }

    private void inheritFrom(TypeDeclaration self, TypeDeclaration target) throws HierarchyCheckException {
        for (Symbol s : target.getSymbolTable().fileSymbolTable.symbols) {
            // Inherit fields and constructors when both self and target are classes
            if (self instanceof ClassDeclaration && target instanceof ClassDeclaration) {
                if (s instanceof FieldSymbol) {
                    inheritField(self.getSymbolTable().fileSymbolTable, (FieldSymbol) s);
                } else if (s instanceof ConstructorSymbol) {
                    inheritConstructor(self.getSymbolTable().fileSymbolTable, (ConstructorSymbol) s);
                }
            }
            if (s instanceof MethodSymbol) {
                inheritMethod(self.getSymbolTable().fileSymbolTable, (MethodSymbol) s);
            }
        }
    }

    public void visit(ArrayAccess node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ArrayCreationExpression node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ArrayType node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(Assignment node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(AST ast) throws HierarchyCheckException {
        visit(ast.compilationUnit);
    }

    public void visit(BinaryOps node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(Block node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(BlockStatement node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(CastExpression node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ClassBodyDeclaration node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    private static void checkSuper(ClassDeclaration node) {
        if (node.supers != null) {
            if (!(node.supers.symbol instanceof ClassSymbol)) {
                throw new HierarchyCheckException(
                        "The type mentioned in the extends clause of a class must be a class.");
            }
            ClassDeclaration superDeclaration = ((ClassSymbol) node.supers.symbol).getDeclaration();
            ModifiersChecker modifiersChecker = new ModifiersChecker(superDeclaration.modifiers);
            if (modifiersChecker.isFinal) {
                throw new HierarchyCheckException("A class must not extend a final class.");
            }
        }
    }

    private static void checkInterfaces(List<InterfaceType> interfaces) {
        List<InterfaceDeclaration> interfaceDeclarations = new ArrayList<>();
        for (InterfaceType interfaceType : interfaces) {
            interfaceDeclarations.add(interfaceType.symbol.getDeclaration());
        }
        for (InterfaceDeclaration declaration : interfaceDeclarations) {
            if (interfaceDeclarations.indexOf(declaration) != interfaceDeclarations.lastIndexOf(declaration)) {
                throw new HierarchyCheckException(
                        "An interface must not be repeated in an implements clause, or in an extends clause of an interface.");
            }
        }
    }

    private void hierarchyCheck(InterfaceDeclaration node) throws HierarchyCheckException {

        if (node.hierarchyChecked) {
            return;
        }

        checkInterfaces(safe(node.extendsInterfaces));
        startCheckForCycles(node);

        // Recursively perform checks on parent interfaces
        for (InterfaceType interfaceType : safe(node.extendsInterfaces)) {
            hierarchyCheck(interfaceType.symbol.getDeclaration());
        }

        List<MethodHeader> localMethods = new ArrayList<>();
        for (MethodHeader h1 : safe(node.interfaceBody)) {
            for (MethodHeader h2 : localMethods) {
                if (h1.id.equals(h2.id) && compareFormalParameters(h1.formalParameters, h2.formalParameters)) {
                    throw new HierarchyCheckException(
                            "An interface must not declare two methods with the same signature (name and parameter types).");
                }
            }
            localMethods.add(h1);
        }

        if (node.extendsInterfaces == null) {
            inheritFrom(node, javaLangObjectSymbol.getDeclaration());
        } else {
            for (InterfaceType interfaceType : safe(node.extendsInterfaces)) {
                inheritFrom(node, interfaceType.symbol.getDeclaration());
            }
        }

        node.hierarchyChecked = true;
    }

    private void hierarchyCheck(ClassDeclaration node) throws HierarchyCheckException {

        if (node.hierarchyChecked) {
            return;
        }

        checkSuper(node);
        checkInterfaces(safe(node.interfaces));
        startCheckForCycles(node);

        // Recursively perform checks on parent classes/interfaces
        if (node.supers != null) {
            hierarchyCheck(((ClassSymbol) node.supers.symbol).getDeclaration());
        }
        for (InterfaceType interfaceType : safe(node.interfaces)) {
            hierarchyCheck(interfaceType.symbol.getDeclaration());
        }

        // Get locals
        List<MethodHeader> localMethods = new ArrayList<>();
        List<ConstructorDeclarator> constructors = new ArrayList<>();
        for (ClassBodyDeclaration declaration : safe(node.body)) {
            if (declaration instanceof MethodDeclaration) {
                for (MethodHeader h2 : localMethods) {
                    MethodHeader h1 = ((MethodDeclaration) declaration).methodHeader;
                    if (h1.id.equals(h2.id) && compareFormalParameters(h1.formalParameters, h2.formalParameters)) {
                        throw new HierarchyCheckException(
                                "A class must not declare two methods with the same signature (name and parameter types).");
                    }
                }
                localMethods.add(((MethodDeclaration) declaration).methodHeader);
            } else if (declaration instanceof ConstructorDeclaration) {
                for (ConstructorDeclarator c2 : constructors) {
                    ConstructorDeclarator c1 = ((ConstructorDeclaration) declaration).constructorDeclarator;
                    if (compareFormalParameters(c1.formalParameters, c2.formalParameters)) {
                        throw new HierarchyCheckException(
                                "A class must not declare two constructors with the same parameter types.");
                    }
                }
                constructors.add(((ConstructorDeclaration) declaration).constructorDeclarator);
            }
        }

        // Inherit from extends
        if (node.supers != null) {
            inheritFrom(node, ((ClassSymbol) node.supers.symbol).getDeclaration());
        } else if (!node.symbolTable.symbols.get(0).equals(javaLangObjectSymbol)) {
            inheritFrom(node, javaLangObjectSymbol.getDeclaration());
        }

        // Inherit from implements
        for (InterfaceType interfaceType : safe(node.interfaces)) {
            inheritFrom(node, interfaceType.symbol.getDeclaration());
        }

        ModifiersChecker classModifiers = new ModifiersChecker(node.modifiers);
        if (!classModifiers.isAbstract) {
            for (Symbol s : node.getSymbolTable().fileSymbolTable.symbols) {
                if (s instanceof MethodSymbol && ((MethodSymbol) s).isAbstract) {
                    throw new HierarchyCheckException("A class that has any abstract methods must be abstract.");
                }
            }
        }

        node.hierarchyChecked = true;
    }

    public void visit(ClassDeclaration node) throws HierarchyCheckException {
        hierarchyCheck(node);
        node.visitChildren(this);
    }

    public void visit(ClassInstanceCreationExpression node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ClassType node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(CompilationUnit node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ConstructorDeclaration node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ConstructorDeclarator node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(EmptyStatement node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ExpressionStatement node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(FieldAccess node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(FieldDeclaration node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(FormalParameter node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ForStatement node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(IfStatement node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ImportDeclaration node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(InstanceOf node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(InterfaceDeclaration node) throws HierarchyCheckException {
        hierarchyCheck(node);
        node.visitChildren(this);
    }

    public void visit(InterfaceType node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(Literal node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(LocalMethodInvocation node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(LocalVariableDeclaration node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(MethodBody node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(MethodDeclaration node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(MethodHeader node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(Modifier node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(Name node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(NameExpression node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(OtherArrayAccess node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(OtherMethodInvocation node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(PackageDeclaration node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ParenthesizedExpression node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(PrimitiveType node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ReturnStatement node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(ThisExpression node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(TokenNode node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(UnaryExpression node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(VariableDeclarator node) throws HierarchyCheckException {
        node.visitChildren(this);
    }

    public void visit(WhileStatement node) throws HierarchyCheckException {
        node.visitChildren(this);
    }
}
