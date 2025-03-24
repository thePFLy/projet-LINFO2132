package compiler.Parser.AST.Declarations;

import compiler.Parser.AST.ASTNode;
import compiler.Parser.AST.ASTVisitor;
import compiler.Parser.AST.Statements.BlockStatement;
import compiler.Parser.AST.Types.Type;

import java.util.List;

public class FunctionDeclaration implements ASTNode {
    public final String name;
    public final List<Parameter> parameters;
    public final Type returnType;
    public final BlockStatement body;

    public FunctionDeclaration(String name, List<Parameter> parameters, Type returnType, BlockStatement body) {
        this.name = name;
        this.parameters = parameters;
        this.returnType = returnType;
        this.body = body;
    }

    @Override
    public <T> T accept(ASTVisitor<T> visitor) {
        return visitor.visit(this);
    }
}