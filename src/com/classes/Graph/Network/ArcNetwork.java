package com.classes.Graph.Network;

import com.classes.Graph.AbstractArc;

public class ArcNetwork extends AbstractArc {
    /* # ------------------> Properties <------------------ # */
    // maximum capacity of the arc
    private float capacity;
    // current flow
    private float flow;

    /* # ------------------> Constructors <------------------ # */
    public ArcNetwork(float capacity){
        this.capacity = capacity;
        this.flow = 0;
    }

    /* # ------------------> Accessors and Mutators <------------------ # */

    public float getCapacity() {
        return capacity;
    }

    public void setCapacity(float capacity) {
        this.capacity = capacity;
    }

    public float getFlow() {
        return flow;
    }

    public void setFlow(float flow) {
        this.flow = flow;
    }

    /* # ------------------> Methods <------------------ # */

}
