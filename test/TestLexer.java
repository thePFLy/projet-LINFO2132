import compiler.Lexer.Symbol;
import compiler.Lexer.Lexer;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import static org.junit.Assert.*;

public class TestLexer {

    @Test
    public void testOperators() throws IOException {
        String input = "-+/*";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);

        assertEquals(Lexer.SymbolType.SYMBOL, lexer.getNextSymbol().getType());
        assertEquals("-", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.SYMBOL, lexer.getNextSymbol().getType());
        assertEquals("+", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.SYMBOL, lexer.getNextSymbol().getType());
        assertEquals("/", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.SYMBOL, lexer.getNextSymbol().getType());
        assertEquals("*", lexer.getNextSymbol().getName());
    }

    @Test
    public void testCp() throws IOException {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        int tokenCount = 0;

        Symbol symbol;
        while ((symbol = lexer.getNextSymbol()).getType() != Lexer.SymbolType.EOF) {
            System.out.println("<Symbol: " + symbol.getType() + ", Lexeme: " + symbol.getName() + ">");
            tokenCount++;
        }

        assertEquals(6, tokenCount); // var, x, int, =, 2, ;
    }

    @Test
    public void testNumber() throws IOException {
        String input = "78915";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertEquals(Lexer.SymbolType.INTEGER, lexer.getNextSymbol().getType());
    }

    @Test
    public void testWhitespace() throws IOException {
        String input = "var    x    =  2";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);

        assertEquals(Lexer.SymbolType.IDENTIFIER, lexer.getNextSymbol().getType());
        assertEquals("var", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.IDENTIFIER, lexer.getNextSymbol().getType());
        assertEquals("x", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.SYMBOL, lexer.getNextSymbol().getType());
        assertEquals("=", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.INTEGER, lexer.getNextSymbol().getType());
        assertEquals("2", lexer.getNextSymbol().getName());
    }

    @Test
    public void testComment() throws IOException {
        String input = "$var x= 2\n int =";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);

        assertEquals(Lexer.SymbolType.KEYWORD, lexer.getNextSymbol().getType());
        assertEquals("int", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.SYMBOL, lexer.getNextSymbol().getType());
        assertEquals("=", lexer.getNextSymbol().getName());
    }

    @Test
    public void testIdentifier() throws IOException {
        String input = "var_x2 = 10\n int =";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);

        assertEquals(Lexer.SymbolType.IDENTIFIER, lexer.getNextSymbol().getType());
        assertEquals("var_x2", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.SYMBOL, lexer.getNextSymbol().getType());
        assertEquals("=", lexer.getNextSymbol().getName());
    }

    @Test
    public void testKeyWord() throws IOException {
        String input = "final int x = 2\n int =";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);

        assertEquals(Lexer.SymbolType.KEYWORD, lexer.getNextSymbol().getType());
        assertEquals("final", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.KEYWORD, lexer.getNextSymbol().getType());
        assertEquals("int", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.IDENTIFIER, lexer.getNextSymbol().getType());
        assertEquals("x", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.SYMBOL, lexer.getNextSymbol().getType());
        assertEquals("=", lexer.getNextSymbol().getName());
    }

    @Test
    public void testFloat() throws IOException {
        String input = " x = 0.2345";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertEquals(Lexer.SymbolType.IDENTIFIER, lexer.getNextSymbol().getType());
        assertEquals("x", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.SYMBOL, lexer.getNextSymbol().getType());
        assertEquals("=", lexer.getNextSymbol().getName());
        assertEquals(Lexer.SymbolType.FLOAT, lexer.getNextSymbol().getType());
        assertEquals("0.2345", lexer.getNextSymbol().getName());
    }

    @Test
    public void testExample() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("code_example2025.txt"))) {
            Lexer lexer = new Lexer(reader);
            assertEquals(Lexer.SymbolType.KEYWORD, lexer.getNextSymbol().getType());
            assertEquals("final", lexer.getNextSymbol().getName());
            assertEquals(Lexer.SymbolType.IDENTIFIER, lexer.getNextSymbol().getType());
            assertEquals("message", lexer.getNextSymbol().getName());
            assertEquals(Lexer.SymbolType.KEYWORD, lexer.getNextSymbol().getType());
            assertEquals("rec", lexer.getNextSymbol().getName());
        }
    }

    @Test
    public void test() throws IOException {
        String input = "var x int = 2;";
        StringReader reader = new StringReader(input);
        Lexer lexer = new Lexer(reader);
        assertNotNull(lexer.getNextSymbol());
    }
}
