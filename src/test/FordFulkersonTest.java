package test;

import com.classes.Graph.Network.ArcNetwork;
import com.classes.Graph.Network.GraphNetwork;
import com.classes.Util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class FordFulkersonTest {

    GraphNetwork g;

    @BeforeEach
    void init() {
        // First TD graph
        ArcNetwork[][] graph = new ArcNetwork[][]{
                new ArcNetwork[]{new ArcNetwork(0), new ArcNetwork(16), new ArcNetwork(13), new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(0)},
                new ArcNetwork[]{new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(10), new ArcNetwork(12), new ArcNetwork(0), new ArcNetwork(0)},
                new ArcNetwork[]{new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(14), new ArcNetwork(0)},
                new ArcNetwork[]{new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(9), new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(20)},
                new ArcNetwork[]{new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(7), new ArcNetwork(0), new ArcNetwork(4)},
                new ArcNetwork[]{new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(0), new ArcNetwork(0)}
        };
        g = new GraphNetwork(graph);
        g = Util.CalculFlotMax(g, false);
    }

    @Test
    void executeAlgorithm() {
        // check all this conditions
        boolean result = true && g.getArcValue(0,1).getFlow() == 16 && g.getArcValue(0,2).getFlow() == 7;
        result = result && g.getArcValue(1,2).getFlow() == 4 && g.getArcValue(1,3).getFlow() == 12;
        result = result && g.getArcValue(2,4).getFlow() == 11;
        result = result && g.getArcValue(3,2).getFlow() == 0 && g.getArcValue(3,5).getFlow() == 19;
        result = result && g.getArcValue(4,3).getFlow() == 7 && g.getArcValue(4,5).getFlow() == 4;
        assertEquals(true, result, "The flow is not the same in the TD and the result here");
    }

    @Test
    void maximumFlow() {
        assertEquals(23, g.currentFlow(), "maximum flow is not equal to 23");
    }
}