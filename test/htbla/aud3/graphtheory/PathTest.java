package htbla.aud3.graphtheory;

import org.junit.*;

import java.io.File;

public class PathTest {
    private static Graph graph;

    public PathTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        graph = new Graph();
        graph.read(new File("Linz_Suchproblem.csv"));
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
    public void testGetNodeIds() {
        Path expResult = new Path(graph, 0, 1);
        Path result = graph.determineShortestPath(0, 1);
        Assert.assertArrayEquals(expResult.getNodeIds(), result.getNodeIds());
    }
}
