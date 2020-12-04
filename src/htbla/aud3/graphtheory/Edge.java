package htbla.aud3.graphtheory;

/*
 * @author Jonas Heschl, Jakob Jodlbauer, Emil Maximilian Hoani Eichsteininger
 */
public class Edge {

    private int from;
    private int to;
    private int weight;

    public Edge(int from, int to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int getFromNodeId() {
        return from;
    }
    
    public int getToNodeId() {
        return to;
    }

    public int getWeight() {
        return weight;
    }
    
}
