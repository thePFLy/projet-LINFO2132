package compiler.Parser.AST.Declarations;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.Types.Type;
import compiler.Parser.AST.ASTVisitor;

public class ArrayDeclaration implements ASTNode {
    public final Type elementType;
    public final String identifier;
    public final ASTNode size;
    public final ASTNode initialValue;

    public ArrayDeclaration(Type elementType, String identifier, ASTNode size, ASTNode initialValue) {
        this.elementType = elementType;
        this.identifier = identifier;
        this.size = size;
        this.initialValue = initialValue;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}