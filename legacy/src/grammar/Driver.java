package grammar;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * Created by daiweifan on 2017-02-07.
 */
public class Driver {



    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try {
            System.setIn(new BufferedInputStream(new FileInputStream("src/grammar/ProductionInput")));
            System.setOut(new PrintStream("src/grammar/joos1w.cfg"));
            ParseProductions.main(args);
            System.setIn(new BufferedInputStream(new FileInputStream("src/grammar/joos1w.cfg")));
            System.setOut(new PrintStream("output/JlalrOutput"));
            Jlalr1.main(args);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
