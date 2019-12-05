package test;

import com.classes.Couple;
import com.classes.Graph.Network.ArcNetwork;
import com.classes.Graph.Network.GraphNetwork;
import com.classes.Graph.Network.NodeNetwork;
import com.classes.ListeAdjadence.Arc;
import com.classes.ListeAdjadence.ListeAdjacence;
import com.classes.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MinimumCutTest {
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
        Couple<ArrayList<Integer>, ArrayList<Integer>> minCut = Util.CalculCoupeMin(g, false);
        boolean cond = minCut.getFirst().size() == 4 && minCut.getSecond().size() == 2;
        cond = cond && minCut.getFirst().contains(0) && minCut.getFirst().contains(1)  && minCut.getFirst().contains(2)  && minCut.getFirst().contains(4);
        cond = cond && minCut.getSecond().contains(3)  && minCut.getSecond().contains(5);
        cond = cond && g.currentFlow() == g.cutScore(minCut.getFirst(), minCut.getSecond());
        assertEquals(true, cond, "The cut is not the same than in the TD");
    }

    @Test
    void executeAlgorithmList() {
        Couple<ArrayList<Integer>, ArrayList<Integer>> minCut = Util.CalculCoupeMin(g2, false);
        boolean cond = minCut.getFirst().size() == 4 && minCut.getSecond().size() == 2;
        cond = cond && minCut.getFirst().contains(0) && minCut.getFirst().contains(1)  && minCut.getFirst().contains(2)  && minCut.getFirst().contains(4);
        cond = cond && minCut.getSecond().contains(3)  && minCut.getSecond().contains(5);
        cond = cond && g2.currentFlow() == g2.cutScore(minCut.getFirst(), minCut.getSecond());
        assertEquals(true, cond, "The cut is not the same than in the TD");
    }
}