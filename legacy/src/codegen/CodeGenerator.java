package codegen;

import ast.*;
import environment.ASTVisitor;
import exception.CodeGenerationException;
import utils.ModifiersChecker;

import java.io.FileNotFoundException;

/**
 * Created by Da on 2017/04/02.
 */
public class CodeGenerator implements ASTVisitor {

    private NASMWriter write;
    private String mainMethodLabel = null;

    public enum Register {
        EAX, EBX, ECX, EDX, EDI, ESI, EBP, ESP, EIP, EFLAGS,
    }

    public enum Instruction {

        // Data Transfer
        MOV, PUSH, POP, XCHG, LEA,

        // Control Flow
        CMP, JMP, JE, JNE, JG, JGE, JA, JAE, JL, JLE, JB, JBE, JO, JNO, JZ, JNZ, CALL, RET,

        // Arithmetic
        ADD, SUB, IMUL, DIV, IDIV, NEG,

        // Logic
        AND, OR, XOR, NOT,
    }

    public enum DataType {
        DB, DW, DD, DQ, DT,
    }

    public CodeGenerator() throws CodeGenerationException {
    }

    public void visit(ArrayAccess node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(ArrayCreationExpression node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(ArrayType node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(Assignment node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(AST ast) throws CodeGenerationException {
        visit(ast.compilationUnit);
    }

    public void visit(BinaryOps node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(Block node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(BlockStatement node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(CastExpression node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(ClassBodyDeclaration node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(ClassDeclaration node) throws CodeGenerationException {
        write.println("section .text");
        node.visitChildren(this);
    }

    public void visit(ClassInstanceCreationExpression node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(ClassType node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(CompilationUnit node) throws CodeGenerationException {
        if (node.typeDeclaration != null) {
            String filePath = "./output/";
            if (node.packageDeclaration != null) {
                for (TokenNode id : node.packageDeclaration.name.ids) {
                    filePath += id + ".";
                }
            }
            filePath += node.typeDeclaration.id + ".s";
            try {
                write = new NASMWriter(filePath);
            } catch (FileNotFoundException e) {
                throw new CodeGenerationException("Error writing to file: " + filePath);
            }

            node.visitChildren(this);
            write.close();
        }
    }

    public void visit(ConstructorDeclaration node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(ConstructorDeclarator node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(EmptyStatement node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(ExpressionStatement node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(FieldAccess node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(FieldDeclaration node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(FormalParameter node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(ForStatement node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(IfStatement node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(ImportDeclaration node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(InstanceOf node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(InterfaceDeclaration node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(InterfaceType node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(Literal node) throws CodeGenerationException {
        if (node.isIntegerLiteral()) {
            write.instruction(Instruction.MOV, Register.EAX, node.getInteger());
        }
    }

    public void visit(LocalMethodInvocation node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(LocalVariableDeclaration node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(MethodBody node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(MethodDeclaration node) throws CodeGenerationException {
        write.label(node.getLabel());
        ModifiersChecker modifiers = new ModifiersChecker(node.methodHeader.modifiers);
        if (!modifiers.isAbstract) {

            if (node.methodHeader.id.token.getLexeme().equals("test") && modifiers.isStatic &&
                node.methodHeader.formalParameters == null &&
                node.methodHeader.returnType instanceof PrimitiveType) {
                mainMethodLabel = node.getLabel();
            }

            write.instruction(Instruction.PUSH, Register.EBP);
            write.instruction(Instruction.MOV, Register.EBP, Register.ESP);
            node.visitChildren(this);
        }
    }

    public void visit(MethodHeader node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(Modifier node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(Name node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(NameExpression node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(OtherArrayAccess node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(OtherMethodInvocation node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(PackageDeclaration node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(ParenthesizedExpression node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(PrimitiveType node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(ReturnStatement node) throws CodeGenerationException {
        node.visitChildren(this);
        write.instruction(Instruction.MOV, Register.ESP, Register.EBP);
        write.instruction(Instruction.POP, Register.EBP);
        write.instruction(Instruction.RET);
    }

    public void visit(ThisExpression node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(TokenNode node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(UnaryExpression node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(VariableDeclarator node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void visit(WhileStatement node) throws CodeGenerationException {
        node.visitChildren(this);
    }

    public void generateStartFile() {
        try {
            write = new NASMWriter("./output/_start.s");
        } catch (FileNotFoundException e) {
            throw new CodeGenerationException("Error writing to _start.s");
        }

        write.label("_start");

        // Call main method
        write.extern(mainMethodLabel);
        write.call(mainMethodLabel);

        // Exit normally
        write.exit();

        write.close();
    }
}
