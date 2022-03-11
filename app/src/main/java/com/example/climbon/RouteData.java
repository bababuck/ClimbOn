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

    public RouteData(RouteData oldRoute) {
        this.holds = new ArrayList<>(oldRoute.holds);
        this.v_rating = oldRoute.v_rating;
        this.type = oldRoute.type;
        this.name = oldRoute.name;
    }

    public String toStringInfo() {
        return name + "," + v_rating.toString() + "," + type;
    }

    public String toString() {
        String outstring = "";
        for (Boolean hold : holds) {
            outstring += hold.toString() + ",";
        }
        return outstring;
    }
}

