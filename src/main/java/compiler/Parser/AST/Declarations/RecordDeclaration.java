package compiler.Parser.AST.Declarations;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;
import java.util.List;

public class RecordDeclaration implements ASTNode {
    public final String name;
    public final List<FieldDeclaration> fields;

    public RecordDeclaration(String name, List<FieldDeclaration> fields) {
        this.name = name;
        this.fields = fields;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}