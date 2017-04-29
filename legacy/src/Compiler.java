import ast.AST;
import codegen.CodeGenerator;
import environment.*;
import exception.CodeGenerationException;
import exception.CompilerException;
import parser.Parser;
import scanner.Tokenizer;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class Compiler {

    public static void main(String args[]) {

        try {
            AST[] asts = new AST[args.length];

            for (int i = 0; i < args.length; i++) {
                String filePath = args[i];
                System.setIn(new BufferedInputStream(new FileInputStream(filePath)));

                Tokenizer tokenizer = new Tokenizer();
                tokenizer.run();

                Parser parser = new Parser();
                parser.parse(tokenizer.tokenList);

                AST ast = new AST(parser.parseTree);

                String[] fileNameBreakDown = filePath.split("/");
                String fileName = fileNameBreakDown[fileNameBreakDown.length - 1];
                Weeder weeder = new Weeder(fileName);
                weeder.visit(ast);

//                StaticAnalyzer staticAnalyzer = new StaticAnalyzer();
//                staticAnalyzer.visit(ast);

                asts[i] = ast;
            }

            EnvironmentBuilder environmentBuilder = new EnvironmentBuilder(asts);
            for (int i = 0; i < args.length; i++) {
                environmentBuilder.visit(asts[i]);
            }

            TypeLinker typeLinker = new TypeLinker(environmentBuilder.rootPackageSymbol);
            for (int i = 0; i < args.length; i++) {
                typeLinker.visit(asts[i]);
            }

            HierarchyChecker hierarchyChecker = new HierarchyChecker(environmentBuilder.javaLangObjectSymbol);
            for (int i = 0; i < args.length; i++) {
                hierarchyChecker.visit(asts[i]);
            }

            CodeGenerator codeGenerator = new CodeGenerator();
            for (int i = 0; i < args.length; i++) {
                codeGenerator.visit(asts[i]);
            }
            codeGenerator.generateStartFile();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(2);
        } catch (CompilerException e) {
            e.printErrorMessage();
            System.exit(42);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.exit(0);

    }

}
