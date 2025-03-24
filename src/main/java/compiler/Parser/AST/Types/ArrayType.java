package compiler.Parser.AST.Types;

import compiler.Parser.AST.ASTVisitor;

public class ArrayType extends Type {
    private final Type elementType;
    private final int dimensions;

    public ArrayType(Type elementType, int dimensions) {
        this.elementType = elementType;
        this.dimensions = dimensions;
    }

    @Override
    public String getTypeName() {
        StringBuilder sb = new StringBuilder(elementType.getTypeName());
        for (int i = 0; i < dimensions; i++) {
            sb.append("[]");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Type other) {
        if (!(other instanceof ArrayType)) return false;
        ArrayType otherArray = (ArrayType) other;
        return this.dimensions == otherArray.dimensions &&
                this.elementType.equals(otherArray.elementType);
    }

    public Type getElementType() {
        return elementType;
    }

    public int getDimensions() {
        return dimensions;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}