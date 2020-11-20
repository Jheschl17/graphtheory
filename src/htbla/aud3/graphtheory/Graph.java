package htbla.aud3.graphtheory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Jonas Heschl, Jakob Jodlbauer, Emil Eichsteininger
 */
public class Graph {

    List<Edge> edges = new ArrayList<>();

    public void read(File adjacencyMatrix) {
        try {
            List<String> lines = Files.readAllLines(adjacencyMatrix.toPath());
            for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                for (int weightIndex = 0; weightIndex < lines.get(lineIndex).split(";").length; weightIndex++) {
                    int weight = Integer.parseInt(lines.get(lineIndex).split(";")[weightIndex]);
                    if (weight != 0) {
                        Edge edge = new Edge(lineIndex, weightIndex, weight);
                        edges.add(edge);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Path determineShortestPath(int sourceNodeId, int targetNodeId) {
        return null;
    }
    
    public Path determineShortestPath(int sourceNodeId, int targetNodeId, int... viaNodeIds) {
        return null;
    }
    
    public double determineMaximumFlow(int sourceNodeId, int targetNodeId) {
        return -1.0;
    }
    
    public List<Edge> determineBottlenecks(int sourceNodeId, int targetNodeId) {
        return null;
    }

    public int weight(int idFrom, int idTo) {
        return edges.stream()
                .filter(edge -> edge.getFromNodeId() == idFrom && edge.getToNodeId() == idTo)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("No edge exists with idFrom=" + idFrom + " and idTo=" + idTo))
                .getWeight();
    }

    /**
     *
     * @return All node ids in this graph.
     */
    public List<Integer> allNodeIds() {
        return edges.stream()
                .map(edge -> new ArrayList<>(Arrays.asList(
                        edge.getFromNodeId(),
                        edge.getToNodeId()
                )))
                .flatMapToInt(list -> (IntStream) list.stream())
                .distinct()
                .boxed()
                .collect(Collectors.toList());
    }

}
