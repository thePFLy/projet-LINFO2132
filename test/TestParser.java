package compiler.Parser;

import compiler.Lexer.Lexer;
import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.*;

public class TestParser {

    private Parser createParser(String input) {
        return new Parser(new Lexer(new StringReader(input)));
    }

    @Test
    public void testSimpleAddition() throws ParserException {
        Parser parser = createParser("10 + 20");
        ASTree ast = parser.parseExpression();
        ast.printTree(0);
    }

    @Test
    public void testVariableDeclaration() throws ParserException {
        Parser parser = createParser("x int = 10 + 20;");
        ASTree ast = parser.parseVariableDeclaration();
        ast.printTree(0);
    }

    // (if-else)
    @Test
    public void testIfStatement() throws ParserException {
        Parser parser = createParser("if (x > 10) { return x; } else { return 0; }");
        ASTree ast = parser.parseIfStatement();
        ast.printTree(0);
    }

    //while
    @Test
    public void testWhileLoop() throws ParserException {
        Parser parser = createParser("while (x > 0) { x = x - 1; }");
        ASTree ast = parser.parseWhileLoop();
        ast.printTree(0);
    }

    //tab
    @Test
    public void testArrayDeclaration() throws ParserException {
        Parser parser = createParser("arr int[] = array [10] of int;");
        ASTree ast = parser.parseArrayDeclaration();
        ast.printTree(0); // Affiche l'arbre pour vérification manuelle
    }

    //function
    @Test
    public void testFunctionDeclaration() throws ParserException {
        Parser parser = createParser("fun add(a int, b int) int { return a + b; }");
        ASTree ast = parser.parseFunctionDeclaration();
        ast.printTree(0);
    }

    //error
    @Test(expected = ParserException.class)
    public void testSyntaxError() throws ParserException {
        Parser parser = createParser("x + ");
        parser.parseExpression(); // ParserException ?
    }
}