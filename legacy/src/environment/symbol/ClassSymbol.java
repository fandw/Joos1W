package environment.symbol;

import ast.ClassDeclaration;

/**
 * Created by Da on 2017/03/14.
 */
public class ClassSymbol extends TypeSymbol {

    public ClassSymbol(ClassDeclaration declaration) {
        super(declaration);
    }

    public ClassSymbol(ClassSymbol symbol, TypeLinkingPriority typeLinkingPriority) {
        super(symbol, typeLinkingPriority);
    }

    public ClassDeclaration getDeclaration() {
        return (ClassDeclaration) this.declaration;
    }
}
