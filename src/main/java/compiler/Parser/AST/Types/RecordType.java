package compiler.Parser.AST.Types;

import compiler.Parser.AST.ASTVisitor;

public class RecordType extends Type {
    private final String name;

    public RecordType(String name) {
        this.name = name;
    }

    @Override
    public String getTypeName() {
        return name;
    }

    @Override
    public boolean equals(Type other) {
        return other instanceof RecordType &&
                ((RecordType)other).name.equals(this.name);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Type)) return false;
        return equals((Type)obj);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "RecordType[" + name + "]";
    }
}