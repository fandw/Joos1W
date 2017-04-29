package environment.symbol;

import ast.TypeDeclaration;

/**
 * Created by daiweifan on 2017-03-14.
 */
public abstract class TypeSymbol extends Symbol {

    public TypeLinkingPriority typeLinkingPriority = null;

    public TypeSymbol(TypeDeclaration declaration) {
        super(declaration.id, declaration);
    }

    public TypeSymbol(TypeSymbol symbol, TypeLinkingPriority typeLinkingPriority) {
        super(symbol.getId(), symbol.getDeclaration());
        this.typeLinkingPriority = typeLinkingPriority;
    }

    public enum TypeLinkingPriority {

        EnclosingClassOrInterface(0), SingleTypeImport(1), SamePackage(2), ImportOnDemand(3);

        public int value;

        TypeLinkingPriority(int value) {
            this.value = value;
        }
    }
}
