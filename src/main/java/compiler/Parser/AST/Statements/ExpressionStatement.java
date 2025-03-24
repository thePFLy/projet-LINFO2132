package compiler.Parser.AST.Statements;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;

public class ExpressionStatement implements ASTNode {
    public final ASTNode expression;

    public ExpressionStatement(ASTNode expression) {
        this.expression = expression;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "ExprStmt(" + expression + ")";
    }
}