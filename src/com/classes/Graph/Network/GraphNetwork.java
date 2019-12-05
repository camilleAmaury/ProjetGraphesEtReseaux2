package com.classes.Graph.Network;

import com.classes.Graph.AbstractGraph;
import com.classes.Graph.File.GraphFile;
import com.classes.Graph.IGraph;
import com.classes.Graph.Residual.GraphResidual;

import java.util.*;

public class GraphNetwork extends AbstractGraph {

    /* # ------------------> Properties <------------------ # */
    // List of LinkedList which contains what the user specify
    protected ArcNetwork[][] graph;

    // Associative table with reference an arc with its content
    protected Dictionary<Integer, NodeNetwork> nodeValue;
    // number of nodes (S)
    protected int nodeNumber;
    protected int[] initialNodeSize;

    /* # ------------------> Constructors <------------------ # */
    public GraphNetwork(GraphFile g){
        this.nodeNumber = g.getNodeNumber()+2;
        this.graph = new ArcNetwork[this.nodeNumber][this.nodeNumber];
        // convert old graph to the new one
        for(int i = 0; i < this.nodeNumber; i++){
            for(int j = 0; j < this.nodeNumber; j++){
                // we add the Source node before all, and the Pit after all
                this.graph[i][j] = new ArcNetwork((i == 0 || i == this.graph.length - 1
                        || j == 0 || j == this.graph[i].length - 1) ? 0 : g.getArcValue(i-1,j-1).getPenality());
            }
        }
        // create new ones
        this.nodeValue = new Hashtable<Integer, NodeNetwork>(){};
        for(int i = 0; i < this.nodeNumber; i++){
            this.nodeValue.put(i, new NodeNetwork(i));
        }

        // set A probabilities from the source to every point except the pit
        for(int j = 1; j < this.nodeNumber-1; j++){
            // we add the Source node before all, and the Pit after all
            this.graph[0][j] = new ArcNetwork(g.getNodeValue(j-1).getProbaA());
        }

        // set B probabilities from the every point to the pit except the source
        for(int i = 1; i < this.nodeNumber-1; i++){
            // we add the Source node before all, and the Pit after all
            this.graph[i][this.nodeNumber-1] = new ArcNetwork(g.getNodeValue(i-1).getProbaB());
        }
        this.initialNodeSize = g.getInitialNodeSize();
    }

    public GraphNetwork(GraphNetwork g){
        this.nodeValue = g.nodeValue;
        this.nodeNumber = g.nodeNumber;
        this.graph = g.graph;
        this.initialNodeSize = g.initialNodeSize;
    }

    public GraphNetwork(ArcNetwork[][] graph){
        this.nodeNumber = graph.length;
        this.graph = graph;
        this.nodeValue = new Hashtable<>();
        for(int i = 0; i < this.nodeNumber; i++){
            this.nodeValue.put(i, new NodeNetwork(i));
        }
        this.initialNodeSize = new int[] {this.nodeNumber, this.nodeNumber};
    }

    /* # ------------------> Accessors and Mutators <------------------ # */

    public ArcNetwork[][] getGraph() {
        return graph;
    }
    public Dictionary<Integer, NodeNetwork> getNodeValue() {
        return nodeValue;
    }

    @Override
    public int getNodeNumber() {
        return this.nodeNumber;
    }

    @Override
    public int[] getInitialNodeSize() {
        return this.initialNodeSize;
    }

    /* # ------------------> Methods <------------------ # */


    public void setNodeValue(Dictionary<Integer, NodeNetwork> nodes) {
        this.nodeValue = nodes;
    }

    /**
     * Method which returns all the successors of a specified node number
     * @param node : int, the specified node
     * @return LinkedList<Integer>, successors
     */
    public LinkedList<Integer> getSuccesors(int node){
        LinkedList<Integer> result = new LinkedList<>();
        for(int j = 0; j < this.graph[node].length; j++){
            if(this.arcExists(node, j)){
                result.add(j);
            }
        }
        return result;
    }

    /**
     * Method which check if a specified arc between two node exists
     * @param node : int, the initial node (from)
     * @param nodeSought : int, the last node (to)
     * @return boolean, if the arc exists or not
     */
    public boolean arcExists(int node, int nodeSought){
        return this.graph[node][nodeSought].getCapacity() != 0;
    }

    /**
     * Method which returns the value of a specified arc value
     * @param fromNode : int, the initial node (from)
     * @param toNode : int, the last node (to)
     * @return the value of the selected arc
     */
    public ArcNetwork getArcValue(int fromNode, int toNode){
        try{
            return (this.graph[fromNode][toNode]);
        }catch (Exception e) {
            System.out.println("No existing arc value found");
            return null;
        }
    }

    /**
     * Method which returns the value of a specified node value
     * @param node : int, the specified node
     * @return the value of the selected node
     */
    public NodeNetwork getNodeValue(int node){
        try{
            return this.nodeValue.get(node);
        }catch (Exception e) {
            System.out.println("No existing node value found");
            return null;
        }
    }

    /**
     * Methods which returns the current flow of the network graph
     * @return float : the current flow
     */
    public float currentFlow() {
        float somme = 0;
        for(int j = 0; j < this.nodeNumber; j++){
            somme += this.getArcValue(0,j).getFlow();
        }
        return(somme);
    }

    /**
     * Transform an existing graph to his residual propagated graph
     * @param g : GraphResidual
     */
    public void replace(GraphResidual g){
        this.graph = g.getGraph();
    }

    /**
     * Methods which returns the value of a cut
     * @param X : ArrayList<Integer>,
     * @param Y : ArrayList<Integer>,
     * @return
     */
    public float cutScore(ArrayList<Integer> X, ArrayList<Integer> Y) {
        float somme = 0;
        for(int i = 0; i < X.size(); i++){
            LinkedList<Integer> succ = this.getSuccesors(X.get(i));
            for(int j = 0; j < succ.size(); j++){
                if(!X.contains(succ.get(j))){
                    somme += this.getArcValue(X.get(i), succ.get(j)).getFlow();
                }
            }
        }
        return(somme);
    }

    /**
     * Methods which print the whole graph for the user (arc + nodes)
     */
    public void print() {
        System.out.println("-----> Graph Network <------");
        // log arcs
        System.out.println("\n-----> Arcs <------\n");
        for(int i = 0; i < this.nodeNumber; i++){
            for(int j = 0; j < this.nodeNumber; j++){
                System.out.println("Position in Graph[" + (i == 0 ? "S" : i == this.nodeNumber-1 ? "T":i) + "," + (j == 0 ? "S" : j == this.nodeNumber-1 ? "T":j) + "] : Flot/Capacity : " + this.getArcValue(i,j).getFlow() + "/" + this.getArcValue(i,j).getCapacity());
            }
        }
        // log nodes
        System.out.println("\n-----> Nodes <------\n");
        for(int i = 0; i < this.nodeNumber; i++){
            System.out.println("Node "+ i + (i != 0 && i != this.nodeNumber-1 ? (" Pixel[" +  (((i-1) / this.initialNodeSize[0])+1) + "," + (((i-1) % this.initialNodeSize[1])+1) + "]") : ""));
        }
    }
}
