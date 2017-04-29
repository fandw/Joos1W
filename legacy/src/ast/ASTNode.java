package ast;

import ast.util.ASTNodeInterface;
import environment.ASTVisitor;
import environment.symbol.NodeSymbolTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daiweifan on 2017-02-27.
 */
public abstract class ASTNode implements ASTNodeInterface {

    private static int nodeCount = 0;
    public int uniqueId;

    public ASTNodeInterface parent;
    public List<ASTNodeInterface> children = new ArrayList<>();
    public NodeSymbolTable symbolTable = null;

    public ASTNode() {
        this.uniqueId = nodeCount;
        nodeCount++;
    }

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

    public abstract void acceptVisitor(ASTVisitor visitor);

    public void visitChildren(ASTVisitor visitor) {
        for (ASTNodeInterface child : children) {
            child.acceptVisitor(visitor);
        }
    }

    public NodeSymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public String getLabel() {
        return this.getClass().getSimpleName() + "@" + uniqueId;
    }
}
