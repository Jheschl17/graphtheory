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
        int[] expResult = {0, 1};
        Path result = new Path(graph, 0, 1);
        Assert.assertArrayEquals(expResult, result.getNodeIds());
    }

    @Test
    public void testComputeDistanceEasy() {
        double expResult = 2000;
        Path result = new Path(graph, 0, 1);
        assertEquals(expResult, result.computeDistance());
    }

    @Test
    public void testComputeDistanceMedium() {
        double expResult = 3000;
        Path result = new Path(graph, 0, 1, 2);
        assertEquals(expResult, result.computeDistance());
    }

    @Test
    public void testComputeDistanceHard() {
        //Path: E - D - C - D - C - B - A
        // 150 + 1000 + 1000 + 1000 + 1000 + 1000 + 2000
        double expResult = 7150;
        Path result = new Path(graph, 4, 3, 2, 3, 2, 1, 0);
        assertEquals(expResult, result.computeDistance());
    }
}
