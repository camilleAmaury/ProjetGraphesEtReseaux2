package com.classes.Graph.File;

import com.classes.Graph.AbstractNode;

public class NodeFile extends AbstractNode {

    /* # ------------------> Properties <------------------ # */
    // List of LinkedList which contains what the user specify
    private float probaA;
    // Associative table with reference an arc with its content
    private float probaB;

    /* # ------------------> Constructors <------------------ # */
    public NodeFile(int id) {
        super(id);
    }

    /* # ------------------> Accessors and Mutators <------------------ # */
    public float getProbaA() {
        return probaA;
    }

    public void setProbaA(float probaA) {
        this.probaA = probaA;
    }
    public float getProbaB() {
        return probaB;
    }

    public void setProbaB(float probaB) {
        this.probaB = probaB;
    }


    /* # ------------------> Methods <------------------ # */

}
