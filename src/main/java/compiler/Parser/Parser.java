package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final Lexer lexer;
    private Symbol lookahead;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        //first token
        this.lookahead = lexer.getNextSymbol();
    }

    //go to next token/verify it
    private void match(Lexer.SymbolType expectedType) throws ParserException {
        if (lookahead.getType() != expectedType) {
            throw new ParserException("Unexpected token", lexer.getLine(), lookahead.getName());
        }
        lookahead = lexer.getNextSymbol();
    }

    public ASTree parseExpression() throws ParserException {
        return parseAdditiveExpression();
    }

    // "+", "-")
    private ASTree parseAdditiveExpression() throws ParserException {
        ASTree left = parseMultiplicativeExpression();
        while (lookahead.getType() == Lexer.SymbolType.SYMBOL && (lookahead.getName().equals("+") || lookahead.getName().equals("-"))) {
            String operator = lookahead.getName();
            match(Lexer.SymbolType.SYMBOL);
            ASTree right = parseMultiplicativeExpression();
            left = new BinaryExpression(left, operator, right);
        }
        return left;
    }

    // "*", "/"
    private ASTree parseMultiplicativeExpression() throws ParserException {
        ASTree left = parsePrimaryExpression();
        while (lookahead.getType() == Lexer.SymbolType.SYMBOL && (lookahead.getName().equals("*") || lookahead.getName().equals("/"))) {
            String operator = lookahead.getName();
            match(Lexer.SymbolType.SYMBOL);
            ASTree right = parsePrimaryExpression();
            left = new BinaryExpression(left, operator, right);
        }
        return left;
    }

    //basic elements
    private ASTree parsePrimaryExpression() throws ParserException {
        switch (lookahead.getType()) {
            case INTEGER:
                Symbol integer = lookahead;
                match(Lexer.SymbolType.INTEGER);
                return new Literal(integer.getName());
            case FLOAT:
                Symbol floatSymbol = lookahead;
                match(Lexer.SymbolType.FLOAT);
                return new Literal(floatSymbol.getName()); //Literal
            case STRING:
                Symbol stringSymbol = lookahead;
                match(Lexer.SymbolType.STRING);
                return new Literal(stringSymbol.getName()); //Literal
            case BOOLEAN:
                Symbol booleanSymbol = lookahead;
                match(Lexer.SymbolType.BOOLEAN);
                return new Literal(booleanSymbol.getName()); //Literal
            case IDENTIFIER:
                Symbol identifier = lookahead;
                match(Lexer.SymbolType.IDENTIFIER);
                return new Identifier(identifier.getName());
            case SYMBOL:
                if (lookahead.getName().equals("(")) {
                    match(Lexer.SymbolType.SYMBOL); // "("
                    ASTree expr = parseExpression();
                    match(Lexer.SymbolType.SYMBOL); // ")"
                    return expr;
                }
            default:
                throw new ParserException("Unexpected token", lexer.getLine(), lookahead.getName());
        }
    }

    private static class VariableDeclaration extends ASTree {
        private final Identifier identifier;
        private final Type type;
        private final ASTree initializer;

        public VariableDeclaration(Identifier identifier, Type type, ASTree initializer) {
            this.identifier = identifier;
            this.type = type;
            this.initializer = initializer;
        }

        @Override
        public String toString() {
            return "VariableDeclaration";
        }

        @Override
        public void printTree(int level) {
            super.printTree(level);
            identifier.printTree(level + 1);
            type.printTree(level + 1);
            if (initializer != null) {
                initializer.printTree(level + 1);
            }
        }
    }

    public ASTree parseVariableDeclaration() throws ParserException {
        Symbol identifierSymbol = lookahead;
        match(Lexer.SymbolType.IDENTIFIER);
        Identifier identifier = new Identifier(identifierSymbol.getName());

        Symbol typeSymbol = lookahead;
        match(Lexer.SymbolType.TYPE);
        Type type = new Type(typeSymbol.getName());

        match(Lexer.SymbolType.SYMBOL);
        ASTree initializer = parseExpression();
        match(Lexer.SymbolType.SYMBOL);

        return new VariableDeclaration(identifier, type, initializer);
    }

    private ASTree parseBlock() throws ParserException {
        match(Lexer.SymbolType.SYMBOL); // "{"
        System.out.println("Matched '{' and starting to parse block...");
        List<ASTree> statements = new ArrayList<>();
        while (lookahead.getType() != Lexer.SymbolType.SYMBOL || !lookahead.getName().equals("}")) {
            statements.add(parseExpression()); // Or use parseStatement() if you have a dedicated method for statements
        }
        match(Lexer.SymbolType.SYMBOL); // "}"
        return new Block(statements);
    }

    private static class Block extends ASTree {
        private final List<ASTree> statements;

        public Block(List<ASTree> statements) {
            this.statements = statements;
        }

        @Override
        public String toString() {
            return "Block";
        }

        @Override
        public void printTree(int level) {
            super.printTree(level);
            for (ASTree statement : statements) {
                statement.printTree(level + 1);
            }
        }
    }
}