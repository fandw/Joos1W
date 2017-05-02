package scan;

/**
 * Created by daiweifan on 2017-05-01.
 */
class Transition {
    State fromState;
    CharSet chars;
    State toState;

    Transition(State fromState, CharSet chars, State toState) {
        this.fromState = fromState;
        this.chars = chars;
        this.toState = toState;
    }
}
