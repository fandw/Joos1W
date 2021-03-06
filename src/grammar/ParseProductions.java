package grammar;

import parser.ProductionRule;

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

    private ArrayList<ProductionRule> rules = new ArrayList<>();
    private HashSet<String> terminal = new HashSet<>();
    private HashSet<String> nonTerminal = new HashSet<>();
    private static HashMap<String, String> setTerminals = new HashMap<>();

    public static void main(String args[]) {
        get_terminals();
        ParseProductions engine = new ParseProductions();
        engine.run();
        engine.header();
        engine.count();
        engine.iterate();

    }

    private static void get_terminals() {
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
        ProductionRule newRule = null;
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
                RHSElement rhsElement = new RHSElement(tokenText, optional_token);
                if (setTerminals.containsKey(tokenText)) {
                    rhsElement.set_RHS(setTerminals.get(rhsElement.get_RHS()));
                    terminal.add(rhsElement.get_RHS());
                } else if (rhsElement.get_RHS().equals(rhsElement.get_RHS().toLowerCase())) {
                    terminal.add(rhsElement.get_RHS());
                } else {
                    nonTerminal.add(rhsElement.get_RHS());
                }

                if (one_of) {
                    newRule = new ProductionRule(lhs);
                    rules.add(newRule);
                } else {
                    if (first_token) {
                        newRule = new ProductionRule(lhs);
                        rules.add(newRule);
                        first_token = false;
                    }
                }
                assert newRule != null;
                newRule.add_RHS(rhsElement);
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
        for (ProductionRule r : this.rules) {
            int count = 0;
            for (RHSElement rhs : r.get_RHS_elements()) {
                if (rhs.optional) count++;
            }
            total += Math.pow(2, count);
        }
        System.out.println(total);
    }

    private void iterate() {
        for (ProductionRule r : this.rules) {
            node(r, 0);
        }
    }

    private void node(ProductionRule r, int rhsIndex) {
        if (rhsIndex == r.get_RHS_elements().size()) {
            print(r);
            return;
        }
        if (r.get_RHS_elements().get(rhsIndex).optional) {
            r.get_RHS_elements().get(rhsIndex).print = false;
            node(r, rhsIndex + 1);
            r.get_RHS_elements().get(rhsIndex).print = true;
            node(r, rhsIndex + 1);
        } else {
            r.get_RHS_elements().get(rhsIndex).print = true;
            node(r, rhsIndex + 1);
        }

    }

    private void print(ProductionRule r) {
        System.out.print(r.get_LHS() + " ");
        for (RHSElement rhsElement : r.get_RHS_elements()) {
            if (rhsElement.print) {
                System.out.print(rhsElement.get_RHS() + " ");
            }
        }
        System.out.println();
    }

}
