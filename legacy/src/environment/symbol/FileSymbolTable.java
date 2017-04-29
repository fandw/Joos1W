package environment.symbol;

import ast.MethodHeader;
import ast.PackageDeclaration;
import ast.TokenNode;

/**
 * Created by daiweifan on 2017-03-16.
 */
public class FileSymbolTable extends NodeSymbolTable {

    public PackageSymbol packages;
    private TypeSymbol type;

    public FileSymbolTable(TypeSymbol type, PackageSymbol packages, PackageDeclaration packageDeclaration) {
        this.fileSymbolTable = this;
        this.type = type;
        this.packages = packages;

        // Add all TypeSymbols from my package, and subpackages too unless I'm in the default package
        PackageSymbol myPackage = this.packages;
        if (packageDeclaration != null) {
            myPackage = this.packages.getPackageSymbol(packageDeclaration.name.ids.listIterator());
        }
        for (TypeSymbol symbol : packageDeclaration != null ? myPackage.getAllTypes() : myPackage.types.values()) {
            if (symbol instanceof ClassSymbol) {
                this.addSymbol(new ClassSymbol((ClassSymbol) symbol, TypeSymbol.TypeLinkingPriority.SamePackage));
            } else if (symbol instanceof InterfaceSymbol) {
                this.addSymbol(new InterfaceSymbol((InterfaceSymbol) symbol, TypeSymbol.TypeLinkingPriority.SamePackage));
            }
        }
    }

    @Override
    public TypeSymbol getType() {
        return this.type;
    }

    @Override
    public FieldSymbol getFieldSymbol(TokenNode id) {
        for (Symbol symbol : symbols) {
            if (symbol instanceof FieldSymbol && symbol.equalsById(id)) {
                return (FieldSymbol) symbol;
            }
        }
        return null;
    }

    @Override
    public TypeSymbol getTypeSymbol(TokenNode id) {
        for (Symbol symbol : symbols) {
            if (symbol instanceof TypeSymbol && symbol.equalsById(id)) {
                return (TypeSymbol) symbol;
            }
        }
        return null;
    }

    @Override
    public PackageSymbol getPackageSymbol(TokenNode id) {
        for (Symbol symbol : symbols) {
            if (symbol instanceof PackageSymbol && symbol.equalsById(id)) {
                return (PackageSymbol) symbol;
            }
        }
        return null;
    }

}
