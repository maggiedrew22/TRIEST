import java.util.HashMap;
import java.util.HashSet;

// TriestImpr class
// implements TriestImpr algorithm
public class TriestImpr implements DataStreamAlgo {
    /*
     * Constructor.
     * The parameter samsize denotes the size of the sample, i.e., the number of
     * edges that the algorithm can store.
     */
    public int sampleSize;
    public int time;
    public double numTriangles;
    public Edge[] edges;
    public HashMap<Integer, HashSet<Integer>> holdGraph;

    public TriestImpr(int samsize) {
        sampleSize = samsize;
        time = 0;
        numTriangles = 0.0;
        edges = new Edge[sampleSize];
        holdGraph = new HashMap<>();
    }

    public void handleEdge(Edge edge) {
        time++;

        int aVertex = Integer.parseInt(String.valueOf(edge.u));
        int bVertex = Integer.parseInt(String.valueOf(edge.v));

        // self loops

        // first, we want to calculate the # of triangles w our new edge
        int counter; // keeps track of the # of new triangles induced with our new edge
        // if both vertices are already in our hashmap, then we may be able to form new triangles
        // we also want to check to make sure we are not dealing with a self loop
        if (holdGraph.containsKey(aVertex) && holdGraph.containsKey(bVertex) && aVertex != bVertex) {
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
        }
        // if one (or both) of the vertices are not in our hashmap, then we can't possibly create
        // a new triangle
        else {
            counter = 0;
        }

        // then, we want to compute n_t
        double n_t = Math.max(1.0, (double) (((double)time - 1) * (time - 2)) / ((double)sampleSize * (sampleSize - 1)));//((double)(time - 1)/(sampleSize)) * ((double)(time - 2)/(sampleSize - 1));
        double gn_t = (n_t * counter);

        // now, we will increment numTriangles by g*n_t
        numTriangles = numTriangles + gn_t;

        // decide whether to update our sample
        // if time is less than sampleSize
        if (time <= sampleSize){
            edges[time - 1] = edge;
            insertEdge(edge);
        }
        // if time is greater than sampleSize
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

    // instead of dealing with multiple cases, in -IMPR we only return numTriangles
    public int getEstimate() {
        return (int)numTriangles;
    }

    // helper method for inserting an edge
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

    }

    // helper function to remove an edge
    public void removeEdge(int toRemoveIndex){
        Edge deleteEdge = edges[toRemoveIndex];
        int aVertex = deleteEdge.u;
        int bVertex = deleteEdge.v;

        holdGraph.get(aVertex).remove(bVertex);
        holdGraph.get(bVertex).remove(aVertex);
    }


}
