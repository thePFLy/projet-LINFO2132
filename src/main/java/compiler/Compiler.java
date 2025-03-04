package compiler;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;

public class Compiler {
    public static void main(String[] args) throws IOException {
        if (args.length != 2 || !args[0].equals("-lexer")) {
            System.out.println("need: -lexer <file>");
            return;
        }
        String file = args[1];
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
