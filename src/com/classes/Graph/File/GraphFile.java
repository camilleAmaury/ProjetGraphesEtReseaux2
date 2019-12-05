package com.classes.Graph.File;

import com.classes.Graph.IGraph;
import com.classes.Graph.Network.GraphNetwork;

import java.util.Dictionary;
import java.util.LinkedList;

public class GraphFile implements IGraph {
    /* # ------------------> Properties <------------------ # */
    // List of LinkedList which contains what the user specify
    protected ArcFile[][] graph;
    // Associative table with reference an arc with its content
    protected Dictionary<Integer, NodeFile> nodeValue;
    // number of nodes (S)
    protected int nodeNumber;
    protected int[] initialNodeSize;

    /* # ------------------> Constructors <------------------ # */
    public GraphFile(int dimensionN, int dimensionP){
        int mul = dimensionN*dimensionP;
        this.graph = new ArcFile[mul][mul];
        for(int i = 0; i < this.graph.length; i++){
            for(int j = 0; j < this.graph[i].length; j++){
                this.graph[i][j] = new ArcFile();
            }
        }
        this.nodeNumber = mul;
        this.initialNodeSize = new int[]{dimensionN, dimensionP};
    }

    /* # ------------------> Accessors and Mutators <------------------ # */


    @Override
    public int getNodeNumber() {
        return this.nodeNumber;
    }

    @Override
    public int[] getInitialNodeSize() {
        return this.initialNodeSize;
    }

    public void setNodeValue(Dictionary<Integer, NodeFile> nodes) {
        this.nodeValue = nodes;
    }

    /* # ------------------> Methods <------------------ # */
    /**
     * Method which returns all the successors of a specified node number
     * @param node : int, the specified node
     * @return LinkedList<Integer>, successors
     */
    public LinkedList<Integer> getSuccesors(int node){
        LinkedList<Integer> result = new LinkedList<>();
        ArcFile[] potentialSuccessors = this.graph[node];
        for(int j = 0; j < potentialSuccessors.length; j++){
            if(potentialSuccessors[j].getPenality() != 0){
                result.add(j);
            }
        }
        return result;
    }

    /**
     * Method which returns all the predecessors of a specified node number
     * @param node : int, the specified node
     * @return LinkedList<Integer>, predecessors
     */
    public LinkedList<Integer> getPredecessors(int node){
        LinkedList<Integer> result = new LinkedList<>();
        for(int i = 0; i < this.graph.length; i++){
            if(this.arcExists(i, node)){
                result.add(i);
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
        return this.graph[node][nodeSought].getPenality() != 0;
    }

    /**
     * Method which returns the value of a specified arc value
     * @param fromNode : int, the initial node (from)
     * @param toNode : int, the last node (to)
     * @return the value of the selected arc
     */
    public ArcFile getArcValue(int fromNode, int toNode){
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
    public NodeFile getNodeValue(int node){
        try{
            return this.nodeValue.get(node);
        }catch (Exception e) {
            System.out.println("No existing node value found");
            return null;
        }
    }

    /**
     * Methods which print the whole graph for the user (arc + nodes)
     */
    public void print() {
        System.out.println("-----> Graph Extracted from file <------");
        // log arcs
        System.out.println("\n-----> Arcs <------\n");
        for(int i = 0; i < this.getNodeNumber(); i++){
            for(int j = 0; j < this.getNodeNumber(); j++){
                System.out.println("Position in Graph[" + i + "," + j + "] : Pénalité = " + this.getArcValue(i,j).getPenality());
            }
        }
        // log nodes
        System.out.println("\n-----> Nodes <------\n");
        for(int i = 0; i < this.getNodeNumber(); i++){
            System.out.println("Node "+ i +" Pixel[" + ((i / this.initialNodeSize[0])+1) + "," + ((i % this.initialNodeSize[1])+1) + "] : P(A) = " + this.getNodeValue(i).getProbaA() + ", P(B) = " + this.getNodeValue(i).getProbaB());
        }
    }

    /**
     * Methods which convert a GraphFile to a GraphNetwork
     * @return GraphNetwork, the GraphFile's graph converted
     */
    public GraphNetwork toNetworkGraph(){
        return new GraphNetwork(this);
    }
}
