package compiler;

import compiler.Lexer.Lexer;
import compiler.Lexer.Symbol;
import compiler.Parser.Parser;
import compiler.Parser.AST.ProgramNode;
import java.io.FileReader;
import java.io.Reader;

public class Compiler {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: -mode <file>");
            System.out.println("Modes: -lexer, -parser");
            return;
        }

        String mode = args[0];
        String file = args[1];

        try (Reader reader = new FileReader(file)) {
            Lexer lexer = new Lexer(reader);

            if (mode.equals("-lexer")) {
                Symbol symbol;
                while ((symbol = lexer.getNextSymbol()).getType() != Lexer.SymbolType.EOF) {
                    System.out.println("Token: " + symbol.getType() + " \tValue: " + symbol.getName());
                }
            } else if (mode.equals("-parser")) {
                Parser parser = new Parser(lexer);
                ProgramNode ast = parser.getAST();
                System.out.println("AST contains " + ast.statements.size() + " top-level statements");
            } else {
                System.out.println("Invalid mode. Use -lexer or -parser");
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}