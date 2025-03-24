package compiler.Parser;

import compiler.Lexer.Lexer;
import compiler.Parser.AST.*;
import compiler.Parser.AST.Declarations.*;
import compiler.Parser.AST.Expressions.*;
import compiler.Parser.AST.Statements.*;
import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.*;

public class TestParser {

    private ProgramNode parse(String input) throws Exception {
        Lexer lexer = new Lexer(new StringReader(input));
        Parser parser = new Parser(lexer);
        return parser.getAST();
    }

    @Test
    public void testVariableDeclaration() throws Exception {
        ProgramNode ast = parse("int x;");
        assertEquals(1, ast.statements.size());
        VariableDeclaration decl = (VariableDeclaration) ast.statements.get(0);
        assertEquals("int", decl.type.getTypeName());
        assertEquals("x", decl.identifier);
        assertNull(decl.initialValue);
    }

    @Test
    public void testInitializedVariable() throws Exception {
        ProgramNode ast = parse("final float pi = 3.14;");
        VariableDeclaration decl = (VariableDeclaration) ast.statements.get(0);
        assertTrue(decl.isFinal);
        assertEquals("float", decl.type.getTypeName());
        assertTrue(decl.initialValue instanceof Literal);
    }

    @Test
    public void testArrayDeclaration() throws Exception {
        ProgramNode ast = parse("int[] arr = array[10] of int;");
        VariableDeclaration varDecl = (VariableDeclaration) ast.statements.get(0);
        assertEquals("int[]", varDecl.type.getTypeName());
        assertEquals("arr", varDecl.identifier);
        assertTrue(varDecl.initialValue instanceof ArrayCreation);

        ArrayCreation arrayCreation = (ArrayCreation) varDecl.initialValue;
        assertEquals("int", arrayCreation.elementType.getTypeName());
        assertTrue(arrayCreation.size instanceof Literal);
    }

    @Test
    public void testFunctionDeclaration() throws Exception {
        String code = "fun add(a int, b int) int { return a + b; }";
        ProgramNode ast = parse(code);
        FunctionDeclaration func = (FunctionDeclaration) ast.statements.get(0);

        assertEquals("add", func.name);
        assertEquals(2, func.parameters.size());
        assertEquals("int", func.returnType.getTypeName());
        assertTrue(func.body.statements.get(0) instanceof ReturnStatement);
    }

    @Test
    public void testRecordDeclaration() throws Exception {
        String code = "rec Point { x int; y int; }";
        ProgramNode ast = parse(code);
        RecordDeclaration rec = (RecordDeclaration) ast.statements.get(0);

        assertEquals("Point", rec.name);
        assertEquals(2, rec.fields.size());
        assertEquals("x", rec.fields.get(0).name);
        assertEquals("int", rec.fields.get(0).type.getTypeName());
    }

    @Test
    public void testForLoop() throws Exception {
        String code = "for (i int = 0; i < 10; i = i + 1) { print(i); }";
        ProgramNode ast = parse(code);
        ForLoop forLoop = (ForLoop) ast.statements.get(0);

        assertTrue(forLoop.initialization instanceof VariableDeclaration);
        assertTrue(forLoop.condition instanceof BinaryExpression);
        assertTrue(forLoop.update instanceof Assignment);
    }

    @Test
    public void testWhileLoop() throws Exception {
        String code = "while (x > 0) { x = x - 1; }";
        ProgramNode ast = parse(code);
        WhileLoop whileLoop = (WhileLoop) ast.statements.get(0);
        assertTrue(whileLoop.condition instanceof BinaryExpression);
    }

    @Test
    public void testBinaryExpressions() throws Exception {
        ProgramNode ast = parse("x = 2 + 3 * 4;");
        Assignment assign = (Assignment) ast.statements.get(0);
        BinaryExpression expr = (BinaryExpression) assign.value;
        assertEquals("+", expr.operator);
        assertTrue(expr.right instanceof BinaryExpression); // Vérifie la priorité des opérateurs
    }

    @Test(expected = Exception.class)
    public void testInvalidSyntax() throws Exception {
        parse("int x ="); // Syntaxe invalide
    }

    @Test
    public void testComplexProgram() throws Exception {
        String code = """
            final float PI = 3.14;
            
            rec Circle {
                radius float;
                area float;
            }
            
            fun calculateArea(c Circle) float {
                c.area = PI * c.radius * c.radius;
                return c.area;
            }
            
            fun main() {
                Circle c;
                c.radius = 5.0;
                float area = calculateArea(c);
                write(area);
            }
            """;

        ProgramNode ast = parse(code);
        assertEquals(4, ast.statements.size());
        assertTrue(ast.statements.get(0) instanceof VariableDeclaration);
        assertTrue(ast.statements.get(1) instanceof RecordDeclaration);
        assertTrue(ast.statements.get(2) instanceof FunctionDeclaration);
        assertTrue(ast.statements.get(3) instanceof FunctionDeclaration);
    }
}