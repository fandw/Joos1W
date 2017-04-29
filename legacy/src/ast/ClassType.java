package ast;

import environment.ASTVisitor;
import environment.symbol.TypeSymbol;
import parser.Tree;

/**
 * Created by daiweifan on 2017-03-01.
 */
public class ClassType extends Type {
    public Name name;
    // Cannot use ClassSymbol here because we cannot know if the Type is a Class or an Interface in a LocalVariableDeclaration
    public TypeSymbol symbol = null;

    public ClassType(Name name) {
        super();

        this.name = name;
        addChild(name);
    }

    public ClassType(Tree parseTree) {
        super();

        name = new Name(parseTree.getChild(0));
        addChild(name);
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        ClassType classType;
        if (obj instanceof ClassType) {
            classType = (ClassType) obj;
            if (this.symbol == null || classType.symbol == null) {
                return classType.name.equals(this.name);
            }
            return classType.symbol.equals(this.symbol);
        }
        return false;
    }
}
