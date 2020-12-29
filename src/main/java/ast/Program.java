package ast;

import cms.util.maybe.Maybe;

/** An abstraction of a critter program. */
public interface Program extends Node {
    /**
     * Mutates this program with a single mutation
     *
     * @return The root of the mutated AST
     */
    Program mutate();

    /**
     * Mutates {@code nodeAt(index)} (and not its children) with mutation {@code m}.
     *
     * @param index The index of the node to mutate
     * @param m The mutation to perform on the node
     * @return The mutated program, or {@code Maybe.none} if {@code m}
     *         represents an invalid mutation for the node at {@code index}
     * @throws IndexOutOfBoundsException if {@code index} is not valid
     */
    Maybe<Program> mutate(int index, Mutation m);

    /**
     * Returns a {@code Node} in this {@code Program} whose {@code NodeCategory} matches {@code type}. If no such
     * node exists, returns {@code Maybe.none}.
     *
     * @param type the
     * @return A node with category {@code type}, or {@code Maybe.none} if no such node is found
     */
    Maybe<Node> findNodeOfType(NodeCategory type);
    
	/**
	 * Called After AST is created to created parent links in the entire tree
	 * Requires: must be invoked after AST has been created and invoked on the
	 * program node
	 */
    void establishParentLinks22();
}
