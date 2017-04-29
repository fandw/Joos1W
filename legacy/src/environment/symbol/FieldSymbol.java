package environment.symbol;

import ast.FieldDeclaration;

/**
 * Created by Da on 2017/03/14.
 */
public class FieldSymbol extends Symbol {

    public boolean isInherited = false;

    public FieldSymbol(FieldSymbol s, boolean isInherited) {
        super(s.getDeclaration().variableDeclarator.id, s.getDeclaration());
        this.isInherited = isInherited;
    }

    public FieldSymbol(FieldDeclaration declaration) {
        super(declaration.variableDeclarator.id, declaration);
    }

    public FieldDeclaration getDeclaration() {
        return (FieldDeclaration) this.declaration;
    }
}
