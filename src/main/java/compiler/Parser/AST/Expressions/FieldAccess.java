package compiler.Parser.AST.Expressions;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;

public class FieldAccess implements ASTNode {
    public final ASTNode record;
    public final String fieldName;

    public FieldAccess(ASTNode record, String fieldName) {
        this.record = record;
        this.fieldName = fieldName;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}