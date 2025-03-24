package compiler.Parser.AST.Statements;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;

public class ForLoop implements ASTNode {
    public final ASTNode initialization;
    public final ASTNode condition;
    public final ASTNode update;
    public final ASTNode body;

    public ForLoop(ASTNode initialization, ASTNode condition, ASTNode update, ASTNode body) {
        this.initialization = initialization;
        this.condition = condition;
        this.update = update;
        this.body = body;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("For(init=%s, cond=%s, update=%s, body=%s)",
                initialization, condition, update, body);
    }
}