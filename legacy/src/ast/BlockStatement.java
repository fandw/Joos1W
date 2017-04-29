package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by Daiwei on 2017-03-08.
 */
public class BlockStatement extends Statement {

    public BlockStatement() {
        super();
    }

    public static Statement interpretBlockStatement(Tree parseTree) {
        if (parseTree.getRule().equals("BlockStatement LocalVariableDeclaration SEMI")) {
            return new LocalVariableDeclaration(parseTree.getChild(0));
        }
        if (parseTree.getRule().equals("BlockStatement Statement")) {
            return Statement.interpretStatement(parseTree.getChild(0));
        }
        return null;
    }

    public static RecursedProductionList<Statement> traverseBlockStatement(Tree parseTree) {

        return new RecursedProductionList<Statement>(parseTree, "BlockStatements BlockStatements BlockStatement", false,
                                                     false) {
            @Override
            public Statement spawn(Tree parseTree) {
                return BlockStatement.interpretBlockStatement(parseTree);
            }
        };
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
