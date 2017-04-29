package environment.symbol;

import ast.TokenNode;
import ast.Type;

import java.util.ArrayList;

/**
 * Created by Da on 2017/03/14.
 */
public class NodeSymbolTable {

    public NodeSymbolTable parent = null;
    public FileSymbolTable fileSymbolTable = null;
    public ArrayList<Symbol> symbols = new ArrayList<>();

    public NodeSymbolTable() {
    }

    public NodeSymbolTable(NodeSymbolTable parent) {
        this.parent = parent;
        this.fileSymbolTable = parent.fileSymbolTable;
    }

    public Symbol getSymbol(TokenNode id) {
        for (Symbol s : symbols) {
            if (s.equalsById(id)) {
                return s;
            }
        }
        return parent != null ? parent.getSymbol(id) : null;
    }

    // Same as getSymbol, except doesn't go up to the file level. Used for LocalVariableSymbol and FormalParameterSymbol
    public Symbol getLocalSymbol(TokenNode id) {
        for (Symbol s : symbols) {
            if (s.equalsById(id)) {
                return s;
            }
        }
        return (parent != null && parent != fileSymbolTable) ? parent.getLocalSymbol(id) : null;
    }

    public TypeSymbol getType() {
        return fileSymbolTable.getType();
    }

    public FieldSymbol getFieldSymbol(TokenNode id) {
        return fileSymbolTable.getFieldSymbol(id);
    }

    public TypeSymbol getTypeSymbol(TokenNode id) {
        return fileSymbolTable.getTypeSymbol(id);
    }

    public PackageSymbol getPackageSymbol(TokenNode id) {
        return fileSymbolTable.getPackageSymbol(id);
    }

    public void addSymbol(Symbol s) {
        if (this.symbols.contains(s)) {
            // Upgrade symbol if higher priority
            if (s instanceof TypeSymbol) {
                for (Symbol existingSymbol : this.symbols) {
                    if (existingSymbol.equals(s) && ((TypeSymbol) s).typeLinkingPriority.value < ((TypeSymbol) existingSymbol).typeLinkingPriority.value) {
                        ((TypeSymbol) existingSymbol).typeLinkingPriority = ((TypeSymbol) s).typeLinkingPriority;
                    }
                }
            }
            return;
        }
        this.symbols.add(s);
    }
}
