package htbla.aud3.graphtheory;

import javax.swing.text.html.Option;
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

    private class SearchItem implements Comparable<SearchItem> {
        public int id;
        public int via = -1;
        public int distance = Integer.MAX_VALUE;

        public SearchItem(int id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SearchItem that = (SearchItem) o;
            return id == that.id && via == that.via && Double.compare(that.distance, distance) == 0;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, via, distance);
        }

        @Override
        public int compareTo(SearchItem o) {
            return this.distance - o.distance;
        }
    }

    // region pathfinding
    public Path determineShortestPath(int sourceNodeId, int targetNodeId) {
        if (sourceNodeId < 0)
            throw new IllegalArgumentException(
                    "The sourceNodeId was negative. As node ids are always positive, this is not valid.");

        if (targetNodeId < 0)
            throw new IllegalArgumentException(
                    "The targetNodeId was negative. As node ids are always positive, this is not valid.");

        List<SearchItem> lst = allNodeIds().stream()
                .map(SearchItem::new)
                .collect(Collectors.toList());

        Map<Integer, SearchItem> map = lst.stream()
                .collect(Collectors.toMap(x -> x.id, x -> x));

        // Set distance of starting point to zero and visited to true
        map.get(sourceNodeId).distance = 0;

        PriorityQueue<SearchItem> pq = new PriorityQueue<>(lst);

        // Determine path using Dijkstra
        while (pq.size() > 0) {
            SearchItem cur = pq.remove();
            for (Edge edge : edgesFrom(cur.id)) {
                SearchItem si = map.get(edge.getSecondNodeId());

                int alt = cur.distance + weight(cur.id, si.id);
                if (alt < si.distance) {
                    si.distance = alt;
                    si.via = cur.id;
                    // Update priority queue
                    pq.remove(si);
                    pq.add(si);
                }
            }
        }

        // Retrieve path from data structure
        return retrievePath(map, targetNodeId, this);
    }

    public Path determineShortestPath(int sourceNodeId, int targetNodeId, int... viaNodeIds) {
        if (sourceNodeId < 0)
            throw new IllegalArgumentException(
                    "The sourceNodeId was negative. As node ids are always positive, this is not valid.");

        if (targetNodeId < 0)
            throw new IllegalArgumentException(
                    "The targetNodeId was negative. As node ids are always positive, this is not valid.");

        if (Arrays.stream(viaNodeIds).anyMatch(nodeId -> nodeId < 0))
            throw new IllegalArgumentException(
                    "A viaNodeId was negative. As node ids are always positive, this is not valid.");

        // Initialize list with all node ids in order of traversal
        List<Integer> ids = new ArrayList<>();
        ids.add(sourceNodeId);
        ids.addAll(IntStream.of(viaNodeIds)
            .boxed()
            .collect(Collectors.toList()));
        ids.add(targetNodeId);

        // Calculate sub-paths
        List<Path> subPaths = new ArrayList<>();
        for (int i = 0; i < ids.size() - 1; i++) {
            int subPathFrom = ids.get(i);
            int subPathTo = ids.get(i + 1);
            subPaths.add(
                determineShortestPath(subPathFrom, subPathTo)
            );
        }

        // Merge sub-paths to total path
        List<Integer> totalPathIds = new ArrayList<>();
        totalPathIds.add(sourceNodeId);
        for (Path subPath : subPaths) {
            totalPathIds.addAll(
                Arrays.stream(subPath.getNodeIds())
                    .skip(1)
                    .boxed()
                    .collect(Collectors.toList())
            );
        }

        int[] totalPathIdsArray = totalPathIds.stream()
                .mapToInt(Integer::intValue)
                .toArray();
        return new Path(this, totalPathIdsArray);
    }

    public static Path determineShortestPath(List<Edge> edges, int sourceNodeId, int targetNodeId) {
        Graph g = new Graph();
        g.edges = edges;
        return g.determineShortestPath(sourceNodeId, targetNodeId);
    }

    public static Path determineShortestPath(List<Edge> edges, int sourceNodeId, int targetNodeId, int... viaNodeIds) {
        Graph g = new Graph();
        g.edges = edges;
        return g.determineShortestPath(sourceNodeId, targetNodeId, viaNodeIds);
    }
    // endregion

    // region flow
    public double determineMaximumFlow(int sourceNodeId, int targetNodeId) {
        List<Edge> flowDiagram = computeFlowDiagramWrapper(edges, sourceNodeId, targetNodeId);

        return flowDiagram.stream()
                .filter(edge -> edge.getSecondNodeId() == targetNodeId)
                .mapToInt(Edge::getWeight)
                .sum();
    }

    public List<Edge> determineBottlenecks(int sourceNodeId, int targetNodeId) {
        List<Edge> flowDiagram = computeFlowDiagramWrapper(edges, sourceNodeId, targetNodeId);

        return flowDiagram.stream()
                .filter(flowEdge -> {
                    // Get regular graph edge corresponding to current edge in the computed flowDiagram
                    Edge edgesEdge = findEdge(edges, flowEdge.getFirstNodeId(), flowEdge.getSecondNodeId());
                    // If the flowEdge weight and the regular graph edge weight are the same, this edge was used to its
                    // full capacity. It is thus a bottleneck.
                    return flowEdge.getWeight() == edgesEdge.getWeight();
                })
                .collect(Collectors.toList());
    }

    /**
     * Wrapper to simplify calling computeFlowDiagram. Since computeFlowDiagram is recursive, some of the methods
     * parameters are exclusively used for passing values down the recursive call tree. They must always have the same
     * value when calling computeFlowDiagram from the outside.
     */
    private static List<Edge> computeFlowDiagramWrapper(List<Edge> edges, int sourceNodeId, int targetNodeId) {
        // Create an empty flowDiagram to save the flown flow for each edge by deep copying the edges list and setting
        // weight to 0 for all elements.
        List<Edge> flowDiagramEmpty = edges.stream()
                .map(edge -> new Edge(edge.getFirstNodeId(), edge.getSecondNodeId(), 0))
                .collect(Collectors.toList());

        return computeFlowDiagram(edges, flowDiagramEmpty, null, sourceNodeId, targetNodeId);
    }

    /**
     * Recursively compute a flow diagram for the given graph `edges` with the flow origin at `sourceNodeId` and the flow drain at
     * `targetNodeId`.
     * This flow diagram is a list of the graphs edges (fromId, toId and weight) where weight is set to the amount of
     * flow flowing through the given edge.
     *
     * @param edges
     * @param sourceNodeId
     * @param targetNodeId
     * @return
     */
    private static List<Edge> computeFlowDiagram(List<Edge> edges, List<Edge> flowDiagram, Edge previousEdge, int sourceNodeId, int targetNodeId) {
        // Arrived at the target. Return and end recursion (for the current branch).
        if (sourceNodeId == targetNodeId) return flowDiagram;

        // Get the amount of flow to transport off of the previousEdge (the amount of flow transported onto the previous
        // edge). If this is the first iteration of computeFlowDiagram, an infinite amount of flow can flow off of the
        // previous edge since the previous edge is the origin.
        int previousFlowAmount;
        if (previousEdge != null) previousFlowAmount = previousEdge.getWeight();
        else previousFlowAmount = Integer.MAX_VALUE;

        // Transport flow off of the previous edge until there is either no flow left on the previous edge or all edges
        // leading from the previous edge's destination have transported as much flow as they are capable of transporting
        // (edges can transport a max amount of flow according to their weight in the variable edges).
        while (previousFlowAmount > 0) {
            Path path = determineShortestPath(flowDiagram, sourceNodeId, targetNodeId);
            Edge nextStep = path.getEdges().get(0);
            Edge nextStepEdges = findEdge(edges, nextStep.getFirstNodeId(), nextStep.getSecondNodeId());

            // Already visited this edge. This means all edges leading from the previous edge's destination have been
            // visited. As such no more flow can be transported off of the previous edge -> break out of the loop.
            if (nextStep.getWeight() != 0) break;

            // Determine the amount of flow transportable off of the previous edge onto this current next edge.
            int nextStepFlowAmount = Math.min(nextStepEdges.getWeight() - nextStep.getWeight(), previousFlowAmount);
            nextStep.addWeight(nextStepFlowAmount);
            previousFlowAmount -= nextStepFlowAmount;

            // Recursively transport the flow transported onto the next edge until all of it reaches the target node.
            flowDiagram = computeFlowDiagram(edges, flowDiagram, nextStep, nextStep.getSecondNodeId(), targetNodeId);
        }

        return flowDiagram;
    }
    // endregion

    // region utility methods
    public int weight(int idFrom, int idTo) {
        return edges.stream()
                .filter(edge -> edge.getFirstNodeId() == idFrom && edge.getSecondNodeId() == idTo)
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
                        edge.getFirstNodeId(),
                        edge.getSecondNodeId()
                )))
                .flatMap(list -> list.stream())
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all edges originating at the given node.
     *
     * @param fromNodeId The id of the node to search originating edges from.
     * @return All edges originating from `fromNodeId`.
     */
    public List<Edge> edgesFrom(int fromNodeId) {
        return edges.stream()
                .filter(edge -> edge.getFirstNodeId() == fromNodeId)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of all edges originating at the given node. Within the given graph edges.
     *
     * @param fromNodeId The id of the node to search originating edges from.
     * @return All edges originating from `fromNodeId`.
     */
    public static List<Edge> edgesFrom(List<Edge> edges, int fromNodeId) {
        return edges.stream()
                .filter(edge -> edge.getFirstNodeId() == fromNodeId)
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of node IDs of nodes neighbouring the node with the given id origin.
     *
     * @param origin The ID of the node to get neighbours from.
     * @return A list of node IDs of nodes neighbouring the given node origin.
     */
    public List<Integer> neighbours(int origin) {
         return edgesFrom(origin).stream()
                .mapToInt(Edge::getSecondNodeId)
                .boxed()
                .collect(Collectors.toList());
    }

    /**
     * Returns a list of node IDs of nodes neighbouring the node with the given id origin. Within the given graph edges.
     *
     * @param origin The ID of the node to get neighbours from.
     * @return A list of node IDs of nodes neighbouring the given node origin.
     */
    public static List<Integer> neighbours(List<Edge> edges, int origin) {
        return edgesFrom(edges, origin).stream()
                .mapToInt(Edge::getSecondNodeId)
                .boxed()
                .collect(Collectors.toList());
    }

    public static Edge findEdge(List<Edge> edges, int firstNodeId, int secondNodeId) {
        return edges.stream()
            .filter(edge -> edge.getFirstNodeId() == firstNodeId && edge.getSecondNodeId() == secondNodeId)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Could not find a edge in the given list of edges with firstNodeId="
                    + firstNodeId + " and secondNodeId=" + secondNodeId));
    }

    private static Path retrievePath(Map<Integer, SearchItem> map, int targetNodeId, Graph graph) {
        List<Integer> ids = new ArrayList<>();
        for (int id = targetNodeId; id != -1;) {
            SearchItem item = map.get(id);
            ids.add(item.id);
            id = item.via;
        }
        Collections.reverse(ids);

        int[] pathIds = ids.stream()
                .mapToInt(Integer::intValue)
                .toArray();
        return new Path(graph, pathIds);
    }
    // endregion

    public void read(File adjacencyMatrix) {
        try {
            List<String> lines = Files.readAllLines(adjacencyMatrix.toPath());
            for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
                for (int weightIndex = 0; weightIndex < lines.get(lineIndex).split(";").length; weightIndex++) {
                    int weight = Integer.parseInt(lines.get(lineIndex).split(";")[weightIndex]);
                    if (weight != 0) {
                        // Add 1 to lineIndex and weightIndex to make node indexes start at 1 instead of 0
                        Edge edge = new Edge(lineIndex + 1, weightIndex + 1, weight);
                        edges.add(edge);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
