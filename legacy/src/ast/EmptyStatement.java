package ast;

import environment.ASTVisitor;

/**
 * Created by Daiwei on 2017-03-09.
 */
public class EmptyStatement extends Statement {

    public EmptyStatement() {
        super();
    }

    public void acceptVisitor(ASTVisitor visitor) {
        visitor.visit(this);
    }
}
