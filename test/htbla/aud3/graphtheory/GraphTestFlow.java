package htbla.aud3.graphtheory;

import org.junit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class GraphTestFlow {

    private static Graph graph;

    public GraphTestFlow() {
    }

    @BeforeClass
    public static void setUpClass() {
        graph = new Graph();
        graph.read(new File("resources/Linz_Flussproblem.csv"));
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDetermineMaximumFlow() {
        double expResult = 2250;
        double result = graph.determineMaximumFlow(1,2);
        assertEquals(expResult, result, 0.1);
    }

    @Test
    public void testDetermineBottlenecks() {
        List<Edge> expResult = new ArrayList<>();
        expResult.add(new Edge(1, 2, 2000));
        expResult.add(new Edge(1, 29, 250));
        expResult.add(new Edge(29, 30, 250));
        expResult.add(new Edge(30, 31, 250));
        List<Edge> result = graph.determineBottlenecks(1,2);
        assertEquals(expResult, result);
    }
}
