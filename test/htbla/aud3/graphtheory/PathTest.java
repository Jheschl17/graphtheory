package htbla.aud3.graphtheory;

import org.junit.*;

import java.io.File;
import static org.junit.Assert.*;

public class PathTest {
    private static Graph graph;

    public PathTest() {
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
    public void testGetNodeIds() {
        int[] expResult = {0, 1};
        Path result = new Path(graph, 0, 1);
        Assert.assertArrayEquals(expResult, result.getNodeIds());
    }

    @Test
    public void testComputeDistanceEasy() {
        double expResult = 500;
        Path result = new Path(graph, 0, 1);
        assertEquals(expResult, result.computeDistance(), 0.1);
    }

    @Test
    public void testComputeDistanceMedium() {
        double expResult = 650;
        Path result = new Path(graph, 0, 1, 2);
        assertEquals(expResult, result.computeDistance(), 0.1);
    }

    @Test
    public void testComputeDistanceHard() {
        // Path: 5 -> 34 -> 3 -> 2 -> 1   --   ids incremented by one as in the linz.jpg plan
        // 120 + 100 + 150 + 500 = 870
        double expResult = 870;
        Path result = new Path(graph, 4, 33, 2, 1, 0);
        assertEquals(expResult, result.computeDistance(), 0.1);
    }
}
