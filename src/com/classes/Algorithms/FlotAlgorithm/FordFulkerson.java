package com.classes.Algorithms.FlotAlgorithm;

import com.classes.Graph.Network.GraphNetwork;
import com.classes.Graph.Residual.GraphResidual;
import com.classes.Util;

import java.util.ArrayList;

public class FordFulkerson {
    /* # ------------------> Properties <------------------ # */
    public int step = 0;
    public GraphNetwork graphNetwork;
    public GraphResidual graphResidual;

    /* # ------------------> Constructors <------------------ # */

    /* # ------------------> Accessors and Mutators <------------------ # */

    /* # ------------------> Methods <------------------ # */
    public GraphNetwork executeAlgorithm(GraphNetwork g, boolean logs) {
        this.graphNetwork = FordFulkerson.initialization(g);
        this.graphResidual = new GraphResidual(this.graphNetwork);
        ArrayList<ArrayList<Integer>>paths = FordFulkerson.findAllPaths(this.graphResidual);
        // while there is a path available
        while(paths.size() > 0){
            if(logs){
                String path = "";
                for(int i = 0; i < paths.size(); i++){
                    for(int j = 0; j < paths.get(i).size(); j++){
                        path += (j == 0 ? "" : ",") + (paths.get(i).get(j) == 0 ? "S" : paths.get(i).get(j) == this.graphNetwork.getNodeNumber()-1 ? "T" : paths.get(i).get(j));
                    }
                    path += "\n";
                }
                System.out.println(path);
            }
            ArrayList<Integer> pathSelected = paths.get(0);
            if(logs) System.out.println("P = " + pathSelected.toString());
            float cf_P = FordFulkerson.minResidualCapacity(pathSelected, this.graphResidual, logs);
            if(logs) System.out.println("Cf(P) = " + cf_P);
            FordFulkerson.spreadFlow(cf_P, this.graphResidual, pathSelected);
            this.graphNetwork = new GraphNetwork(graphResidual);
            this.step++;
            paths = FordFulkerson.findAllPaths(this.graphResidual);
        }
        return this.graphNetwork;
    }
    private static GraphNetwork initialization(GraphNetwork g){
        // instanciate all flow to 0
        for(int i = 0; i < g.getNodeNumber(); i++){
            for(int j = 0; j < g.getNodeNumber(); j++){
                g.getArcValue(i,j).setFlow(0);
            }
        }
        return g;
    }

    private static ArrayList<ArrayList<Integer>> findAllPaths(GraphResidual g){
        // instanciate a boolean tab representing the nodes
        boolean[] isAlreadySeen = new boolean[g.getNodeNumber()];
        for(int i = 0; i < isAlreadySeen.length; i++){
            isAlreadySeen[i] = false;
        }
        return rec_findAllPaths(g, 0, isAlreadySeen, new ArrayList<Integer>(){});
    }

    private static ArrayList<ArrayList<Integer>> rec_findAllPaths(GraphResidual g, int currentNode, boolean[] isAlreadySeen, ArrayList<Integer> li){
        ArrayList<ArrayList<Integer>> encapsulation = new ArrayList<ArrayList<Integer>>(){};
        ArrayList<Integer> li_current = Util.clone(li);
        // if nodes is the pit
        if(currentNode == g.getNodeNumber()-1){
            li_current.add(currentNode);
            encapsulation.add(li_current);
            return encapsulation;
        }
        else{
            isAlreadySeen[currentNode] = true;
            li_current.add(currentNode);
            // for each successor
            for(int node : g.getSuccesors(currentNode)){
                if(!isAlreadySeen[node]){
                    encapsulation.addAll(rec_findAllPaths(g, node, isAlreadySeen, li_current));
                }
            }
            isAlreadySeen[currentNode] = false;
            return encapsulation;
        }
    }

    private static float minResidualCapacity(ArrayList<Integer> path, GraphResidual g, boolean logs){
        ArrayList<Float> pathSelected = new ArrayList<>();
        for(int i = 0; i < path.size()-1; i++){
            pathSelected.add(g.getArcValue(path.get(i), path.get(i+1)).getCapacity() - g.getArcValue(path.get(i), path.get(i+1)).getFlow());
        }
        if(logs){
            for(int i = 0; i < pathSelected.size(); i++){
                System.out.println("Cf(" + (path.get(i) == 0 ? "S" :path.get(i))  + "," + (path.get(i+1) == g.getNodeNumber()-1 ? "T" :path.get(i+1)) + ") = " + pathSelected.get(i));
            }
        }
        return Util.min(pathSelected);
    }

    private static void spreadFlow(float flow, GraphResidual g, ArrayList<Integer> path){
        for(int i = 0; i < path.size()-1; i++){
            g.getArcValue(path.get(i), path.get(i+1)).setFlow(g.getArcValue(path.get(i), path.get(i+1)).getFlow() + flow);
            g.getArcValue(path.get(i+1), path.get(i)).setFlow(-g.getArcValue(path.get(i), path.get(i+1)).getFlow());
        }
    }
}
