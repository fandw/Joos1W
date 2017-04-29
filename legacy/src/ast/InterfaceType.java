package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import environment.symbol.InterfaceSymbol;
import parser.Tree;

public class InterfaceType extends Type {
    public Name name;
    public InterfaceSymbol symbol = null;

    public InterfaceType(Tree parseTree) {
        super();

        this.name = new Name(parseTree.getChild(0));
        addChild(name);
    }

    public static RecursedProductionList<InterfaceType> translateInterfaces(Tree parseTree, String rule,
                                                                            boolean hasFirst) {
        return new RecursedProductionList<InterfaceType>(parseTree, rule, true, hasFirst) {
            @Override
            public InterfaceType spawn(Tree parseTree) {
                return new InterfaceType(parseTree);
            }
        };
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        InterfaceType interfaceType;
        if (obj instanceof InterfaceType) {
            interfaceType = (InterfaceType) obj;
            if (this.symbol == null || interfaceType.symbol == null) {
                return interfaceType.name.equals(this.name);
            }
            return interfaceType.symbol.equals(this.symbol);
        }
        return false;
    }
}
