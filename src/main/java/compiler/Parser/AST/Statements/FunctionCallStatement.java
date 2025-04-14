package compiler.Parser.AST.Statements;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;
import compiler.Parser.AST.Expressions.FunctionCall;

public class FunctionCallStatement implements ASTNode {
    public final FunctionCall functionCall;

    public FunctionCallStatement(FunctionCall functionCall) {
        this.functionCall = functionCall;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("FunctionCallStatement(%s)", functionCall);
    }
}