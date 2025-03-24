package compiler.Parser.AST.Declarations;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.Types.Type;
import compiler.Parser.AST.ASTVisitor;

public class FieldDeclaration implements ASTNode {
    public final String name;
    public final Type type;

    public FieldDeclaration(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}