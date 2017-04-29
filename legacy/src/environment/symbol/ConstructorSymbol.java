package environment.symbol;

import ast.ConstructorDeclaration;

/**
 * Created by daiweifan on 2017-03-14.
 */
public class ConstructorSymbol extends Symbol {

    public boolean isInherited = false;

    public ConstructorSymbol(ConstructorSymbol s, boolean isInherited) {
        super(s.getDeclaration().constructorDeclarator.id, s.getDeclaration());
        this.isInherited = isInherited;
    }

    public ConstructorSymbol(ConstructorDeclaration declaration) {
        super(declaration.constructorDeclarator.id, declaration);
    }

    public ConstructorDeclaration getDeclaration() {
        return (ConstructorDeclaration) this.declaration;
    }
}
