package ast;

import parser.Tree;

/**
 * Created by daiweifan on 2017-02-27.
 */
public class AST {
    public CompilationUnit compilationUnit;

    public AST(Tree parseTree) {

        this.compilationUnit = new CompilationUnit(parseTree.getChild(1));

    }
}
