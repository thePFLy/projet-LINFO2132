package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Lexer.Lexer.SymbolType;
import compiler.Lexer.Symbol;

public class Parser {
    private Lexer lexer;
    private Symbol lookahead;

    public Symbol match(SymbolType symbolType) throws ParserException {
        if(symbolType != lookahead.getType()) {
            throw new ParserException("wrong match", lexer.getLine(), lookahead.getName());
        }
        else {
            Symbol symbol = lookahead;
            lookahead = lexer.getNextSymbol();
            return symbol;
        }
    }

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        parse();
    }

    public void parse() {
        lookahead = lexer.getNextSymbol();
    }

    public ASTree parseExpression() throws ParserException {
        switch (lookahead.getType()) {
            case IDENTIFIER:
                Symbol identifierSymbol = match(SymbolType.IDENTIFIER);
                return new Identifier(identifierSymbol.getName());
            case KEYWORD:
                Symbol keywordSymbol = match(SymbolType.KEYWORD);
                return new Keyword(keywordSymbol.getName());
            case USER_TYPE:
                Symbol typeSymbol = match(SymbolType.USER_TYPE);
                return new Type(typeSymbol.getName());
            default:
                throw new ParserException("Unexpected token", lexer.getLine(), lookahead.getName());
        }
    }

    public static void main(String[] args) {
    }
}
