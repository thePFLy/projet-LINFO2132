package compiler.Parser;

public class Identifier extends ASTree{
    private String identifier;
    public Identifier(String identifier) {
        this.identifier = identifier;
    }
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public void print() {

    }
}
