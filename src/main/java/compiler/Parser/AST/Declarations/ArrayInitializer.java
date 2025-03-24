package compiler.Parser.AST.Declarations;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;
import java.util.List;

public class ArrayInitializer implements ASTNode {
    public final List<ASTNode> elements;

    public ArrayInitializer(List<ASTNode> elements) {
        this.elements = elements;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}