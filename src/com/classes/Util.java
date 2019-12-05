package com.classes;

import com.classes.Algorithms.CutAlgorithm.MinimumCut;
import com.classes.Algorithms.FlotAlgorithm.FordFulkerson;
import com.classes.Graph.AbstractGraph;
import com.classes.Graph.File.GraphFile;
import com.classes.Graph.Network.GraphNetwork;
import com.classes.Graph.File.NodeFile;
import com.classes.ListeAdjadence.ListeAdjacence;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;

public class Util {

    /** ----------------------------------------------------> Problem Functions <---------------------------------------------------- **/

    /**
     * Methods which take a formated text file and convert it into java Objects
     * @param filename : the "xxx".txt filename
     * @param logs : if you want logs to be printed
     * @param method : "list" or "matrix" to change the graph implementation
     */
    public static AbstractGraph ConstructionReseau(String filename, String method, boolean logs){
        try{
            // parse the file given to a graphFile object
            GraphFile g = Util.parserFileToGraphFile(filename, logs);
            AbstractGraph gr = null;
            if(method == "matrix"){
                // convert to the network graph
                gr = g.toNetworkGraph();
            }else if (method == "list"){
                // convert to the adj list
                gr = g.toAdjListGraph();
            }
            if(logs) gr.print();
            return gr;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Methods which print the maximum flow
     * @param graph : GraphNetwork, a random network graph to iterate on
     * @param logs : boolean, if you want to print information
     * @return GraphNetwork, the graph in his maximal flow state
     */
    public static AbstractGraph CalculFlotMax(AbstractGraph graph, boolean logs) {
        FordFulkerson algo = new FordFulkerson();
        graph = algo.executeAlgorithm(graph, logs);
        if(logs) graph.print();
        System.out.println("\nmaximum flow : " +
                (graph instanceof ListeAdjacence ? ((ListeAdjacence)graph).currentFlow() : ((GraphNetwork)graph).currentFlow()));
        return graph;
    }

    /**
     * Methods which give the minimal cut from a maximum flow stated graph
     * @param graph : GraphNetwork, the maximum flow stated graph
     * @param logs : boolean, if you want to print the information
     */
    public static Couple<ArrayList<Integer>, ArrayList<Integer>> CalculCoupeMin(AbstractGraph graph, boolean logs){
        MinimumCut minCutAlgo = new MinimumCut();
        return(minCutAlgo.executeAlgorithm(graph, logs));
    }

    /**
     * Methods which resolve the BinIm problem
     * @param filename : String, the 1st min cut corresponding to the nodes which belong to the first dimension
     * @param logs : boolean, if you want to print information
     * @param method : "list" or "matrix" to change the graph implementation
     */
    public static void ResolveBinIm(String filename, String method, boolean logs){
        // construct the graph from file
        AbstractGraph g = Util.ConstructionReseau(filename, method, logs);

        // calculate the maximum flow
        g = Util.CalculFlotMax(g, logs);
        // calculate the minimum cut
        Couple<ArrayList<Integer>, ArrayList<Integer>> cut = Util.CalculCoupeMin(g, logs);
        //logs the problem
        System.out.println(" ------------>  Appartenance au plan 1 <------------ ");
        for(int i =0; i < cut.getFirst().size(); i++){
            // exclude the source
            int node = cut.getFirst().get(i);
            if(node != 0){
                System.out.println("Node "+ node + " Pixel[" +  (((node-1) / g.getInitialNodeSize()[0])+1) + "," + (((node-1) % g.getInitialNodeSize()[1])+1) + "]");
            }
        }
        System.out.println("\n ------------>  Appartenance au plan 2 <------------ ");
        for(int i =0; i < cut.getSecond().size(); i++){
            // exclude the source
            int node = cut.getSecond().get(i);
            if(node != g.getNodeNumber()-1){
                System.out.println("Node "+ node + " Pixel[" +  (((node-1) / g.getInitialNodeSize()[0])+1) + "," + (((node-1) % g.getInitialNodeSize()[1])+1) + "]");
            }
        }
    }

    /** ----------------------------------------------------> Side Functions <---------------------------------------------------- **/

    /**
     * Methods which clone an arraylist (not the same pointers)
     * @param copy : ArrayList<Integer> the list to copy
     * @return ArrayList<Integer>, the new copied list
     */
    public static ArrayList<Integer> clone(ArrayList<Integer> copy) {
        ArrayList<Integer> copied = new ArrayList<>();
        for(int i = 0; i < copy.size(); i++){
            copied.add(copy.get(i));
        }
        return copied;
    }

    /**
     * Methods which gives the minimum value of an arraylist
     * @param li : ArrayList<Float>, the array list to look into
     * @return float, the minimum value
     */
    public static float min(ArrayList<Float> li) {
        float min = 1000000000;
        for(int i = 0; i < li.size(); i++){
            if(min > li.get(i)) min = li.get(i);
        }
        return min;
    }
    /**
     * Methods which parse a text file to a graphfile object
     * @param filename : string,  the .txt file name
     * @param logs : boolean, if you want to print the information
     * @return a graphfile object representing the file
     */
    private static GraphFile parserFileToGraphFile(String filename, boolean logs) throws IOException {
        // some variables needed to create the graph and extract from file
        String[] nm = {};
        GraphFile g = null;
        Hashtable<Integer, NodeFile> nodes = new Hashtable<Integer, NodeFile>(){};
        String ligne = "";
        int compteur = 0;
        int compteurNode = 0;
        int compteurLigne = 0;

        // Open the streams and try to read the specified filename without extension
        InputStream flux = new FileInputStream(filename + ".txt");
        InputStreamReader lecture = new InputStreamReader(flux);
        BufferedReader buffer = new BufferedReader(lecture);

        // parser
        while ((ligne=buffer.readLine())!=null){
            if(compteur == 0){
                if(logs) System.out.println(ligne);
                //n and m
                nm = ligne.split(" ");

                for(int i = 0; i < nm.length; i++){
                    nm[i] = nm[i].trim();
                }
                g = new GraphFile(Integer.parseInt(nm[0]), Integer.parseInt(nm[1]));
                compteur++;
            }else if(compteur == 1){
                // ligne vide
                if(logs) System.out.println(ligne);
                compteur++;
            }else if(compteur == 2){
                // matrice A
                if(logs) System.out.println(ligne);
                if(ligne.isEmpty()){
                    compteur++;
                    compteurNode = 0;
                }else{
                    String[] values = ligne.split(" ");
                    for(int i = 0; i < values.length; i++){
                        values[i] = values[i].trim();
                        nodes.put(compteurNode, new NodeFile(compteurNode));
                        nodes.get(compteurNode).setProbaA(Float.valueOf(values[i]));
                        compteurNode++;
                    }
                }
            }else if(compteur == 3){
                // matrice B
                if(logs) System.out.println(ligne);
                if(ligne.isEmpty()){
                    compteur++;
                    compteurNode = 0;
                }else{
                    String[] values = ligne.split(" ");
                    for(int i = 0; i < values.length; i++){
                        values[i] = values[i].trim();
                        nodes.get(compteurNode).setProbaB(Float.valueOf(values[i]));
                        compteurNode++;
                    }
                }
            }else if(compteur == 4){
                // matrice P - voisinnage left/right
                if(logs) System.out.println(ligne);
                if(ligne.isEmpty()){
                    compteur++;
                    compteurNode = 0;
                    compteurLigne = 0;
                }else{
                    String[] values = ligne.split(" ");
                    for(int i = 0; i < values.length; i++){
                        values[i] = values[i].trim();
                        int fromNode = compteurLigne * (Integer.parseInt(nm[0])) + compteurNode;
                        int toNode = compteurLigne * (Integer.parseInt(nm[0])) + compteurNode + 1;
                        g.getArcValue(fromNode, toNode).setPenality(Float.valueOf(values[i]));
                        g.getArcValue(toNode, fromNode).setPenality(Float.valueOf(values[i]));
                        compteurNode++;
                    }
                    compteurLigne++;
                    compteurNode = 0;
                }
            }else if(compteur == 5){
                // matrice P - voisinnage left/right
                if(logs) System.out.println(ligne);
                if(ligne.isEmpty()){
                    compteur++;
                }else{
                    String[] values = ligne.split(" ");
                    for(int i = 0; i < values.length; i++){
                        values[i] = values[i].trim();
                        int fromNode = compteurLigne * (Integer.parseInt(nm[0])) + compteurNode;
                        int toNode = (compteurLigne + 1) * (Integer.parseInt(nm[0])) + compteurNode;
                        g.getArcValue(fromNode, toNode).setPenality(Float.valueOf(values[i]));
                        g.getArcValue(toNode, fromNode).setPenality(Float.valueOf(values[i]));
                        compteurNode++;
                    }
                    compteurLigne++;
                    compteurNode = 0;
                }
            }
        }
        buffer.close();
        g.setNodeValue(nodes);
        if(logs) g.print();
        return g;
    }

}
