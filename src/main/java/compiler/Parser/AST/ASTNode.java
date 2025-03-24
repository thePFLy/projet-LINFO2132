package compiler.Parser.AST;

public interface ASTNode {
    <T> T accept(ASTVisitor<T> visitor);
}