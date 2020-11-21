import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WGraph_AlgoTest {
    weighted_graph g;


    @Test
    void emptyGraphCopy() {
        g = WGraph_DSTest.graph_creator(0, 0, 1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g);
        weighted_graph g2 = ag0.copy();
        weighted_graph_algorithms ag1 = new WGraph_Algo();
        ag1.init(g2);
        assertEquals(ag0,ag1);
    }

    @Test
    void copy() {
        g = WGraph_DSTest.graph_creator(50, 50*5, 1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g);
        weighted_graph g2 = ag0.copy();
        weighted_graph_algorithms ag1 = new WGraph_Algo();
        ag1.init(g2);
        assertEquals(ag0,ag1);
    }

    @Test
    void isConnected() {
        weighted_graph g0 = WGraph_DSTest.graph_creator(0, 0, 1);
        weighted_graph_algorithms ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(1, 0, 1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2, 0, 1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertFalse(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(2, 1, 1);
        ag0 = new WGraph_Algo();
        ag0.init(g0);
        assertTrue(ag0.isConnected());

        g0 = WGraph_DSTest.graph_creator(10, 30, 1);
        ag0.init(g0);
        boolean b = ag0.isConnected();
        Assertions.assertTrue(b);
    }

    @Test
    void shortestPathDist() {
        g = myGraph();
        weighted_graph_algorithms g0 = new WGraph_Algo();
        g0.init(g);
        assertTrue(g0.isConnected());
        double shortest = g0.shortestPathDist(0, 9);
        Assertions.assertEquals(10.4, shortest, 0.000001);
        assertEquals(0,g0.shortestPathDist(4,4));
    }


    @Test
    void shortestPath() {
        g = myGraph();
        weighted_graph_algorithms g0 = new WGraph_Algo();
        g0.init(g);
        List shortestP = g0.shortestPath(0, 9);
        Assertions.assertEquals(7, shortestP.size(), 0.000001);
        int path[] = listToArray(shortestP);
        int expected[] = {0, 6, 5, 4, 7, 10, 9};
        Assertions.assertArrayEquals(expected, path);
        List shortestP2 = g0.shortestPath(4, 4);
        int path2[] = listToArray(shortestP2);
        int expected2[] = {4};
        Assertions.assertArrayEquals(expected2, path2);

    }

    @Test
    void saveAndLoad() {
        g = WGraph_DSTest.graph_creator(30,30*4,1);
        weighted_graph_algorithms g0 = new WGraph_Algo();
        g0.init(g);
        g0.save("theGraph.obj");
        weighted_graph gra = WGraph_DSTest.graph_creator(30,30*4,1);
        weighted_graph_algorithms g1 = new WGraph_Algo();
        g1.init(gra);
        g0.load("theGraph.obj");
        assertEquals(g0,g1);
        gra.removeNode(10);
        g1.init(gra);
        assertNotEquals(g0,g1);

    }


    private weighted_graph myGraph() {
        weighted_graph g = new WGraph_DS();
        int nodeSize = 10;
        for (int i = 0; i <= nodeSize; i++) {
            g.addNode(i);
        }
        g.connect(0, 1, 4);
        g.connect(0, 2, 6.5);
        g.connect(0, 5, 2.6);
        g.connect(0, 6, 1);    //1
        g.connect(1, 9, 17.8);
        g.connect(2, 3, 2);
        g.connect(2, 8, 2);
        g.connect(3, 7, 2);
        g.connect(3, 8, 2);
        g.connect(4, 7, 3.7);  //8.7
        g.connect(4, 5, 2.5);  //5
        g.connect(4, 9, 6);
        g.connect(5, 6, 1.5);  //2.5
        g.connect(7, 9, 1.8);
        g.connect(7, 10, 0.5);  //9.2
        g.connect(9, 10, 1.2);  //10.4
        return g;
    }

    private int[] listToArray(List<node_info> lst){
        int nodeArr[] = new int[lst.size()];
        Iterator<node_info> itr = lst.listIterator();
        int i = 0;
        while(itr.hasNext()){
            node_info n = itr.next();
            nodeArr[i++] = n.getKey();
        }
        return nodeArr;
    }

}