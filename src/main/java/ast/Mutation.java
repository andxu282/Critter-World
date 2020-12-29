package ast;

import cms.util.maybe.Maybe;

/** A mutation to the AST */
public interface Mutation {
    /**
     * Compares the type of this mutation to {@code m}
     *
     * @param m The mutation to compare with
     * @return Whether this mutation is the same type as {@code m}
     */
    boolean equals(Mutation m);

    /**
     * Applies this mutation to the given {@code Node} within this {@code Program}
     *
     * @param program the program to mutated.
     * @param node the specific node to perform mutation on.
     * @return a mutated program or {@code Maybe.none} if the mutation is unsuccessful.
     */
    Maybe<Program> apply(Program program, Node node);

    /**
     * Returns true if and only if this type of mutation can be applied to the given node.
     * @param n the node to mutate
     * @return whether this mutation can be applied to {@code n}
     */
    boolean canApply(Node n);
}
