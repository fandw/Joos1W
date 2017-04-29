package ast;

import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public abstract class Type extends ASTNode {

    public Type() {
        super();
    }

    public static Type interpretType(Tree parseTree) {
        if (parseTree.getRule().equals("Type PrimitiveType")) {
            return new PrimitiveType(parseTree.getChild(0));
        }

        if (parseTree.getRule().equals("Type ReferenceType")) {
            return interpretType(parseTree.getChild(0));
        }

        if (parseTree.getRule().equals("ReferenceType Name")) {
            return new ClassType(new Name(parseTree.getChild(0)));
        }

        if (parseTree.getRule().equals("ReferenceType ArrayType")) {
            return new ArrayType(parseTree.getChild(0));
        }

        return null;
    }
}
