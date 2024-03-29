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

    /**
     * Methods which execute the ford fulkerson algorithm given a graph
     * @param g : AbstractGraph(ListeAdjacence or NetworkGraph), the graph
     * @param logs : if you want to print more informations
     * @return the maximal stated flow graph
     */
    public AbstractGraph executeAlgorithm(AbstractGraph g, boolean logs) {
        this.graphNetwork = FordFulkerson.initialization(g);
        this.graphResidual = this.graphNetwork instanceof ListeAdjacence ? new ResidualList((ListeAdjacence)this.graphNetwork) : new GraphResidual((GraphNetwork) this.graphNetwork);
        ArrayList<Integer>paths = FordFulkerson.findFirstPath(this.graphResidual);
        // while there is a path available
        while(paths.contains(g.getNodeNumber()-1)){
            if(logs) System.out.println("P = " + paths.toString());
            float cf_P = FordFulkerson.minResidualCapacity(paths, this.graphResidual, logs);
            if(logs) System.out.println("Cf(P) = " + cf_P);
            FordFulkerson.spreadFlow(cf_P, this.graphResidual, paths);
            if(this.graphNetwork instanceof ListeAdjacence && this.graphResidual instanceof ResidualList){
                ((ListeAdjacence)this.graphNetwork).replace((ResidualList) this.graphResidual);
            }else if(this.graphNetwork instanceof GraphNetwork && this.graphResidual instanceof GraphResidual){
                ((GraphNetwork)this.graphNetwork).replace((GraphResidual) this.graphResidual);
            }
            this.step++;
            paths = FordFulkerson.findFirstPath(this.graphResidual);
        }
        return this.graphNetwork;
    }

    /**
     * Methods which initialize every flow to 0
     * @param g : AbstractGraph(ListeAdjacence or NetworkGraph), the graph
     * @return : AbstractGraph initialized
     */
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

    /**
     * Methods which find a path in the Residual Graph
     * @param g : AbstractGraph(ResidualList or ResidualGraph), the graph
     * @return ArrayList<Integer> : the path represented by nodes' id
     */
    private static ArrayList<Integer> findFirstPath(AbstractGraph g){
        // instanciate a boolean tab representing the nodes
        boolean[] isAlreadySeen = new boolean[g.getNodeNumber()];
        for(int i = 0; i < isAlreadySeen.length; i++){
            isAlreadySeen[i] = false;
        }
        return rec_findFirstPaths(g, 0, isAlreadySeen);
    }

    /**
     * Methods which find a path in a graph
     * @param g : AbstractGraph(ResidualList or ResidualGraph), the graph
     * @param currentNode : int, the current node id
     * @param isAlreadySeen : boolean[] which nodes have already been seen
     * @return ArrayList<Integer> : the path represented by nodes' id
     */
    private static ArrayList<Integer> rec_findFirstPaths(AbstractGraph g, int currentNode, boolean[] isAlreadySeen){
        ArrayList<Integer> encapsulation = new ArrayList<Integer>(){};
        ArrayList<Integer> resRec = new ArrayList<Integer>(){};
        // if nodes is the pit
        if(currentNode == g.getNodeNumber()-1){
            encapsulation.add(currentNode);
            return encapsulation;
        }
        else{
            isAlreadySeen[currentNode] = true;
            // for each successor
            encapsulation.add(currentNode);
            for(int node : g.getSuccesors(currentNode)){
                if(!isAlreadySeen[node]){
                    resRec.addAll(rec_findFirstPaths(g, node, isAlreadySeen));
                    // we break the successor iteration if we found a path to the last node
                    if(resRec.contains(g.getNodeNumber()-1)){
                        encapsulation.addAll(resRec);
                        return encapsulation;
                    }else{
                        resRec.clear();
                    }
                }
            }
            isAlreadySeen[currentNode] = false;
            return encapsulation;
        }
    }

    /**
     * Methods which calculate the min residual capacity to propagate on a path
     * @param path : ArrayList<Integer>, the path represented by nodes' id
     * @param g : AbstractGraph(ResidualList or ResidualGraph), the graph
     * @param logs : Boolean, if you want to print more information
     * @return : AbstractGraph(ResidualList or ResidualGraph), the graph
     */
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

    /**
     * Methods which spead the flow on a path in a graph
     * @param flow : float, value to propagate
     * @param g : AbstractGraph(ResidualList or ResidualGraph), the graph
     * @param path : ArrayList<Integer>, the path represented by nodes' id
     */
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
