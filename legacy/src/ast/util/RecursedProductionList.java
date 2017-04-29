package ast.util;

import ast.ASTNode;
import environment.ASTVisitor;
import environment.symbol.NodeSymbolTable;
import parser.Tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by daiweifan on 2017-03-01.
 */
public abstract class RecursedProductionList<T extends ASTNode> extends ArrayList<T> implements ASTNodeInterface {

    public List<ASTNodeInterface> children = new ArrayList<>();
    public ASTNodeInterface parent;

    public static RecursedProductionList EMPTY_LIST = new RecursedProductionList() {
        @Override
        public ASTNode spawn(Tree parseTree) {
            return null;
        }
    };

    private RecursedProductionList() {}

    public RecursedProductionList(Tree parseTree, String spawnRule, boolean hasSeparator, boolean hasFirst) {
        handleRule(parseTree, spawnRule, hasSeparator, hasFirst);
    }

    public void handleRule(Tree parseTree, String spawnRule, boolean hasSeparator, boolean hasFirst) {
        if (parseTree.getRule().equals(spawnRule)) {
            handleRule(parseTree.getChild(0), spawnRule, hasSeparator, hasFirst);
            T child = spawn(parseTree.getChild(hasSeparator ? 2 : 1));
            child.addParent(this);
            this.add(child);
        } else {
            T child = spawn(parseTree.getChild(hasFirst ? 1 : 0));
            child.addParent(this);
            this.add(child);
        }
    }

    public abstract T spawn(Tree parseTree);

    public void addChild(ASTNodeInterface astNode) {
        astNode.addParent(this);
        children.add(astNode);
    }

    public void addParent(ASTNodeInterface astNode) {
        parent = astNode;
    }

    public ASTNodeInterface getParent() {
        return parent;
    }

    public NodeSymbolTable getSymbolTable() {
        return parent.getSymbolTable();
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitChildren(visitor);
    }

    public void visitChildren(ASTVisitor visitor) {
        for (ASTNodeInterface child : this) {
            child.acceptVisitor(visitor);
        }
    }
}
