package compiler.Parser;

public class Type extends ASTree{
    String name;
    public Type(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public void print() {

    }
}
