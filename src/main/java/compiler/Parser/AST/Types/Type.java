package compiler.Parser.AST.Types;

import compiler.Parser.AST.ASTNode;

public abstract class Type implements ASTNode {
    public abstract String getTypeName();
    public abstract boolean equals(Type other);
}