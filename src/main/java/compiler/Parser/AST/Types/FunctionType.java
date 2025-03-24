package compiler.Parser.AST.Types;

import compiler.Parser.AST.ASTVisitor;
import java.util.List;

public class FunctionType extends Type {
    private final List<Type> parameterTypes;
    private final Type returnType;

    public FunctionType(List<Type> parameterTypes, Type returnType) {
        this.parameterTypes = parameterTypes;
        this.returnType = returnType;
    }

    @Override
    public String getTypeName() {
        StringBuilder sb = new StringBuilder("(");
        for (Type t : parameterTypes) {
            sb.append(t.getTypeName()).append(",");
        }
        if (!parameterTypes.isEmpty()) {
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append(")->").append(returnType.getTypeName());
        return sb.toString();
    }

    @Override
    public boolean equals(Type other) {
        if (!(other instanceof FunctionType)) return false;
        FunctionType otherFunc = (FunctionType)other;
        return this.returnType.equals(otherFunc.returnType) &&
                this.parameterTypes.equals(otherFunc.parameterTypes);
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}