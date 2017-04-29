package environment.symbol;

import ast.FormalParameter;

/**
 * Created by daiweifan on 2017-03-14.
 */
public class FormalParameterSymbol extends Symbol {
    public FormalParameterSymbol(FormalParameter declaration) {
        super(declaration.id, declaration);
    }

    public FormalParameter getDeclaration() {
        return (FormalParameter) this.declaration;
    }
}
