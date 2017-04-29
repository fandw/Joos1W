package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by Daiwei on 2017-03-08.
 */
public class FormalParameter extends ASTNode {

    public Type type;
    public TokenNode id;

    public FormalParameter(Tree parseTree) {
        super();

        type = Type.interpretType(parseTree.getChild(0));
        id = new TokenNode(parseTree.getChild(1));
        addChild(type);
        addChild(id);
    }

    public static RecursedProductionList<FormalParameter> recurseFormalParameterList(Tree parseTree) {
        return new RecursedProductionList<FormalParameter>(parseTree,
                                                           "FormalParameterList FormalParameterList COMMA FormalParameter",
                                                           true, false) {
            @Override
            public FormalParameter spawn(Tree parseTree) {
                return new FormalParameter(parseTree);
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
        FormalParameter formalParameter;
        if (obj instanceof FormalParameter) {
            formalParameter = (FormalParameter) obj;
            return this.type.equals(formalParameter.type) && this.id.equals(formalParameter.id);
        }
        return false;
    }

    public boolean equalsByType(Object obj) {
        if (this == obj) {
            return true;
        }
        FormalParameter formalParameter;
        if (obj instanceof FormalParameter) {
            formalParameter = (FormalParameter) obj;
            return this.type.equals(formalParameter.type);
        }
        return false;
    }
}
