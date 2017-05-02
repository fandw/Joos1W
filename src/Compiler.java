import common.Token;
import exception.CompilerException;
import scan.Tokenizer;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by daiweifan on 2017-04-28.
 */
public class Compiler {
    public static void main(String args[]) {
        try {

            for (String filePath : args) {
                List<Token> tokens = Tokenizer.scan(filePath);
                System.out.println("Tokenizer finished.");

//                Parser parser = new Parser();
//                parser.parse(tokenizer.tokenList);
//
//                AST ast = new AST(parser.parseTree);
//
//                String[] fileNameBreakDown = filePath.split("/");
//                String fileName = fileNameBreakDown[fileNameBreakDown.length - 1];
//                Weeder weeder = new Weeder(fileName);
//                weeder.visit(ast);
//
//                asts[i] = ast;
            }


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
