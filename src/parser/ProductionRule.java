package parser;

import common.NonTerminal;
import common.ProductionElement;
import grammar.RHSElement;

import java.util.ArrayList;

/**
 * Created by daiweifan on 2017-05-18.
 */
public class ProductionRule {
    private NonTerminal lhs;
    private ArrayList<RHSElement> rhsElements = new ArrayList<>();

    public ProductionRule(String lhs) {
        this.lhs = new NonTerminal(lhs);
    }

    public void addRHS(RHSElement rhsElement) {
        rhsElements.add(rhsElement);
    }

    public NonTerminal getLhs() {
        return lhs;
    }

    public ArrayList<RHSElement> getRhsElements() {
        return rhsElements;
    }
}
