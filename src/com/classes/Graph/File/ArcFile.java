package com.classes.Graph.File;

import com.classes.Graph.AbstractArc;

public class ArcFile extends AbstractArc {
    /* # ------------------> Properties <------------------ # */
    // Penality given if we separe neightboor Pixels
    private float Penality;

    /* # ------------------> Constructors <------------------ # */
    public ArcFile(){
        this.Penality = 0;
    }

    /* # ------------------> Accessors and Mutators <------------------ # */

    public float getPenality() {
        return Penality;
    }

    public void setPenality(float penality) {
        Penality = penality;
    }


    /* # ------------------> Methods <------------------ # */

}
