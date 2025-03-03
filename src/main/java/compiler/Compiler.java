package compiler;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;

public class Compiler {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Need one argument");
            return;
        }
        String file = args[0];
        // file reading
        Reader reader = new FileReader(file);
        // lexer file
        Lexer lexer = new Lexer(reader);
        Symbol symbol;
        // print tokens
        while ((symbol = lexer.getNextSymbol()).getType() != Lexer.SymbolType.EOF) {
            System.out.println("token: " + symbol.getType() + " value: " + symbol.getName());
        }
        reader.close();
    }
}
