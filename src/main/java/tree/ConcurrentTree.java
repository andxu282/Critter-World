package tree;

/**
 * A concurrent binary search tree.
 * Do not change this file; we will be grading query() and insert().
 * @param <T>
 */
public interface ConcurrentTree<T extends Comparable<T>> {
    /**
     * Insert a value without doing any rebalancing. This method
     * is not thread-safe.
     * @param val the value to insert
     */
    void insert(T val);

    /**
     * Insert a value in a thread-safe way.
     * Rebalancing is not done.
     * @param val the value to give to the tree
     */
    void give(T val);

    /**
     * Return whether a value is in in the tree. Not thread-safe.
     * @param val the value to lookup
     * @return whether val is in the tree
     */
    boolean contains(T val);

    /**
     * Return whether a value is in the tree in a thread-safe way.
     * @param val the value to look for
     * @return whether or not val was in the tree
     */
    boolean query(T val);

    /**
     * Print out the values in the tree using a preorder traversal.
     */
    void preOrder();

    /**
     * Print out the values in the tree using an in-order traversal.
     */
    void inOrder();
}
