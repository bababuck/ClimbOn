package com.example.climbon;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
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
    public SQLiteDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        WallInfoDbHelper dbHelper = new WallInfoDbHelper(this);
        db = dbHelper.getReadableDatabase();
    }

    private void loadWallNames() {
        /* Load all the wall names from the save data. */
        Log.e("Application","Loading wall names...");
        File cacheDir = this.getFilesDir();

        File[] files = cacheDir.listFiles();
        if (files != null) {
            for (File file : files) {
                String file_name = file.getName();
                data.wall_names.add(file_name);
            }
        }
        Log.e("Application","Finished loading wall names...");
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

            String file_path = this.getFilesDir() + File.separator + wall_name + File.separator + data.PANEL_FILE;
            Log.e("Application","Writing file: " + file_path);

            Log.e("Application","Create FileWriter");
            FileWriter fw = new FileWriter(file_path, false);
            Log.e("Application","Create BufferedWriter");
            BufferedWriter bw = new BufferedWriter(fw);
            for (String line : data.wall.printString()) {
                bw.write(line + "");
                bw.newLine();
            }
            Log.e("Application","Closing BufferedWriter");
            bw.close();

            Log.e("Application","Exiting saveWallPanels successfully...");
        } catch (IOException e) {
            Log.e("Application", e.toString());
            Log.e("Application","Exiting saveWallPanels un-successfully...");
        }
    }

    public void saveHoldTypes() {
        /* Save the current walls hold types into memory. */
        try {
            Log.e("Application","Entering saveHoldTypes...");
            String wall_name = data.current_wall;

            String file_path = this.getFilesDir() + File.separator + wall_name + File.separator + data.HOLD_TYPES_FILE;
            Log.e("Application","Writing file: " + file_path);

            Log.e("Application","Create FileWriter");
            FileWriter fw = new FileWriter(file_path, false);
            Log.e("Application","Create BufferedWriter");
            BufferedWriter bw = new BufferedWriter(fw);
            for (Shape shape : data.wall.panel_set) {
                bw.write(shape.holdTypesToString());
                bw.newLine();
            }
            Log.e("Application","Closing BufferedWriter");
            bw.close();

            Log.e("Application","Exiting saveHoldTypes successfully...");
        } catch (IOException e) {
            Log.e("Application", e.toString());
            Log.e("Application","Exiting saveHoldTypes un-successfully...");
        }
    }

    public void updateAllRoutes() {
        /* Save all routes, will need to be done after file deletion. */
        try {
            String wall_name = data.current_wall;

            String base_path = this.getFilesDir() + File.separator + wall_name + File.separator;
            Log.e("Application","Writing file: " + base_path + data.ROUTES_FILE);
            FileWriter fw_routes = new FileWriter(base_path + data.ROUTES_FILE, false);
            Log.e("Application","Create BufferedWriter");
            BufferedWriter bw_routes = new BufferedWriter(fw_routes);
            for (String line : data.routes.printRoutes()) {
                bw_routes.write(line);
                bw_routes.newLine();
            }
            Log.e("Application","Closing BufferedWriter");
            bw_routes.close();

            Log.e("Application","Writing file: " + base_path + data.ROUTE_INFO_FILE);
            FileWriter fw_info = new FileWriter(base_path + data.ROUTE_INFO_FILE, false);
            Log.e("Application","Create BufferedWriter");
            BufferedWriter bw_info = new BufferedWriter(fw_info);
            for (String line : data.routes.printRouteInfo()) {
                bw_info.write(line);
                bw_info.newLine();
            }
            Log.e("Application","Closing BufferedWriter");
            bw_info.close();

            Log.e("Application","Exiting updateAllRoutes successfully...");
        } catch (IOException e) {
            Log.e("Application", e.toString());
            Log.e("Application","Exiting updateAllRoutes un-successfully...");
        }
    }

    public void saveAddRoute() {
        /* Save final route to end of file. */
        try {
            String wall_name = data.current_wall;
            int size = data.routes.routes.size();
            String base_path = this.getFilesDir() + File.separator + wall_name + File.separator;

            Log.e("Application","Writing file: " + base_path + data.ROUTES_FILE);
            FileWriter fw_routes = new FileWriter(base_path + data.ROUTES_FILE, true);
            Log.e("Application","Create BufferedWriter");
            BufferedWriter bw_routes = new BufferedWriter(fw_routes);
            bw_routes.write(data.routes.routes.get(size - 1).toString());
            Log.e("Application","Closing BufferedWriter");
            bw_routes.newLine();
            bw_routes.close();

            Log.e("Application","Writing file: " + base_path + data.ROUTE_INFO_FILE);
            FileWriter fw_info = new FileWriter(base_path + data.ROUTE_INFO_FILE, true);
            Log.e("Application","Create BufferedWriter");
            BufferedWriter bw_info = new BufferedWriter(fw_info);
            bw_info.write(data.routes.routes.get(size - 1).toStringInfo());
            bw_info.newLine();
            Log.e("Application","Closing BufferedWriter");
            bw_info.close();

            Log.e("Application","Exiting saveAddRoute un-successfully...");
        } catch (IOException e) {
            Log.e("Application", e.toString());
            Log.e("Application","Exiting saveAddRoute un-successfully...");
        }
    }

    public void loadWall(String wall_name) {
        /* Load all the information about the current wall.

        For now will load all the information about every route.
        Will be 4 files:
        - panel corner information: corners.txt
        - hold type information: holds.txt
        - route name/rating information: route_info.txt
        - route hold status information: routes.txt
        */
        File cacheDir = this.getFilesDir();

        Log.e("Application","Loading Wall");
        loadCornersHolds(cacheDir + File.separator + wall_name);
        loadRoutes(cacheDir + File.separator + wall_name);
        Log.e("Application","Finished Loading Wall");
    }

    private void loadRoutes(String wall_name) {
        /* Load the routes from a wall */
        data.routes = new AllRoute();
        try {
            Log.e("Application","Loading Routes...");
            File info_file = new File(wall_name + File.separator + data.ROUTE_INFO_FILE);
            File route_file = new File(wall_name + File.separator + data.ROUTES_FILE);

            Log.e("Application","Creating scanner for: " + info_file);
            Scanner info_inputStream = new Scanner(info_file);
            Log.e("Application","Creating scanner for: " + route_file);
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
            info_inputStream.close();
            route_inputStream.close();

            Log.e("Application","Successfully finished loading Routes...");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Application", e.toString());
            Log.e("Application","Un-successfully exiting loading Routes...");
        }
    }

    private void loadCornersHolds(String wall_name) {
        /* Load the corners from a wall. */
        ArrayList<ArrayList<Float>> corners = new ArrayList<>();
        try {
            Log.e("Application","Loading corners...");
            File file = new File(wall_name + File.separator + data.PANEL_FILE);
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
            Log.e("Application","Successfully loaded corner holds...");
        } catch (Exception e) {
            Log.e("Application","Un-successfully loaded corner holds...");
            e.printStackTrace();
        }

        ArrayList<Integer> hold_types = new ArrayList<>();
        try {
            Log.e("Application","Loading hold types...");
            File file = new File(wall_name + File.separator + data.HOLD_TYPES_FILE);
            Scanner inputStream = new Scanner(file);
            while(inputStream.hasNext()){
                String line= inputStream.next();
                String[] values = line.split(",");
                for (String value : values) {
                    hold_types.add(Integer.parseInt(value));
                }
            }
            Log.e("Application","Successfully loaded hold_types...");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Application", e.toString());
            Log.e("Application","Un-successfully loaded hold_types...");
        }

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