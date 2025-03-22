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
        //Parser parser = createParser("if (x > 10) { return x; }");
    }

    //error
    @Test(expected = ParserException.class)
    public void testSyntaxError() throws ParserException {
        Parser parser = createParser("x + ");
        parser.parseExpression(); // ParserException ?
    }

    //tab
    @Test
    public void testArrayDeclaration() throws ParserException {
    }

    //function
    @Test
    public void testFunctionDeclaration() throws ParserException {
    }
}