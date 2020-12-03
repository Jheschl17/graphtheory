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
        List<Integer> list=(Arrays.stream(determineShortestPath(sourceNodeId, viaNodeIds[0]).getNodeIds()).boxed().collect(Collectors.toList()));
        if(viaNodeIds.length>1) IntStream.range(0, viaNodeIds.length - 1).mapToObj(i -> Arrays.stream(determineShortestPath(viaNodeIds[i], viaNodeIds[i + 1]).getNodeIds()).boxed().collect(Collectors.toList())).forEach(list::addAll);
        list.addAll(Arrays.stream(determineShortestPath(viaNodeIds[viaNodeIds.length-1],targetNodeId).getNodeIds()).boxed().collect(Collectors.toList()));
        IntStream.range(0, list.size()).filter(i -> list.get(i) == list.get(i + 1)).forEach(list::remove);
        return new Path(this, list.stream().mapToInt(i->i).toArray());
    }
    //Annahme: Maximumflow ist die Edge mit der größten Gewichtung
    public double determineMaximumFlow(int sourceNodeId, int targetNodeId) {
        List<Edge> edgeList=determineShortestPath(sourceNodeId, targetNodeId).getEdges();
        edgeList.sort(Comparator.comparingInt(Edge::getWeight));
        return edgeList.get(0).getWeight();
    }
    //Annahme: Das sind alle Edges die eine kleinere Gewichtung haben als maximum
    public List<Edge> determineBottlenecks(int sourceNodeId, int targetNodeId) {
        List<Edge> edgeList=determineShortestPath(sourceNodeId, targetNodeId).getEdges();
        edgeList.sort(Comparator.comparingInt(Edge::getWeight));
        return edgeList.stream().filter(x -> x.getWeight()<determineMaximumFlow(sourceNodeId, targetNodeId)).collect(Collectors.toList());
    }

    public int weight(int idFrom, int idTo) {
        return edges.stream()
                .filter(edge -> edge.getFromNodeId() == idFrom && edge.getToNodeId() == idTo)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("No edge exists with idFrom=" + idFrom + " and idTo=" + idTo))
                .getWeight();
    }

}
