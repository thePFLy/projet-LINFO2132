package compiler.Parser.AST.Expressions;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;
import compiler.Parser.AST.Types.Type;

public class ArrayCreation implements ASTNode {
    public final Type elementType;
    public final ASTNode size;

    public ArrayCreation(Type elementType, ASTNode size) {
        this.elementType = elementType;
        this.size = size;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}