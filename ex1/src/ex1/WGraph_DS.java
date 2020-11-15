package ex1;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class is an implementation of weighted_graph interface.
 * WGraph_DS class implement an unidirectional weighted graph.
 * It support a large number of nodes (over 10^6, with average degree of 10).
 * This implementation based on HashMap data structure.
 *
 * @author itai.lashover
 */
public class WGraph_DS implements weighted_graph, Serializable {

    /**
     * Each WGraph_DS contains few fields:
     * wg : HashMap data structure that represent a graph, used to store all the node_data in the graph.
     * numOfEdge : A variable that stored the amount of edges in this graph.
     * numOfNode : A variable that stored the amount of nodes in this graph.
     * mc : Mode Count a variable that stored the amount of changes(add node, remove node, add edge, remove edge)made in this graph.
     */
    private HashMap<Integer, node_info> wg;
    private int numOfEdge;
    private int numOfNode;
    private int mc;

    /**
     * Default constructor
     */
    public WGraph_DS() {
        this.wg = new HashMap<>();
        this.numOfEdge = 0;
        this.numOfNode = 0;
    }

    /**
     * This method is a deep copy constructor.
     * It's build a new WGraph_DS with the same numOfEdge,numOfNode and mc.
     * Note: This constructor does not build a new HashMap.
     * The constructor calls another methods "edgeDeepCopy" and "nodeDeepCopy" that copy this HashMap.
     *
     * @param gra other graph that you want to duplicate.
     */
    public WGraph_DS(weighted_graph gra) {
        this.wg = nodeDeepCopy(gra);
        edgeDeepCopy(gra);
        this.numOfEdge = gra.edgeSize();
        this.numOfNode = gra.nodeSize();
        //this.mc = gra.getMC();
    }

    /**
     * This private method gets a graph and return a duplicate of his HashMap.
     * At first the method build a new and empty HashMap.
     * Then the method add a deep copy nodes to the new HashMap in the right keys.
     * Note: This method build a new and empty HashMap of nodes.
     * That means the method will not copy the edges, just the vertices.
     * Complexity: O(n) , |V|=n.
     *
     * @param other other graph that you want to duplicate his HashMap.
     * @return HashMap   new and identical HashMap.
     */
    private HashMap<Integer, node_info> nodeDeepCopy(weighted_graph other) {
        HashMap<Integer, node_info> h = new HashMap<>();
        for (node_info n : other.getV()) {
            h.put(n.getKey(), new node(n));
        }
        return h;
    }

    /**
     * This private method gets a graph and adds to this HashMap the same weighted edges.
     * Note: The method will used only after we have used the previous "nodeDeepCopy" method.
     * Thus in the beginning of the method we already have a HashMap with nodes.
     * All that is left is to connect the right nodes.
     * The method check which nodes connected in the other graph and connect them in this graph.
     * Complexity: O(n^2) , |V|=n.
     *
     * @param other other graph that you want to duplicate his HashMap.
     */
    private void edgeDeepCopy(weighted_graph other) {
        Collection<node_info> valArr = other.getV();
        for (node_info n : valArr) {
            node temp = (node) n;
            Collection<node_info> niArr = temp.getNi();
            for (node_info n2 : niArr) {
                node temp2 = (node) n2;
                if(!this.hasEdge(temp.getKey(),temp2.getKey())) {
                    this.connect(temp.getKey(), temp2.getKey(), other.getEdge(temp.getKey(), temp2.getKey()));
                }
            }
        }
    }

    /**
     * This method return the node_info by the node unique key.
     * NOTE: if that key is already exist in the graph the method simply do nothing.
     *
     * @param key - the node unique key.
     * @return the node_data, null if none.
     */
    @Override
    public node_info getNode(int key) {
        if (this.wg.isEmpty() || !this.wg.containsKey(key)) {
            return null;
        }
        return this.wg.get(key);
    }

    /**
     * This method returns true iff (if and only if) there is an edge between node1 and node2.
     * Complexity: this method run in O(1) time.
     *
     * @param node1 - a key(int)
     * @param node2 - a key(int)
     * @return true or false.
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        node n = (node) this.wg.get(node1);
        return n.hasNi(node2);
    }

    /**
     * This method returns the weight of the edge (node1, node2).
     * In case there is no such edge : return -1
     * Complexity: this method run in O(1) time.
     *
     * @param node1 - a key(int)
     * @param node2 - a key(int)
     * @return double.
     */
    @Override
    public double getEdge(int node1, int node2) {
        if(!this.wg.containsKey(node1) || !this.wg.containsKey(node2)){
            throw new RuntimeException("One or more of your keys does not exist in the graph");
        }
        node n1 = (node) this.wg.get(node1);
        if (n1.hasNi(node2)) {
            return n1.niDis.get(node2);
        } else
            return -1;
    }

    /**
     * This method adds a new node to the graph with the given key.
     * Complexity: this method run in O(1) time.
     * Note: if there is already a node with such a key -> no action be performed.
     *
     * @param key - the key of the new node
     */
    @Override
    public void addNode(int key) {
        if (!this.wg.containsKey(key)) {
            node n = new node(key);
            this.wg.put(key, n);
            this.numOfNode++;
        }
    }

    /**
     * Connects an edge between node1 and node2, with an edge with weight >=0.
     * Complexity: this method run in O(1) time.
     * Note: if the edge node1-node2 already exists - the method simply updates the weight of the edge.
     * @param node1 - node's key
     * @param node2 - node's key
     * @param w - edge length
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if(w<0){
            throw new RuntimeException("The weight must be positive");
        }
        node n1 = (node) this.wg.get(node1);
        node n2 = (node) this.wg.get(node2);
        if(hasEdge(node1,node2) && getEdge(node1,node2) != w){
            n1.addNi(n2, w);
            n2.addNi(n1, w);
            this.mc++;
        }
        else if (!n1.hasNi(node2) && node1 != node2 && n1 != null & n2 != null) {
            n1.addNi(n2, w);
            n2.addNi(n1, w);
            this.mc++;
            this.numOfEdge++;
        }
    }

    /**
     * This method returns a pointer (shallow copy) for a
     * Collection representing all the nodes in the graph.
     * Complexity: this method run in O(1) time.
     *
     * @return Collection of nodes
     */
    @Override
    public Collection<node_info> getV() {
        return this.wg.values();
    }

    /**
     * This method returns a collection of the neighbors of the node by his key.
     * This collection represents all the nodes connected to node_id
     * Complexity: this method run in O(1) time.
     *
     * @param node_id - key
     * @return Collection of nodes
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        node n = (node) this.wg.get(node_id);
        return n.getNi();
    }

    /**
     * This method deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * Complexity: This method run in O(n), |V|=n, as all the edges should be removed.
     * Note: if the node (with the given ID) does not exists - the method simply does nothing.
     *
     * @param key - the key of the node to be deleted
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_info removeNode(int key) {
        if (wg.containsValue(wg.get(key))) {
            node temp = (node) wg.get(key);
            Collection<node_info> valArr = temp.getNi();
            for (node_info n : valArr) {
                node n1 = (node) n;
                n1.removeNode(temp);
                this.numOfEdge--;
                this.mc++;
            }
            wg.remove(key);
            this.numOfNode--;
            mc++;
            return temp;
        }
        return null;
    }

    /**
     * This method delete the edge node1<==>node2 from the graph.
     * Complexity: this method run in O(1) time.
     * Note: if the edge does not exists in the graph - the method simply does nothing.
     *
     * @param node1 - a key(int)
     * @param node2 - a key(int)
     */
    @Override
    public void removeEdge(int node1, int node2) {
        node n1 = (node) this.wg.get(node1);
        node n2 = (node) this.wg.get(node2);
        if (n1.hasNi(node2) && n2.hasNi(node1) && node1 != node2) {
            n1.removeNode(n2);
            n2.removeNode(n1);
            mc++;
            this.numOfEdge--;
        }
    }

    /**
     * This method returns the number of nodes in the graph.
     * Complexity: this method run in O(1) time.
     *
     * @return the number of nodes in the graph
     */
    @Override
    public int nodeSize() {
        return numOfNode;
    }

    /**
     * This method returns the number of edges (unidirectional graph).
     * Complexity: this method run in O(1) time.
     *
     * @return the number of edges in the graph
     */
    @Override
    public int edgeSize() {
        return numOfEdge;
    }

    /**
     * This method returns the Mode Count - for testing changes in the graph.
     * Any change in the inner state of the graph cause an increment in the ModeCount.
     * Complexity: O(1).
     *
     * @return the number of changes that has been in the graph
     */
    @Override
    public int getMC() {
        return mc;
    }

    /**
     * toString method
     */
    public String toString() {
        String str = "";
        Iterator<node_info> itr = this.wg.values().iterator();
        while (itr.hasNext()) {
            node_info n = itr.next();
            if (!itr.hasNext()) {
                str += n;
            } else {
                str += n + " |";
            }
        }
        return str;
    }

    /**
     * This private method returns true if the two HashMaps are equal to each other and false otherwise.
     * Equality is determined by comparing the keys and values of the two Hashmaps.
     * Note: The method uses "equals" method that compares each pair of nodes.
     *
     * @param other - an HashMap<Integer,node_info>
     * @return true if the arguments are equal to each other and false otherwise
     */
    private boolean graphNodeEquals(HashMap<Integer, node_info> other) {
        if (!this.wg.keySet().equals(other.keySet())) {
            return false;
        }
        Collection<node_info> c1 = this.wg.values();
        Iterator<node_info> iterator1 = c1.iterator();
        Collection<node_info> c2 = other.values();
        Iterator<node_info> iterator2 = c2.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            node n1 = (node) iterator1.next();
            node n2 = (node) iterator2.next();
            if (!n1.equals(n2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method returns true if the arguments are equal to each other and false otherwise.
     * Consequently, if both arguments are null, true is returned
     * and if exactly one argument is null, false is returned.
     * Otherwise, equality is determined by comparing all the fields of the object.
     * Note: The method uses "graphNodeEquals" method that compares the two HashMaps.
     *
     * @param o - an object
     * @return true if the arguments are equal to each other and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if(!(o instanceof WGraph_DS)) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return this.numOfEdge == wGraph_ds.numOfEdge &&
                this.numOfNode == wGraph_ds.numOfNode &&
                //         this.mc == wGraph_ds.mc &&
                this.graphNodeEquals(wGraph_ds.wg);
    }


    /**
     * This private inner class is an implementation of node_info interface.
     * node class implement Set of operations applicable on a
     * node (vertex) in an (unidirectional) weighted graph.
     * </p>
     *
     * @author itai.lashover
     */
    public class node implements node_info, Comparable<node>, Serializable {

        /**
         * Each node contains few fields:
         * key : A unique key that is used as each NodeData's ID.
         * info : A variable that is used in later functions, by default Initialized to "Blue".
         * tag : A variable that is used in later functions, by default Initialized to Integer.MAX_VALUE(infinite).
         * pre: A variable that is used in later functions, by default Initialized to null.
         * ni : HashMap data structure that is used to store this NodeData neighbors.
         * niDis: HashMap data structure that is used to store the weight (length) of the edge between this node and its neighbors.
         */
        private int key;
        private String info;
        private double tag;
        private node_info pre;
        private HashMap<Integer, node_info> ni;
        private HashMap<Integer, Double> niDis;

        /**
         * Constructor
         */
        public node(int k) {
            this.key = k;
            this.info = "Blue";
            this.tag = Integer.MAX_VALUE;
            this.pre = null;
            this.ni = new HashMap<>();
            this.niDis = new HashMap<>();
        }

        /**
         * This method is a deep copy constructor.
         * It builds a new node with the same key,tag and info.
         * Note: This method builds a new and empty HashMaps of neighbors.
         * That means the method will not copy the neighbors of this node.
         *
         * @param n other node that you want to duplicate.
         */
        public node(node_info n) {
            this.key = n.getKey();
            this.info = n.getInfo();
            this.tag = n.getTag();
            this.ni = new HashMap<>();
            this.niDis = new HashMap<>();
        }

        /**
         * This method returns the key (id) associated with this node.
         *
         * @return this node unique key.
         */
        @Override
        public int getKey() {
            return this.key;
        }

        /**
         * This method returns the remark (meta data) associated with this node.
         *
         * @return info
         */
        @Override
        public String getInfo() {
            return this.info;
        }

        /**
         * This method allows changing the remark (meta data) associated with this node.
         *
         * @param s the new value of the info.
         */
        @Override
        public void setInfo(String s) {
            this.info = s;
        }

        /**
         * This method temporal returns the tag associated with this node.
         * Used in later functions.
         *
         * @return tag
         */
        @Override
        public double getTag() {
            return this.tag;
        }

        /**
         * This method allows setting the "tag" value for temporal marking a node.
         *
         * @param t the new value of the tag.
         */
        @Override
        public void setTag(double t) {
            this.tag = t;
        }

        /**
         * This method returns a collection with all the neighbors of this node.
         *
         * @return collections of node_info.
         */
        public Collection<node_info> getNi() {
            return this.ni.values();
        }

        /**
         * This method returns true iff this<==>key are adjacent, as an edge between them.
         *
         * @param key - int
         * @return true or false.
         */
        public boolean hasNi(int key) {
            return this.ni.containsKey(key);
        }

        /**
         * This method adds the node t to this NodeData.
         * In other words, the method add a weighted edge between this node ==> node t.
         * NOTE: if the map previously contained a mapping for the key, the old value is replaced.
         *
         * @param t - edge length
         */
        public void addNi(node_info t, double d) {
            if (this != t) {
                this.ni.put(t.getKey(), t);
                this.niDis.put(t.getKey(), d);
            }
        }

        /**
         * This method removes the edge between this node ==> node.
         *
         * @param node - the node to be deleted
         */
        public void removeNode(node_info node) {
            if (this.ni.containsKey(node.getKey())) {
                this.ni.remove(node.getKey());
                this.niDis.remove(node.getKey());
            }
        }

        /**
         * This method temporal returns the pre associated with this node.
         * Used in later functions.
         *
         * @return tag
         */
        public node_info getPre() {
            return this.pre;
        }

        /**
         * This method allows setting the pre value for temporal marking a node.
         *
         * @param n the new value of the pre.
         */
        public void setPre(node_info n) {
            this.pre = n;
        }

        /**
         * toString method
         */
        @Override
        public String toString() {
            String str = "[";
            Collection<node_info> keyArr = ni.values();
            Iterator<node_info> iterator = keyArr.iterator();
            Collection<Double> lenArr = niDis.values();
            Iterator<Double> iterator2 = lenArr.iterator();
            while (iterator.hasNext()) {
                node_info n = iterator.next();
                double d = iterator2.next();
                if (!iterator.hasNext()) {
                    str += n.getKey() + "(" + d + ")" + "]";
                } else {
                    str += n.getKey() + "(" + d + ")" + ",";
                }
            }
            return "{Key:" + this.key + ",Neighbors:" + str + "}";
        }


        /**
         * This method override on compareTo in order to compare two nodes just by their tag.
         */
        @Override
        public int compareTo(node o) {
            return Double.compare(this.getTag(), o.getTag());
        }

        /**
         * This private method returns true if the two nodes are equal to each other and false otherwise.
         * Equality is determined by comparing the shallow fields of the node(key,tag and info).
         *
         * @param n - a node_info
         * @return true if the arguments are equal to each other and false otherwise
         */
        private boolean nodeEquals(node_info n) {
            node temp = (node) n;
            return this.key == temp.key &&
                    this.info.compareTo(temp.info) == 0 &&
                    this.tag == temp.tag;
        }

        /**
         * This private method returns true if the two HashMaps are equal to each other and false otherwise.
         * Equality is determined by comparing the keys and values of the two Hashmaps.
         * Note: The method uses "nodeEquals" method that compares two nodes only by their shallow fields.
         *
         * @param other - an HashMap<Integer,node_info>
         * @return true if the arguments are equal to each other and false otherwise
         */
        private boolean hashMapOfNodeNiEquals(HashMap<Integer, node_info> other) {
            if (!this.ni.keySet().equals(other.keySet())) {
                return false;
            }
            Collection<node_info> c1 = this.ni.values();
            Iterator<node_info> iterator1 = c1.iterator();
            Collection<node_info> c2 = other.values();
            while (iterator1.hasNext()) {
                node n1 = (node) iterator1.next();
                boolean flag = false;
                for(node_info n2 : c2){
                    if(n1.nodeEquals(n2)){
                        flag = true;
                    }
                }
                if (!flag) {
                    return false;
                }
            }
            return true;
        }

        /**
         * This private method returns true if the two HashMaps are equal to each other and false otherwise.
         * Equality is determined by comparing the keys and values of the two Hashmaps.
         *
         * @param other - an HashMap<Integer,Double>
         * @return true if the arguments are equal to each other and false otherwise
         */
        private boolean hashMapOfDoublesEquals(HashMap<Integer, Double> other) {
            if (!this.niDis.keySet().equals(other.keySet())) {
                return false;
            }
            Collection<Double> c1 = this.niDis.values();
            Iterator<Double> iterator1 = c1.iterator();
            Collection<Double> c2 = other.values();
            while (iterator1.hasNext()) {
                boolean flag = false;
                double d1 = iterator1.next();
                for(double d2 : c2){
                    if(d1 == d2){
                        flag = true;
                    }
                }
                if (!flag) {
                    return false;
                }
            }
            return true;
        }

        /**
         * This method returns true if the arguments are equal to each other and false otherwise.
         * Consequently, if both arguments are null, true is returned
         * and if exactly one argument is null, false is returned.
         * Otherwise, equality is determined by comparing all the fields of the object.
         * Note1: The method uses "hashMapOfNodeNiEquals" method that compares the two HashMaps of nodes.
         * Note2: The method uses "hashMapOfDoublesEquals" method that compares the two HashMaps of doubles.
         *
         * @param o - an Object
         * @return true if the arguments are equal to each other and false otherwise
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if(!(o instanceof node)) return false;
            node node = (node) o;
            return this.key == node.key &&
                    Double.compare(node.tag, this.tag) == 0 &&
                    this.info.compareTo(node.info) == 0 &&
                    this.hashMapOfNodeNiEquals(node.ni) &&
                    this.hashMapOfDoublesEquals(node.niDis);
        }
    }
}
