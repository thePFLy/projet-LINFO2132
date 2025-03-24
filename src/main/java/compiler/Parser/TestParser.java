package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Parser.AST.ProgramNode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

public class TestParser {

    private ProgramNode parse(String code) throws Exception {
        Lexer lexer = new Lexer(new StringReader(code));
        Parser parser = new Parser(lexer);
        return parser.getAST();
    }

    private void assertParsesSuccessfully(String code) throws Exception {
        Assertions.assertNotNull(parse(code));
    }

    private void assertParseFails(String code) {
        Assertions.assertThrows(ParserException.class, () -> parse(code));
    }

    // var
    @Test
    void testVariableDeclarations() throws Exception {
        assertParsesSuccessfully("int x;");
        assertParsesSuccessfully("float y = 3.14;");
        assertParsesSuccessfully("final bool flag = true;");
        assertParsesSuccessfully("string s = \"hello\";");

        assertParseFails("int x =");
        assertParseFails("final x = 5;");
    }

    // tab
    @Test
    void testArrays() throws Exception {
        assertParsesSuccessfully("int[] arr = array[10] of int;");
        assertParsesSuccessfully("arr[0] = 42;");
        assertParsesSuccessfully("int x = matrix[5][2];");

        assertParseFails("array[10] of int;");
        assertParseFails("arr[] = 5;");
    }

    // struc
    @Test
    void testControlStructures() throws Exception {
        assertParsesSuccessfully("if (x > 0) { return x; }");
        assertParsesSuccessfully("if (x) {} else {}");
        assertParsesSuccessfully("while (true) { break; }");
        assertParsesSuccessfully("for (i int = 0; i < 10; i = i + 1) {}");

        assertParseFails("if x > 0 {}");
        assertParseFails("while () {}");
    }

    // fun
    @Test
    void testFunctions() throws Exception {
        assertParsesSuccessfully("fun sum(a int, b int) int { return a + b; }");
        assertParsesSuccessfully("fun main() { print(\"Hello\"); }");
        assertParsesSuccessfully("fun max(a int, b int) { if (a > b) { return a; } return b; }");

        assertParseFails("fun add(a int, b int { }");
        assertParseFails("fun { return 0; }");
    }

    // rec
    @Test
    void testRecords() throws Exception {
        assertParsesSuccessfully("rec Point { x int; y int; }");
        assertParsesSuccessfully("Point p; p.x = 10; p.y = 20;");
        assertParsesSuccessfully("rec Data { id int; values float[]; }");

        assertParseFails("rec Point { x int y int; }");
        assertParseFails("rec { x int; }");
    }

    // expression
    @Test
    void testExpressions() throws Exception {
        assertParsesSuccessfully("int x = 2 + 3 * 4;");
        assertParsesSuccessfully("bool b = (x >= y) && (z != 0);");
        assertParsesSuccessfully("int r = add(1, 2 * 3) - 4;");
        assertParsesSuccessfully("int y = point.x + arr[0];");

        assertParseFails("int x = 2 + * 3;");
        assertParseFails("x = ;");
    }

    @Test
    void testEdgeCases() throws Exception {
        assertParsesSuccessfully(";");
        assertParsesSuccessfully("{}");

        assertParseFails("int");
        assertParseFails("x = y");
    }
}