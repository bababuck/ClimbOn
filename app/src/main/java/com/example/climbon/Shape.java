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

    class Edge {
        // y = mx + b
        boolean vertical; // if vertical, max/min are y, else x
        float max;
        float min;
        float b;
        float m;
    }

    float distance_to_edge; // inches

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
    ArrayList<Coordinate> getHoldCoordinates(Coordinate top_left){
        /* Create array of the holds and their locations.

        First hold is (1) highest, (2) leftmost hold.
        Assumes we are given a random, VALID hold.
        */


    };
// Switch to ax + by + c = 0
    boolean isInside(Coordinate point, ArrayList<Edge> edges){
        Edge ray = new Edge(true, min=0, max=0, m=1); // min/max are don't cares
    }

    boolean intersects(Edge edge, Edge ray){
        /* Determines if an edge will intersect with a ray.

        Assume ray is vertical, since we will always define it as such.
        */
        if (!ray.vertical){
            // throw
        }
        Coordinate intersection = findIntersection(edge, ray);
        if (intersection == null){ // Parallel, no intersection
            return false;
        } else { // Not parallel, so edge isn't vertical (since ray is)
            return (edge.min <= intersection.x) && (intersection.x <= edge.max);
        }
    }

    Coordinate findIntersection(Edge edge, Edge ray){
        /* Find if a ray intersects with an edge, return the location.

        Might get finicky since floats (oh well)
        */
        if ((edge.vertical && ray.vertical) || (edge.m == ray.m)){
            // Check corner case of two parallel lines
            return null;
        } else {
            Coordinate intersection = new Coordinate();
            intersection.x = (ray.b - edge.b)/(edge.m - ray.m); // checked if denominator == 0
            intersection.y = ray.b + intersection.x * ray.m;
            return intersection;
        }
    }

}
