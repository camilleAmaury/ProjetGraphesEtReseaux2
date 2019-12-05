package com.classes.ListeAdjadence;

import com.classes.Graph.AbstractGraph;
import com.classes.Graph.File.GraphFile;
import com.classes.Graph.Network.ArcNetwork;
import com.classes.Graph.Network.NodeNetwork;
import com.classes.Graph.Residual.GraphResidual;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.LinkedList;

public class ListeAdjacence extends AbstractGraph {

        /* # ------------------> Properties <------------------ # */
        // List of LinkedList which contains what the user specify
        protected LinkedList<Arc>[] graph;

        // Associative table with reference an arc with its content
        protected Dictionary<Integer, NodeNetwork> nodeValue;
        // number of nodes (S)
        protected int nodeNumber;
        protected int[] initialNodeSize;

        /* # ------------------> Constructors <------------------ # */
        public ListeAdjacence(LinkedList<Arc>[] g){
            this.graph = g;
            this.initialNodeSize = new int[]{g.length,g.length};
            this.nodeNumber = g.length;
            // create new nodes
            this.nodeValue = new Hashtable<Integer, NodeNetwork>(){};
            for(int i = 0; i < this.nodeNumber; i++){
                this.nodeValue.put(i, new NodeNetwork(i));
            }
        }
        public ListeAdjacence(GraphFile g){
            this.nodeNumber = g.getNodeNumber()+2;
            // instanciate graph
            this.graph = new LinkedList[this.nodeNumber];
            for(int i = 0; i < this.graph.length; i++){
                this.graph[i] = new LinkedList<>();
            }
            // create new nodes
            this.nodeValue = new Hashtable<Integer, NodeNetwork>(){};
            for(int i = 0; i < this.nodeNumber; i++){
                this.nodeValue.put(i, new NodeNetwork(i));
            }
            // convert old graph to the new one
            for(int i = 0; i < g.getNodeNumber(); i++){
                for(int j = 0; j < g.getNodeNumber(); j++){
                    // we add the Source node before all, and the Pit after all
                    if(g.getArcValue(i,j).getPenality() != 0){
                        this.graph[i+1].add(new Arc(this.nodeValue.get(j+1), g.getArcValue(i,j).getPenality()));
                    }
                }
            }

            // set A probabilities from the source to every point except the pit
            for(int j = 1; j < this.nodeNumber-1; j++){
                // we add the Source node before all, and the Pit after all
                this.graph[0].add(new Arc(this.nodeValue.get(j), g.getNodeValue(j-1).getProbaA()));
                this.graph[j].add(new Arc(this.nodeValue.get(0), 0));
            }

            // set B probabilities from the every point to the pit except the source
            for(int i = 1; i < this.nodeNumber-1; i++){
                // we add the Source node before all, and the Pit after all
                this.graph[i].add(new Arc(this.nodeValue.get(this.nodeNumber-1), g.getNodeValue(i-1).getProbaB()));
                this.graph[this.nodeNumber-1].add(new Arc(this.nodeValue.get(i), 0));
            }
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
                result.add(this.graph[node].get(j).getTo().getId());
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
                    if(arc.getCapacity() != 0){
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

        /**
         * Methods which returns the current flow of the network graph
         * @return float : the current flow
         */
        public float currentFlow() {
            float somme = 0;
            for(int j = 0; j < this.graph[0].size(); j++){
                somme += this.getArcValue(0,this.graph[0].get(j).getTo().getId()).getFlow();
            }
            return(somme);
        }

        /**
         * Transform an existing graph to his residual propagated graph
         * @param g : GraphResidual
         */
        public void replace(ResidualList g){
            for(int i = 0; i < this.nodeNumber; i++){
                for(int j = 0; j < this.graph[i].size(); j++){
                    Arc arc = this.graph[i].get(j);
                    arc.setFlow(g.getArcValue(i, arc.getTo().getId()).getFlow());
                }
            }
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
                System.out.println("Node "+ i + (i != 0 && i != this.nodeNumber-1 ? (" Pixel[" +  (((i-1) / this.initialNodeSize[0])+1) + "," + (((i-1) % this.initialNodeSize[1])+1) + "]") : ""));
            }
        }

}
