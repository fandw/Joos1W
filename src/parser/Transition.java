package parser;

import common.Token;

/**
 * Created by daiweifan on 2017-05-20.
 */
public class Transition {

    public enum Action {
        SHIFT,
        REDUCE
    }

    private int fromState;
    private String tokenType;
    private Action action;
    private int actionNum;

    public Transition(int fromState, String tokenType, String action, int actionNum) {
        this.fromState = fromState;
        this.tokenType = tokenType;
        this.actionNum = actionNum;
        this.action = action.equals(Action.SHIFT.toString()) ? Action.SHIFT : Action.REDUCE;
    }

    public int get_fromState() {
        return fromState;
    }

    public String get_tokenType() {
        return tokenType;
    }

    public Action get_action() {
        return action;
    }

    public int get_actionNum() {
        return actionNum;
    }
}
