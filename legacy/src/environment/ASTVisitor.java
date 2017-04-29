package environment;

import ast.*;
import exception.WeedException;

public interface ASTVisitor {
    void visit(ArrayAccess node) throws WeedException;

    void visit(ArrayCreationExpression node) throws WeedException;

    void visit(ArrayType node) throws WeedException;

    void visit(Assignment node) throws WeedException;

    void visit(AST ast) throws WeedException;

    void visit(BinaryOps node) throws WeedException;

    void visit(Block node) throws WeedException;

    void visit(BlockStatement node) throws WeedException;

    void visit(CastExpression node) throws WeedException;

    void visit(ClassBodyDeclaration node) throws WeedException;

    void visit(ClassDeclaration node) throws WeedException;

    void visit(ClassInstanceCreationExpression node) throws WeedException;

    void visit(ClassType node) throws WeedException;

    void visit(CompilationUnit node) throws WeedException;

    void visit(ConstructorDeclaration node) throws WeedException;

    void visit(ConstructorDeclarator node) throws WeedException;

    void visit(EmptyStatement node) throws WeedException;

    void visit(ExpressionStatement node) throws WeedException;

    void visit(FieldAccess node) throws WeedException;

    void visit(FieldDeclaration node) throws WeedException;

    void visit(FormalParameter node) throws WeedException;

    void visit(ForStatement node) throws WeedException;

    void visit(IfStatement node) throws WeedException;

    void visit(ImportDeclaration node) throws WeedException;

    void visit(InstanceOf node) throws WeedException;

    void visit(InterfaceDeclaration node) throws WeedException;

    void visit(InterfaceType node) throws WeedException;

    void visit(Literal node) throws WeedException;

    void visit(LocalMethodInvocation node) throws WeedException;

    void visit(LocalVariableDeclaration node) throws WeedException;

    void visit(MethodBody node) throws WeedException;

    void visit(MethodDeclaration node) throws WeedException;

    void visit(MethodHeader node) throws WeedException;

    void visit(Modifier node) throws WeedException;

    void visit(Name node) throws WeedException;

    void visit(NameExpression node) throws WeedException;

    void visit(OtherArrayAccess node) throws WeedException;

    void visit(OtherMethodInvocation node) throws WeedException;

    void visit(PackageDeclaration node) throws WeedException;

    void visit(ParenthesizedExpression node) throws WeedException;

    void visit(PrimitiveType node) throws WeedException;

    void visit(ReturnStatement node) throws WeedException;

    void visit(ThisExpression node) throws WeedException;

    void visit(TokenNode node) throws WeedException;

    void visit(UnaryExpression node) throws WeedException;

    void visit(VariableDeclarator node) throws WeedException;

    void visit(WhileStatement node) throws WeedException;
}
