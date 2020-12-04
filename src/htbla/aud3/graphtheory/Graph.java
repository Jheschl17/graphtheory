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

    public Path determineShortestPath(int sourceNodeId, int targetNodeId) {
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
                SearchItem si = map.get(edge.getToNodeId());

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
    
    public Path determineShortestPath(int sourceNodeId, int targetNodeId, int... viaNodeIds) {
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

    //Annahme: Maximumflow ist die Edge mit der größten Gewichtung
    public double determineMaximumFlow(int sourceNodeId, int targetNodeId) {
        List<Edge> edgeList = determineShortestPath(sourceNodeId, targetNodeId).getEdges();
        edgeList.sort(Comparator.comparingInt(Edge::getWeight));
        return edgeList.get(0).getWeight();
    }

    //Annahme: Das sind alle Edges die eine kleinere Gewichtung haben als maximum
    public List<Edge> determineBottlenecks(int sourceNodeId, int targetNodeId) {
        List<Edge> edgeList = determineShortestPath(sourceNodeId, targetNodeId).getEdges();
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
                .filter(edge -> edge.getFromNodeId() == fromNodeId)
                .collect(Collectors.toList());
    }

}
