package utils;

import ast.Modifier;
import ast.util.RecursedProductionList;
import exception.WeedException;

/**
 * Created by dawei on 3/10/2017.
 */
public class ModifiersChecker {

    public boolean isPublic = false;
    public boolean isProtected = false;
    public boolean isPrivate = false;
    public boolean isStatic = false;
    public boolean isAbstract = false;
    public boolean isFinal = false;
    public boolean isNative = false;
    public boolean isPackagePrivate = true;

    public ModifiersChecker(RecursedProductionList<Modifier> modifiers) throws WeedException {
        String error = null;

        if (modifiers != null) {
            for (Modifier m : modifiers) {
                switch (m.token.getKind()) {
                    case PUBLIC:
                        isPublic = true;
                        if (isPackagePrivate) {
                            isPackagePrivate = false;
                        } else {
                            error = "Cannot use more than one access modifier at the same time.";
                        }
                        break;
                    case PROTECTED:
                        isProtected = true;
                        if (isPackagePrivate) {
                            isPackagePrivate = false;
                        } else {
                            error = "Cannot use more than one access modifier at the same time.";
                        }
                        break;
                    case PRIVATE:
                        isPrivate = true;
                        if (isPackagePrivate) {
                            isPackagePrivate = false;
                        } else {
                            error = "Cannot use more than one access modifier at the same time.";
                        }
                        break;
                    case STATIC:
                        isStatic = true;
                        break;
                    case ABSTRACT:
                        isAbstract = true;
                        break;
                    case FINAL:
                        isFinal = true;
                        break;
                    case NATIVE:
                        isNative = true;
                        break;
                    default:
                        break;
                }
            }
        }

        if (error != null) {
            throw new WeedException(error);
        }
    }
}
