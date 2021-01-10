package htbla.aud3.graphtheory;

import java.util.Arrays;

import java.util.ArrayList;
import java.util.List;

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

    public List<Edge> getEdges() {
        List<Edge> toReturn = new ArrayList<>();
        for (int i = 0; i < nodeIds.length-1; i++) {
            int from = nodeIds[i];
            int to = nodeIds[i+1];
            for (Edge e : graph.edges) {
                if(e.getFirstNodeId() == from && e.getSecondNodeId() == to) {
                    toReturn.add(e);
                    break;
                }
            }
        }
        return toReturn;
    }

    @Override
    public String toString() {
        return "Path{" +
                "graph=" + graph +
                ", nodeIds=" + Arrays.toString(nodeIds) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Path path = (Path) o;
        return Arrays.equals(nodeIds, path.nodeIds);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(nodeIds);
    }
}
