package com.example.climbon;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UniversalData {
    /* Stores data to be stored across activities.

    Namely, we want to store search results for after viewing route.
    Can also store current route (while searching for other things).

    Also need to store the wall so don't regenerate every time.
    */
    Wall wall = new Wall();
    int current_shape; // Store which panel we are currently looking at for PanelViewer
}