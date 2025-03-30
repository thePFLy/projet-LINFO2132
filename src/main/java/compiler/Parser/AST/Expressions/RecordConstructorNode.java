package compiler.Parser.AST.Expressions;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;
import java.util.List;

public class RecordConstructorNode implements ASTNode {
    private final String recordType;
    private final List<ASTNode> arguments;

    public RecordConstructorNode(String recordType, List<ASTNode> arguments) {
        this.recordType = recordType;
        this.arguments = arguments;
    }

    // Getters
    public String getRecordType() {
        return recordType;
    }

    public List<ASTNode> getArguments() {
        return arguments;
    }

    @Override
    public String toString() {
        return "RecordConstructorNode{" +
                "recordType='" + recordType + '\'' +
                ", arguments=" + arguments +
                '}';
    }
    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}