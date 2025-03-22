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

    public ASTree parseIfStatement() throws ParserException {
        match(Lexer.SymbolType.KEYWORD); // "if"
        match(Lexer.SymbolType.SYMBOL); // "("
        ASTree condition = parseExpression();
        match(Lexer.SymbolType.SYMBOL); // ")"
        ASTree ifBlock = parseBlock();
        ASTree elseBlock = null;
        if (lookahead.getType() == Lexer.SymbolType.KEYWORD && lookahead.getName().equals("else")) {
            match(Lexer.SymbolType.KEYWORD); // "else"
            elseBlock = parseBlock();
        }
        return new IfStatement(condition, ifBlock, elseBlock);
    }

    private ASTree parseBlock() throws ParserException {
        match(Lexer.SymbolType.SYMBOL); // "{"
        ASTree block = parseExpression(); // 1 statement
        match(Lexer.SymbolType.SYMBOL); // "}"
        return block;
    }

    private static class IfStatement extends ASTree {
        private final ASTree condition;
        private final ASTree ifBlock;
        private final ASTree elseBlock;

        public IfStatement(ASTree condition, ASTree ifBlock, ASTree elseBlock) {
            this.condition = condition;
            this.ifBlock = ifBlock;
            this.elseBlock = elseBlock;
        }

        @Override
        public String toString() {
            return "IfStatement";
        }

        @Override
        public void printTree(int level) {
            super.printTree(level);
            condition.printTree(level + 1);
            ifBlock.printTree(level + 1);
            if (elseBlock != null) elseBlock.printTree(level + 1);
        }
    }

    public ASTree parseWhileLoop() throws ParserException {
        match(Lexer.SymbolType.KEYWORD); // "while"
        match(Lexer.SymbolType.SYMBOL); // "("
        ASTree condition = parseExpression();
        match(Lexer.SymbolType.SYMBOL); // ")"
        ASTree body = parseBlock();
        return new WhileLoop(condition, body);
    }

    private static class WhileLoop extends ASTree {
        private final ASTree condition;
        private final ASTree body;

        public WhileLoop(ASTree condition, ASTree body) {
            this.condition = condition;
            this.body = body;
        }

        @Override
        public String toString() {
            return "WhileLoop";
        }

        @Override
        public void printTree(int level) {
            super.printTree(level);
            condition.printTree(level + 1);
            body.printTree(level + 1);
        }
    }

    public ASTree parseArrayDeclaration() throws ParserException {
        Symbol identifierSymbol = lookahead;
        match(Lexer.SymbolType.IDENTIFIER);
        Identifier identifier = new Identifier(identifierSymbol.getName());

        Symbol typeSymbol = lookahead;
        match(Lexer.SymbolType.TYPE);
        Type type = new Type(typeSymbol.getName());

        match(Lexer.SymbolType.SYMBOL); // "["
        match(Lexer.SymbolType.SYMBOL); // "]"
        match(Lexer.SymbolType.SYMBOL); // "="
        match(Lexer.SymbolType.KEYWORD); // "array"
        match(Lexer.SymbolType.SYMBOL); // "["
        ASTree size = parseExpression();
        match(Lexer.SymbolType.SYMBOL); // "]"
        match(Lexer.SymbolType.KEYWORD); // "of"
        match(Lexer.SymbolType.TYPE); // type

        return new ArrayDeclaration(identifier, type, size);
    }

    private static class ArrayDeclaration extends ASTree {
        private final Identifier identifier;
        private final Type type;
        private final ASTree size;

        public ArrayDeclaration(Identifier identifier, Type type, ASTree size) {
            this.identifier = identifier;
            this.type = type;
            this.size = size;
        }

        @Override
        public String toString() {
            return "ArrayDeclaration";
        }

        @Override
        public void printTree(int level) {
            super.printTree(level);
            identifier.printTree(level + 1);
            type.printTree(level + 1);
            size.printTree(level + 1);
        }
    }

    public ASTree parseFunctionDeclaration() throws ParserException {
        match(Lexer.SymbolType.KEYWORD); // "fun"
        Symbol nameSymbol = lookahead;
        match(Lexer.SymbolType.IDENTIFIER);
        Identifier name = new Identifier(nameSymbol.getName());

        match(Lexer.SymbolType.SYMBOL); // "("
        List<ASTree> parameters = parseParameters();
        match(Lexer.SymbolType.SYMBOL); // ")"

        Symbol returnTypeSymbol = lookahead;
        match(Lexer.SymbolType.TYPE);
        Type returnType = new Type(returnTypeSymbol.getName());

        ASTree body = parseBlock();
        return new FunctionDeclaration(name, parameters, returnType, body);
    }

    private List<ASTree> parseParameters() throws ParserException {
        List<ASTree> parameters = new ArrayList<>();
        while (lookahead.getType() != Lexer.SymbolType.SYMBOL || !lookahead.getName().equals(")")) {
            parameters.add(parseVariableDeclaration());
            if (lookahead.getType() == Lexer.SymbolType.SYMBOL && lookahead.getName().equals(",")) {
                match(Lexer.SymbolType.SYMBOL); // ","
            }
        }
        return parameters;
    }

    private static class FunctionDeclaration extends ASTree {
        private final Identifier name;
        private final List<ASTree> parameters;
        private final Type returnType;
        private final ASTree body;

        public FunctionDeclaration(Identifier name, List<ASTree> parameters, Type returnType, ASTree body) {
            this.name = name;
            this.parameters = parameters;
            this.returnType = returnType;
            this.body = body;
        }

        @Override
        public String toString() {
            return "FunctionDeclaration";
        }

        @Override
        public void printTree(int level) {
            super.printTree(level);
            name.printTree(level + 1);
            for (ASTree param : parameters) param.printTree(level + 1);
            returnType.printTree(level + 1);
            body.printTree(level + 1);
        }
    }
}