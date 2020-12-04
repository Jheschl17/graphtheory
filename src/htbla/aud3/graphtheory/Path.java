package htbla.aud3.graphtheory;

import java.util.Arrays;

/**
 * @author Jonas Heschl, Jakob Jodlbauer, Emil Maximilian Hoani Eichsteininger
 */
public class Path {

    private final Graph graph;
    private final int[] nodeIds;

    public Path(Graph graph, int... nodeIds) {
        this.graph = graph;
        this.nodeIds = nodeIds;
    }
    
    public int[] getNodeIds() {
        return nodeIds;
    }
    
    public double computeDistance() {
        int totalDistance = 0;
        for (int i = 0; i < nodeIds.length - 1; i++) {
            totalDistance += graph.weight(nodeIds[i], nodeIds[i + 1]);
        }

        return totalDistance;
    }

    @Override
    public String toString() {
        return "Path{" +
                "graph=" + graph +
                ", nodeIds=" + Arrays.toString(nodeIds) +
                '}';
    }
}
