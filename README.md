![ArielLogo](docs/ArielLogo.png)
# Assignment 1

> Made by Itai Lashover
>
> GitHub page: [https://github.com/ItaiLash](https://github.com/ItaiLash)


### Introduction
This project is an assignment in an object-oriented course at Ariel University.
The project consists of 5 classes, 3 interfaces and 2 implementations that I will detail below.
This assignment is an infrastructure of data structure and algorithms for the duration of the course.
The task implements a data structure of a weighted and unidirectional graph.
The project implements a number of algorithms on the graph including the ability to duplicate a graph, check if the graph is linked, calculate a short path between two vertices in the graph and the ability to save a graph to your computer and to load it from your computer.
In the paragraphs below I will detail the classes in the project.

## WGraph_DS class

WGraph_DS class is an implementation of weighted_graph interface.
The class contains an inner class - **node**  which is the implementation of a node_info interface.

### node inner class
node is an implementation of a node_info interface, Comparable<node> and Serializable.
node class implement Set of operations applicable on a
node (vertex) in a (unidirectional) weighted graph.
  
Each NodeData contains few fields:
* key : A unique key used as each node's ID.
* tag & info : Variables that is used in some later methods (represent distance from another node, etc.)
* pre: A variable that is used in later functions, by default initialized to null.
* ni : HashMap data structure that is used to store each NodeData neighbors(by neighbor I mean another node that has an edge with this node).
* niDis: HashMap data structure that is used to store the weight (length) of the edge between this node and its neighbors.
       
In fact each node contains a list of all the nodes that it has an edge with,
And another list of all the distances from those nodes.
Both neighbor and edges lists are implemented by HashMap.
I chose to use HashMap data structure because it is easy to store data with the help of the unique key of each node and in addition it allows quick access to each of the neighbors of the node O(1).
Detailed explanation about HashMap:
[https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html)

#### main methods 

* info Getter and Setter : allows changing the remark (meta data) associated with this node or to return it.
* tag Getter and Setter : allows setting the "tag" value for temporal marking a node or to return it.
* pre Getter and Setter : allows setting the pre value for temporal marking a node or to return it.
* getKey : returns the key (id) associated with this NodeData - O(1).
* getNi : returns a collection with all the neighbors of this NodeData - O(1).
* hasNi : checks if the nodes are adjacent (there is an edge between them) - O(1).
* addNi : add a weighted edge between the nodes (in other words the method adds the node to this node HashMap) - O(1).
* removeNode : removes the edge between the nodes (in other words the method delete the node from this node HashMap) - O(1)
* compareTo : override on compareTo in order to compare two nodes just by their tag (used in WGraph_Algo class).
* equals : override on equals in order to checks if two nodes are equal, equality is determined by comparing all the fields of the two nodes including Hashmap's keys and values.

**Back to WGraph_DS class**

This class is an implementation of weighted_graph interface.
WGraph_DS class implement an unidirectional weighted graph.
It supports a large number of nodes (over 10^6, with average degree of 10).
This implementation also based on HashMap data structure.

Each WGraph_DS contains few fields:
‫*‬ wg : HashMap data structure that represent a graph, used to store all the nodes in the graph.
‫*‬ numOfEdge : A variable that stored the amount of edges in this graph.
‫*‬ numOfNode : A variable that stored the amount of nodes in this graph.
‫*‬ mc : Mode Count, a variable that stored the amount of changes(add node, remove node, add edge, remove edge)made in this graph.

In fact each WGraph_DS contains a list of all the nodes in the graph,
And at the same time each node contains two lists of its neighbors(=edges) and distances from them(=edge length).

### main methods 

* getNode : returns the node_data by the node unique key - O(1).
* hasEdge : returns true if and only if there is an edge between node1 and node2 - O(1).
* getEdge : eturns the weight of the edge between node1 and node2 - O(1).
* addNode : adds a new node to the graph with the given key - O(1).
* connect : Connects an edge between node1 and node2, with an edge with positive weight(if the edge node1-node2 already exists - the method simply updates the weight of the edge) - O(1).
* getV : returns a pointer (shallow copy) for the collection representing all the nodes in the graph - O(1).
* getV : returns a collection of the neighbors of the node by his key - O(1).
* removeNode : delete the node (with the given ID) from the graph and removes all edges which starts or ends at this node - O(n), |v|=n.
* removeEdge : delete the edge node1-node2 from the graph (if the edge does not exist in the graph the method simply does nothing) - O(1).
* nodeSize : returns the number of nodes in the graph - O(1).
* edgeSize : returns the number of edges in the graph - O(1).
* getMC : returns the Mode Count for testing changes in the graph - O(1).
* equals : override on equals in order to checks if two weighted graphs are equal, equality is determined by comparing all the fields of the two weighted graphs including Hashmap's keys and values.

#### private methods
* nodeDeepCopy : private method gets a graph and return a duplicate of his HashMap (In fact the method deep copies only the nodes without the edges).
* edgeDeepCopy : private method gets a graph and adds to this HashMap the same edges with the same weight(In fact the method deep copies only the edges assuming the nodes are already in the graph).
* graphNodeEquals : private method checks if two HashMaps are equal to each other, equality is determined by comparing the keys and values of the two Hashmaps.


## WGraph_Algo class
This class is an implementation of weighted_graph_algorithms interface.
WGraph_Algo class implement undirected (positive) Weighted Graph Theory algorithms.
The only field in the class is a weighted graph on which we want to perform the methods.

### main methods 

* init : initializes the graph on which this set of algorithms operates - O(1).
* getGraph : returns the underlying graph of which this class works.
* copy : computes a deep copy of this graph using WGraph_DS copy constructor that used node copy constructor - O(n^2), |V|=n.
* isConnected : returns true iff there is a valid path from every node to each other node, The method uses BFS algorithm that will be detailed below ‫-‬ O(|V|+|E|), |V|=number of nodes, |E|=number of edges.
* shortestPathDist : returns the length of the shortest path between src to dest, returns -1 if no such path. The method used Dijkstra's algorithms - O((|V|+|E|)log|V|), |V|=number of nodes, |E|=number of edges.
* shortestPath : returns  the shortest path between src to dest - as an ordered List of nodes:src --> n1 --> n2 --> ... --> dest - O((|V|+|E|)log|V|), |V|=number of nodes, |E|=number of edges.
* save : saves this weighted (undirected) graph to the given file name.
* load : loads a graph to this graph algorithm.


#### private methods
* bfs(weighted_graph g) : private method based on breadth-first search, BFS is an algorithm for traversing or searching graph data structures.
The method checks whether the graph is linked, in other words it checks whether there is a path between each node and each node.
The method uses the info of each vertex to know whether it has been visited or not
The method stored a queue of the visited nodes:
1. Pop the first node from the queue
2. Check if the node has already been visited, if so skip it(tag = Green -> visited, tag = Blue -> not visited).
Otherwise, mark it as visited (update his own info) and add the node to the queue.
3. Add this node's neighbors to the queue and repeat these steps.

After the queue is empty check the info of all the nodes in this graph.
If all the nodes in the graph marked as visited the method will return true, otherwise false.
Note: The method change the info values.
Complexity: O(|V|+|E|), |V|=number of nodes, |E|=number of edges.

* Dijkstra(node_info src, node_info dest) : private method based on Dijkstra's algorithm.
Dijkstra's algorithm is an algorithm for finding the shortest paths between nodes in a graph.
In other words it finds the shortest paths between the source node and the destination node.
The method uses the tag of each node to update his current distance from the source node.
The method stored a priority queue(priority is determined by the tag) of the visited nodes:
1. Pop the first node from the queue.
2. Visit each one of this nodes neighbors:
   - Check if the node has already been visited, if so skip it(tag = Green -> visited, tag = Blue -> not visited).
   - Updates his tag to be the distance between the node and the source node.
   - Updates his pre To be the node from which he came to.
   - Add this node to the queue.
3. After going through all the neighbors of the node,updates that we visited this node by change his info to "Green" and therefore will not visit it again.
4. Repeat these steps until the queue is empty or has reached the destination node.

If the queue is empty it means it did not reach the destination node (the graph not connected), return infinity.
Otherwise, returns the tag of the destination node.
Note: The method change the info,tag and pre values.
Complexity: O((|V|+|E|)log|V|), |V|=number of nodes, |E|=number of edges.

* resetInfo : private method resets the value of info in each node in the graph.
Reset the value = change it back to default value: Blue.
* resetTag : private method resets the values of all the tags of the nodes in the graph.
Reset the value = change it back to default value: Integer.MAX_VALUE (infinity).
* resetPre : private method resets the value of pre in each node in the graph.
Reset the value = change it back to default value: null.

# How to use?
Create main class and run the code below (for example):
```
public static void main(String[] args) {  

     WGraph_Algo wg = new WGraph_Algo();
     WGraph_DS wg2 = new WGraph_DS();
     wg2.addNode(1);
     wg2.addNode(2);
     wg2.addNode(3);
     wg2.addNode(4);
     wg2.addNode(5);
     wg2.addNode(6);

     wg2.connect(1, 4, 1.7);
     wg2.connect(1, 6, 9.3);
     wg2.connect(2, 4, 3.1);
     wg2.connect(3, 4, 5.0);
     wg2.connect(4, 5, 2.3);
     wg2.connect(5, 6, 2.7);
     wg2.connect(5, 6, 2.7);

     wg.init(wg2);
        
     System.out.println(wg.isConnected());
     System.out.println(wg.shortestPathDist(1, 6));        
     System.out.println(wg.shortestPath(1, 6));
     }
```
The code creates the graph:

<img src="../../docs/graphExample.png" width="500">

The output will be:
```
True
6.7
[{Key:1,Neighbors:[4(1.7),6(9.3)]}, {Key:4,Neighbors:[1(1.7),2(3.1),3(5.0),5(2.3)]}, {Key:5,Neighbors:[4(2.3),6(2.7)]}, {Key:6,Neighbors:[1(9.3),5(2.7)]}]
```
## External info:
- More about graph : https://en.wikipedia.org/wiki/Graph_%28discrete_mathematics%29
- More about Dijkstra's algorithm : https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
- More about BFS algorithm : https://en.wikipedia.org/wiki/Breadth-first_search

