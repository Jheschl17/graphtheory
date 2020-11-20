package htbla.aud3.graphtheory;

import java.io.File;

/**
 * @author Torsten Welsch
 */
public class Main {
    
    public static void main(String[] args) {
        Graph g = new Graph();
        g.read(new File("resources/Linz_Suchproblem.csv"));
    }
    
}
