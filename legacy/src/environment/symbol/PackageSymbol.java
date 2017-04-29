package environment.symbol;

import ast.PackageDeclaration;
import ast.TokenNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Da on 2017/03/14.
 */
public class PackageSymbol extends Symbol {
    public HashMap<TokenNode, PackageSymbol> packages = new HashMap<>();
    public HashMap<TokenNode, TypeSymbol> types = new HashMap<>();

    public PackageSymbol() {
        super();
    }

    public PackageSymbol(TokenNode id) {
        super(id, null);
    }

    public PackageDeclaration getDeclaration() {
        return (PackageDeclaration) this.declaration;
    }

    public PackageSymbol parsePackageDeclaration(Iterator<TokenNode> ids) {
        PackageSymbol packagePointer = this;

        while (ids.hasNext()) {
            TokenNode id = ids.next();
            if (!packagePointer.packages.containsKey(id)) {
                PackageSymbol newPackageSymbol = new PackageSymbol(id);
                packagePointer.packages.put(id, newPackageSymbol);
            }
            packagePointer = packagePointer.packages.get(id);
        }
        return packagePointer;
    }

    public PackageSymbol getPackageSymbol(Iterator<TokenNode> ids) {
        PackageSymbol packagePointer = this;
        while (ids.hasNext()) {
            TokenNode id = ids.next();
            if (!packagePointer.packages.containsKey(id)) {
                return null;
            }
            packagePointer = packagePointer.packages.get(id);
        }
        return packagePointer;
    }

    // Find a Type by id, in this package only
    public TypeSymbol getType(TokenNode id) {
        try {
            return this.types.get(id);
        } catch (NullPointerException e) {
            return null;
        }
    }

    // Return all Types included in this package, including those in subpackages
    public List<TypeSymbol> getAllTypes() {
        List<TypeSymbol> results = new ArrayList<>();
        results.addAll(this.types.values());
        for (PackageSymbol subpackage : this.packages.values()) {
            results.addAll(subpackage.getAllTypes());
        }
        return results;
    }

    private boolean addType(TypeSymbol type) {
        TokenNode id = type.getId();
        if (types.containsKey(id)) {
            return false;
        }
        types.put(id, type);
        return true;
    }

    public boolean addType(TypeSymbol type, Iterator<TokenNode> ids) {
        PackageSymbol packagePointer = parsePackageDeclaration(ids);
        return packagePointer.addType(type);
    }
}
