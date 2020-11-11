package ex1;

import java.util.*;
import java.io.*;

/**
 * This class is an implementation of weighted_graph_algorithms interface.
 * WGraph_Algo class implement undirected (positive) Weighted Graph Theory algorithms including:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected();
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file);
 * 6. Load(file);
 *
 * @author itai.lashover
 */
public class WGraph_Algo implements weighted_graph_algorithms, java.io.Serializable {

    /**
     * The only field in the class is a graph on which we want to perform the methods.
     */
    private weighted_graph wg;


    /**
     * Default constructor
     */
    public WGraph_Algo() {
        this.wg = new WGraph_DS();
    }

    /**
     * This method initializes the graph on which this set of algorithms operates.
     *
     * @param  g
     */
    @Override
    public void init(weighted_graph g) {
        this.wg = g;

    }

    /**
     * This method returns the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public weighted_graph getGraph() {
        return this.wg;
    }

    /**
     * This method computes a deep copy of this graph.
     * The method does this by using the deep copy constructor in WGraph_DS.
     *
     * @return identical graph.
     */
    @Override
    public weighted_graph copy() {
        return new WGraph_DS(this.wg);
    }

    /**
     * This method returns true iff there is a valid path from every node to each other node.
     * The method uses BFS algorithm.
     * Note: BFS method changes the value of each node's tag.
     * Thus the method calls resetInfo function that resets the info that changed.
     * Complexity: O(|V|+|E|), |V|=number of nodes, |E|=number of edges.
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        if (this.wg.nodeSize() == 0) {
            return true;
        }
        boolean b = this.bfs(this.wg);
        resetInfo();
        return b;
    }

    /**
     * This method returns the length of the shortest path between src to dest.
     * Note: if no such path --> returns -1
     * The method uses a combination of BFS and Dijkstra's algorithms.
     * Note2: Dijkstra method changes the value of each node's tag, info and pre.
     * Thus the method calls resetTag, resetInfo and resetPre functions that resets the tag ,the info and rhe pre that changed.
     * Complexity: O(|V|+|E|), |V|=number of nodes, |E|=number of edges.
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return the length of the shortest path between src to dest, -1 if there is no path.
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        double d = Dijkstra(this.wg.getNode(src), this.wg.getNode(dest));
        resetInfo();
        resetTag();
        resetPre();
        if (d == Integer.MAX_VALUE) {
            return -1;
        }
        return d;

    }

    /**
     * This method returns  the shortest path between src to dest - as an ordered List of nodes:
     * src --> n1 --> n2 --> ... --> dest.
     * Note: if no such path --> null.
     * The method uses a combination of BFS and Dijkstra's algorithms.
     * Note2: Dijkstra method changes the value of each node's tag, info and pre.
     * Thus the method calls resetTag, resetInfo and resetPre functions that resets the tag ,the info and rhe pre that changed.
     * The method uses Dijkstra algorithm to build a List od nodes: dest --> ... -->src
     * Thus the method need to reverse the list later.
     * Complexity: O(|V|+|E|), |V|=number of nodes, |E|=number of edges.
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return List of nodes.
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        List<node_info> list = new LinkedList<>();
        if (shortestPathDist(src, dest) == Integer.MAX_VALUE) {
            return null;
        }
        if (this.wg.getNode(src) == null) {
            throw new RuntimeException("This graph does not contain key " + src);
        }
        if (this.wg.getNode(dest) == null) {
            throw new RuntimeException("This graph does not contain key " + dest);
        }
        if (src == dest) {
            list.add(this.wg.getNode(dest));
            return list;
        }
        Dijkstra(this.wg.getNode(src), this.wg.getNode(dest));
        WGraph_DS.node src2 = (WGraph_DS.node) this.wg.getNode(src);
        WGraph_DS.node dest2 = (WGraph_DS.node) this.wg.getNode(dest);
        List<node_info> reverseList = new LinkedList<>();
        WGraph_DS.node temp = dest2;
        while (temp.getPre() != null) {
            reverseList.add(temp);
            temp = (WGraph_DS.node) temp.getPre();
        }
        node_info arr[] = reverseList.toArray(node_info[]::new);
        list.add(src2);
        for (int i = arr.length - 1; i >= 0; i--) {
            list.add(arr[i]);
        }
        resetInfo();
        resetTag();
        resetPre();
        return list;
    }

    /**
     * This method saves this weighted (undirected) graph to the given file name.
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved, otherwise false.
     */
    @Override
    public boolean save(String file) {
        try {
            //Saving of object in a file
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fos);

            // Method for serialization of object
            out.writeObject(this.wg);

            out.close();
            fos.close();

            System.out.println("The weighted graph has been serialized");

        }
        catch (IOException ex) {
            System.out.print("Error writing file\n" + ex);
            return false;        }
        return true;
    }
    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph of this class will be changed (to the loaded one),
     * In case the graph was not successfully loaded, the original graph would remain "as is".
     *
     * @param file - file name
     * @return true - iff the graph was successfully loaded, otherwise false.
     */
    @Override
    public boolean load(String file) {
        try
        {
            // Reading the object from a file
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(fis);

            // Method for deserialization of object
            this.init((weighted_graph) in.readObject());

            in.close();
            fis.close();

            System.out.println("The weighted graph has been deserialized ");

        }

        catch(IOException ex) {
            System.out.print("Error reading file\n" + ex);
            return false;
        }

        catch(ClassNotFoundException ex) {
            System.out.print("Error reading file\n" + ex);
            return false;
        }

    return true;
    }

    /**
     * This private method based on breadth-first search.
     * BFS is an algorithm for traversing or searching graph data structures.
     * The method checks whether or not the graph is linked,
     * in other words it checks whether there is a path between each node and each node.
     * The method uses the tag of each vertex to know whether it has been visited or not
     * The method stored a queue of the visited nodes:
     * Pop the first node from the queue
     * Check if the node has already been visited, if so skip it(tag = Green -> visited, tag = Blue -> not visited).
     * Otherwise mark it as visited (update his own tag) and add the node to the queue.
     * Add this node's neighbors to the queue and repeat these steps
     * Note: The method change the info values.
     * After the queue is empty check the info of all the nodes in this graph.,
     * If all the nodes in the graph are marked as visited the method will return true,
     * Otherwise false.
     * Complexity: O(|V|+|E|), |V|=number of nodes, |E|=number of edges.
     *
     * @param g
     * @return
     */
    private boolean bfs(weighted_graph g) {
        node_info n = g.getV().iterator().next();
        Queue<node_info> queue = new LinkedList<node_info>();
        n.setInfo("Green");
        queue.add(n);
        while (!queue.isEmpty()) {
            WGraph_DS.node temp = (WGraph_DS.node) queue.poll();
            Collection<node_info> h = temp.getNi();
            for (node_info next : h) {
                if (next.getInfo() == "Blue") {
                    next.setInfo("Green");
                    queue.add(next);
                }
            }
        }

        for (node_info node : this.wg.getV()) {
            if (node.getInfo() == "Blue") {
                return false;
            }
        }
        return true;
    }

    /**
     * This private method based on Dijkstra's algorithm.
     * Dijkstra's algorithm is an algorithm for finding the shortest paths between nodes in a graph.
     * In other words it finds the shortest paths between the source node and the destination node.
     * The method uses the tag of each node to update his current distance from the source node.
     * The method stored a priority queue(riority is determined by the tag) of the visited nodes:
     * Pop the first node from the queue.
     * Visit each one of this nodes neighbors:
     * Check if the node has already been visited, if so skip it(tag = Green -> visited, tag = Blue -> not visited).
     * Updates his tag to be the distance between the node and the source node.
     * Updates his pre To be the node from which he came to.
     * Add this node to the queue.
     * After going through all the neighbors of the node,
     * updates that we visited this node by change his info to "Green" and therefore will not visit it again.
     * Repeat these steps until the queue is empty or has reached the destination node.
     * If the queue is empty it means it did not reach the destination node (the graph is not connected), return infinity.
     * Otherwise returns the tag of the destination node
     * Note: The method change the info and pre values.
     * Complexity: O((|V|+|E|)log|V|), |V|=number of nodes, |E|=number of edges.
     *
     * @param src - the source node_info
     * @param dest - the destination node_info
     * @return the shortest path between the two nodes and infinity(Integer.MAX_VALUE) if there is no path like this.
     */
    private double Dijkstra(node_info src, node_info dest) {
        double shortest = Integer.MAX_VALUE;
        PriorityQueue<node_info> pq = new PriorityQueue<>();
        src.setInfo("Green");
        src.setTag(0);
        pq.add(src);

        while (!pq.isEmpty()) {
            WGraph_DS.node temp = (WGraph_DS.node) pq.poll();
            for (node_info n : temp.getNi()) {
                if (n.getInfo() == "Blue") {
                    WGraph_DS.node temp2 = (WGraph_DS.node) n;
                    if (n.getTag() > temp.getTag() + this.wg.getEdge(n.getKey(), temp.getKey())) {
                        n.setTag(Math.min(n.getTag(), temp.getTag() + this.wg.getEdge(n.getKey(), temp.getKey())));
                        temp2.setPre(temp);
                    }
                    pq.add(n);
                }

            }
            temp.setInfo("Green");
            if (temp == dest) {
                return temp.getTag();
            }
        }
        return shortest;
    }


    /**
     * This private method resets the value of info in each node in the graph.
     * Reset the value = change it back to default value: Blue
     */
    private void resetInfo() {
        for (node_info n : this.wg.getV()) {
            n.setInfo("Blue");
        }
    }

    /**
     * This private method resets the values of all the tags of the nodes in the graph.
     * Reset the value = change it back to default value: Integer.MAX_VALUE (infinity).
     */
    private void resetTag() {
        for (node_info n : this.wg.getV()) {
            n.setTag(Integer.MAX_VALUE);
        }
    }

    /**
     * This private method resets the value of pre in each node in the graph.
     * Reset the value = change it back to default value: null.
     */
    private void resetPre() {
        WGraph_DS.node temp;
        for (node_info n : this.wg.getV()) {
            temp = (WGraph_DS.node) n;
            temp.setPre(null);
        }
    }

    /**
     * This method returns true if the arguments are equal to each other and false otherwise.
     * Consequently, if both arguments are null, true is returned
     * and if exactly one argument is null, false is returned.
     * Otherwise, equality is determined by comparing the two weighted graph.
     * Note: The method uses "equals" method that compares two weighted graphs.
     *
     * @param o - an object
     * @return true if the arguments are equal to each other and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        WGraph_Algo that = (WGraph_Algo) o;
        return this.wg.equals(that.wg);
    }




      ////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
     //////////////////////////MY SAVE & LOAD\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    //////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public boolean mySave(String file) {
        try {
            FileWriter fw = new FileWriter(file);
            PrintWriter outs = new PrintWriter(fw);
            outs.println(this.wg);
            outs.close();
            fw.close();
        } catch (IOException ex) {
            System.out.print("Error writing file\n" + ex);
            return false;
        }
        return true;
    }

    public boolean myLoad(String file) {
        String str = "";
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            str = br.readLine();
            br.close();
            fr.close();
        } catch (IOException ex) {
            System.out.print("Error reading file\n" + ex);
            System.exit(2);
            return false;
        }
        if (str.length() != 0) {
            init(strToGraph(str));
        }
        return true;
    }
    private weighted_graph strToGraph(String s) {
        weighted_graph nwg = new WGraph_DS();
        String arr[] = s.split("\\|");
        for (int i = 0; i < arr.length; i++) {
            String key = arr[i].substring(5, arr[i].indexOf(","));
            nwg.addNode(Integer.parseInt(key));
        }
        for (int i = 0; i < arr.length; i++) {
            String ni = arr[i].substring(arr[i].indexOf("[") + 1, arr[i].indexOf("]"));
            String nib[] = ni.split(",");
            String key = arr[i].substring(5, arr[i].indexOf(","));
            for(int j = 0 ; j < nib.length ; j++){
                int k1 = Integer.parseInt(key);
                int k2 = Integer.parseInt(nib[j].substring(0,nib[j].indexOf("(")));
                double d = Double.parseDouble(nib[j].substring(nib[j].indexOf("(")+1,nib[j].indexOf(")")));
                nwg.connect(k1,k2,d);
            }
        }
        return nwg;
    }


    public static void main(String[] args) {
        WGraph_Algo wg = new WGraph_Algo();
        WGraph_DS wg2 = new WGraph_DS();
        wg2.addNode(0);
        wg2.addNode(1);
        wg2.addNode(2);
        wg2.addNode(3);
        wg2.addNode(4);
        wg2.addNode(5);
        wg2.addNode(6);
        wg2.addNode(7);
        wg2.addNode(8);
        wg2.addNode(10);

        wg2.connect(0, 1, 1);
        wg2.connect(0, 2, 10);

        wg2.connect(1, 4, 2);
        wg2.connect(1, 5, 7);

        wg2.connect(2, 4, 3);
        wg2.connect(2, 6, 10);
        wg2.connect(2, 3, 10);

        wg2.connect(3, 6, 10);

        wg2.connect(4, 5, 1);
        wg2.connect(4, 7, 4);


        wg2.connect(5, 7, 1);
        wg2.connect(5, 8, 10);
        wg2.connect(5, 10, 10);

        wg2.connect(6, 10, 10);

        wg2.connect(7, 10, 2);

        wg2.connect(8, 10, 10);

        wg.init(wg2);
        System.out.println(wg.isConnected());
        System.out.println(wg);
        double d = wg.shortestPathDist(0, 2);
        System.out.println(d);
        System.out.println(wg.shortestPath(0, 2));
        weighted_graph wg3 = wg.copy();
        weighted_graph_algorithms gra = new WGraph_Algo();
        gra.init(wg3);

          System.out.println(wg.save("/Users/itailash/Desktop/test/testSeri2.txt"));
         System.out.println(wg.load("/Users/itailash/Desktop/test/testSeri2.txt"));
         System.out.println(wg.wg);
        System.out.println(wg.equals(gra));



    }
}
