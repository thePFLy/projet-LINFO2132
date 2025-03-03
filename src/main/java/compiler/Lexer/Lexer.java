package compiler.Lexer;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class Lexer {
    public enum SymbolType {
        IDENTIFIER, KEYWORD, INTEGER, FLOAT, STRING, BOOLEAN, SYMBOL, EOF, REC, FIELD_OPERATOR, TYPE
    }

    private final PushbackReader reader;
    private int currentChar;
    private int line = 1;
    private int column = 0;
    private static final String[] BOOLEAN_VALUES = {"true", "false"};
    private static final String SYMBOLS = "=+-*/%(){}[].,;<>!&|";
    private final ArrayList<String> KEYWORDS = new ArrayList<>(Arrays.asList(
            "free", "final", "rec", "fun", "for", "while", "if", "else", "return", "struct", "def", "break"));
    private static final ArrayList<String> BUILT_IN_FUNCTIONS = new ArrayList<>(Arrays.asList(
            "readInt", "readFloat", "readString", "writeInt", "writeFloat", "write", "writeln"
    ));
    private static final ArrayList<String> TYPES = new ArrayList<>(Arrays.asList(
            "int", "float", "string", "bool", "free"
    ));

    public Lexer(Reader input) {
        this.reader = new PushbackReader(input);
        advance();
    }

    private int nextCharView() {
        try {
            reader.mark(1);
            int nextChar = reader.read();
            reader.reset();
            return nextChar;
        } catch (IOException e) {
            return -1;
        }
    }


    private void advance() {
        try {
            currentChar = reader.read();
            column++;
            if (currentChar == '\n') {
                line++;
                column = 0;
            }
        } catch (IOException e) {
            currentChar = -1;
        }
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(currentChar)) {
            advance();
        }
    }

    public Symbol getNextSymbol() {
        skipWhitespace();

        if (currentChar == -1) return new Symbol(SymbolType.EOF, "EOF");

        // Comments
        if (currentChar == '$') {
            while (currentChar != '\n' && currentChar != -1) advance();
            return getNextSymbol();
        }

        // Identifiers, keywords
        if (Character.isLetter(currentChar) || currentChar == '_') {
            StringBuilder sb = new StringBuilder();
            while (Character.isLetterOrDigit(currentChar) || currentChar == '_') {
                sb.append((char) currentChar);
                advance();
            }
            String word = sb.toString();

            if (KEYWORDS.contains(word)) return new Symbol(SymbolType.KEYWORD, word);
            if (TYPES.contains(word)) return new Symbol(SymbolType.TYPE, word);
            if (Arrays.asList(BOOLEAN_VALUES).contains(word)) return new Symbol(SymbolType.BOOLEAN, word);
            if (BUILT_IN_FUNCTIONS.contains(word)) return new Symbol(SymbolType.IDENTIFIER, word);

            if (Character.isUpperCase(word.charAt(0))) {
                return new Symbol(SymbolType.REC, word);
            }

            return new Symbol(SymbolType.IDENTIFIER, word);
        }


        // Field operator (.)
        if (currentChar == '.') {
            advance();
            return new Symbol(SymbolType.FIELD_OPERATOR, ".");
        }

        // Numbers (integers and floats)
        if (Character.isDigit(currentChar) || currentChar == '.') {
            StringBuilder sb = new StringBuilder();
            boolean isFloat = false;

            // .125
            if (currentChar == '.') {
                if (!Character.isDigit(nextCharView())) {
                    LexerError.reportError(line, column, (char) currentChar);
                    advance();
                    return getNextSymbol();
                }
                sb.append('0');
                sb.append((char) currentChar);
                isFloat = true;
                advance();
            }

            while (Character.isDigit(currentChar)) {
                sb.append((char) currentChar);
                advance();
            }

            // 1 more .
            if (currentChar == '.') {
                if (isFloat) {
                    return new Symbol(SymbolType.FLOAT, sb.toString());
                }
                sb.append((char) currentChar); // Ajouter le point
                isFloat = true;
                advance();
                while (Character.isDigit(currentChar)) {
                    sb.append((char) currentChar);
                    advance();
                }
            }
            if (sb.toString().endsWith(".")) {
                sb.append('0');
            }

            return new Symbol(isFloat ? SymbolType.FLOAT : SymbolType.INTEGER, sb.toString());
        }

        // Strings
        if (currentChar == '"') {
            StringBuilder sb = new StringBuilder();
            advance();
            while (currentChar != '"' && currentChar != -1) {
                if (currentChar == '\\') {
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
            // not closed '
            if (currentChar == -1) {
                LexerError.reportError(line, column, ' ');
                return new Symbol(SymbolType.STRING, sb.toString());
            }
            advance();
            return new Symbol(SymbolType.STRING, sb.toString());
        }

        // Symbols and multi-character operators
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

        // Unrecognized character
        LexerError.reportError(line, column, (char) currentChar);
        advance();
        return getNextSymbol();
    }
}
