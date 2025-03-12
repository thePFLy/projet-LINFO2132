package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Lexer.Lexer.SymbolType;
import compiler.Lexer.Symbol;

import java.text.ParseException;

public class Parser {
    private Lexer lexer;
    private Symbol lookahead;
    public Symbol match(SymbolType symbolType) throws ParserException {
        if(symbolType != lookahead.getType()) {
            throw new ParserException("wrong match",lexer.getLine(),lookahead.getName());
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
    public static void main(String[] args) {

    }
}
