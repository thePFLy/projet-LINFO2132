package compiler.Lexer;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Lexer {
    public enum SymbolType {
        COMMENT, IDENTIFIER, KEYWORD, INTEGER, FLOAT, STRING, BOOLEAN, SYMBOL, EOF
    }

    private final PushbackReader reader;
    private int currentChar;
//    private static final String[] KEYWORDS =  {"free", "final", "rec", "fun", "for", "while", "if", "else", "return"};
    private static final String[] BOOLEAN_VALUES = {"true", "false"};
    private static final String SYMBOLS = "=+-*/%(){}[].,;<>!&|";
    private ArrayList<String> KEYWORDS = new ArrayList<>(Arrays.asList("free", "final", "rec", "fun", "for", "while", "if", "else", "return"));
    public Lexer(Reader input) {
        this.reader = new PushbackReader(input);
        advance();  // Read the first character
    }

    private void advance() {
        try {
            currentChar = reader.read();
        } catch (IOException e) {
            currentChar = -1;
        }
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(currentChar)) {
            advance();
        }
    }
    //TODO:check for function names  + built in functions + own types ex: Point rec {...} + index operator [i] + record field operator . in (a.x) + concatenation (+) in strings and others
    //TODO:see code example_file

    public Symbol getNextSymbol() {
        skipWhitespace();

        if (currentChar == -1) return new Symbol(SymbolType.EOF, "EOF");

        //  comments
        if (currentChar == '$') {
            while (currentChar != '\n' && currentChar != -1) advance();
            return getNextSymbol();
        }

        // variables
        if (Character.isLetter(currentChar) || currentChar == '_') {
            StringBuilder sb = new StringBuilder();
            while (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                sb.append((char) currentChar);
                advance();
            }
            String word = sb.toString();
            for (String keyword : KEYWORDS) {
                if (word.equals(keyword)) return new Symbol(SymbolType.KEYWORD, word);
            }
            for (String boolVal : BOOLEAN_VALUES) {
                if (word.equals(boolVal)) return new Symbol(SymbolType.BOOLEAN, word);
            }
            return new Symbol(SymbolType.IDENTIFIER, word);
        }

        // numbers (integers and floats)
        if (Character.isDigit(currentChar) || currentChar == '.') {
            StringBuilder sb = new StringBuilder();
            boolean isFloat = false;

            if (currentChar == '.') {  // Handle cases like ".234"
                sb.append('0');
                isFloat = true;
            }

            while (Character.isDigit(currentChar) || currentChar == '.') {
                if (currentChar == '.') {
                    isFloat = true;
                }
                sb.append((char) currentChar);
                advance();
            }
            return new Symbol(isFloat ? SymbolType.FLOAT : SymbolType.INTEGER, sb.toString());
        }

        // strings
        if (currentChar == '"') {
            StringBuilder sb = new StringBuilder();
            advance();
            while (currentChar != '"' && currentChar != -1) {
                if (currentChar == '\\') {  // Handle escape sequences
                    advance();
                    if (currentChar == 'n') sb.append('\n');
                    else if (currentChar == '\\') sb.append('\\');
                    else if (currentChar == '"') sb.append('"');
                    else sb.append((char) currentChar);
                } else {
                    sb.append((char) currentChar);
                }
                advance();
            }
            advance();  // Skip closing quote
            return new Symbol(SymbolType.STRING, sb.toString());
        }

        // Handle multi-character operators (==, !=, <=, >=, &&, ||)
        if (SYMBOLS.indexOf(currentChar) != -1) {
            StringBuilder sb = new StringBuilder();
            sb.append((char) currentChar);
            advance();

            if ((sb.charAt(0) == '=' || sb.charAt(0) == '!' || sb.charAt(0) == '<' || sb.charAt(0) == '>') && currentChar == '=') {
                sb.append((char) currentChar);
                advance();
            } else if ((sb.charAt(0) == '&' || sb.charAt(0) == '|') && currentChar == sb.charAt(0)) {
                sb.append((char) currentChar);
                advance();
            }

            return new Symbol(SymbolType.SYMBOL, sb.toString());
        }

        // If unrecognized character is encountered
        //TODO:error handling


        try {
            throw new SyntaxErrorException("unrecognised token");
        }
        catch (SyntaxErrorException e) {
            Symbol unknown = new Symbol(SymbolType.SYMBOL, Character.toString((char) currentChar));
            advance();
            return unknown;
        }
    }
}
