package com.example.climbon;

import android.app.Application;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class ClimbOnApplication extends Application {
    /* Holds global data for storage.

    TODO: Add null checks when gathering data since can be deleted
    - Try/catch statements
    */
    public UniversalData data = new UniversalData();

    class WallData {
        ArrayList<ArrayList<Float>> corners;
        ArrayList<Integer> hold_types;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadWallNames();
        // initializeSaveData();
    }

    private void loadWallNames() {
        /* Load all the wall names from the save data. */
        File cacheDir = this.getFilesDir();

        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; ++i) {
                File file = files[i];

                String file_name = file.getName();
                data.wall_names.add(file_name);
            }
        }
    }

    private void LoadWall(String wall_name) {
        /* Load all the information about the current wall.

        For now will load all the information about every route.
        Will be 4 files:
        - panel corner information: corners.txt
        - hold type information: holds.txt
        - route name/rating information: route_info.txt
        - route hold status information: routes.txt
        */
        File cacheDir = this.getFilesDir();
        String dir_path = cacheDir.getName();

        loadCornersHolds(dir_path + wall_name);
        loadRoutes(dir_path + wall_name);

//        cacheDir.mkdir();
//
//        current_shape = saved_data.wall.panel_set.get(saved_data.current_shape);
//        current_hold_types = current_shape.hold_types;
    }

    private void loadRoutes(String wall_name) {
        /* Load the routes from a wall */
        HashMap<String, RouteData> all_data = new HashMap();
        try {
            File info_file = new File(wall_name + "/" + data.ROUTE_INFO_FILE);
            File route_file = new File(wall_name + "/" + data.ROUTES_FILE);
            Scanner info_inputStream = new Scanner(info_file);
            Scanner route_inputStream = new Scanner(route_file);
            while(info_inputStream.hasNext() && route_inputStream.hasNext()){
                String info_line= info_inputStream.next();
                String route_line= route_inputStream.next();

                String[] info_values = info_line.split(",");
                String name= info_values[0];
                int rating = Integer.parseInt(info_values[1]);
                int type = Integer.parseInt(info_values[2]);

                String[] route_values = route_line.split(",");
                ArrayList<Boolean> current_route_holds = new ArrayList<>();
                for (int i=0; i< route_values.length; ++i) {
                    current_route_holds.add(Boolean.parseBoolean(route_values[i]));
                }
                RouteData current_route = new RouteData(rating, current_route_holds, type, name);
                all_data.put(name, current_route);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCornersHolds(String wall_name) {
        /* Load the corners from a wall. */
        ArrayList<ArrayList<Float>> corners = new ArrayList<>();
        try {
            File file = new File(wall_name + "/" + data.PANEL_FILE);
            Scanner inputStream = new Scanner(file);
            while(inputStream.hasNext()){
                String line= inputStream.next();
                String[] values = line.split(",");
                ArrayList<Float> current_panel = new ArrayList<>();
                for (int i=0; i< values.length; ++i) {
                    current_panel.add(Float.parseFloat(values[i]));
                }
                corners.add(current_panel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<Integer> hold_types = new ArrayList<>();
        try {
            File file = new File(wall_name + "/" + data.HOLD_TYPES_FILE);
            Scanner inputStream = new Scanner(file);
            while(inputStream.hasNext()){ // Should only go once
                String line= inputStream.next();
                String[] values = line.split(",");
                for (int i=0; i< values.length; ++i) {
                    hold_types.add(Integer.parseInt(values[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        createShapes(corners, hold_types);
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
        /* Going to be used when downloading data.

        The data download will be in the format of:
         - an array list of array list of floats for the panel shapes
         - an array list of integers for the hold types

         TODO: Actually save/download from device
         TODO: Remove try catch?
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