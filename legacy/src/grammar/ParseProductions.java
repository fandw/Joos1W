package grammar;

import parser.RHSToken;
import parser.Rule;
import parser.Token;

import java.io.*;
import java.util.*;

/**
 * Created by daiweifan on 2017-01-25.
 */
public class ParseProductions {

    private static final String COLON = ":";
    private static final String OPT = "opt";
    private static final String OF = "of";
    private static final String ONE = "one";

    private ArrayList<Rule> rules = new ArrayList<>();
    private HashSet<String> terminal = new HashSet<>();
    private HashSet<String> nonTerminal = new HashSet<>();
    private static HashMap<String, String> setTerminals = new HashMap<>();

    public static void main(String args[]) {
        getTerminals();
        ParseProductions engine = new ParseProductions();
        engine.run();
        engine.header();
        engine.count();
        engine.iterate();

    }

    private static void getTerminals() {
        InputStream copyIn = System.in;
        try {
            System.setIn(new BufferedInputStream(new FileInputStream("src/grammar/TokensInput")));
            Scanner in = new Scanner(System.in);

            while (in.hasNextLine()) {
                String[] tuple = in.nextLine().split(" ");
                if (tuple.length == 1) {
                    setTerminals.put(tuple[0], tuple[0]);
                } else {
                    setTerminals.put(tuple[1], tuple[0]);
                }
            }

            System.setIn(copyIn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void run() {

        Scanner in = new Scanner(System.in);
        in.useDelimiter("\\s");
        boolean one_of = false;
        boolean first_token = false;
        Rule newRule = null;
        String lhs = "";
        in.delimiter();
        while (in.hasNext()) {
            String token = in.next();
            if (token.trim().isEmpty()) {
                if (!first_token) {
                    first_token = true;
                }
                continue;
            }
            if (token.endsWith(COLON) && !token.equals(COLON)) {
                one_of = false;
                first_token = true;

                lhs = token.substring(0, token.length() - 1);
                assert (!lhs.toLowerCase().equals(lhs));
                nonTerminal.add(lhs);

            } else if (token.equals(ONE)) {
                token = in.next();
                assert (token.equals(OF));
                one_of = true;
            } else {
                boolean optional_token = false;
                if (token.endsWith(OPT)) {
                    optional_token = true;
                }

                String tokenText = optional_token ? token.substring(0, token.length() - 3) : token;
                RHSToken rhsToken = new RHSToken(new Token(tokenText, false), optional_token);
                if (setTerminals.containsKey(rhsToken.token.getText())) {
                    rhsToken.token.setText(setTerminals.get(rhsToken.token.getText()));
                    terminal.add(rhsToken.token.getText());
                } else if (rhsToken.token.equals(rhsToken.token.getText().toLowerCase())) {
                    terminal.add(rhsToken.token.getText());
                } else {
                    nonTerminal.add(rhsToken.token.getText());
                }

                if (one_of) {
                    newRule = new Rule(new Token(lhs, false));
                    rules.add(newRule);
                } else {
                    if (first_token) {
                        newRule = new Rule(new Token(lhs, false));
                        rules.add(newRule);
                        first_token = false;
                    }
                }
                assert newRule != null;
                newRule.addRHS(rhsToken);
            }
        }
        System.out.flush();
    }

    private void header() {
        System.out.println(terminal.size());
        for (String t : terminal) {
            System.out.println(t);
        }
        System.out.println(nonTerminal.size());
        for (String nt : nonTerminal) {
            System.out.println(nt);
        }
        System.out.println("Goal");
    }

    private void count() {
        int total = 0;
        for (Rule r : this.rules) {
            int count = 0;
            for (RHSToken rhs : r.rhs) {
                if (rhs.optional) count++;
            }
            total += Math.pow(2, count);
        }
        System.out.println(total);
    }

    private void iterate() {
        for (Rule r : this.rules) {
            node(r, 0);
        }
    }

    private void node(Rule r, int rhsIndex) {
        if (rhsIndex == r.rhs.size()) {
            print(r);
            return;
        }
        if (r.rhs.get(rhsIndex).optional) {
            r.rhs.get(rhsIndex).print = false;
            node(r, rhsIndex + 1);
            r.rhs.get(rhsIndex).print = true;
            node(r, rhsIndex + 1);
        } else {
            r.rhs.get(rhsIndex).print = true;
            node(r, rhsIndex + 1);
        }

    }

    private void print(Rule r) {
        System.out.print(r.lhs.getText() + " ");
        for (RHSToken rhsToken : r.rhs) {
            if (rhsToken.print) {
                System.out.print(rhsToken.token.getText() + " ");
            }
        }
        System.out.println();
    }

}
