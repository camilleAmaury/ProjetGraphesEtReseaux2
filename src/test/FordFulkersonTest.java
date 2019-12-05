package test;

import com.classes.Graph.Network.ArcNetwork;
import com.classes.Graph.Network.GraphNetwork;
import com.classes.Graph.Network.NodeNetwork;
import com.classes.ListeAdjadence.Arc;
import com.classes.ListeAdjadence.ListeAdjacence;
import com.classes.Util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

class FordFulkersonTest {

    GraphNetwork g;
    ListeAdjacence g2;

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
        LinkedList<Arc>[] graph2 = new LinkedList[]{
                new LinkedList(),
                new LinkedList(),
                new LinkedList(),
                new LinkedList(),
                new LinkedList(),
                new LinkedList()
        };
        graph2[0].add(new Arc(new NodeNetwork(1), 16));
        graph2[1].add(new Arc(new NodeNetwork(0), 0));
        graph2[0].add(new Arc(new NodeNetwork(2), 13));
        graph2[2].add(new Arc(new NodeNetwork(0), 0));
        graph2[1].add(new Arc(new NodeNetwork(2), 10));
        graph2[2].add(new Arc(new NodeNetwork(1), 0));
        graph2[1].add(new Arc(new NodeNetwork(3), 12));
        graph2[3].add(new Arc(new NodeNetwork(1), 0));
        graph2[2].add(new Arc(new NodeNetwork(4), 14));
        graph2[4].add(new Arc(new NodeNetwork(2), 0));
        graph2[3].add(new Arc(new NodeNetwork(2), 9));
        graph2[2].add(new Arc(new NodeNetwork(3), 0));
        graph2[3].add(new Arc(new NodeNetwork(5), 20));
        graph2[5].add(new Arc(new NodeNetwork(3), 0));
        graph2[4].add(new Arc(new NodeNetwork(3), 7));
        graph2[3].add(new Arc(new NodeNetwork(4), 0));
        graph2[4].add(new Arc(new NodeNetwork(5), 4));
        graph2[5].add(new Arc(new NodeNetwork(4), 0));
        g = new GraphNetwork(graph);
        g = (GraphNetwork)Util.CalculFlotMax(g, false);

        g2 = new ListeAdjacence(graph2);
        g2 = (ListeAdjacence)Util.CalculFlotMax(g2, false);
    }

    @Test
    void executeAlgorithmMatrix() {
        // check all this conditions
        boolean result = true && g.getArcValue(0,1).getFlow() == 16 && g.getArcValue(0,2).getFlow() == 7;
        result = result && g.getArcValue(1,2).getFlow() == 4 && g.getArcValue(1,3).getFlow() == 12;
        result = result && g.getArcValue(2,4).getFlow() == 11;
        result = result && g.getArcValue(3,2).getFlow() == 0 && g.getArcValue(3,5).getFlow() == 19;
        result = result && g.getArcValue(4,3).getFlow() == 7 && g.getArcValue(4,5).getFlow() == 4;
        assertEquals(true, result, "The flow is not the same in the TD and the result here");
    }

    @Test
    void executeAlgorithmList() {
        // check all this conditions
        boolean result = true && g2.getArcValue(0,1).getFlow() == 16 && g2.getArcValue(0,2).getFlow() == 7;
        result = result && g2.getArcValue(1,2).getFlow() == 4 && g2.getArcValue(1,3).getFlow() == 12;
        result = result && g2.getArcValue(2,4).getFlow() == 11;
        result = result && g2.getArcValue(3,2).getFlow() == 0 && g2.getArcValue(3,5).getFlow() == 19;
        result = result && g2.getArcValue(4,3).getFlow() == 7 && g2.getArcValue(4,5).getFlow() == 4;
        assertEquals(true, result, "The flow is not the same in the TD and the result here");
    }

    @Test
    void maximumFlowMatrix() {
        assertEquals(23, g.currentFlow(), "maximum flow is not equal to 23");
    }

    @Test
    void maximumFlowList() {
        assertEquals(23, g2.currentFlow(), "maximum flow is not equal to 23");
    }
}