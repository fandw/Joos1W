package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;

/**
 * Created by Daiwei on 2017-03-10.
 */
public class ConstructorDeclarator extends ASTNode {
    // ConstructorDeclarator identifier ( FormalParameterListopt )

    public TokenNode id;
    public RecursedProductionList<FormalParameter> formalParameters;

    public ConstructorDeclarator(Tree parseTree) {
        super();

        id = new TokenNode(parseTree.getChildByName("ID"));

        int formalIndex = parseTree.getChildIndexByName("FormalParameterList");
        if (formalIndex != -1) {
            formalParameters = FormalParameter.recurseFormalParameterList(parseTree.getChild(formalIndex));
            addChild(formalParameters);
        } else {
            formalParameters = null;
        }
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
