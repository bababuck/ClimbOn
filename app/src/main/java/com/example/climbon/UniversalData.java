package com.example.climbon;

import java.util.ArrayList;

public class UniversalData {
    /* Stores data to be stored across activities.

    Namely, we want to store search results for after viewing route.
    Can also store current route (while searching for other things).

    Also need to store the wall so don't regenerate every time.
    */
    Wall wall = new Wall();
    int current_shape; // Store which panel we are currently looking at for PanelViewer
    String current_wall = "";
    ArrayList<String> wall_names = new ArrayList<>();

    String PANEL_FILE = "corners.txt";
    String HOLD_TYPES_FILE = "holds.txt";
    String ROUTE_INFO_FILE = "route_info.txt";
    String ROUTES_FILE = "routes.txt";
}