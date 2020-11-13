package htbla.aud3.graphtheory;

import org.junit.*;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class GraphTest {

    private static Graph graph;

    public GraphTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        graph = new Graph();
        graph.read(new File("Suchproblem.csv"));
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
    public void testDetermineShortestPathZero() {
        Path expResult = null;
        Path result = graph.determineShortestPath(0, 13);
        assertEquals(expResult, result);
    }

    @Test
    public void testDetermineShortestPathExists() {
        Path expResult = null;
        Path result = graph.determineShortestPath(0, 13);
        assertEquals(expResult, result);
    }
}
