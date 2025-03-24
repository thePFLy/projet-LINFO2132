package compiler.Parser.AST.Statements;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;

public class IfStatement implements ASTNode {
    public final ASTNode condition;
    public final ASTNode thenBranch;
    public final ASTNode elseBranch; // nullable

    public IfStatement(ASTNode condition, ASTNode thenBranch, ASTNode elseBranch) {
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("If(cond=%s, then=%s, else=%s)",
                condition, thenBranch, elseBranch);
    }
}