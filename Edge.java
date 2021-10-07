// edge class
public class Edge {
    // create instance variables for first value and second value in each line
    public final int u;
    public final int v;

    // edge constructor
    public Edge(String e) {
        // split string along " "
        // u = first vertex
        // v = second vertex
        String[] ns = e.split(" ");
        u = Integer.parseInt(ns[0]);
        v = Integer.parseInt(ns[1]);
    }

    // toString method overwrites default toString - returns each vertex separated by a space
    public String toString() {
        return u + " " + v;
    }
}
