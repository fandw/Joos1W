package environment.symbol;

import ast.TokenNode;
import ast.util.ASTNodeInterface;

/**
 * Created by daiweifan on 2017-03-10.
 */
public abstract class Symbol {
    protected TokenNode id;
    protected ASTNodeInterface declaration;

    public Symbol() {
        this.id = null;
        this.declaration = null;
    }

    public Symbol(TokenNode id, ASTNodeInterface declaration) {
        this.id = id;
        this.declaration = declaration;
    }

    abstract ASTNodeInterface getDeclaration();

    public TokenNode getId() {
        return this.id;
    }

    public boolean equalsById(TokenNode id) {
        return this.id.equals(id);
    }

    @Override
    public boolean equals(Object obj) {
        return this == obj || obj instanceof Symbol && ((Symbol) obj).equalsById(this.id) &&
                              ((Symbol) obj).declaration == this.declaration;
    }
}
