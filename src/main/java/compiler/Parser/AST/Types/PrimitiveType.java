package compiler.Parser.AST.Types;

import compiler.Parser.AST.ASTVisitor;

public class PrimitiveType extends Type {
    public enum Primitive {
        INT, FLOAT, BOOL, STRING, VOID
    }

    private final Primitive type;

    public PrimitiveType(Primitive type) {
        this.type = type;
    }

    @Override
    public String getTypeName() {
        return type.name().toLowerCase();
    }

    @Override
    public boolean equals(Type other) {
        return other instanceof PrimitiveType &&
                ((PrimitiveType)other).type == this.type;
    }

    public static PrimitiveType fromString(String typeName) {
        return new PrimitiveType(Primitive.valueOf(typeName.toUpperCase()));
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}