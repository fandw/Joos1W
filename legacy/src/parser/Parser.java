package parser;

/**
 * Created by daiweifan on 2017-02-07.
 */

import exception.InvalidSyntaxException;
import scanner.TerminalToken;
import utils.Pair;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;


public class Parser {

    private static ArrayList<Rule> rules = new ArrayList<>();
    private static Transitions trans = new Transitions();
    private static ArrayList<Rule> steps = new ArrayList<>();
    private static Stack<String> symbols = new Stack<>();
    private static Stack<Integer> states = new Stack<>();
    private static String start = "";
    private static HashSet<String> nonTerms = new HashSet<>();
    private static HashSet<String> terminals = new HashSet<>();
    private static int rIndex = 0;

    public Tree parseTree;

    private static void fillConstants() {
        InputStream copyIn = System.in;
        try {
            System.setIn(new BufferedInputStream(new FileInputStream("jlalr/JlalrOutput")));
            Scanner in = new Scanner(System.in);

            int numOfTerminal = Integer.parseInt(in.nextLine());
            for (int i = 0; i < numOfTerminal; i++) {
                String s = in.nextLine();
                terminals.add(s);
                nonTerms.add(s);
            }

            int numOfNonTerminal = Integer.parseInt(in.nextLine());
            for (int i = 0; i < numOfNonTerminal; i++) {
                nonTerms.add(in.nextLine());
            }

            start = in.nextLine();

            int numOfRules = Integer.parseInt(in.nextLine());
            for (int i = 0; i < numOfRules; i++) {
                String[] s = in.nextLine().split(" ");
                Rule r = new Rule(new Token(s[0], terminals.contains(s[0])));
                for (int j = 1; j < s.length; j++) {
                    r.addRHS(new RHSToken(new Token(s[j], terminals.contains(s[j]))));
                }
                rules.add(r);
            }

            in.nextLine();
            int numOfTransitions = Integer.parseInt(in.nextLine());
            for (int i = 0; i < numOfTransitions; i++) {
                trans.add(in.nextLine().split(" "));
            }

            System.setIn(copyIn);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static boolean testSeq(ArrayList<TerminalToken> tokenList) throws InvalidSyntaxException {
        int state = 0;
        int counter = 0;
        for (TerminalToken token : tokenList) {
            //read in the next symbol
            state = process(state, token);
            if (state == -1) {
                throw new InvalidSyntaxException("ERROR at token " + (counter + 1) + ": " + token.getLexeme());
            }
            Rule r = new Rule(token);
            steps.add(r);
            counter++;
        }
        if (checkIdentical()) {
            return true;
        } else {
            throw new InvalidSyntaxException("Parsing did not converge to Goal symbol");
        }
    }

    private static int process(int state, Token token) {
        while (true) {
            String[] transition = trans.findTransition(Integer.toString(state), token.getText());
            if (transition != null) {
                if (transition[2].equals("shift")) {
                    symbols.push(token.getText());
                    states.push(Integer.parseInt(transition[3]));
                    return Integer.parseInt(transition[3]);
                } else if (transition[2].equals("reduce")) {
                    state = reduce(Integer.parseInt(transition[3]));
                }
            } else {
                return -1;
            }
        }
    }

    private static int reduce(int ruleNum) {
        Rule rule = rules.get(ruleNum);
        for (int i = 0; i < rule.getRHS().size(); i++) {
            symbols.pop();
            states.pop();
        }
        steps.add(rule);
        return process(states.peek(), rule.lhs);
    }

    private static boolean checkIdentical() {
        for (Rule rule : rules) {
            if (rule.lhs.equals(start)) {
                ArrayList<String> rhs = rule.getRHS();
                if (rhs.size() != symbols.size()) {
                    return false;
                }
                for (int j = rhs.size() - 1; j >= 0; j--) {
                    if (!rhs.get(j).equals(symbols.pop())) {
                        return false;
                    }
                }
                steps.add(rule);
                return true;
            }
        }
        // You should not be here
        return false;
    }

    public void parse(ArrayList<TerminalToken> tokenList) throws InvalidSyntaxException {
        fillConstants();

        states.push(0);

        if (testSeq(tokenList)) {
            parseTree = lrdo();
        } else {
            System.exit(1);
        }
    }

    private Tree lrdo() {
        Stack<Tree> stack = new Stack<>();
        Token l; // lhs symbol
        do {
            Rule f = steps.get(rIndex);
            rIndex++;
            List<String> r = new ArrayList<>(); // rhs symbols

            l = f.lhs;
            for (String s : f.getRHS()) {
                if (nonTerms.contains(s)) {
                    if (!l.equals("ID")) {
                        r.add(s); // only non-terminals
                    }
                }
            }
            popper(stack, r, f); // reduce rule
        } while (!l.equals(start));
        return stack.peek();
    }

    private void popper(Stack<Tree> stack, List<String> rhs, Rule rule) {
        Tree n = new Tree();
        n.rule = rule;
        for (String s : rhs) {
            n.children.addFirst(stack.pop());
        }
        stack.push(n);
    }

    static class Transitions {
        HashMap<String, HashMap<String, Pair<String, String>>> collection = new HashMap<>();

        void add(String[] transition) {
            if (collection.containsKey(transition[0])) {
                if (!collection.get(transition[0]).containsKey(transition[1])) {
                    Pair<String, String> p = new Pair<>(transition[2], transition[3]);
                    collection.get(transition[0]).put(transition[1], p);
                }
            } else {
                Pair<String, String> p = new Pair<>(transition[2], transition[3]);
                HashMap<String, Pair<String, String>> m = new HashMap<>();
                m.put(transition[1], p);
                collection.put(transition[0], m);
            }
        }

        String[] findTransition(String fromState, String token) {
            if (collection.containsKey(fromState)) {
                if (collection.get(fromState).containsKey(token)) {
                    return new String[] {
                            fromState,
                            token,
                            collection.get(fromState).get(token).getO1(),
                            collection.get(fromState).get(token).getO2()
                    };
                }
            }
            return null;
        }
    }

}

