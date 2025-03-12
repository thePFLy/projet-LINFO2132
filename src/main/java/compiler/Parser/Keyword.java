package compiler.Parser;

import org.checkerframework.checker.units.qual.A;

public class Keyword extends ASTree {
    private String keyword;
    public Keyword(String keyword) {
        this.keyword = keyword;
    }
    public String getKeyword() {
        return keyword;
    }

    @Override
    public void print() {

    }
}
