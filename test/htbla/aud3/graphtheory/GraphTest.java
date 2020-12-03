package htbla.aud3.graphtheory;

import org.junit.*;

import java.io.File;

import static org.junit.Assert.*;

public class GraphTest {

    private static Graph graph;

    public GraphTest() {
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
    public void testDetermineShortestPathEasy() {
        Path expResult = new Path(graph, 0, 1);
        Path result = graph.determineShortestPath(0, 1);
        Assert.assertArrayEquals(expResult.getNodeIds(), result.getNodeIds());
    }

    @Test
    public void testDetermineShortestPathNegativeId() {
        Path result = graph.determineShortestPath(0, -69);
        assertNull(result);
    }

    @Test
    public void testDetermineShortestPathWithParamsExists() {
        Path expResult = new Path(graph, 0, 1);
        Path result = graph.determineShortestPath(2,0);
        Assert.assertArrayEquals(expResult.getNodeIds(), result.getNodeIds());
    }

    @Test
    public void testDetermineShortestPathWithParamsNegativeId() {
        Path result = graph.determineShortestPath(2,-187);
        assertNull(result);
    }

    @Test
    public void testReadFileDoesNotExist() {
        try {
            graph.read(new File("Complete this sentence: Resurrection by E..."));
        } catch (Exception e) {
            fail();
        }
    }
}
