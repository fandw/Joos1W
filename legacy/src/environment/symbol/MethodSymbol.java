package environment.symbol;

import ast.MethodDeclaration;
import ast.MethodHeader;

/**
 * Created by Da on 2017/03/14.
 */
public class MethodSymbol extends Symbol {

    public boolean isInherited = false;
    public boolean isAbstract = false;

    public MethodSymbol(MethodSymbol s, boolean isInherited) {
        super(s.getDeclaration().id, s.getDeclaration());
        this.isInherited = isInherited;
        this.isAbstract = s.isAbstract;
    }

    public MethodSymbol(MethodHeader declaration) {
        super(declaration.id, declaration);
    }

    public MethodHeader getDeclaration() {
        return (MethodHeader) this.declaration;
    }
}
