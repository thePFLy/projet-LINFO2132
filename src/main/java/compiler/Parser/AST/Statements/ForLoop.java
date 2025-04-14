package compiler.Parser.AST.Statements;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;

public class ForLoop implements ASTNode {
    public final ASTNode init;
    public final ASTNode start;
    public final ASTNode end;
    public final ASTNode step;
    public final BlockStatement body;

    public ForLoop(ASTNode init, ASTNode start, ASTNode end, ASTNode step, BlockStatement body) {
        this.init = init;
        this.start = start;
        this.end = end;
        this.step = step;
        this.body = body;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("For(init=%s, start=%s, end=%s, step=%s, body=%s)",
                init, start, end, step, body);
    }
}