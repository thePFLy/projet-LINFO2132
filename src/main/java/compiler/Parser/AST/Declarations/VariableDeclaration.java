package compiler.Parser.AST.Declarations;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;
import compiler.Parser.AST.Types.Type;

public class VariableDeclaration implements ASTNode {
    public final Type type;
    public final String identifier;
    public final boolean isFinal;
    public final ASTNode initialValue;

    public VariableDeclaration(Type type, String identifier, boolean isFinal, ASTNode initialValue) {
        this.type = type;
        this.identifier = identifier;
        this.isFinal = isFinal;
        this.initialValue = initialValue;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}