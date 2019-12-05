package com.classes.Graph;

public abstract class AbstractNode {
    /* # ------------------> Properties <------------------ # */
    // Associative table with reference an arc with its content
    private int id;

    /* # ------------------> Constructors <------------------ # */
    public AbstractNode(int id){
        this.id = id;
    }

    /* # ------------------> Accessors and Mutators <------------------ # */
    public int getId() {
        return id;
    }


    /* # ------------------> Methods <------------------ # */

}
