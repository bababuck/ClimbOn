package com.example.climbon;
import java.util.ArrayList;

class RouteData {
    int v_rating;
    ArrayList<Boolean> holds;
    int type;
    String name;
    public RouteData(int v_rating, ArrayList<Boolean> holds, int type, String name) {
        this.holds = holds;
        this.v_rating = v_rating;
        this.type = type;
        this.name = name;
    }
}

