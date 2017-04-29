package environment;

import ast.*;
import ast.util.RecursedProductionList;
import exception.StaticAnalysisException;
import scanner.Kind;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Da on 2017/03/16.
 */
public class StaticAnalyzer implements ASTVisitor {

    private TokenNode currentLocalVariable = null;

    public StaticAnalyzer() {
    }

    public void visit(ArrayAccess node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(ArrayCreationExpression node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(ArrayType node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(Assignment node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(AST ast) throws StaticAnalysisException {
        visit(ast.compilationUnit);
    }

    public void visit(BinaryOps node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(Block node) throws StaticAnalysisException {
        ArrayList<Statement> statements = node.blockStatements;
        if (statements != null) {
            if (!node.reachable) {
                throw new StaticAnalysisException("Block unreachable");
            }
            checkReachableStatements(statements);
            node.completable = statements.get(statements.size() - 1).completable;
        } else {
            node.completable = node.reachable;
        }
    }

    public void visit(BlockStatement node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(CastExpression node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(ClassBodyDeclaration node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(ClassDeclaration node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(ClassInstanceCreationExpression node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(ClassType node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(CompilationUnit node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(ConstructorDeclaration node) throws StaticAnalysisException {
        ArrayList<Statement> statements = node.constructorBody;
        checkReachableStatements(statements);
        node.visitChildren(this);
    }

    public void visit(ConstructorDeclarator node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(EmptyStatement node) throws StaticAnalysisException {
        node.completable = true;
    }

    public void visit(ExpressionStatement node) throws StaticAnalysisException {
        node.completable = true;
    }

    public void visit(FieldAccess node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(FieldDeclaration node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(FormalParameter node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(ForStatement node) throws StaticAnalysisException {
        node.forCondition = simplifyExpression(node.forCondition);
        node.forUpdate.reachable = !node.forCondition.equalsFalse();
        node.forUpdate.completable = node.forUpdate.reachable;
        node.stmt.reachable = node.reachable && !node.forCondition.equalsFalse();
        checkReachableStatement(node.stmt);
        node.completable = !node.forCondition.equalsTrue();
    }

    public void visit(IfStatement node) throws StaticAnalysisException {
        //if statement is an exception, do no evaluate the test expr
        node.thenStatement.reachable = node.reachable;
        checkReachableStatement(node.thenStatement);
        if (node.elseStatement != null) {
            node.elseStatement.reachable = node.reachable;
            checkReachableStatement(node.elseStatement);
            if (node.thenStatement.completable || node.elseStatement.completable) {
                node.completable = true;
            }
        } else {
            node.completable = node.reachable;
        }
    }

    public void visit(ImportDeclaration node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(InstanceOf node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(InterfaceDeclaration node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(InterfaceType node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(Literal node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(LocalMethodInvocation node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(LocalVariableDeclaration node) throws StaticAnalysisException {
        node.type.acceptVisitor(this);
        node.id.acceptVisitor(this);
        currentLocalVariable = node.id;
        node.expr.acceptVisitor(this);
        currentLocalVariable = null;
        node.completable = node.reachable;
    }

    public void visit(MethodBody node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(MethodDeclaration node) throws StaticAnalysisException {
        RecursedProductionList<Modifier> modifiers = node.methodHeader.modifiers;
        if (Modifier.hasModifier(modifiers, Kind.NATIVE) || Modifier.hasModifier(modifiers, Kind.ABSTRACT)) {
            return;
        }

        RecursedProductionList<Statement> statements = node.methodBody.block.blockStatements;
        if (statements != null) {
            checkReachableStatements(statements);
            if (node.methodHeader.returnType != null && (statements.get(statements.size() - 1).completable)) {
                throw new StaticAnalysisException("Need return statement for non-void method!");
            }
        } else if (node.methodHeader.returnType != null) {
            throw new StaticAnalysisException("Need return statement for non-void method!");
        }
    }

    public void visit(MethodHeader node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(Modifier node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(Name node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(NameExpression node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(OtherArrayAccess node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(OtherMethodInvocation node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(PackageDeclaration node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(ParenthesizedExpression node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(PrimitiveType node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(ReturnStatement node) throws StaticAnalysisException {
        node.completable = false;
    }

    public void visit(ThisExpression node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(TokenNode node) throws StaticAnalysisException {
        if (node.isKind(Kind.ID) && node.equals(currentLocalVariable)) {
            throw new StaticAnalysisException("The variable must not occur in its own initializer!");
        }
        node.visitChildren(this);
    }

    public void visit(UnaryExpression node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(VariableDeclarator node) throws StaticAnalysisException {
        node.visitChildren(this);
    }

    public void visit(WhileStatement node) throws StaticAnalysisException {
        node.expr = simplifyExpression(node.expr);
        node.stmt.reachable = node.reachable && !node.expr.equalsFalse();
        checkReachableStatement(node.stmt);
        node.completable = !node.expr.equalsTrue();
    }

    public void checkReachableStatements(ArrayList<Statement> statements) throws StaticAnalysisException {
        if (statements == null || statements.isEmpty()) {
            return;
        }

        Iterator<Statement> it = statements.iterator();
        Statement current = it.next();
        current.reachable = true;
        while (it.hasNext()) {
            Statement next = it.next();
            checkReachableStatement(current, next);
            current = next;
        }
        checkReachableStatement(current);
    }

    public void checkReachableStatement(Statement current) throws StaticAnalysisException {
        if (!current.reachable) {
            throw new StaticAnalysisException("Statement unreachable!");
        }
        current.acceptVisitor(this);
    }

    public void checkReachableStatement(Statement current, Statement next) throws StaticAnalysisException {
        checkReachableStatement(current);
        next.reachable = current.completable;
    }

    public Expression simplifyExpression(Expression expr) {
        if (expr == null) {
            return null;
        }

        if (expr instanceof BinaryOps) {
            Expression first = ((BinaryOps) expr).first;
            Expression second = ((BinaryOps) expr).second;
            first = simplifyExpression(first);
            second = simplifyExpression(second);

            if (first.isBooleanLiteral() && second.isBooleanLiteral()) {
                boolean firstBoolean = first.getBoolean();
                boolean secondBoolean = second.getBoolean();
                if (((BinaryOps) expr).operator.isKind(Kind.OR) || ((BinaryOps) expr).operator.isKind(Kind.BOR)) {
                    return new Literal(firstBoolean || secondBoolean);
                } else if (((BinaryOps) expr).operator.isKind(Kind.AND) ||
                           ((BinaryOps) expr).operator.isKind(Kind.BAND)) {
                    return new Literal(firstBoolean && secondBoolean);
                }
            } else if (first.isIntegerLiteral() && second.isIntegerLiteral()) {
                int firstInteger = first.getInteger();
                int secondInteger = second.getInteger();

                if (((BinaryOps) expr).operator.isKind(Kind.EQ)) {
                    return new Literal(firstInteger == secondInteger);
                } else if (((BinaryOps) expr).operator.isKind(Kind.NE)) {
                    return new Literal(firstInteger != secondInteger);
                } else if (((BinaryOps) expr).operator.isKind(Kind.LT)) {
                    return new Literal(firstInteger < secondInteger);
                } else if (((BinaryOps) expr).operator.isKind(Kind.GT)) {
                    return new Literal(firstInteger > secondInteger);
                } else if (((BinaryOps) expr).operator.isKind(Kind.LE)) {
                    return new Literal(firstInteger <= secondInteger);
                } else if (((BinaryOps) expr).operator.isKind(Kind.GE)) {
                    return new Literal(firstInteger >= secondInteger);
                } else if (((BinaryOps) expr).operator.isKind(Kind.PLUS)) {
                    return new Literal(firstInteger + secondInteger);
                } else if (((BinaryOps) expr).operator.isKind(Kind.MINUS)) {
                    return new Literal(firstInteger - secondInteger);
                } else if (((BinaryOps) expr).operator.isKind(Kind.STAR)) {
                    return new Literal(firstInteger * secondInteger);
                } else if (((BinaryOps) expr).operator.isKind(Kind.SLASH)) {
                    return new Literal(firstInteger / secondInteger);
                } else if (((BinaryOps) expr).operator.isKind(Kind.MOD)) {
                    return new Literal(firstInteger % secondInteger);
                }
            }
        } else if (expr instanceof UnaryExpression) {
            Expression e = simplifyExpression(((UnaryExpression) expr).expr);
            if (e.isBooleanLiteral()) {
                boolean eBoolean = e.getBoolean();
                if (((UnaryExpression) expr).operator.isKind(Kind.NOT)) {
                    return new Literal(!eBoolean);
                }
            } else if (e.isIntegerLiteral()) {
                int eInt = e.getInteger();
                if (((UnaryExpression) expr).operator.isKind(Kind.MINUS)) {
                    return new Literal(-eInt);
                }
            }

        } else if (expr instanceof ParenthesizedExpression) {
            expr = simplifyExpression(((ParenthesizedExpression) expr).expr);
        }
        return expr;
    }
}
