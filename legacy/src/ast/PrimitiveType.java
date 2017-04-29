package ast;

import environment.ASTVisitor;
import environment.symbol.PackageSymbol;
import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class PrimitiveType extends Type {
    public TokenNode token;

    public PrimitiveType(Tree parseTree) {
        super();

        if (parseTree.getRule().equals("PrimitiveType BOOLEAN")) {
            // PrimitiveType boolean
            token = new TokenNode(parseTree.getChild(0));
        } else {
            // PrimitiveType NumericType
            token = new TokenNode(parseTree.getChild(0).getChild(0));
        }
        addChild(token);

    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof PrimitiveType && ((PrimitiveType) obj).token.equals(this.token);
    }
}
