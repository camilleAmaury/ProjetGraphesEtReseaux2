package com.classes.Algorithms.FlotAlgorithm;

import com.classes.Graph.AbstractGraph;
import com.classes.Graph.Network.ArcNetwork;
import com.classes.Graph.Network.GraphNetwork;
import com.classes.Graph.Residual.GraphResidual;
import com.classes.ListeAdjadence.Arc;
import com.classes.ListeAdjadence.ListeAdjacence;
import com.classes.ListeAdjadence.ResidualList;
import com.classes.Util;

import java.util.ArrayList;
import java.util.List;

public class FordFulkerson {
    /* # ------------------> Properties <------------------ # */
    public int step = 0;
    public AbstractGraph graphNetwork;
    public AbstractGraph graphResidual;

    /* # ------------------> Constructors <------------------ # */

    /* # ------------------> Accessors and Mutators <------------------ # */

    /* # ------------------> Methods <------------------ # */
    public AbstractGraph executeAlgorithm(AbstractGraph g, boolean logs) {
        this.graphNetwork = FordFulkerson.initialization(g);
        this.graphResidual = this.graphNetwork instanceof ListeAdjacence ? new ResidualList((ListeAdjacence)this.graphNetwork) : new GraphResidual((GraphNetwork) this.graphNetwork);
        ArrayList<ArrayList<Integer>>paths = FordFulkerson.findPaths(this.graphResidual);
        // while there is a path available
        while(paths.size() > 0){
            ArrayList<Integer> pathSelected = paths.get(0);
            if(logs) System.out.println("P = " + pathSelected.toString());
            float cf_P = FordFulkerson.minResidualCapacity(pathSelected, this.graphResidual, logs);
            if(logs) System.out.println("Cf(P) = " + cf_P);
            FordFulkerson.spreadFlow(cf_P, this.graphResidual, pathSelected);
            if(this.graphNetwork instanceof ListeAdjacence && this.graphResidual instanceof ResidualList){
                ((ListeAdjacence)this.graphNetwork).replace((ResidualList) this.graphResidual);
            }else if(this.graphNetwork instanceof GraphNetwork && this.graphResidual instanceof GraphResidual){
                ((GraphNetwork)this.graphNetwork).replace((GraphResidual) this.graphResidual);
            }
            this.step++;
            paths = FordFulkerson.findPaths(this.graphResidual);
        }
        return this.graphNetwork;
    }
    private static AbstractGraph initialization(AbstractGraph g){
        // instanciate all flow to 0
        for(int i = 0; i < g.getNodeNumber(); i++){
            int arcs = g instanceof ListeAdjacence ? ((ListeAdjacence)g).getGraph()[i].size() : g.getNodeNumber();
            for(int j = 0; j < arcs; j++){
                if(g instanceof ListeAdjacence){
                    ((ListeAdjacence)g).getArcValue(i,((ListeAdjacence)g).getGraph()[i].get(j).getTo().getId()).setFlow(0);
                }else if(g instanceof GraphNetwork){
                    ((GraphNetwork)g).getArcValue(i,j).setFlow(0);
                }
            }
        }
        return g;
    }

    private static ArrayList<ArrayList<Integer>> findPaths(AbstractGraph g){
        // instanciate a boolean tab representing the nodes
        boolean[] isAlreadySeen = new boolean[g.getNodeNumber()];
        for(int i = 0; i < isAlreadySeen.length; i++){
            isAlreadySeen[i] = false;
        }
        return rec_findAllPaths(g, 0, isAlreadySeen, new ArrayList<Integer>(){});
    }

    private static ArrayList<ArrayList<Integer>> rec_findAllPaths(AbstractGraph g, int currentNode, boolean[] isAlreadySeen, ArrayList<Integer> li){
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

    private static float minResidualCapacity(ArrayList<Integer> path, AbstractGraph g, boolean logs){
        ArrayList<Float> pathSelected = new ArrayList<>();
        for(int i = 0; i < path.size()-1; i++){
            if(g instanceof ResidualList){
                Arc arc = ((ResidualList)g).getArcValue(path.get(i), path.get(i+1));
                pathSelected.add(arc.getCapacity() - arc.getFlow());
            }else if(g instanceof GraphResidual){
                ArcNetwork arc = ((GraphResidual)g).getArcValue(path.get(i), path.get(i+1));
                pathSelected.add(arc.getCapacity() - arc.getFlow());
            }
        }
        if(logs){
            for(int i = 0; i < pathSelected.size(); i++){
                System.out.println("Cf(" + (path.get(i) == 0 ? "S" :path.get(i))  + "," + (path.get(i+1) == g.getNodeNumber()-1 ? "T" :path.get(i+1)) + ") = " + pathSelected.get(i));
            }
        }
        return Util.min(pathSelected);
    }

    private static void spreadFlow(float flow, AbstractGraph g, ArrayList<Integer> path){
        for(int i = 0; i < path.size()-1; i++){
            if(g instanceof ResidualList){
                Arc arc = ((ResidualList)g).getArcValue(path.get(i), path.get(i+1));
                arc.setFlow(arc.getFlow() + flow);
                ((ResidualList)g).getArcValue(path.get(i+1), path.get(i)).setFlow(-arc.getFlow());
            }else if(g instanceof GraphResidual){
                ArcNetwork arc = ((GraphResidual)g).getArcValue(path.get(i), path.get(i+1));
                arc.setFlow(arc.getFlow() + flow);
                ((GraphResidual)g).getArcValue(path.get(i+1), path.get(i)).setFlow(-arc.getFlow());
            }
        }
    }
}
