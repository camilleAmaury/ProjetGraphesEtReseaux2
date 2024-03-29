package com.classes.Algorithms.CutAlgorithm;

import com.classes.Couple;
import com.classes.Graph.AbstractGraph;
import com.classes.Graph.Network.GraphNetwork;
import com.classes.Graph.Network.NodeNetwork;
import com.classes.Graph.Residual.GraphResidual;
import com.classes.ListeAdjadence.ListeAdjacence;
import com.classes.ListeAdjadence.ResidualList;

import java.util.*;

public class MinimumCut {
    /**
     * Methods which returns the X and Y from the minimum cut
     * @param g : GraphNetwork, the maximum flow stated graph
     * @param logs : boolean, if you want to print the information
     * @return : ArrayList<ArrayList<NodeNetwork>>, a.get(0) --> X, a.get(1) --> Y
     */
    public Couple<ArrayList<Integer>, ArrayList<Integer>> executeAlgorithm(AbstractGraph g, boolean logs) {
        // residual graph construction
        AbstractGraph gr = null;
        if(g instanceof ListeAdjacence){
            gr = new ResidualList((ListeAdjacence)g);
        }else if(g instanceof GraphNetwork){
            gr = new GraphResidual((GraphNetwork) g);
        }
        // get X cut
        ArrayList<Integer> X = getX(gr);
        // get Y cut
        ArrayList<Integer> Y = getY(gr, X);

        // logs
        if(logs){
            System.out.println(" ----------> X cut <---------- ");
            for(int i = 0; i < X.size(); i++){
                System.out.println(" Node N°" + X.get(i));
            }
            System.out.println(" ----------> Y cut <---------- ");
            for(int i = 0; i < Y.size(); i++){
                System.out.println(" Node N°" + Y.get(i));
            }
        }
        if(g instanceof ListeAdjacence){
            System.out.println("\nMinimal Cut : " + ((ListeAdjacence)g).cutScore(X, Y));
        }else if(g instanceof GraphNetwork){
            System.out.println("\nMinimal Cut : " + ((GraphNetwork)g).cutScore(X, Y));
        }

        return(new Couple<>(X, Y));
    }

    /**
     * Methods which gives the X cut from a graph
     * @param g : graph residual, the maximum flow stated graph
     * @return ArrayList<Integer>, all nodes id belonging to X
     */
    private ArrayList<Integer> getX(AbstractGraph g) {
        // instanciate a boolean tab which tells us if we have already visited a node
        boolean[] visited = new boolean[g.getNodeNumber()];
        return(getXrec(g, visited,
                g instanceof ResidualList ? ((ResidualList)g).getNodeValue(0) : ((GraphResidual)g).getNodeValue(0)).getFirst());
    }

    /**
     * Methods which looks for the X cut (the first set in which we can access directly in the residual graph)
     * @param g : graph residual, Residual maximum stated flow graph
     * @param visited : boolean[], if we already visited the node
     * @param current : NodeNetwork, the current node
     * @return
     */
    private Couple<ArrayList<Integer>, boolean[]> getXrec(AbstractGraph g, boolean[] visited, NodeNetwork current){
        if(visited[current.getId()]) return new Couple<ArrayList<Integer>, boolean[]>(new ArrayList<>(), visited);
        visited[current.getId()] = true;
        LinkedList<Integer> successor = g.getSuccesors(current.getId());
        ArrayList<Integer> nodes = new ArrayList<>();
        nodes.add(current.getId());
        for(int i = 0; i < successor.size(); i++){
            Couple<ArrayList<Integer>, boolean[]> res = getXrec(g, visited,
                    g instanceof ResidualList ? ((ResidualList)g).getNodeValue(successor.get(i)) : ((GraphResidual)g).getNodeValue(successor.get(i)));
            nodes.addAll(res.getFirst());
            visited = res.getSecond();
        }
        return new Couple<ArrayList<Integer>, boolean[]>(nodes, visited);
    }

    /**
     * Methods which gives the Y cut from a graph
     * @param g : graph residual, the maximum flow stated graph
     * @param X : ArrayList<NodeNetwork>, all nodes beloging to X
     * @return ArrayList<NodeNetwork>, all nodes beloging to Y
     */
    private ArrayList<Integer> getY(AbstractGraph g, ArrayList<Integer> X){
        ArrayList<Integer> Y = new ArrayList<>();
        for (int i = 0; i < g.getNodeNumber(); i++) {
            if(!X.contains(i)){
                Y.add(i);
            }
        }
        return(Y);
    }
}
