package com.classes.ListeAdjadence;

import com.classes.Graph.AbstractArc;
import com.classes.Graph.Network.NodeNetwork;

public class Arc extends AbstractArc {

    /* # ------------------> Properties <------------------ # */
    private NodeNetwork to;
    // maximum capacity of the arc
    private float capacity;
    // current flow
    private float flow;

    /* # ------------------> Constructors <------------------ # */
    public Arc(NodeNetwork to, float capacity){
        this.capacity = capacity;
        this.flow = 0;
        this.to = to;
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

    public NodeNetwork getTo() {
        return to;
    }

    public void setTo(NodeNetwork to) {
        this.to = to;
    }

    /* # ------------------> Methods <------------------ # */

}
