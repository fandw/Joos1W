package ast;

import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class ArrayType extends Type {
    public Type type;

    public ArrayType(Tree parseTree) {
        super();

        if (parseTree.getRule().equals("ArrayType PrimitiveType LBRACK RBRACK")) {
            type = new PrimitiveType(parseTree.getChild(0));
        }
        if (parseTree.getRule().equals("ArrayType Name LBRACK RBRACK")) {
            type = new ClassType(new Name(parseTree.getChild(0)));
        }
        addChild(type);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        ArrayType arrayType;
        if (obj instanceof ArrayType) {
            arrayType = (ArrayType) obj;
            return arrayType.type.equals(this.type);
        }
        return false;
    }
}
