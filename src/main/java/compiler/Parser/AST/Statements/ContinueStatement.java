package compiler.Parser.AST.Statements;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;

public class ContinueStatement implements ASTNode {
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Continue";
    }
}