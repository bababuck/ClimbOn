package com.example.climbon;

import android.graphics.Canvas;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Shape {
    /* Contains the shape of a panel.

    Shape is assumed to be non-concave.
    Edges of shape are straight.
    Hold is first placed at highest, leftmost possible point.
    Rest of holds are generated from there in grid-like fashion.
    Holds must be *0* inches vertically and horizontally from edge.

    Picture a shape as being inscribed in a rectangle,
    bottom let is (0,0)

    TODO:
    constructor/builder
    will generate once on app startup, and then store
    */

    class Edge {
        // ax + by+ c = 0
        // Max and min are x unless b = 0, then y
        double max;
        double min;
        double a;
        double b;
        double c;

        Edge (Coordinate left, Coordinate right) {
            /* Creates an edge from two points */
            a = -(left.y - right.y);
            b = (left.x - right.x);
            c = -1 * (a * left.x + b * left.y);
            if (a == 0) { // if vertical, use y for min/max
                min = Math.min(left.y, right.y);
                max = Math.max(left.y, right.y);
            }
            if (a != 0) {
                min = Math.min(left.x, right.x);
                max = Math.max(left.x, right.x);
            }
        }
    }

    static double DISTANCE_BETWEEN_HOLDS = 3; // inches
//    double distance_to_edge; // inches, will use 0 for now
    ArrayList<Coordinate> corners;
    ArrayList<Edge> edges;
    ArrayList<Button> hold_set;
    private final Boundary boundary;

    public Shape(ArrayList<Double> _corners, Coordinate start_point) throws Exception {
        /* Initializes a shape with holds from list of x,y pairs */
        if (_corners.size() % 2 == 1){
            throw new Exception("Can't give half coordinate.");
        }
        for (int i=0;i<_corners.size() / 2;++i) {
            corners.add(new Coordinate(_corners.get(2*i),_corners.get(2*i+1)));
        }
        shiftCorners();
        cornersToEdges();
        genHolds(start_point);
        boundary = new Boundary(corners);
    }

    public void draw(@NonNull Canvas canvas) {
        /* Draw the shape outline. */
        boundary.draw(canvas);
    }

    private void cornersToEdges() throws Exception {
        /* Converts the corners list to an edges list

        Edges are used to make holds.
        To be called in the constructor.
        */
        if (corners.size() < 3) {
            throw new Exception("Shape must have at least 3 sides.");
        }
        for (int i=0;i<corners.size();++i) {
            edges.add(new Edge(corners.get(i), corners.get(i+1)));
        }
        edges.add(new Edge(corners.get(corners.size()-1), corners.get(0)));
    }

    public int getNumHolds(){
        /* Calculates how many holds a shape will contain. */
        return hold_set.size();
    }

    public void updateHolds(Coordinate new_point) throws Exception {
        /* Reinitialize the holds with a new starting point. */
        genHolds(new_point);
    }

    private void shiftCorners() {
        /* Shifts the corners so the surrounding "rectangle" is at 0,0

        Easier to work with if the surrounding "rectangle" is at 0,0.
        For now, assumes all points are positive.
        */
        double min_x = Double.POSITIVE_INFINITY;
        double min_y = Double.POSITIVE_INFINITY;
        for (int i=0;i<corners.size();++i) {
            if (corners.get(i).x < min_x) {
                min_x = corners.get(i).x;
            }
            if (corners.get(i).y < min_y) {
                min_y = corners.get(i).y;
            }
        }
        for (int i=0;i<corners.size();++i) {
            corners.get(i).x -= min_x;
            corners.get(i).y -= min_y;
        }
    }

    void genShape(){
        /* Generates a boundary for the shape. */
    }

    private void genHolds(Coordinate start_point) throws Exception {
        /* Generates the images buttons of holds.

        Stores them with the object.
        TODO:
        -Finish this, then add interface to "draw"
        */
        ArrayList<Coordinate> hold_locations = getHoldCoordinates(start_point, true, true);
        //ArrayList<Button>
    }

    ArrayList<Coordinate> getHoldCoordinates(Coordinate point, boolean above, boolean below) throws Exception {
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
        if (!isInside(point)) {
            throw new Exception("Given point is outside shape.");
        }
        ArrayList<Coordinate> current_row = new ArrayList<>();
        ArrayList<Coordinate> above_rows = new ArrayList<>();
        ArrayList<Coordinate> below_rows = new ArrayList<>();
        ArrayList<Coordinate> hold_locations = new ArrayList<>();
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
                    below_rows = getHoldCoordinates(below_point, false, true);
                    below = false;
                }
                current_row.add(current_point);
                current_point = new Coordinate(current_point.x + DISTANCE_BETWEEN_HOLDS, current_point.y);
            }
        }
        hold_locations.addAll(above_rows);
        hold_locations.addAll(current_row);
        hold_locations.addAll(below_rows);
        return hold_locations;
    }


    boolean isInside(Coordinate point) throws Exception {
        /* Finds if a point is inside a shape using the ray method.

        If passes through an even number of edges, then is outside the figure.
        */
        Edge ray = new Edge(point, new Coordinate(point.x, Double.POSITIVE_INFINITY));
        int count = 0;
        for (int i=0;i<edges.size();++i){
            if (intersects(edges.get(i),ray)){
                ++count;
            }
        }
        return (count % 2) == 0;
    }

    boolean intersects(Edge edge, Edge ray) throws Exception {
        /* Determines if an edge will intersect with a ray.

        Assume ray is vertical, since we will always define it as such.
        */
        if (ray.b != 0){
            throw new Exception("Ray isn't vertical.");
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

        Might get finicky since doubles (oh well)
        */
        if ((edge.b == 0 && ray.b == 0) ||
                ((edge.b != 0 && ray.b != 0) &&
                (edge.a/edge.b == ray.a/ray.b))){
            // Check corner case of two parallel lines
            return null;
        } else {
            Coordinate intersection = new Coordinate(0,0); //Don't cares
            intersection.x = (ray.c/ray.b - edge.c/edge.b)/(edge.a/edge.b - ray.a/ray.b); // checked if denominator == 0
            intersection.y = -(ray.c + intersection.x * ray.a)/ray.b;
            return intersection;
        }
    }
}
