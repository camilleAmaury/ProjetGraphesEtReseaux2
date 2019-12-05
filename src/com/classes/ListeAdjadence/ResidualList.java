package com.classes.ListeAdjadence;

import com.classes.Graph.AbstractGraph;
import com.classes.Graph.Network.ArcNetwork;
import com.classes.Graph.Network.NodeNetwork;

import java.util.Dictionary;
import java.util.LinkedList;

public class ResidualList extends AbstractGraph {
    /* # ------------------> Properties <------------------ # */
    /* # ------------------> Properties <------------------ # */
    // List of LinkedList which contains what the user specify
    protected LinkedList<Arc>[] graph;

    // Associative table with reference an arc with its content
    protected Dictionary<Integer, NodeNetwork> nodeValue;
    // number of nodes (S)
    protected int nodeNumber;
    protected int[] initialNodeSize;

    /* # ------------------> Constructors <------------------ # */
    public ResidualList(ListeAdjacence g){
        this.nodeValue = g.getNodeValue();
        this.nodeNumber = g.getNodeNumber();
        this.graph = g.getGraph();
        this.initialNodeSize = g.getInitialNodeSize();
    }

    /* # ------------------> Accessors and Mutators <------------------ # */

    public LinkedList<Arc>[] getGraph() {
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
        for(int j = 0; j < this.graph[node].size(); j++){
            if(arcExists(node, this.graph[node].get(j).getTo().getId())){
                result.add(this.graph[node].get(j).getTo().getId());
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
        for(int j = 0; j < this.graph[node].size(); j++){
            Arc arc = this.graph[node].get(j);
            if(arc.getTo().getId() == nodeSought){
                if(arc.getCapacity() != arc.getFlow()){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method which returns the value of a specified arc value
     * @param fromNode : int, the initial node (from)
     * @param toNode : int, the last node (to)
     * @return the value of the selected arc
     */
    public Arc getArcValue(int fromNode, int toNode){
        try{
            for(int j = 0; j < this.graph[fromNode].size(); j++) {
                if (this.graph[fromNode].get(j).getTo().getId() == toNode) {
                    return this.graph[fromNode].get(j);
                }
            }
            throw new Exception("Not found");
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

    public void print() {
        System.out.println("-----> List Network <------");
        // log arcs
        System.out.println("\n-----> Arcs <------\n");
        for(int i = 0; i < this.nodeNumber; i++){
            for(int j = 0; j < this.graph[i].size(); j++){
                System.out.println("Position in Graph[" + (i == 0 ? "S" : i) + "," + (j == this.nodeNumber-1 ? "T" : j) + "] : Capacity = " + this.getArcValue(i,this.graph[i].get(j).getTo().getId()).getCapacity() + ", Flot = " + this.getArcValue(i,this.graph[i].get(j).getTo().getId()).getFlow());
            }
        }
        // log nodes
        System.out.println("\n-----> Nodes <------\n");
        for(int i = 0; i < this.nodeNumber; i++){
            System.out.println("Node "+ i + (i != 0 && i != this.nodeNumber-1 ? (" Initial Number[" +  (((i-1) / this.initialNodeSize[0])+1) + "," + (((i-1) % this.initialNodeSize[1])+1) + "]") : ""));
        }
    }
}
