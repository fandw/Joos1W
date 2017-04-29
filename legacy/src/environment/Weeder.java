package environment;

import ast.*;
import exception.WeedException;
import scanner.Kind;
import utils.ModifiersChecker;

import java.util.Objects;

public class Weeder implements ASTVisitor {

    private String fileName;

    public Weeder(String fileName) {
        this.fileName = fileName;
    }

    public void visit(AST ast) throws WeedException {
        visit(ast.compilationUnit);
    }

    public void visit(ArrayAccess node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ArrayCreationExpression node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ArrayType node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(Assignment node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(BinaryOps node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(Block node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(BlockStatement node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(CastExpression node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ClassBodyDeclaration node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ClassDeclaration node) throws WeedException {

        ModifiersChecker c = new ModifiersChecker(node.modifiers);

        if (c.isPackagePrivate) {
            throw new WeedException("A class cannot be package private.");
        }

        if (c.isAbstract && c.isFinal) {
            throw new WeedException("A class cannot be both abstract and final.");
        }

        if (!fileName.endsWith(".java")) {
            throw new WeedException(
                    "A class/interface must be declared in a .java file with the same base name as the class/interface.");
        }

        if (!Objects.equals(fileName, node.id.token.getLexeme() + ".java")) {
            throw new WeedException(
                    "A class/interface must be declared in a .java file with the same base name as the class/interface.");
        }

        boolean hasConstructor = false;
        for (ClassBodyDeclaration d : node.body) {
            if (d instanceof ConstructorDeclaration) {
                hasConstructor = true;
                break;
            }
        }
        if (!hasConstructor) {
            throw new WeedException("Every class must contain at least one explicit constructor.");
        }

        node.visitChildren(this);
    }

    public void visit(ClassInstanceCreationExpression node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ClassType node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(CompilationUnit node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ConstructorDeclaration node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ConstructorDeclarator node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(EmptyStatement node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ExpressionStatement node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(FieldAccess node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(FieldDeclaration node) throws WeedException {

        ModifiersChecker c = new ModifiersChecker(node.modifiers);

        if (c.isPrivate) {
            throw new WeedException("Private fields not allowed in Joos.");
        }

        if (c.isPackagePrivate) {
            throw new WeedException("A field cannot be package private.");
        }

        if (c.isFinal) {
            throw new WeedException("No field can be final.");
        }

        node.visitChildren(this);
    }

    public void visit(FormalParameter node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ForStatement node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(IfStatement node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ImportDeclaration node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(InstanceOf node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(InterfaceDeclaration node) throws WeedException {

        if (!fileName.endsWith(".java")) {
            throw new WeedException(
                    "A class/interface must be declared in a .java file with the same base name as the class/interface.");
        }

        if (!Objects.equals(fileName, node.id.token.getLexeme() + ".java")) {
            throw new WeedException(
                    "A class/interface must be declared in a .java file with the same base name as the class/interface.");
        }

        node.visitChildren(this);
    }

    public void visit(InterfaceType node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(Literal node) throws WeedException {
        if (node.token.token.getKind() == Kind.DecimalIntegerLiteral) {
            String lexeme = node.token.token.getLexeme();
            if (node.isNegativeInteger) {
                lexeme = "-" + lexeme;
            }
            try {
                Integer.parseInt(lexeme);
            } catch (NumberFormatException e) {
                throw new WeedException("Out-of-range decimal integer literal detected.");
            }
        }
        node.visitChildren(this);
    }

    public void visit(LocalMethodInvocation node) throws WeedException {
        if (node.name.ids.size() == 1 && (Objects.equals(node.name.ids.get(0).token.getLexeme(), "this") ||
                                          Objects.equals(node.name.ids.get(0).token.getLexeme(), "super"))) {
            throw new WeedException("A method or constructor must not contain explicit this() or super() calls.");
        }
        node.visitChildren(this);
    }

    public void visit(LocalVariableDeclaration node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(MethodBody node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(MethodDeclaration node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(MethodHeader node) throws WeedException {

        ModifiersChecker c = new ModifiersChecker(node.modifiers);

        if (c.isPrivate) {
            throw new WeedException("Private methods not allowed in Joos.");
        }

        if (c.isPackagePrivate) {
            throw new WeedException("A method cannot be package private.");
        }

        if (c.isAbstract && (c.isStatic || c.isFinal)) {
            throw new WeedException("An abstract method cannot be static or final.");
        }

        if (c.isStatic && c.isFinal) {
            throw new WeedException("A static method cannot be final.");
        }

        if (c.isNative && !c.isStatic) {
            throw new WeedException("A native method must be static.");
        }

        if (node.getParent().getParent() instanceof InterfaceDeclaration && (c.isStatic || c.isFinal)) {
            throw new WeedException("An interface method cannot be static, final, or native.");
        }

        if (c.isAbstract && node.getParent() instanceof MethodDeclaration &&
            (((MethodDeclaration) node.getParent()).methodBody.block) != null) {
            throw new WeedException("Abstract method cannot have a body.");
        }

        node.visitChildren(this);
    }

    public void visit(Modifier node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(Name node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(NameExpression node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(OtherArrayAccess node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(OtherMethodInvocation node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(PackageDeclaration node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ParenthesizedExpression node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(PrimitiveType node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ReturnStatement node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(ThisExpression node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(TokenNode node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(UnaryExpression node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(VariableDeclarator node) throws WeedException {
        node.visitChildren(this);
    }

    public void visit(WhileStatement node) throws WeedException {
        node.visitChildren(this);
    }
}
