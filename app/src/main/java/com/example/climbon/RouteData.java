package com.example.climbon;
import java.util.ArrayList;

class RouteData {
    Integer v_rating;
    ArrayList<Boolean> holds;
    String type;
    String name;
    public RouteData(int v_rating, ArrayList<Boolean> holds, String type, String name) {
        this.holds = holds;
        this.v_rating = v_rating;
        this.type = type;
        this.name = name;
    }

    public String toStringInfo() {
        return name + v_rating.toString() + type;
    }

    public String toString() {
        return holds.toString();
    }
}

