package environment.symbol;

import ast.InterfaceDeclaration;

/**
 * Created by Da on 2017/03/14.
 */
public class InterfaceSymbol extends TypeSymbol {

    public InterfaceSymbol(InterfaceDeclaration declaration) {
        super(declaration);
    }

    public InterfaceSymbol(InterfaceSymbol symbol, TypeLinkingPriority typeLinkingPriority) {
        super(symbol, typeLinkingPriority);
    }

    public InterfaceDeclaration getDeclaration() {
        return (InterfaceDeclaration) this.declaration;
    }
}
