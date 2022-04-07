package com.example.climbon;

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
    */

    static class Edge {
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

    ArrayList<Coordinate> corners;
    ArrayList<Edge> edges;

    public Shape(ArrayList<Float> _corners) throws Exception {
        /* Initializes a shape with holds from list of x,y pairs */
        corners = new ArrayList<>();
        edges = new ArrayList<>();
        for (int i=0;i<_corners.size() / 2;++i) {
            corners.add(new Coordinate(_corners.get(2*i),_corners.get(2*i+1)));
        }
        shiftCorners();
        cornersToEdges();
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
            edges.add(new Edge(corners.get(i), corners.get(i + 1)));
        }
        edges.add(new Edge(corners.get(corners.size() - 1), corners.get(0)));
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
}
