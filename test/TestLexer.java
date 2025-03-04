import compiler.Lexer.Symbol;
import compiler.Lexer.Lexer;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import static org.junit.Assert.*;

public class TestLexer {

    private Lexer createTmpLexer(String input) {
        return new Lexer(new StringReader(input));
    }

    private void assertSymbol(Lexer lexer, Lexer.SymbolType expectedType, String expectedName) throws IOException {
        Symbol symbol = lexer.getNextSymbol();
        assertEquals(expectedType, symbol.getType());
        assertEquals(expectedName, symbol.getName());
    }

    @Test
    public void testOperators() throws IOException {
        Lexer lexer = createTmpLexer("-+");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "-");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "+");
    }

    @Test
    public void testNumber() throws IOException {
        Lexer lexer = createTmpLexer("78915");
        assertSymbol(lexer, Lexer.SymbolType.INTEGER, "78915");
    }

    @Test
    public void testWhitespace() throws IOException {
        Lexer lexer = createTmpLexer("var  x    =  2");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "var");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "x");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "=");
        assertSymbol(lexer, Lexer.SymbolType.INTEGER, "2");
    }

    @Test
    public void testComment() throws IOException {
        Lexer lexer = createTmpLexer("$var x= 2\n int =");
        assertSymbol(lexer, Lexer.SymbolType.TYPE, "int");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "=");
    }

    @Test
    public void testIdentifier() throws IOException {
        Lexer lexer = createTmpLexer("var_x2 = 10\n int =");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "var_x2");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "=");
        assertSymbol(lexer, Lexer.SymbolType.INTEGER, "10");
        assertSymbol(lexer, Lexer.SymbolType.TYPE, "int");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "=");
    }

    @Test
    public void testFloat() throws IOException {
        Lexer lexer = createTmpLexer(" x = 0.125.3");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "x");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "=");
        assertSymbol(lexer, Lexer.SymbolType.FLOAT, "0.125");
    }

    @Test
    public void test() throws IOException {
        Lexer lexer = createTmpLexer("var x int = 2;");
        assertNotNull(lexer.getNextSymbol());
    }

    @Test
    public void testPointCode() throws IOException {
        Lexer lexer = createTmpLexer("Point rec { x int; y int; }");
        assertSymbol(lexer, Lexer.SymbolType.REC, "Point");
        assertSymbol(lexer, Lexer.SymbolType.KEYWORD, "rec");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "{");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "x");
        assertSymbol(lexer, Lexer.SymbolType.TYPE, "int");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ";");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "y");
        assertSymbol(lexer, Lexer.SymbolType.TYPE, "int");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ";");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "}");
    }

    @Test
    public void testPersonCode() throws IOException {
        Lexer lexer = createTmpLexer("Person rec { name string; location Point; history int[]; }");
        assertSymbol(lexer, Lexer.SymbolType.REC, "Person");
        assertSymbol(lexer, Lexer.SymbolType.KEYWORD, "rec");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "{");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "name");
        assertSymbol(lexer, Lexer.SymbolType.TYPE, "string");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ";");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "location");
        assertSymbol(lexer, Lexer.SymbolType.REC, "Point");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ";");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "history");
        assertSymbol(lexer, Lexer.SymbolType.TYPE, "int");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "[");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "]");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ";");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "}");
    }

    @Test
    public void testSquareCode() throws IOException {
        Lexer lexer = createTmpLexer("fun square(int v) int { return v*v; }");
        assertSymbol(lexer, Lexer.SymbolType.KEYWORD, "fun");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "square");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "(");
        assertSymbol(lexer, Lexer.SymbolType.TYPE, "int");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "v");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ")");
        assertSymbol(lexer, Lexer.SymbolType.TYPE, "int");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "{");
        assertSymbol(lexer, Lexer.SymbolType.KEYWORD, "return");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "v");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "*");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "v");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ";");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "}");
    }

    @Test
    public void testCopyCode() throws IOException {
        Lexer lexer = createTmpLexer("fun copyPoints(Point[] p) Point { return Point(p[0].x+p[1].x, p[0].y+p[1].y); }");
        assertSymbol(lexer, Lexer.SymbolType.KEYWORD, "fun");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "copyPoints");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "(");
        assertSymbol(lexer, Lexer.SymbolType.REC, "Point");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "[");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "]");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "p");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ")");
        assertSymbol(lexer, Lexer.SymbolType.REC, "Point");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "{");
        assertSymbol(lexer, Lexer.SymbolType.KEYWORD, "return");
        assertSymbol(lexer, Lexer.SymbolType.REC, "Point");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "(");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "p");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "[");
        assertSymbol(lexer, Lexer.SymbolType.INTEGER, "0");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "]");
        assertSymbol(lexer, Lexer.SymbolType.FIELD_OPERATOR, ".");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "x");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "+");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "p");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "[");
        assertSymbol(lexer, Lexer.SymbolType.INTEGER, "1");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "]");
        assertSymbol(lexer, Lexer.SymbolType.FIELD_OPERATOR, ".");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "x");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ",");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "p");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "[");
        assertSymbol(lexer, Lexer.SymbolType.INTEGER, "0");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "]");
        assertSymbol(lexer, Lexer.SymbolType.FIELD_OPERATOR, ".");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "y");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "+");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "p");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "[");
        assertSymbol(lexer, Lexer.SymbolType.INTEGER, "1");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "]");
        assertSymbol(lexer, Lexer.SymbolType.FIELD_OPERATOR, ".");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "y");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ")");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ";");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "}");
    }

    @Test
    public void testMainCode() throws IOException {
        Lexer lexer = createTmpLexer("fun main() {\n" +
                "    value int = readInt();\n" +
                "    writeln(square(value));\n" +
                "    i int;\n" +
                "    for (i, 1, 100, 1) {\n" +
                "        while (value != 3) {\n" +
                "            if (i > 10) {\n" +
                "                $ ....\n" +
                "            } else {\n" +
                "                $ ....\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    i = (i + 2) * 2;\n" +
                "}");
        assertSymbol(lexer, Lexer.SymbolType.KEYWORD, "fun");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "main");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "(");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ")");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "{");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "value");
        assertSymbol(lexer, Lexer.SymbolType.TYPE, "int");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "=");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "readInt");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "(");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ")");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ";");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "writeln");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "(");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "square");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, "(");
        assertSymbol(lexer, Lexer.SymbolType.IDENTIFIER, "value");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ")");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ")");
        assertSymbol(lexer, Lexer.SymbolType.SYMBOL, ";");
    }
}