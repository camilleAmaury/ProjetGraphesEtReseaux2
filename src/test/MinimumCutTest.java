package test;

import com.classes.Couple;
import com.classes.Graph.Network.ArcNetwork;
import com.classes.Graph.Network.GraphNetwork;
import com.classes.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class MinimumCutTest {
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
        Couple<ArrayList<Integer>, ArrayList<Integer>> minCut = Util.CalculCoupeMin(g, false);
        boolean cond = minCut.getFirst().size() == 4 && minCut.getSecond().size() == 2;
        cond = cond && minCut.getFirst().contains(0) && minCut.getFirst().contains(1)  && minCut.getFirst().contains(2)  && minCut.getFirst().contains(4);
        cond = cond && minCut.getSecond().contains(3)  && minCut.getSecond().contains(5);
        cond = cond && g.currentFlow() == g.cutScore(minCut.getFirst(), minCut.getSecond());
        assertEquals(true, cond, "The cut is not the same than in the TD");
    }
}