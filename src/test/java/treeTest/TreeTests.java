package treeTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import org.junit.jupiter.api.Test;

import tree.ConcurrentTree;
import util.ConcurrentTreeFactory;

//note this depends on the RWLock class which is the one from class with a minor change
public class TreeTests
  {
    private long sleep = 100;
    volatile boolean failed = false;

    /**
     * helper function to get a tree of Strings
     * @return string tree
     */
    private ConcurrentTree<String> getTree()
      {
        return ConcurrentTreeFactory.makeStringTree();
      }

    /**
     * helper function to get a tree of Integers
     * @return integer tree
     */
    private ConcurrentTree<Integer> getITree()
      {
        return ConcurrentTreeFactory.makeIntTree();
      }

    /**
     * get a list of random integers
     * @param length # of elements
     * @return the list
     */
    private ArrayList<Integer> getList(int length)
      {
        ArrayList<Integer> lst = new ArrayList<>();
        for (int i = 0; i < length; ++i)
          lst.add((int) (Math.random() * 0x7fffffff));
        return lst;
      }


    /**
     * tests two threads giving values at once.
     * If testQuery is set to true, also queries those values back
     * @throws InterruptedException
     */
    @Test
    public void testGiveAndQuery() throws InterruptedException
      {
        boolean testQuery = true;
        var tree = getTree();
        Thread t1 = new Thread(() ->
        {
          for (char c : "qwertyuiopasdfghjklzxcvbnm".toCharArray())
            {
              tree.give(String.valueOf(c));
              try
                {
                  Thread.sleep(sleep);
                  if(testQuery)
                    assertTrue(tree.query(String.valueOf(c)));
                } catch (InterruptedException e)
                {
                  e.printStackTrace();
                  failed = true;
                }
            }
        });
        Thread t2 = new Thread(() ->
        {
          for (char c : "qwertyuiopasdfghjklzxcvbnm".toUpperCase().toCharArray())
            {
              tree.give(String.valueOf(c));
              try
                {
                  Thread.sleep(sleep);
                  if(testQuery)
                    assertTrue(tree.query(String.valueOf(c)));
                } catch (InterruptedException e)
                {
                  e.printStackTrace();
                  failed = true;
                }
            }
        });
        if (failed)
          {
            failed = false;
            fail();
          }
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        tree.preOrder();
        tree.inOrder();
        for (char c : "qwertyuiopasdfghjklzxcvbnm".toCharArray())
          {
				assertTrue(tree.contains(String.valueOf(c)));
            assertTrue(tree.contains(String.valueOf(c).toUpperCase()));
          }
      }

    /**
     * Runs tests on n threads, multiple times, averages results, and writes them
     * @param n number of threads to use
     * @param lst the list of integers to pass in
     * @param os the stream to write the results to
     * @param numRuns and the number of runs to average over
     */
    public void testNThreads(int n, ArrayList<Integer> lst, OutputStream os, int numRuns)
      {
        float avgPerf = 0;
        long avgTime = 0;
        for (int k = 0; k < numRuns; ++k)
          {
            var threadList = new ArrayList<Thread>();

            var tree = getITree();
            for (int i = 0; i < n; ++i)
              {
                int finalI = i;
                threadList.add(new Thread(() ->
                {
                  for (int j = finalI; j < lst.size(); j += n)
                    {
                      tree.give(lst.get(j));
                      assertTrue(tree.query(lst.get(j)));
                    }
                  for (int j = finalI; j < lst.size(); j += n)
                    assertTrue(tree.query(lst.get(j)));
                }));
              }
            long tStart = System.nanoTime();
            for (Thread t : threadList)
              t.start();
            for (Thread t : threadList)
              try
                {
                  t.join();
                } catch (InterruptedException i)
                {
                  i.printStackTrace();
                }
            long rsp = (System.nanoTime() - tStart) / 1000000;
            avgPerf += 1f / (float) rsp;
            avgTime += rsp;
          }
        avgPerf /= (float) numRuns;
        avgTime /= numRuns;
        try
          {
            os.write((n + ", " + avgPerf + ", " + avgTime + "\n").getBytes());
          } catch (IOException ignored) {}
      }

    /**
     * Runs tests for 1 to n threads where n is the number of logical cores on your machine
     * if you do too many more than this the overhead from the threads will outweigh any concurrency
     * advantages. Feel free to set this to go higher to experiment with it. Also tests insertion
     * and lookups without any locks, as well as AVL insertion with lookups (also without locks)
     *
     * Results are stored in a file perfX.csv where X is the number of times you have run this function
     * This gives you a mehtod of measuring how concurrent your tree is. If it doesn't improve at all
     * with more threads, you are probably being too conservative with your locking. If you are having
     * correctness issues, then you want to lock more.
     */
    @Test
    public void testAll()
      {
        int numRuns = 10;
        var prefs = Preferences.userNodeForPackage(ConcurrentTree.class);
        int run = prefs.getInt("Test Number", 0);
        var lst = getList(500000);
        FileOutputStream fo = null;
        try
          {
            fo = new FileOutputStream(new File("perf" + run + ".csv"));
          } catch (IOException ignored) {}
			for (int i = 1; i <= Runtime.getRuntime().availableProcessors(); ++i) {
				System.out.println(i);
				testNThreads(i, lst, fo, numRuns);
			}
        long tAvg = 0;
        for(int i = 0; i < numRuns; ++i)
          {
            var tree = getITree();
            long tInit = System.nanoTime();
            for(int j : lst)
              {
                tree.insert(j);
                assertTrue(tree.contains(j));
              }
            for (int j : lst)
              assertTrue(tree.contains(j));
            tAvg += System.nanoTime() - tInit;
          }
        tAvg /= (numRuns * 1000000);
        try
          {
            fo.write(("single, " + 1f / tAvg + ", " + tAvg + "\n").getBytes());
          } catch (IOException ignored) {}

        try
          {
            fo.close();
          } catch (IOException ignored) {}
        prefs.putInt("Test Number", run + 1);

      }



  }
