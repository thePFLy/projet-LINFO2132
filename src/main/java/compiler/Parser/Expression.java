package compiler.Parser;

public abstract class Expression extends ASTree {
    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public abstract String toString();

    @Override
    public void printTree(int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(this.toString());
    }
}

//binary
class BinaryExpression extends Expression {
    private ASTree left;
    private String operator;
    private ASTree right;

    public BinaryExpression(ASTree left, String operator, ASTree right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public String toString() {
        return "BinaryExpression: " + operator;
    }

    @Override
    public void printTree(int level) {
        super.printTree(level);
        left.printTree(level + 1); //sous-arbre de gauche
        right.printTree(level + 1); //sous-arbre droit
    }
}

//litterals
class Literal extends Expression {
    private String value;

    public Literal(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Literal: " + value;
    }
}

//if, while
class Keyword extends Expression {
    private String keyword;

    public Keyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    @Override
    public String toString() {
        return "Keyword: " + keyword;
    }
}