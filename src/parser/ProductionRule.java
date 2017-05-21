package parser;

import common.NonTerminal;
import common.ProductionElement;
import grammar.RHSElement;

import java.util.ArrayList;

/**
 * Created by daiweifan on 2017-05-18.
 */
public class ProductionRule {
    private String lhs;
    private ArrayList<RHSElement> rhsElements = new ArrayList<>();

    public ProductionRule(String lhs) {
        this.lhs = lhs;
    }

    public void add_RHS(RHSElement rhsElement) {
        rhsElements.add(rhsElement);
    }

    public String get_LHS() {
        return lhs;
    }

    public ArrayList<RHSElement> get_RHS_elements() {
        return rhsElements;
    }
}
