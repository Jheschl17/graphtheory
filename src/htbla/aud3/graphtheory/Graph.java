package htbla.aud3.graphtheory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Jonas Heschl, Jakob Jodlbauer, Emil Eichsteininger
 */
public class Graph {
    public class Node
    {
        protected int id;
        protected String name;
        protected List<Edge> connections;
        public Node(String name, List<Edge> connections)
        {
            this.name=name;
            this.connections=connections;
        }
        public void setConnections(List<Edge> connections)
        {
            this.connections = connections;
        }
    }
    Node[] nodes = new Node[50];
    public void read(File adjacencyMatrix) {
        Scanner s = null;
        try {
            s = new Scanner(adjacencyMatrix);
        } catch (Exception e) {}
        s.useDelimiter("\n");
        int rep = 0;
        for (int i = 0; i < 50; i++) {
            String name = "";
            for (int j = rep; j < rep+1; j++) {
                if(i>25)
                {
                    char c = (char) (40+i-1);
                    name ="A"+c;
                }
                else
                {
                    char c = (char)(i+65);
                    name = ""+c;
                }
                rep=(i/25);
            }
            Node n = new Node(name, null);
            nodes[i]=n;
        }
       /* while(s.hasNext())
        {
            String line = s.nextLine();
            String[] parts = line.split(";");
            List<Edge> edges = new ArrayList<>();
        }*/
        for (int i = 0; i < nodes.length; i++) {
            System.out.println(nodes[i].name);
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

}
