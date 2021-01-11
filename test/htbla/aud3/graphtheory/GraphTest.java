package htbla.aud3.graphtheory;

import org.junit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GraphTest {

    private static Graph graph;

    public GraphTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        graph = new Graph();
        graph.read(new File("resources/Linz_Suchproblem.csv"));
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
        Path expResult = new Path(graph, 1, 2);
        Path result = graph.determineShortestPath(1, 2);
        Assert.assertArrayEquals(expResult.getNodeIds(), result.getNodeIds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDetermineShortestPathNegativeId() {
        Path result = graph.determineShortestPath(1, -69);
    }

    @Test
    public void testDetermineShortestPathWithParamsExists() {
        Path expResult = new Path(graph, 3, 2, 1);
        Path result = graph.determineShortestPath(3,1, 2);
        assertEquals(expResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDetermineShortestPathWithParamsNegativeId() {
        Path result = graph.determineShortestPath(2,-187, 420);
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
