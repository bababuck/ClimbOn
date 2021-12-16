package com.example.climbon;

import android.widget.Button;

import java.util.ArrayList;

public abstract class Shape {
    /* Contains the shape of a panel.

    Shape is assumed to be non-concave.
    Edges of shape are straight.
    Hold is first placed at highest, leftmost possible point.
    Rest of holds are generated from there in gridlike fashion.
    Holds must be *0* inches vertically and horizontally from edge.

    Picture a shape as being inscribed in a rectangle,
    bottom let is (0,0)

    TODO:
    constructor/builder
    shift all points show edge so shape is 0,0
     will generate once on app startup, and then store
     */

    class Coordinate {
        float x;
        float y;
    }

    class Edge {
        // ax + by+ c = 0
        float max;
        float min;
        float a;
        float b;
        float c;
    }

    static float DISTANCE_BETWEEN_HOLDS = 3; // inches
    float distance_to_edge; // inches, will use 0 for now
    ArrayList<Edge> edges = new ArrayList();
    ArrayList<Coordinate> hold_set = new ArrayList();


    int getNumHolds(){
        /* Calculates how many holds a shape will contain. */
        return hold_set.size();
    }

    void genShape(){
        /* Generates a boundary for the shape. */
    };
    ArrayList<Button> genHolds(){
        /* Generates the images buttons of holds. */
    };

    void getHoldCoordinates(Coordinate point, boolean above, boolean below){
        /* Create array of the holds and their locations.

        First hold is (1) highest, (2) leftmost hold.
        Assumes we are given a random, VALID hold.
        Also assumes convex shape.

        |-----------|
        | 1 2 3 4 5 |
        | 6 7 8 9 10|
        |11 |-------|
        |---|

        There is a chance this could fail for narrow diagonal boards,
        but if is an issue can address later.

        Not worried about time complexity of adding to front/back.
        */
        float x, y;
        ArrayList<Coordinate> current_row = new ArrayList();
        ArrayList<Coordinate> above_rows = new ArrayList();
        ArrayList<Coordinate> below_rows = new ArrayList();
        hold_set = new ArrayList();
        current_row.add(point);
        {
            Coordinate current_point = new Coordinate(point.x - DISTANCE_BETWEEN_HOLDS, point.y);
            while (isInside(current_point)) {
                Coordinate above_point = new Coordinate(point.x, point.y + DISTANCE_BETWEEN_HOLDS);
                if (above && isInside(above_point)) {
                    above_rows = getHoldCoordinates(above_point, true, false);
                    above = false;
                }
                Coordinate below_point = new Coordinate(point.x, point.y - DISTANCE_BETWEEN_HOLDS);
                if (below && isInside(below_point)) {
                    below_rows = getHoldCoordinates(above_point, false, true);
                    below = false;
                }
                current_row.add(0, current_point);
                current_point = new Coordinate(current_point.x - DISTANCE_BETWEEN_HOLDS, current_point.y);
            }
        }
        {
            Coordinate current_point = new Coordinate(point.x + DISTANCE_BETWEEN_HOLDS, point.y);
            while (isInside(current_point)) {
                Coordinate above_point = new Coordinate(point.x, point.y + DISTANCE_BETWEEN_HOLDS);
                if (above && isInside(above_point)) {
                    above_rows = getHoldCoordinates(above_point, true, false);
                    above = false;
                }
                Coordinate below_point = new Coordinate(point.x, point.y - DISTANCE_BETWEEN_HOLDS);
                if (below && isInside(above_point)) {
                    below_rows = getHoldCoordinates(above_point, false, true);
                    below = false;
                }
                current_row.add(current_point);
                current_point = new Coordinate(current_point.x + DISTANCE_BETWEEN_HOLDS, current_point.y);
            }
        }
        hold_set.addAll(above_rows);
        hold_set.addAll(current_row);
        hold_set.addAll(below_rows);
    }


    boolean isInside(Coordinate point){
        /* Finds if a point is inside a shape using the ray method.

        If passes through an even number of edges, then is outside the figure.
        */
        Edge ray = new Edge(true, min=point.y, max=Inf, a=1, b=0 , c=point.x);
        int count = 0;
        for (int i=0;i<edges.size();++i){
            if (intersects(edges.get(i),ray)){
                ++count;
            }
        }
    }

    boolean intersects(Edge edge, Edge ray){
        /* Determines if an edge will intersect with a ray.

        Assume ray is vertical, since we will always define it as such.
        */
        if (ray.b != 0){
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
        if ((edge.b == 0 && ray.b == 0) ||
                ((edge.b != 0 && ray.b != 0) &&
                (edge.a/edge.b == ray.a/ray.b))){
            // Check corner case of two parallel lines
            return null;
        } else {
            Coordinate intersection = new Coordinate();
            intersection.x = (ray.c/ray.b - edge.c/edge.b)/(edge.a/edge.b - ray.a/ray.b); // checked if denominator == 0
            intersection.y = -(ray.c + intersection.x * ray.a)/ray.b;
            return intersection;
        }
    }
}
