package parser;

import scanner.TerminalToken;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by daiweifan on 2017-02-28.
 */
public class Rule {
    public Token lhs;
    public ArrayList<RHSToken> rhs = new ArrayList<>();
    private String text;
    private int rhsCount;
    private HashMap<String, Integer> tokenSet = new HashMap<>();
    private TerminalToken ttoken;

    public Rule(Token lhs) {
        this.lhs = lhs;
        text = lhs.getText();
        rhsCount = 0;
    }

    public Rule(TerminalToken ttoken) {
        this.ttoken = ttoken;
        lhs = new Token(ttoken.getText(), true);
        rhs.add(new RHSToken(new Token(ttoken.getLexeme(), true)));
    }

    public void addRHS(RHSToken rhsToken) {
        rhs.add(rhsToken);
        tokenSet.put(rhsToken.token.getText(), rhsCount++);
        text += " " + rhsToken.token.getText();
    }

    public ArrayList<String> getRHS() {
        ArrayList<String> buffer = new ArrayList<>();
        for (RHSToken r : this.rhs) {
            buffer.add(r.token.getText());
        }
        return buffer;
    }

    public int hasRHS(String rhsToken) {
        if (tokenSet.containsKey(rhsToken)) {
            return tokenSet.get(rhsToken);
        } else {
            return -1;
        }
    }

    public boolean equals(String rule) {
        return text.equals(rule);
    }

    public boolean startsWith(String head) {
        return lhs.equals(head);
    }

    public TerminalToken getTtoken() {
        return this.ttoken;
    }
}
