package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

import java.util.Iterator;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class Name extends ASTNode {

    public RecursedProductionList<TokenNode> ids;

    public Name(Tree parseTree) {
        super();

        ids = new RecursedProductionList<TokenNode>(parseTree, "Name Name DOT ID", true, false) {
            @Override
            public TokenNode spawn(Tree parseTree) {
                return new TokenNode(parseTree);
            }
        };
        addChild(ids);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        Name name;
        if (obj instanceof Name) {
            name = (Name) obj;
            Iterator<TokenNode> thisIterator = this.ids.listIterator();
            Iterator<TokenNode> objIterator = name.ids.listIterator();
            while (thisIterator.hasNext()) {
                TokenNode thisId = thisIterator.next();
                if (!objIterator.hasNext()) {
                    return false;
                }
                TokenNode objId = objIterator.next();
                if (!thisId.equals(objId)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
