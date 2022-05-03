package com.example.climbon;

import java.util.ArrayList;

public class UniversalData {
    /* Stores data to be stored across activities.

    Namely, we want to store search results for after viewing route.
    Can also store current route (while searching for other things).

    Also need to store the wall so don't regenerate every time.
    */
    String current_wall = "";
    String current_user;
    RouteData current_route;
    Boolean saved;
    int total_bits;
}