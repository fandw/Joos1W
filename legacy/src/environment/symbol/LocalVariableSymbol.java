package environment.symbol;

import ast.LocalVariableDeclaration;

/**
 * Created by Da on 2017/03/14.
 */
public class LocalVariableSymbol extends Symbol {
    public LocalVariableSymbol(LocalVariableDeclaration declaration) {
        super(declaration.id, declaration);
    }

    public LocalVariableDeclaration getDeclaration() {
        return (LocalVariableDeclaration) this.declaration;
    }
}
