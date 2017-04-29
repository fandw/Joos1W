package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by Daiwei on 2017-03-08.
 */
public class MethodBody extends ASTNode {

    public Block block;

    public MethodBody(Tree parseTree) {
        super();

        int blockIndex = parseTree.getChildIndexByName("Block");
        if (blockIndex != -1) {
            block = new Block(parseTree.getChild(blockIndex));
            addChild(block);
        } else {
            block = null;
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
