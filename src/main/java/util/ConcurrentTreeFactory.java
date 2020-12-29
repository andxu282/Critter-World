package util;

import tree.*;

public class ConcurrentTreeFactory
  {
    public static ConcurrentTree<String> makeStringTree()
      {
        return new ConcurrentTreeImpl<>();
      }
    public static ConcurrentTree<Integer> makeIntTree()
      {
        return new ConcurrentTreeImpl<Integer>();
      }
  }
