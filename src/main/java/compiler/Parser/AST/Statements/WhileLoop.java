package compiler.Parser.AST.Statements;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;

public class WhileLoop implements ASTNode {
    public final ASTNode condition;
    public final ASTNode body;

    public WhileLoop(ASTNode condition, ASTNode body) {
        this.condition = condition;
        this.body = body;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("While(cond=%s, body=%s)", condition, body);
    }
}