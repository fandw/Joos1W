package ast;

import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public abstract class Expression extends ASTNode {

    private static String[] trickleDown = new String[] {
            "Expression AssignmentExpression",
            "AssignmentExpression ConditionalOrExpression",
            "AssignmentExpression Assignment",
            "ConditionalOrExpression ConditionalAndExpression",
            "ConditionalAndExpression InclusiveOrExpression",
            "InclusiveOrExpression AndExpression",
            "AndExpression EqualityExpression",
            "EqualityExpression RelationalExpression",
            "RelationalExpression AdditiveExpression",
            "AdditiveExpression MultiplicativeExpression",
            "MultiplicativeExpression UnaryExpression",
            "UnaryExpression UnaryExpressionNotPlusMinus",
            "UnaryExpressionNotPlusMinus PostfixExpression",
            "UnaryExpressionNotPlusMinus CastExpression",
            "PostfixExpression Primary",
            "PostfixExpression Name",
            "Primary PrimaryNoNewArray",
            "Primary ArrayCreationExpression",
            "PrimaryNoNewArray ClassInstanceCreationExpression",
            "PrimaryNoNewArray FieldAccess",
            "PrimaryNoNewArray MethodInvocation",
            "PrimaryNoNewArray ArrayAccess"
    };
    private static String[] binaryOps = new String[] {
            "ConditionalOrExpression ConditionalOrExpression OR ConditionalAndExpression",
            "ConditionalAndExpression ConditionalAndExpression AND InclusiveOrExpression",
            "InclusiveOrExpression InclusiveOrExpression BOR AndExpression",
            "AndExpression AndExpression BAND EqualityExpression",
            "EqualityExpression EqualityExpression EQ RelationalExpression",
            "EqualityExpression EqualityExpression NE RelationalExpression",
            "RelationalExpression RelationalExpression LT AdditiveExpression",
            "RelationalExpression RelationalExpression GT AdditiveExpression",
            "RelationalExpression RelationalExpression LE AdditiveExpression",
            "RelationalExpression RelationalExpression GE AdditiveExpression",
            "AdditiveExpression AdditiveExpression PLUS MultiplicativeExpression",
            "AdditiveExpression AdditiveExpression MINUS MultiplicativeExpression",
            "MultiplicativeExpression MultiplicativeExpression STAR UnaryExpression",
            "MultiplicativeExpression MultiplicativeExpression SLASH UnaryExpression",
            "MultiplicativeExpression MultiplicativeExpression MOD UnaryExpression"
    };

    public Expression() {
        super();
    }

    public static Expression translateExpression(Tree parseTree) {
        for (String r : trickleDown) {
            if (parseTree.getRule().equals(r)) {
                return translateExpression(parseTree.getChild(0));
            }
        }

        for (String r : binaryOps) {
            if (parseTree.getRule().equals(r)) {
                return new BinaryOps(parseTree);
            }
        }

        if (parseTree.getRule().equals("Assignment LeftHandSide BECOMES AssignmentExpression")) {
            return new Assignment(parseTree);
        }

        if (parseTree.getRule().equals("RelationalExpression RelationalExpression INSTANCEOF ReferenceType")) {
            return new InstanceOf(parseTree);
        }

        if (parseTree.getRule().equals("UnaryExpression MINUS UnaryExpression")) {
            return new UnaryExpression(parseTree);
        }

        if (parseTree.getRule().equals("UnaryExpressionNotPlusMinus NOT UnaryExpression")) {
            return new UnaryExpression(parseTree);
        }

        if (parseTree.getRule().startsWith("Name")) {
            return new NameExpression(parseTree);
        }

        if (parseTree.getRule().startsWith("CastExpression")) {
            return new CastExpression(parseTree);
        }

        if (parseTree.getRule().equals("PrimaryNoNewArray Literal")) {
            return new Literal(parseTree.getChild(0));
        }

        if (parseTree.getRule().equals("PrimaryNoNewArray THIS")) {
            return new ThisExpression(parseTree.getChild(0));
        }

        if (parseTree.getRule().equals("PrimaryNoNewArray LPAREN Expression RPAREN")) {
            return new ParenthesizedExpression(parseTree);
        }

        if (parseTree.getRule().startsWith("ArrayCreationExpression")) {
            return new ArrayCreationExpression(parseTree);
        }

        if (parseTree.getRule().startsWith("ClassInstanceCreationExpression")) {
            return new ClassInstanceCreationExpression(parseTree);
        }

        if (parseTree.getRule().equals("FieldAccess Primary DOT ID")) {
            return new FieldAccess(parseTree);
        }

        if (parseTree.getRule().equals("MethodInvocation Name LPAREN ArgumentList RPAREN") ||
            parseTree.getRule().equals("MethodInvocation Name LPAREN RPAREN")) {
            return new LocalMethodInvocation(parseTree);
        }

        if (parseTree.getRule().equals("MethodInvocation Primary DOT ID LPAREN ArgumentList RPAREN") ||
            parseTree.getRule().equals("MethodInvocation Primary DOT ID LPAREN RPAREN")) {
            return new OtherMethodInvocation(parseTree);
        }

        if (parseTree.getRule().equals("ArrayAccess Name LBRACK Expression RBRACK")) {
            return new ArrayAccess(parseTree);
        }

        if (parseTree.getRule().equals("ArrayAccess PrimaryNoNewArray LBRACK Expression RBRACK")) {
            return new OtherArrayAccess(parseTree);
        }

        return null;
    }

    public boolean equalsTrue() {
        return false;
    }

    public boolean equalsFalse() {
        return false;
    }

    public boolean isBooleanLiteral() {
        return false;
    }

    public Boolean getBoolean() {
        return null;
    }

    public boolean isIntegerLiteral() {
        return false;
    }

    public Integer getInteger() {
        return null;
    }
}
