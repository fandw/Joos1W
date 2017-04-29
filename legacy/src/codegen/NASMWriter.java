package codegen;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import codegen.CodeGenerator.Instruction;
import codegen.CodeGenerator.Register;

/**
 * Created by Da on 2017/04/02.
 */
public class NASMWriter extends PrintStream {

    private static String DEBEXIT = "__debexit";
    private static String EXCEPTION = "__exception";
    private static String MALLOC = "__malloc";

    public NASMWriter(String filePath) throws FileNotFoundException {
        super(filePath);
    }

    public void instruction(Instruction instruction) {
        println(instruction);
    }

    public void instruction(Instruction instruction, Object arg) {
        print(instruction);
        print(" ");
        println(arg);
    }

    public void instruction(Instruction instruction, Object dest, Object src) {
        print(instruction);
        print(" ");
        print(dest);
        print(", ");
        println(src);
    }

    public void instruction(Instruction instruction, Object dest, Object src, Object aux) {
        print(instruction);
        print(" ");
        print(dest);
        print(", ");
        print(src);
        print(", ");
        println(aux);
    }

    public void comment(String comment) {
        println("; " + comment);
    }

    public void label(String label, boolean isGlobal) {
        if (isGlobal) println("global " + label);
        println(label + ":");
    }

    public void label(String label) {
        label(label, true);
    }

    public void extern(String label) {
        println("EXTERN " + label);
    }

    public void call(String label) {
        println(Instruction.CALL + " " + label);
    }

    // EAX <- number of bytes to allocate
    public void malloc() {
        extern(MALLOC);
        call(MALLOC);
    }

    public void exception() {
        extern(EXCEPTION);
        call(EXCEPTION);
    }

    public void exit() {
        extern(DEBEXIT);
        call(DEBEXIT);
    }

    public void nativeMethod(String fullName) {
        extern("NATIVE" + fullName);
        call("NATIVE" + fullName);
    }
}
