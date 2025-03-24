package compiler.Parser.AST.Expressions;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;

public class ArrayAccess implements ASTNode {
    public final ASTNode array;
    public final ASTNode index;

    public ArrayAccess(ASTNode array, ASTNode index) {
        this.array = array;
        this.index = index;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("ArrayAccess(%s[%s])", array, index);
    }
}