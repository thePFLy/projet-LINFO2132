package compiler.Parser;

import compiler.Lexer.*;
import compiler.Parser.AST.*;
import compiler.Parser.AST.Declarations.*;
import compiler.Parser.AST.Expressions.*;
import compiler.Parser.AST.Statements.*;
import compiler.Parser.AST.Types.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private Symbol currentToken;
    private Symbol nextToken;
    private Symbol nextNextToken;
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        advance(); // first token
        advance(); // nextToken
        advance(); // nextNextToken
    }

    public ProgramNode getAST() throws ParserException {
        return parseProgram();
    }

    public ProgramNode parseProgram() throws ParserException {
        List<ASTNode> statements = new ArrayList<>();
        while (!checkType(Lexer.SymbolType.EOF)) {
            statements.add(parseStatement());
        }
        return new ProgramNode(statements);
    }

    private ASTNode parseStatement() throws ParserException {
        skipSemicolons();

        if (checkType(Lexer.SymbolType.KEYWORD)) {
            switch (currentToken.getName()) {
                case "final": return parseFinalDeclaration();
                case "rec": return parseRecordDefinition();
                case "fun": return parseFunctionDefinition();
                case "if": return parseIfStatement();
                case "for": return parseForLoop();
                case "while": return parseWhileLoop();
                case "return": return parseReturnStatement();
                case "break":
                    advance();
                    match(";");
                    return new BreakStatement();
            }
        }

        if (checkType(Lexer.SymbolType.TYPE)) {
            return parseVariableDeclaration();
        }

        if (checkType(Lexer.SymbolType.IDENTIFIER)) {
            switch (nextToken.getName()) {
                case "=":
                    return parseAssignment();
                case "(":
                    return parseFunctionCall();
                case "[":
                    return parseArrayAssignment();
                case ".":
                    return parseFieldAssignment();
            }
        }

        if (checkType(Lexer.SymbolType.TYPE)) {
            return parseVariableDeclaration();
        }

        throw new ParserException(
                "Invalid statement: " + currentToken.getName(),
                lexer.getLine(),
                currentToken.getName()
        );
    }

    // Declaration parsing
    private VariableDeclaration parseFinalDeclaration() throws ParserException {
        match("final");
        Type type = parseType();
        String identifier = currentToken.getName();
        match(identifier);
        ASTNode initialValue = null;
        if (check("=")) {
            match("=");
            initialValue = parseExpression();
        }
        match(";");
        return new VariableDeclaration(type, identifier, true, initialValue);
    }

    private RecordDeclaration parseRecordDefinition() throws ParserException {
        match("rec");
        String name = currentToken.getName();
        match(name);
        match("{");

        List<FieldDeclaration> fields = new ArrayList<>();
        while (!check("}")) {
            if (checkType(Lexer.SymbolType.IDENTIFIER) && nextToken.getType() == Lexer.SymbolType.TYPE) {
                String fieldName = currentToken.getName();
                match(fieldName);
                Type fieldType = parseType();
                fields.add(new FieldDeclaration(fieldName, fieldType));
            } else {
                Type fieldType = parseType();
                String fieldName = currentToken.getName();
                match(fieldName);
                fields.add(new FieldDeclaration(fieldName, fieldType));
            }
            match(";");
        }

        match("}");
        return new RecordDeclaration(name, fields);
    }

    private FunctionDeclaration parseFunctionDefinition() throws ParserException {
        match("fun");
        String name = currentToken.getName();
        match(name);
        match("(");
        List<Parameter> parameters = parseParameterList();
        match(")");
        Type returnType = null;
        if (checkType(Lexer.SymbolType.TYPE)) {
            returnType = parseType();
        }
        BlockStatement body = parseBlock();
        return new FunctionDeclaration(name, parameters, returnType, body);
    }

    private List<Parameter> parseParameterList() throws ParserException {
        List<Parameter> parameters = new ArrayList<>();
        while (!check(")")) {
            if (checkType(Lexer.SymbolType.IDENTIFIER) && nextToken.getType() == Lexer.SymbolType.TYPE) {
                String name = currentToken.getName();
                match(name);
                Type type = parseType();
                parameters.add(new Parameter(name, type));
            } else {
                Type type = parseType();
                String name = currentToken.getName();
                match(name);
                parameters.add(new Parameter(name, type));
            }

            if (!check(")")) {
                match(",");
            }
        }
        return parameters;
    }

    // Statement parsing
    private IfStatement parseIfStatement() throws ParserException {
        match("if");
        match("(");
        ASTNode condition = parseExpression();
        match(")");
        BlockStatement thenBlock = parseBlock();
        BlockStatement elseBlock = null;
        if (check("else")) {
            match("else");
            elseBlock = parseBlock();
        }
        return new IfStatement(condition, thenBlock, elseBlock);
    }

    private ForLoop parseForLoop() throws ParserException {
        match("for");
        match("(");

        ASTNode init = parseForInit();
        match(";");

        ASTNode condition = check(";") ? null : parseExpression();
        match(";");

        ASTNode update = check(")") ? null : parseExpression();
        match(")");

        BlockStatement body = parseBlock();

        return new ForLoop(init, condition, update, body);
    }

    private ASTNode parseForInit() throws ParserException {
        skipSemicolons();

        if (checkType(Lexer.SymbolType.TYPE)) {
            return parseVariableDeclaration();
        }
        else if (checkType(Lexer.SymbolType.IDENTIFIER) &&
                nextToken.getType() == Lexer.SymbolType.TYPE) {
            String identifier = currentToken.getName();
            match(identifier);
            Type type = parseType();
            match("=");
            ASTNode initialValue = parseExpression();
            return new VariableDeclaration(type, identifier, false, initialValue);
        }
        else if (check("final")) {
            return parseFinalDeclaration();
        }
        else {
            return parseExpression();
        }
    }

    private VariableDeclaration parseVariableDeclaration() throws ParserException {
        Type type = parseType();
        String identifier = currentToken.getName();
        match(identifier);

        ASTNode initialValue = null;
        if (check("=")) {
            match("=");
            if (check("array")) {
                initialValue = parseArrayCreation();
            } else {
                initialValue = parseExpression();
            }
        }
        match(";");
        return new VariableDeclaration(type, identifier, false, initialValue);
    }

    private WhileLoop parseWhileLoop() throws ParserException {
        match("while");
        match("(");
        ASTNode condition = parseExpression();
        match(")");
        BlockStatement body = parseBlock();
        return new WhileLoop(condition, body);
    }

    private ReturnStatement parseReturnStatement() throws ParserException {
        match("return");
        ASTNode expr = check(";") ? null : parseExpression();
        match(";");
        return new ReturnStatement(expr);
    }

    // Expression parsing
    private Assignment parseAssignment() throws ParserException {
        String identifier = currentToken.getName();
        match(identifier);
        match("=");
        ASTNode value = parseExpression();
        match(";");
        return new Assignment(new Identifier(identifier), value);
    }

    private Assignment parseArrayAssignment() throws ParserException {
        String identifier = currentToken.getName();
        match(identifier);
        match("[");
        ASTNode index = parseExpression();
        match("]");
        match("=");
        ASTNode value = parseExpression();
        match(";");
        return new Assignment(new ArrayAccess(new Identifier(identifier), index), value);
    }

    private Assignment parseFieldAssignment() throws ParserException {
        String recordName = currentToken.getName();
        match(recordName);
        match(".");
        String fieldName = currentToken.getName();
        match(fieldName);
        match("=");
        ASTNode value = parseExpression();
        match(";");
        return new Assignment(new FieldAccess(new Identifier(recordName), fieldName), value);
    }

    private FunctionCall parseFunctionCall() throws ParserException {
        String name = currentToken.getName();
        match(name);
        match("(");
        List<ASTNode> args = new ArrayList<>();
        while (!check(")")) {
            args.add(parseExpression());
            if (!check(")")) {
                match(",");
            }
        }
        match(")");
        match(";");
        return new FunctionCall(name, args);
    }

    private ASTNode parseExpression() throws ParserException {
        return parseAssignmentExpression();
    }

    private ASTNode parseAssignmentExpression() throws ParserException {
        ASTNode left = parseLogicalOr();
        if (check("=")) {
            match("=");
            ASTNode right = parseAssignmentExpression();
            return new Assignment(left, right);
        }
        return left;
    }

    private ASTNode parseLogicalOr() throws ParserException {
        ASTNode left = parseLogicalAnd();
        while (check("||")) {
            String op = currentToken.getName();
            match(op);
            ASTNode right = parseLogicalAnd();
            left = new BinaryExpression(op, left, right);
        }
        return left;
    }

    private ASTNode parseLogicalAnd() throws ParserException {
        ASTNode left = parseEquality();
        while (check("&&")) {
            String op = currentToken.getName();
            match(op);
            ASTNode right = parseEquality();
            left = new BinaryExpression(op, left, right);
        }
        return left;
    }

    private ASTNode parseEquality() throws ParserException {
        ASTNode left = parseRelational();
        while (check("==") || check("!=")) {
            String op = currentToken.getName();
            match(op);
            ASTNode right = parseRelational();
            left = new BinaryExpression(op, left, right);
        }
        return left;
    }

    private ASTNode parseRelational() throws ParserException {
        ASTNode left = parseAdditive();
        while (check("<") || check("<=") || check(">") || check(">=")) {
            String op = currentToken.getName();
            match(op);
            ASTNode right = parseAdditive();
            left = new BinaryExpression(op, left, right);
        }
        return left;
    }

    private ASTNode parseAdditive() throws ParserException {
        ASTNode left = parseMultiplicative();
        while (check("+") || check("-")) {
            String op = currentToken.getName();
            match(op);
            ASTNode right = parseMultiplicative();
            left = new BinaryExpression(op, left, right);
        }
        return left;
    }

    private ASTNode parseMultiplicative() throws ParserException {
        ASTNode left = parseUnary();
        while (check("*") || check("/") || check("%")) {
            String op = currentToken.getName();
            match(op);
            ASTNode right = parseUnary();
            left = new BinaryExpression(op, left, right);
        }
        return left;
    }

    private ASTNode parseUnary() throws ParserException {
        if (check("-") || check("!")) {
            String op = currentToken.getName();
            match(op);
            ASTNode operand = parseUnary();
            return new UnaryExpression(op, operand);
        }
        return parsePrimary();
    }

    private ASTNode parsePrimary() throws ParserException {
        if (check("(")) {
            match("(");
            ASTNode expr = parseExpression();
            match(")");
            return expr;
        }
        if (checkType(Lexer.SymbolType.IDENTIFIER)) {
            String name = currentToken.getName();
            match(name);

            if (check("(")) {
                return parseFunctionCall();
            } else if (check("[")) {
                match("[");
                ASTNode index = parseExpression();
                match("]");
                return new ArrayAccess(new Identifier(name), index);
            } else if (check(".")) {
                match(".");
                String field = currentToken.getName();
                match(field);
                return new FieldAccess(new Identifier(name), field);
            }
            return new Identifier(name);
        }
        if (check("array")) {
            return parseArrayCreation();
        }
        if (checkType(Lexer.SymbolType.INTEGER) || checkType(Lexer.SymbolType.FLOAT) ||
                checkType(Lexer.SymbolType.BOOLEAN) || checkType(Lexer.SymbolType.STRING)) {
            Object value = currentToken.getName();
            String type = currentToken.getType().toString().toLowerCase();
            match(currentToken.getName());
            return new Literal(value, type);
        }
        throw new ParserException("Unexpected token in expression", lexer.getLine(), currentToken.getName());
    }

    private ArrayCreation parseArrayCreation() throws ParserException {
        match("array");
        match("[");
        ASTNode size = parseExpression();
        match("]");
        match("of");
        Type elementType = parseType();
        return new ArrayCreation(elementType, size);
    }

    private BlockStatement parseBlock() throws ParserException {
        match("{");
        List<ASTNode> statements = new ArrayList<>();
        while (!check("}")) {
            statements.add(parseStatement());
        }
        match("}");
        return new BlockStatement(statements);
    }

    private Type parseType() throws ParserException {

        if (check("array")) {
            match("array");
            match("[");
            match("]");
            Type elementType = parseType();
            return new ArrayType(elementType, 1);
        }
        String typeName = currentToken.getName();
        match(typeName);

        if (check("[")) {
            match("[");
            match("]");
            return new ArrayType(PrimitiveType.fromString(typeName), 1);
        }

        return PrimitiveType.fromString(typeName);
    }

    // Utility methods
    private void advance() {
        currentToken = nextToken;
        nextToken = nextNextToken;
        nextNextToken = lexer.getNextSymbol();
    }

    private void match(String expected) throws ParserException {
        if (!currentToken.getName().equals(expected)) {
            throw new ParserException(
                    "Expected '" + expected + "', found '" + currentToken.getName() + "'",
                    lexer.getLine(),
                    currentToken.getName()
            );
        }
        advance();
    }

    private boolean check(String value) {
        return currentToken.getName().equals(value);
    }

    private boolean checkType(Lexer.SymbolType type) {
        return currentToken.getType() == type;
    }

    private void skipSemicolons() {
        while (check(";")) advance();
    }
}