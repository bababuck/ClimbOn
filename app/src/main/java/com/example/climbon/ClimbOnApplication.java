package com.example.climbon;

import android.app.Application;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ClimbOnApplication extends Application {
    /* Holds global data for storage.

    TODO: Add null checks when gathering data since can be deleted
    - Try/catch statements
    */
    public UniversalData data = new UniversalData();

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
            for (File file : files) {
                String file_name = file.getName();
                data.wall_names.add(file_name);
            }
        }
    }

    public void saveWallPanels() {
        /* Save the current wall panel data into memory. */
        try {
            Log.e("Application","Entering saveWallPanels...");
            String wall_name = data.current_wall;

            if (!data.wall_names.contains(wall_name)) {
                File wall_dir = new File(this.getFilesDir(), wall_name);
                Log.e("Application","Making new directory");
                wall_dir.mkdir();
            }

            String file_path = this.getFilesDir() + File.pathSeparator + wall_name + File.pathSeparator + data.PANEL_FILE;
            Log.e("Application","Writing file: " + file_path);

            Log.e("Application","Create FileWriter");
            FileWriter fw = new FileWriter(file_path, false);
            Log.e("Application","Create BufferedWriter");
            BufferedWriter bw = new BufferedWriter(fw);
            for (String line : data.wall.printString()) {
                bw.write(line);
            }
            Log.e("Application","Closing BufferedWriter");
            bw.close();

            Log.e("Application","Exiting saveWallPanels successfully...");
        } catch (IOException e) {
            Log.e("Application", e.toString());
            Log.e("Application","Exiting saveWallPanels un-successfully...");
        }
    }

    public void updateAllRoutes() {
        /* Save all routes, will need to be done after file deletion. */
        try {
            String wall_name = data.current_wall;

            FileWriter fw_routes = new FileWriter(wall_name + "/" + data.ROUTES_FILE, false);
            FileWriter fw_info = new FileWriter(wall_name + "/" + data.ROUTE_INFO_FILE, false);
            BufferedWriter bw_routes = new BufferedWriter(fw_routes);
            BufferedWriter bw_info = new BufferedWriter(fw_info);
            for (String line : data.routes.printRoutes()) {
                bw_routes.write(line);
            }
            for (String line : data.routes.printRouteInfo()) {
                bw_info.write(line);
            }
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
    }

    public void saveAddRoute() {
        /* Save final route to end of file. */
        try {
            String wall_name = data.current_wall;

            FileWriter fw_routes = new FileWriter(wall_name + "/" + data.ROUTES_FILE, true);
            FileWriter fw_info = new FileWriter(wall_name + "/" + data.ROUTE_INFO_FILE, true);
            BufferedWriter bw_routes = new BufferedWriter(fw_routes);
            BufferedWriter bw_info = new BufferedWriter(fw_info);
            int size = data.routes.routes.size();
            bw_routes.write(data.routes.routes.get(size - 1).toString());
            bw_info.write(data.routes.routes.get(size - 1).toStringInfo());
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
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
    }

    private void loadRoutes(String wall_name) {
        /* Load the routes from a wall */
        data.routes = new AllRoute();
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
                String type = info_values[2];

                String[] route_values = route_line.split(",");
                ArrayList<Boolean> current_route_holds = new ArrayList<>();
                for (String hold : route_values) {
                    current_route_holds.add(Boolean.parseBoolean(hold));
                }
                RouteData current_route = new RouteData(rating, current_route_holds, type, name);
                data.routes.routes.add(current_route);
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
                for (String value : values) {
                    current_panel.add(Float.parseFloat(value));
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
                for (String value : values) {
                    hold_types.add(Integer.parseInt(value));
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