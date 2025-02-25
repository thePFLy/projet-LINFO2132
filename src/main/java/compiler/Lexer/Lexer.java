package compiler.Lexer;
import java.io.Reader;

public class Lexer {
    public enum SymbolType{
        COMMENT,
        IDENTIFIER,
        KEYWORD,
        INTEGER,
        FLOAT,
        STRING,
        BOOLEAN,
        SYMBOL
    }
    private Reader reader;
    public Lexer(Reader input) {
        this.reader = input;
    }
    
    public Symbol getNextSymbol() {
        return null;
    }
}
