package compiler.Parser;

public class ParserException extends Exception {
    public ParserException(String wrongMatch, int line, String text) {
        super(wrongMatch);
        System.err.println(wrongMatch + " at line : " + line + " for " + text);
    }
}
