package parser;

import exception.InvalidSyntaxException;

import java.util.ArrayList;

/**
 * Created by daiweifan on 2017-02-08.
 */

public class Weeder {
    private ArrayList<String> buffer = new ArrayList<>();

    public void weed(Tree parseTree, String fileName) throws InvalidSyntaxException {
        String error;

        switch (parseTree.rule.lhs.getText()) {
            case "ClassDeclaration":
                error = "A class cannot be both abstract and final!";
                if (parseTree.rule.getRHS().contains("Modifiers")) {
                    buffer.clear();
                    buffer.add("Modifiers");
                    flatten(parseTree.children.get(0));
                    if (buffer.contains("final") && buffer.contains("abstract")) {
                        throw new InvalidSyntaxException(error);
                    }
                }

                error =
                        "A class/interface must be declared in a .java file with the same base name as the class/interface!";
                if (!fileName.endsWith(".java")) {
                    throw new InvalidSyntaxException(error);
                }
                if (!parseTree.children.get(parseTree.rule.getRHS().indexOf("ID")).rule.rhs.get(0).token
                        .equals(fileName.substring(0, fileName.length() - 5))) {
                    throw new InvalidSyntaxException(error);
                }
                break;
            case "MethodDeclaration":
                buffer.clear();
                buffer.add("MethodHeader");
                flatten(parseTree.children.get(0));
                boolean valuation = !buffer.contains("native") && !buffer.contains("abstract");
                error = "A method has a body if and only if it is neither abstract nor native!";
                if (parseTree.children.get(1).children.get(0).rule.lhs.equals("SEMI")) {
                    if (valuation) {
                        throw new InvalidSyntaxException(error + " -- empty body");
                    }
                } else {
                    if (!valuation) {
                        throw new InvalidSyntaxException(error);
                    }
                }

                if (buffer.contains("abstract")) {
                    error = "An abstract method cannot be static or final!";
                    if (buffer.contains("static") || buffer.contains("final")) {
                        throw new InvalidSyntaxException(error);
                    }
                }

                if (buffer.contains("static")) {
                    error = "A static method cannot be final!";
                    if (buffer.contains("final")) {
                        throw new InvalidSyntaxException(error);
                    }
                }

                if (buffer.contains("native")) {
                    error = "A native method must be static!";
                    if (!buffer.contains("static")) {
                        throw new InvalidSyntaxException(error);
                    }
                }
                break;

            // An interface method cannot be static, final, or native.
            case "AbstractMethodDeclaration":
                buffer.clear();
                buffer.add("MethodHeader");
                flatten(parseTree.children.get(0));
                error = "An interface method cannot be static, final, or native!";
                if (buffer.contains("static") || buffer.contains("static") || buffer.contains("static")) {
                    throw new InvalidSyntaxException(error);
                }
                break;

            case "FieldDeclaration":
                error = "No field can be final!";
                if (parseTree.rule.getRHS().contains("Modifiers")) {
                    buffer.clear();
                    buffer.add("Modifiers");
                    flatten(parseTree.children.get(0));
                    if (buffer.contains("final")) {
                        throw new InvalidSyntaxException(error);
                    }
                }
                break;
            case "PostDecrementExpression":
                error = "No dec or inc!";
                throw new InvalidSyntaxException(error);
            case "PreDecrementExpression":
                error = "No dec or inc!";
                throw new InvalidSyntaxException(error);
            case "PostIncrementExpression":
                error = "No dec or inc!";
                throw new InvalidSyntaxException(error);
            case "PreIncrementExpression":
                error = "No dec or inc!";
                throw new InvalidSyntaxException(error);
            case "ThrowStatement":
                error = "No dec or inc!";
                throw new InvalidSyntaxException(error);
                // Every class must contain at least one explicit constructor.
                // No multidimensional array types or multidimensional array creation expressions are allowed.
        }

        // The following should have been taken care of by the grammar already:
        // The type void may only be used as the return type of a method.
        // A formal parameter of a method must not have an initializer.
        // An interface cannot contain fields or constructors.
        // An interface method cannot have a body.
        // A method or constructor must not contain explicit this() or super() calls.

        for (Tree t : parseTree.children) {
            weed(t, fileName);
        }
    }

    private void flatten(Tree parseTree) {
        Rule rule = parseTree.rule;
        int index = buffer.indexOf(rule.lhs.getText());
        buffer.remove(index);
        for (RHSToken r : rule.rhs) {
            buffer.add(index++, r.token.getText());
        }
        for (Tree t : parseTree.children) {
            flatten(t);
        }
    }
}
