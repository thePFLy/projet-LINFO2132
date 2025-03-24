package compiler.Parser.AST.Expressions;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;

public class UnaryExpression implements ASTNode {
    public final String operator;
    public final ASTNode operand;

    public UnaryExpression(String operator, ASTNode operand) {
        this.operator = operator;
        this.operand = operand;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("UnaryOp(%s, %s)", operator, operand);
    }
}