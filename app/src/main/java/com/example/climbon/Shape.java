package com.example.climbon;

import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;

public class Shape {
    /* Contains the shape of a panel.

    Shape is assumed to be non-concave.
    Edges of shape are straight.
    Hold is first placed at highest, leftmost possible point.
    Rest of holds are generated from there in grid-like fashion.
    Holds must be *0* inches vertically and horizontally from edge.

    Picture a shape as being inscribed in a rectangle,
    bottom let is (0,0)

    TODO: constructor/builder
    TODO: will generate once on app startup, and then store
    */

    class Edge {
        // ax + by+ c = 0
        // Max and min are x unless b = 0, then y
        float max;
        float min;
        float a;
        float b;
        float c;

        Edge (Coordinate left, Coordinate right) {
            /* Creates an edge from two points

            ax + by + c = 0
            */
            a = -(left.y - right.y);
            b = (left.x - right.x);
            c = -1 * (a * left.x + b * left.y);
            if (b == 0) { // if vertical, use y for min/max
                min = Math.min(left.y, right.y);
                max = Math.max(left.y, right.y);
            }
            if (b != 0) {
                min = Math.min(left.x, right.x);
                max = Math.max(left.x, right.x);
            }
        }
    }

    static float DISTANCE_BETWEEN_HOLDS = 3; // inches
//    float distance_to_edge; // inches, will use 0 for now
    ArrayList<Coordinate> corners;
    ArrayList<Edge> edges;
    ArrayList<Coordinate> hold_set;
    public ArrayList<Integer> hold_types = new ArrayList<>();

    public Shape(ArrayList<Float> _corners, Coordinate start_point) throws Exception {
        /* Initializes a shape with holds from list of x,y pairs */
        corners = new ArrayList<>();
        edges = new ArrayList<>();
        if (_corners.size() % 2 == 1){
            throw new Exception("Can't give half coordinate.");
        }
        for (int i=0;i<_corners.size() / 2;++i) {
            corners.add(new Coordinate(_corners.get(2*i),_corners.get(2*i+1)));
        }
        shiftCorners();
        cornersToEdges();
        genHolds(start_point);
        hold_types = new ArrayList<Integer>(Collections.nCopies(this.getNumHolds(), HoldType.NO_HOLD.ordinal()));
    }

    public void setHoldTypes(ArrayList<Integer> hold_types) {
        this.hold_types= hold_types;
    }

    private void cornersToEdges() throws Exception {
        /* Converts the corners list to an edges list

        Edges are used to make holds.
        To be called in the constructor.
        */
        if (corners.size() < 3) {
            throw new Exception("Shape must have at least 3 sides.");
        }
        for (int i=0;i<corners.size()-1;++i) {
            edges.add(new Edge(corners.get(i), corners.get(i+1)));
        }
        edges.add(new Edge(corners.get(corners.size()-1), corners.get(0)));
    }

    public int getNumHolds(){
        /* Calculates how many holds a shape will contain. */
        return hold_set.size();
    }

    public float get_height() {
        /* Calculate the height of the shape.

        Assume everything is non-negative.
        */
        float min = Float.POSITIVE_INFINITY;
        float max = 0;
        for (int i=0;i<corners.size();++i) {
            if (corners.get(i).y < min) {
                min = corners.get(i).y;
            }
            if (corners.get(i).y > max) {
                max = corners.get(i).y;
            }
        }
        return max - min;
    }

    public float get_width() {
        /* Calculate the height of the shape.

        Assume everything is non-negative.
        */
        float min = Float.POSITIVE_INFINITY;
        float max = 0;
        for (int i=0;i<corners.size();++i) {
            if (corners.get(i).x < min) {
                min = corners.get(i).x;
            }
            if (corners.get(i).x > max) {
                max = corners.get(i).x;
            }
        }
        return max - min;
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
        float min_x = Float.POSITIVE_INFINITY;
        float min_y = Float.POSITIVE_INFINITY;
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

    public void genHolds(Coordinate start_point) throws Exception {
        /* Generates the coordinates of holds.

        Stores them with the object.
        To support convex and irregular shapes, we check every hold location in boundary.
        Since shape is shifted, min_x and min_y are 0, and max_x and max_y are width and height.
        start_point does not have to be inside the shape.

                First hold is (1) highest, (2) leftmost hold.
        Assumes we are given a random, VALID hold.
        Also assumes convex shape.

        |---|   |---|
        |11 |---| 12|
        | 6 7 8 9 10|
        | 1 2 3 4 5 |
        |-----------|
        */
        float max_y = get_height();
        float max_x = get_width();
        hold_set = new ArrayList<>();

        // Calculate the first valid hold above 0, 0
        float start_x = start_point.x % DISTANCE_BETWEEN_HOLDS;
        float start_y = start_point.y % DISTANCE_BETWEEN_HOLDS;
        if (start_x < 0) start_x *= -1;
        if (start_y < 0) start_y *= -1;

        float current_y = start_y;
        while (current_y < max_y) {
            float current_x = start_x;
            while (current_x < max_x) {
                Coordinate current_point = new Coordinate(current_x, current_y);
                if (isInside(current_point)) {
                    hold_set.add(current_point);
                }
                current_x += DISTANCE_BETWEEN_HOLDS;
            }
            current_y += DISTANCE_BETWEEN_HOLDS;
        }
    }

    boolean isInside(Coordinate point) throws Exception {
        /* Finds if a point is inside a shape using the ray method.

        If passes through an even number of edges, then is outside the figure.
        */
        Edge ray = new Edge(point, new Coordinate(point.x, 1000.0f));
        int count = 0;
        for (int i=0;i<edges.size();++i){
            if (intersects(edges.get(i),ray)){
                ++count;
            }
        }
        return (count % 2) == 1;
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
            return (edge.min <= intersection.x) && (intersection.x <= edge.max) &&
                    (ray.min <= intersection.y) && (intersection.y <= ray.max);
        }
    }

    private Coordinate findIntersection(Edge edge, Edge ray){
        /* Find if a ray intersects with an edge, return the location.

        Might get finicky since floats (oh well)
        Assumes that ray.b == 0.
        */
        if (edge.b == 0f || edge.b == -0f) {
            // Check corner case of two parallel lines
            return null;
        } else {
            // Since ray is vertical, will intersect edge where x = ray.c
            Coordinate intersection = new Coordinate(0,0); //Don't cares
            intersection.y = -(-ray.c/ray.a * edge.a + edge.c)/edge.b;
            intersection.x = -ray.c/ray.a;
            return intersection;
        }
    }

    public String toString() {
        String outstring = "";
        for (Coordinate corner : corners) {
            outstring += corner.toString();
            outstring += ",";
        }
        outstring += hold_set.get(0).toString();
        return outstring;
    }

    public String holdTypesToString() {
        String outstring = "";
        for (Integer hold : hold_types) {
            outstring += hold.toString();
            outstring += ",";
        }
        return outstring;
    }
}
