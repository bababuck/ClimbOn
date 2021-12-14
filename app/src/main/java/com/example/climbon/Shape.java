package com.example.climbon;

import android.widget.Button;

import java.util.ArrayList;

public abstract class Shape {
    /* Contains the shape of a panel.

    Shape is assumed to be non-concave.
    Edges of shape are straight.
    Hold is first placed at highest, leftmost possible point.
    Rest of holds are generated from there in gridlike fashion.
    Holds must be 6 inches vertically and horizontally from edge.

    Picture a shape as being inscribed in a rectangle,
    bottom let is (0,0)
     */

    class Coordinate {
        float x;
        float y;
    }

    ArrayList<Coordinate> panel_set = new ArrayList();
    int getNumHolds(){
        /* Calculates how many holds a shape will contain. */
        int num_holds = 0;

        return num_holds;
    }
    void genShape(){
        /* Generates a boundary for the shape. */
    };
    ArrayList<Button> genHolds(){
        /* Generates the images buttons of holds. */
    };
    ArrayList<Coordinate> getHoldCoordinates(){
        /* Create array of the holds and their locations. */
    };
}
