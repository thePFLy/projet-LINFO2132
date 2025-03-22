package compiler.Parser;

public abstract class LexicalElement extends ASTree {
    @Override
    public void print() {
        System.out.println(this.toString());
    }

    //display tre
    @Override
    public void printTree(int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(this.toString());
    }

    @Override
    public abstract String toString();
}

class Type extends LexicalElement {
    private String name;

    public Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Type: " + name;
    }
}

//id
class Identifier extends LexicalElement {
    private String identifier;

    public Identifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return "Identifier: " + identifier;
    }
}