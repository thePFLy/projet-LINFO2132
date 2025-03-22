package compiler.Parser;

public abstract class ASTree {
    public void printTree(int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(this.toString());
    }

    public void print() {
        System.out.println(this.toString());
    }

    public abstract String toString();
}