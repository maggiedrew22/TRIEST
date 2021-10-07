import java.util.*;

// TriestBase class
// implements TriestBase algorithm
public class TriestBase implements DataStreamAlgo {
    /*
     * Constructor.
     * The parameter samsize denotes the size of the sample, i.e., the number of
     * edges that the algorithm can store.
     */
    public int sampleSize;
    public int time;
    public int numTriangles;
    public Edge[] edges;
    public HashMap<Integer, HashSet<Integer>> holdGraph;

    // constructor
    public TriestBase(int samsize) {
        sampleSize = samsize;
        time = 0;
        numTriangles = 0;
        edges = new Edge[sampleSize];
        holdGraph = new HashMap<>();
    }

    public void handleEdge(Edge edge) {
        time++;

        // if both vertexes are the same, then we want to do nothing (i.e. there is no edge) and increment time

        if (edge.u == edge.v){ // finish this case
            if (time <= sampleSize) {
                edges[time - 1] = edge;
                //insertEdge(edge);
            }else {
                // create bias coin based on current time and randNum
                double biasCoin = (double) sampleSize / time;
                double randNum = Math.random();
                // if less than the bias coin
                if (randNum <= biasCoin) {
                    // create a random value to remove
                    int toRemoveIndex = (int) (Math.random() * sampleSize);
                    removeEdge(toRemoveIndex);
                    //insertEdge(edge);
                    edges[toRemoveIndex] = edge;
                }
            }
        }
        else {
            // if we have not filled our sampleSize yet, then we want to insert our edge into holdGraph
            if (time <= sampleSize) {
                    edges[time - 1] = edge;
                    insertEdge(edge);
            }
            // if we have already filled sampleSize, we want to insert using a bias function
            else {
                // create bias coin based on current time and randNum
                double biasCoin = (double) sampleSize / time;
                double randNum = Math.random();
                // if less than the bias coin
                if (randNum <= biasCoin) {
                    // create a random value to remove
                    int toRemoveIndex = (int) (Math.random() * sampleSize);
                    removeEdge(toRemoveIndex);
                    insertEdge(edge);
                    edges[toRemoveIndex] = edge;
                }
            }
        }
    }

    public int getEstimate() {
        // if we have not filled our sample, we want to just return the number of triangles
        if (time < sampleSize){
            return numTriangles;
        }
        // otherwise if we haven't filled our sample, then we'll want to estimate # of triangles
        else {
            double pi = (double)(((double)sampleSize - 0.0 )/ ((double)time - 0.0)) * ((double)((double)sampleSize - 1.0)/((double)time - 1.0)) * ((double)((double)sampleSize - 2.0)/((double)time - 2.0));
            double estimate = numTriangles / pi;
            return (int)estimate;
        }
    }

    // helper function to insert an edge
    public void insertEdge(Edge edge){
        int aVertex = Integer.parseInt(String.valueOf(edge.u));
        int bVertex = Integer.parseInt(String.valueOf(edge.v));

        // if our hashmap contains aVertex already as a key
        if (holdGraph.containsKey(aVertex)){
            // create a temporary hashset of current integers in the hashset associated with aVertex
            // add bVertex to the new temporary hashset
            HashSet<Integer> tempSet = holdGraph.get(aVertex);
            tempSet.add(bVertex);
            holdGraph.put(aVertex, tempSet);
        }
        // else if our hashmap does not contain aVertex already as a key
        else {
            // add aVertex as a key and bVertex to the associated hashset
            HashSet<Integer> toAdd = new HashSet<>();
            toAdd.add(bVertex);
            holdGraph.put(aVertex,toAdd);
        }

        // if our hashmap contains bVertex already as a key
        if (holdGraph.containsKey(bVertex)){
            // create a temporary hashset of current integers in the hashset associated with aVertex
            // add bVertex to the new temporary hashset
            HashSet<Integer> tempSet = holdGraph.get(bVertex);
            tempSet.add(aVertex);
            holdGraph.put(bVertex, tempSet);
        }
        // else if our hashmap does not contain bVertex already as a key
        else {
            // add bVertex as a key and aVertex to the associated hashset
            HashSet<Integer> toAdd = new HashSet<>();
            toAdd.add(aVertex);
            holdGraph.put(bVertex,toAdd);
        }

        int counter;
        // calculating number of triangles
        HashSet<Integer> temp = new HashSet<>();
        temp.addAll(holdGraph.get(aVertex));
        temp.retainAll(holdGraph.get(bVertex));
        counter = temp.size();
        if (temp.contains(aVertex)){
            counter--;
        }
        if (temp.contains(bVertex)){
            counter--;
        }
        numTriangles = numTriangles + counter;
    }

    // helper function to remove an edge
    public void removeEdge(int toRemoveIndex){
        Edge deleteEdge = edges[toRemoveIndex];
        int aVertex = deleteEdge.u;
        int bVertex = deleteEdge.v;
        int counter = 0;

        // if the two vertices are not the same, subtract from numTriangles
        if (aVertex != bVertex) {
            HashSet<Integer> temp = new HashSet<>();
            temp.addAll(holdGraph.get(aVertex));
            temp.retainAll(holdGraph.get(bVertex));
            counter = temp.size();
            if (temp.contains(aVertex)) {
                counter--;
            }
            if (temp.contains(bVertex)) {
                counter--;
            }

            numTriangles = numTriangles - counter;
            holdGraph.get(aVertex).remove(bVertex);
            holdGraph.get(bVertex).remove(aVertex);
        }

    }


}
