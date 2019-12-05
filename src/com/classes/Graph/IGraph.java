package com.classes.Graph;

import java.util.LinkedList;

public interface IGraph {
    int[] initialNodeSize = new int[]{};
    int nodeNumber = 0;
    int getNodeNumber();
    int[] getInitialNodeSize();
    LinkedList<Integer> getSuccesors(int node);
    boolean arcExists(int node, int nodeSought);
    AbstractArc getArcValue(int fromNode, int toNode);
    AbstractNode getNodeValue(int node);
    void print();
}
