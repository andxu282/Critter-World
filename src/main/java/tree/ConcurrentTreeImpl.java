package tree;

import maybe.Maybe;
import maybe.NoMaybeValue;

/**
 * An implementation of a concurrently accessible tree.  You are free to change anything
 * in this file, but there is no reason to change any of the functions other
 * than give and query. We have given you single-threaded versions as both
 * reference and something to test against. If you choose to change the
 * implementation of the tree, you will have to change insert and contains to
 * work with it. You could trivially do this by just returning the locking
 * versions, but it is nice to be able to see how many threads it takes for the
 * locking overhead to be worth it in terms of runtime.
 */
public class ConcurrentTreeImpl<T extends Comparable<T>> implements ConcurrentTree<T>
  {

    /**
     * A node of the concurrent tree. You are free to change anything you like
     * here so long as you implement the {@code ConcurrentTree} interface.
     */
    private class TNode
      {
			TNode parent;
        T value;
		RWLock lock = new RWLock();
        Maybe<TNode> left;
        Maybe<TNode> right;

        /**
         * Constructs a TNode given a value which cannot be null
         * @param val a non-null value
         */
		TNode(T val, TNode parent)
          {
            assert(val != null);
            this.value = val;
            this.left = Maybe.none();
            this.right = Maybe.none();
			this.parent = parent;
          }

        boolean isLeafLeft()
          {
            return this.left.equals(Maybe.none());
          }
        boolean isLeafRight()
          {
            return this.right.equals(Maybe.none());
          }
      }

    Maybe<TNode> root;

    public ConcurrentTreeImpl()
      {
        root = Maybe.none();
      }

    @Override
    public void insert(T val)
      {
        try
          {
            insert(root.get(), val);
          } catch (NoMaybeValue nmb)
          {
				root = Maybe.some(new TNode(val, null));
          }
      }

    private void insert(TNode nd, T val)
      {
        if (val.compareTo(nd.value) < 0)
          {
            try
              {
                insert(nd.left.get(), val);
              } catch (NoMaybeValue nmb)
              {
					nd.left = Maybe.some(new TNode(val, nd));
              }
          } else
          {
            try
              {
                insert(nd.right.get(), val);
              } catch (NoMaybeValue nmb)
              {
					nd.right = Maybe.some(new TNode(val, nd));
              }
          }
      }

		// insert but make it thread safe
		public void give(T val)
		{
			try {
				give(root.get(), val);
			} catch (NoMaybeValue nmb) {
				root = Maybe.some(new TNode(val, null));
			}
			// throw new UnsupportedOperationException();
      }

		public void give(TNode nd, T val) {
			if (nd.parent != null) {
				nd.parent.lock.writeLock().unlock();
			}

			nd.lock.writeLock().lock();

			if (val.compareTo(nd.value) < 0) {
				try {
					nd.left.get();
					give(nd.left.get(), val);
				} catch (NoMaybeValue nmb) {
					nd.lock.writeLock().unlock();
					nd.left = Maybe.some(new TNode(val, nd));
				}
			} else {
				try {
					nd.right.get();
					give(nd.right.get(), val);
				} catch (NoMaybeValue nmb) {
					nd.lock.writeLock().unlock();
					nd.right = Maybe.some(new TNode(val, nd));
				}
			}
		}

    public boolean contains(T val)
      {
        return contains(root, val);
      }

    private boolean contains(Maybe<TNode> nd, T val)
      {
        TNode node;
        try
          {
            node = nd.get();
          } catch (NoMaybeValue nmb) {return false;}
        if (val.compareTo(node.value) < 0)
          return contains(node.left, val);
        else if (val.compareTo(node.value) > 0)
          return contains(node.right, val);
        return true;
      }

    public boolean query(T val)
      {
			return contains(root, val);

      }


		public boolean query(Maybe<TNode> nd, T val) {
			TNode node;
			try {
				node = nd.get();
				if (node.parent != null) {
				node.parent.lock.readLock().unlock();
				}

				node.lock.readLock().lock();
			} catch (NoMaybeValue nmb) {
				return false;
			}
			if (val.compareTo(node.value) < 0) {
				return contains(node.left, val);
			} else if (val.compareTo(node.value) > 0) {
				return contains(node.right, val);
			}
			node.lock.readLock().unlock();
			return true;
		}

    public void inOrder()
      {
        inOrder(root);
        System.out.println();
      }

    private void inOrder(Maybe<TNode> node)
      {
        TNode nd;
        try
          {
            nd = node.get();
          } catch (NoMaybeValue nmb) {return;}
        inOrder(nd.left);
        System.out.print(nd.value + " ");
        inOrder(nd.right);
      }

    public void preOrder()
      {
        preOrder(root);
        System.out.println();
      }

    void preOrder(Maybe<TNode> node)
      {
        TNode nd;
        try
          {
            nd = node.get();
          } catch (NoMaybeValue nmb) {return;}
        System.out.print(nd.value + " ");
        preOrder(nd.left);
        preOrder(nd.right);
      }

  }
