package utils;

import ast.ASTNode;
import ast.util.RecursedProductionList;

public class Helpers {
    public static <T extends ASTNode> RecursedProductionList<T> safe(RecursedProductionList<T> list) {
        if (list == null) {
            return RecursedProductionList.EMPTY_LIST;
        }
        return list;
    }
}
