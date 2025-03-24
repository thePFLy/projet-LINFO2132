package compiler.Parser.AST;

import java.util.List;

public class ProgramNode implements ASTNode {
    public final List<ASTNode> statements;

    public ProgramNode(List<ASTNode> statements) {
        this.statements = statements;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}