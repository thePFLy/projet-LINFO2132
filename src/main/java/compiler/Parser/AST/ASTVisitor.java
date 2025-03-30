package compiler.Parser.AST;

import compiler.Parser.AST.Statements.*;
import compiler.Parser.AST.Expressions.*;
import compiler.Parser.AST.Declarations.*;

public interface ASTVisitor<T> {
    default T visit(ASTNode node) {
        return node.accept(this);
    }
    // Declarations
    T visit(VariableDeclaration node);
    T visit(RecordDeclaration node);
    T visit(FieldDeclaration node);
    T visit(ArrayDeclaration node);
    T visit(FunctionDeclaration node);
    T visit(Parameter node);
    T visit(TypeAliasDeclaration node);
    T visit(ArrayInitializer node);
    // expr
    T visit(BinaryExpression expr);
    T visit(UnaryExpression expr);
    T visit(Literal expr);
    T visit(Identifier expr);
    T visit(FunctionCall expr);
    T visit(ArrayAccess expr);
    T visit(Assignment expr);
    T visit(TypeConversion expr);
    T visit(FieldAccess expr);
    T visit(ArrayCreation expr);
    T visit(RecordConstructorNode expr);
    T visit(ProgramNode node);
    // statements
    T visit(BlockStatement stmt);
    T visit(IfStatement stmt);
    T visit(ForLoop stmt);
    T visit(WhileLoop stmt);
    T visit(ReturnStatement stmt);
    T visit(BreakStatement stmt);
    T visit(ContinueStatement stmt);
    T visit(ExpressionStatement stmt);
}