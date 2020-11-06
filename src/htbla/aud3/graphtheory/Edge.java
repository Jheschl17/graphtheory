package htbla.aud3.graphtheory;

/*
 * @author TODO Bitte Gruppenmitglieder eintragen!
 */
public class Edge {

    private Graph.Node from;
    private Graph.Node to;
    private int weight;
    public Edge(Graph.Node from, Graph.Node to, int weight)
    {
        this.from=from;
        this.to = to;
        this.weight=weight;
    }
    public int getFromNodeId() {
        return -1;
    }
    
    public int getToNodeId() {
        return -1;
    }
    
}
