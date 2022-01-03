package com.example.climbon;

import android.app.Application;

import java.util.ArrayList;
import java.util.Arrays;

public class ClimbOnApplication extends Application {
    /* Holds gloabl data for storage.

    TODO:
    Refator with Unviversal Data?
    */
    private static ClimbOnApplication singleton;
    public UniversalData data = new UniversalData();

    public ClimbOnApplication getInstance() {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleton = this;
        initializeSaveData();
    }

    private void initializeSaveData() {
        /* Fake function since no data to download yet. */
        data.current_shape = 1;
        ArrayList<ArrayList<Float>> corners = new ArrayList<>();
        ArrayList<Float> corner1 = new ArrayList<>(Arrays.asList(0f, 0f, 10f, 0f, 10f, 8f, 8f, 10f, 0f, 10f, 0.5f, 0.5f));
        ArrayList<Float> corner2 = new ArrayList<>(Arrays.asList(0f, 0f, 4f, 0f, 5f, 16f, 3f, 18f, 0f, 12f, 0.5f, 0.5f ));
        ArrayList<Float> corner3 = new ArrayList<>(Arrays.asList(0f, 0f, 10f, 0f, 10f, 8f, 8f, 10f, 0f, 10f, 0.5f, 0.5f));
        corners.add(corner1);
        corners.add(corner2);
        corners.add(corner3);

        ArrayList<Integer> hold_types = new ArrayList<>(Arrays.asList(1, 2, 3, 5, 7, 0, 1, 0, 3, 2, 4, 1, 4, 2, 6,3,5,2,5,2,4,1,5,0,0,0,4,2,4,3,2,4,7,5,1,0,3,2,6,6,6,3,5,1,4,5));

        createShapes(corners, hold_types);
    }

    private void createShapes(ArrayList<ArrayList<Float>> panels, ArrayList<Integer> holds) {
        /*
        The data download will be in the format of:
         - an array list of array list of floats for the panel shapes
         - an array list of integers for the
         */
        int holds_counter = 0; // track current index into holds
        for (int i=0;i<panels.size();++i) {
            ArrayList<Float> current_panel =  panels.get(i);
            ArrayList<Float> current_border = new ArrayList<>(current_panel.subList(0, current_panel.size()-2));
            Coordinate current_start_point = new Coordinate(current_panel.get(current_panel.size()-2),
                                                            current_panel.get(current_panel.size()-1));
            try {
                Shape current_shape = new Shape(current_border, current_start_point);
                ArrayList<Integer> current_holds = new ArrayList<>(holds.subList(holds_counter,
                                                                    holds_counter += current_shape.getNumHolds()));
                current_shape.setHoldTypes(current_holds);
                data.wall.panel_set.add(current_shape);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}