package parser;

import common.Token;
import common.TokenType;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by daiweifan on 2017-05-20.
 */
public class TransitionSet {
    private static TransitionSet transitionSet;
    private HashMap<Integer, HashMap<String, Transition>> collection = new HashMap<>();

    private TransitionSet() {}

    private void add(int fromState, String tokenType, String action, int actionNum) {
        if (!collection.containsKey(fromState)) collection.put(fromState, new HashMap<>());
        HashMap<String, Transition> subTransition = collection.get(fromState);
        if (!subTransition.containsKey(tokenType)) {
            subTransition.put(tokenType, new Transition(fromState, tokenType, action, actionNum));
        }
    }

    Transition find_transition(int fromState, Token token) {
        if (!collection.containsKey(fromState)) return null;
        if (!collection.get(fromState).containsKey(token.getType().toString())) return null;
        return collection.get(fromState).get(token.getType().toString());
    }

    static TransitionSet get_transition_set(Scanner in) {
        if (transitionSet != null) return transitionSet;
        transitionSet = new TransitionSet();
        int numOfTransitions = Integer.parseInt(in.nextLine());
        for (int i = 0; i < numOfTransitions; i++) {
            String[] transitionElements = in.nextLine().split(" ");
            transitionSet.add(Integer.parseInt(transitionElements[0]), transitionElements[1],
                    transitionElements[2], Integer.parseInt(transitionElements[3]));
        }
        return transitionSet;
    }
}
