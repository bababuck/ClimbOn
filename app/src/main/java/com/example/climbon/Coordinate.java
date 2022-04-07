package com.example.climbon;

public class Coordinate {
    /* Simple class to store a coordinate in a 2D-plane. */
    float x;
    float y;

    Coordinate(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return x + "," + y;
    }

}
