package compiler.Lexer;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class Lexer {
    public enum SymbolType {
        COMMENT, IDENTIFIER, KEYWORD, INTEGER, FLOAT, STRING, BOOLEAN, SYMBOL, EOF, FUNCTION, FUNCTION_CALL, USER_TYPE, RECORD, FIELD_OPERATOR
    }

    private final PushbackReader reader;
    private int currentChar;
    private int line = 1;
    private int column = 0;
    private static final String[] BOOLEAN_VALUES = {"true", "false"};
    private static final String SYMBOLS = "=+-*/%(){}[].,;<>!&|";
    private final ArrayList<String> KEYWORDS = new ArrayList<>(Arrays.asList("free", "final", "rec", "fun", "for", "while", "if", "else", "return"));

    public Lexer(Reader input) {
        this.reader = new PushbackReader(input);
        advance();
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

    private boolean isBuiltInFunction(String word) {
        String[] builtInFunctions = {
                "readInt", "readFloat", "readString", "writeInt", "writeFloat", "write", "writeln"
        };
        for (String func : builtInFunctions) {
            if (word.equals(func)) {
                return true;
            }
        }
        return false;
    }


    public Symbol getNextSymbol() {
        skipWhitespace();

        if (currentChar == -1) return new Symbol(SymbolType.EOF, "EOF");

        // comments
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

            if (KEYWORDS.contains(word)) return new Symbol(SymbolType.KEYWORD, word);
            if (Arrays.asList(BOOLEAN_VALUES).contains(word)) return new Symbol(SymbolType.BOOLEAN, word);
            if (isBuiltInFunction(word)) return new Symbol(SymbolType.KEYWORD, word);

            skipWhitespace();
            if (currentChar == '(') {
                advance();
                return new Symbol(SymbolType.FUNCTION_CALL, word);
            }

            return new Symbol(SymbolType.IDENTIFIER, word);
        }


        // field operator (.)
        if (currentChar == '.') {
            advance();
            return new Symbol(SymbolType.FIELD_OPERATOR, ".");
        }

        if (Character.isDigit(currentChar) || currentChar == '.') {
            StringBuilder sb = new StringBuilder();
            boolean isFloat = false;

            while (Character.isDigit(currentChar) || currentChar == '.') {
                if (currentChar == '.') {
                    if (isFloat) break;
                    isFloat = true;
                }
                sb.append((char) currentChar);
                advance();
            }
            return new Symbol(isFloat ? SymbolType.FLOAT : SymbolType.INTEGER, sb.toString());
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
            if (sb.toString().endsWith(".")) sb.append("0");

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
            advance();
            return new Symbol(SymbolType.STRING, sb.toString());
        }

        // Handle multi-character operators (==, !=, <=, >=, &&, ||, [], +)
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

            // rec
            if (currentChar == '{') {
                advance(); // Consume '{'
                do {
                    advance(); // Ignore everything inside
                } while (currentChar != '}' && currentChar != -1);
                if (currentChar == '}') {
                    advance(); // Consume '}'
                }
                return new Symbol(SymbolType.SYMBOL, "{...}");
            }

            // Concat +
            if (sb.charAt(0) == '+') {
                return new Symbol(SymbolType.SYMBOL, "+");
            }

            // Handle index operator '[' and ']'
            if (sb.charAt(0) == '[' || sb.charAt(0) == ']') {
                return new Symbol(SymbolType.SYMBOL, String.valueOf(sb.charAt(0)));
            }

            return new Symbol(SymbolType.SYMBOL, sb.toString());
        }

        // If unrecognized character is encountered
        LexerError.reportError(line, column, (char) currentChar);
        advance();
        return getNextSymbol();
    }
}
