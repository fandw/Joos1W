package ast;

import ast.util.RecursedProductionList;
import environment.ASTVisitor;
import parser.Tree;
import scanner.Kind;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class Modifier extends TokenNode {

    public Modifier(Tree parseTree) {
        // Modifier oneof(public private statc ...)
        super(parseTree.getChild(0));
    }

    public static RecursedProductionList<Modifier> traverseModifiers(Tree parseTree) {
        return new RecursedProductionList<Modifier>(parseTree, "Modifiers Modifiers Modifier", false, false) {
            @Override
            public Modifier spawn(Tree parseTree) {
                return new Modifier(parseTree);
            }
        };

    }

    public static boolean hasModifier(RecursedProductionList<Modifier> modifiers, Kind kind) {
        for (Modifier m : modifiers) {
            if (m.token.getKind() == kind) {
                return true;
            }
        }
        return false;
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
