package parser;

import java.util.LinkedList;

/**
 * Created by daiweifan on 2017-02-28.
 */
public class Tree {

    Rule rule;
    LinkedList<Tree> children = new LinkedList<>();

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Tree getChild(int n) {
        return children.get(n);
    }

    public void setChildren(LinkedList<Tree> children) {
        this.children = children;
    }

    public int getChildIndexByName(String s) {
        return getRule().hasRHS(s);
    }

    public Tree getChildByName(String s) {
        return getChild(getChildIndexByName(s));
    }
}
