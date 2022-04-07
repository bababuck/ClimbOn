package com.example.climbon;

class RouteData {
    private Integer v_rating;
    private Integer type;
    private String name;
    private Integer id;
    public RouteData(int v_rating, Integer type, String name, Integer id) {
        this.id = id;
        this.v_rating = v_rating;
        this.type = type;
        this.name = name;
    }

    public RouteData(RouteData oldRoute) {
        this.id = oldRoute.id;
        this.v_rating = oldRoute.v_rating;
        this.type = oldRoute.type;
        this.name = oldRoute.name;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVRating() {
        return v_rating;
    }

    public String toStringInfo() {
        return name + "," + v_rating + "," + type;
    }
}

