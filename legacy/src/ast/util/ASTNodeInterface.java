package ast.util;

import environment.ASTVisitor;
import environment.symbol.NodeSymbolTable;

/**
 * Created by daiweifan on 2017-03-06.
 */
public interface ASTNodeInterface {

    void addChild(ASTNodeInterface astNode);

    void addParent(ASTNodeInterface astNode);

    ASTNodeInterface getParent();

    void acceptVisitor(ASTVisitor visitor);

    void visitChildren(ASTVisitor visitor);

    NodeSymbolTable getSymbolTable();
}
