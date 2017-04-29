package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class Block extends Statement {

    public RecursedProductionList<Statement> blockStatements;

    public Block(Tree parseTree) {
        super();

        if (parseTree.getChildIndexByName("BlockStatements") != -1) {
            blockStatements = BlockStatement.traverseBlockStatement(parseTree.getChild(1));
            addChild(blockStatements);
        } else {
            blockStatements = null;
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
