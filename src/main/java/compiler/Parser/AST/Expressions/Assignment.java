package compiler.Parser.AST.Expressions;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;

public class Assignment implements ASTNode {
    public final ASTNode target;
    public final ASTNode value;

    public Assignment(ASTNode target, ASTNode value) {
        this.target = target;
        this.value = value;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("Assign(%s = %s)", target, value);
    }
}