package htbla.aud3.graphtheory;

import org.junit.*;
import static org.junit.Assert.*;

public class EdgeTest {

    public EdgeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
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
    public void testGetFirstNodeId() {
        int expResult = 0;
        Edge e = new Edge(0,1, 1000);
        assertEquals(expResult, e.getFirstNodeId());
    }

    @Test
    public void testGetSecondNodeId() {
        int expResult = 1;
        Edge e = new Edge(0,1, 1000);
        assertEquals(expResult, e.getSecondNodeId());
    }
}
