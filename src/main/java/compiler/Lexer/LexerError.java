package compiler.Lexer;

public class LexerError {
    public static void reportError(int line, int column, char unrecognizedChar) {
        System.err.println("Error for Lexer at line" + line +  ", column " + column + ", unexpected char: " + unrecognizedChar);
    }
}
