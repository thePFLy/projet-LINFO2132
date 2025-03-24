package compiler.Parser.AST.Statements;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;
import java.util.List;

public class BlockStatement implements ASTNode {
    public final List<ASTNode> statements;

    public BlockStatement(List<ASTNode> statements) {
        this.statements = statements;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Block{" + statements + "}";
    }
}