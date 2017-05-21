package parser;

import grammar.RHSElement;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Created by daiweifan on 2017-05-19.
 */
public class Parser {
    private static final String grammar_file_path = "output/JlalrOutput";
    private final HashSet<String> terminals = new HashSet<>();
    private final HashSet<String> nonTerminals = new HashSet<>();
    private final HashMap<String, ArrayList<ProductionRule>> productionRules = new HashMap<>();
    private TransitionSet transitionSet;

    private void load_grammar() throws FileNotFoundException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(grammar_file_path));
        Scanner in = new Scanner(inputStream);

        int numOfTerminals = Integer.parseInt(in.nextLine());
        for (int i = 0; i < numOfTerminals; i++) {
            terminals.add(in.nextLine());
        }

        int numOfNonTerminals = Integer.parseInt(in.nextLine());
        for (int i = 0; i < numOfNonTerminals; i++) {
            nonTerminals.add(in.nextLine());
        }

        int numOfProductionRules = Integer.parseInt(in.nextLine());
        for (int i = 0; i < numOfProductionRules; i++) {
            String[] ruleElements = in.nextLine().split(" ");
            if (!productionRules.containsKey(ruleElements[0])) {
                productionRules.put(ruleElements[0], new ArrayList<>());
            }

            ProductionRule newProductionRule = new ProductionRule(ruleElements[0]);
            for (int j = 1; j < ruleElements.length; j++) {
                newProductionRule.add_RHS(new RHSElement(ruleElements[j]));
            }

            productionRules.get(ruleElements[0]).add(newProductionRule);
        }

        // Skip the number of states
        in.nextLine();

        transitionSet = TransitionSet.get_transition_set(in);
    }
}
