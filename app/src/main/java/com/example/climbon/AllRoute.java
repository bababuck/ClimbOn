package com.example.climbon;

import java.util.ArrayList;

public class AllRoute {
    ArrayList<RouteData> routes = new ArrayList<>();

    public ArrayList<String> printRouteInfo() {
        ArrayList<String> outstrings = new ArrayList<>();
        for (RouteData route : routes) {
            outstrings.add(route.toStringInfo());
        }
        return outstrings;
    }

    public ArrayList<String> printRoutes() {
        ArrayList<String> outstrings = new ArrayList<>();
        for (RouteData route : routes) {
            outstrings.add(route.toString());
        }
        return outstrings;
    }
}
