package compiler.Parser.AST.Expressions;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;
import compiler.Parser.AST.Types.Type;

public class TypeConversion implements ASTNode {
    public final ASTNode expression;
    public final Type targetType;

    public TypeConversion(ASTNode expression, Type targetType) {
        this.expression = expression;
        this.targetType = targetType;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("TypeCast(%s as %s)", expression, targetType);
    }
}