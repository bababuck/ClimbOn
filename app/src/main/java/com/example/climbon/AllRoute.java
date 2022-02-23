package com.example.climbon;

import java.util.ArrayList;

public class AllRoute {
    ArrayList<RouteData> routes = new ArrayList<>();

    public ArrayList<String> printRouteInfo() {
        ArrayList<String> out_strings = new ArrayList<>();
        for (RouteData route : routes) {
            out_strings.add(route.toStringInfo());
        }
        return out_strings;
    }

    public ArrayList<String> printRoutes() {
        ArrayList<String> out_strings = new ArrayList<>();
        for (RouteData route : routes) {
            out_strings.add(route.toString());
        }
        return out_strings;
    }
}
