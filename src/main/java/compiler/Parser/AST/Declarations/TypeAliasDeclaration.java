package compiler.Parser.AST.Declarations;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;
import compiler.Parser.AST.Types.Type;

public class TypeAliasDeclaration implements ASTNode {
    public final String alias;
    public final Type originalType;

    public TypeAliasDeclaration(String alias, Type originalType) {
        this.alias = alias;
        this.originalType = originalType;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}