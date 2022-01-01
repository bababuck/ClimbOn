package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class RouteView extends AppCompatActivity {
    /* Allows the user to view the entire wall at once.

    Will allow the user to scroll sideways through the panels.
    Panels will be shown side by side with only holds turned on
    visible, reset of holds will be missing.

    If a panel is clicked on, it will take the viewer to the
    panel view screen and allow for editing of route.
    */

    UniversalData saved_data;
    // set image drawable
    // extend boundary class to class that also draws select holds.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_view);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;
        initializeSaveData(); // Needed for testing
        // calculateBuffers();
        // initializeDimensions();
        createButtons();
    }

    private void initializeSaveData() {
        /* Fake function since no universal data yet since testing. */
        saved_data.current_shape = 0;
        ArrayList<Float> corners = new ArrayList<>(Arrays.asList(0f,0f,10f,0f,10f,8f,8f,10f,0f,10f));
        try {
            saved_data.shapes.add(new Shape(corners, new Coordinate(0.5f,0.5f)));
            saved_data.hold_types.add(new ArrayList<>(Arrays.asList(1,2,3,5,7,0,1,0,3,2,4,1,4,2,6)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createButtons(){
        /* Create buttons and add them to the layout.

        Each button will be an image of a panel.
        */
        int number_of_shapes = saved_data.shapes.size();
        for (int i=0;i<number_of_shapes;++i){
            createButton(i);
        }
    }

    private void createButton(int i) {
        // Load the current shape from the save data.
        Shape current_shape = saved_data.shapes.get(saved_data.current_shape);
        ArrayList<Integer> current_hold_types = saved_data.hold_types.get(saved_data.current_shape);
        ArrayList<Coordinate> new_corners = new ArrayList<>();
        this.getResources().getDisplayMetrics();
        float dp_to_px = ((float) this.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        Translater translater = new Translater(100, 0, 10, 1500-50, 1000-75, (int) current_shape.get_height(), (int) current_shape.get_width());
        for (int j=0;j<current_shape.corners.size();++j) {
            new_corners.add(new Coordinate(translater.translateX((int) current_shape.corners.get(j).x), translater.translateY((int)current_shape.corners.get(j).y)));
        }
        Boundary boundary = new Boundary(new_corners); // need to scaled somewhere
        ArrayList<Boolean> hold_status = new ArrayList<>(Arrays.asList(true,false,true,false,true,false,false,false,false,false,false,true,false,true,false));
        RouteViewPanel panel = new RouteViewPanel(this, boundary, current_shape.hold_set, current_hold_types, hold_status, translater);
        LinearLayout layout = findViewById(R.id.RouteViewLL);
        Log.e("Help", "Coor:"+String.valueOf(dp_to_px));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(1000, 1500);
        ImageButton button = new ImageButton(this);
        button.setImageDrawable(panel);

        layout.addView(button, params);
        button = new ImageButton(this);
        button.setImageDrawable(panel);

        layout.addView(button, params);
    }
}//1000,1000);//