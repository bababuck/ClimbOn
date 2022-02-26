package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class RouteView extends AppCompatActivity {
    /* Allows the user to view the entire wall at once.

    Will allow the user to scroll sideways through the panels.
    Panels will be shown side by side with only holds turned on
    visible, reset of holds will be missing.

    If a panel is clicked on, it will take the viewer to the
    panel view screen and allow for editing of route.

    TODO: 3D model
    */

    UniversalData saved_data;
    float ratio;
    int number_of_shapes;
    int top_buffer, bottom_buffer;
    int screen_height= 1500;
    int total_width = 1500;
    int screen_width;
    // SET CURRENT_ROUTE_TO_OFF (in RouteView, set attrs to null?)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Download data and then create the needed panels. */
        Log.e("RouteView","Entering RouteViewActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_view);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;

        initializeDimensions();
        createButtons();
    }

    private void initializeDimensions() {
        /* Initialize certain final variables. */
        Log.e("RouteView","Initializing dimensions...");
        number_of_shapes = saved_data.wall.panel_set.size();
        screen_width = total_width / number_of_shapes;
    }

    private void createButtons(){
        /* Create buttons and add them to the layout.

        Each button will be an image of a panel.
        */
        Log.e("RouteView","Creating buttons...");
        getMinRatio();
        for (int i=0;i<number_of_shapes;++i){
            createButton(i);
        }
    }

    private void createButton(int i) {
        /* Create a single button (panel). */
        // Load the current shape from the save data.
        Log.e("RouteView","Creating button for panel number " + Integer.toString(i));
        Shape current_shape;
        current_shape = saved_data.wall.panel_set.get(i);

        // Setup the translater
        Translater translater = new Translater(top_buffer, 0, bottom_buffer, screen_height, screen_width, current_shape.get_height(), current_shape.get_width(), ratio);

        // Generate the drawable to set to the button
        ArrayList<Boolean> holds = null;
        if (saved_data.current_route != null) {
            holds = saved_data.current_route.holds;
        }
        RouteViewPanel panel = new RouteViewPanel(this, current_shape, holds, translater, saved_data.wall.findCumulativeHoldNumbers().get(i));

        LinearLayout layout = findViewById(R.id.RouteViewLL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screen_width, screen_height);
        ImageButton button = new ImageButton(this);
        button.setBackgroundColor(Color.BLACK);//TRANSPARENT
        button.setImageDrawable(panel);
        button.setCropToPadding(false);
        setNavigation(button, i);
        layout.addView(button, params);
    }

    /* Where to route to when the button is clicked. */
    abstract void setNavigation(ImageButton button, int i);

    private void getMinRatio() {
        /* Finds the smallest scaling ratio out of all the shapes.

        Not the most efficient since the constructor performs some
        extra operations in addition to calculating the ratio, but
        just simple arithmetic stuff, not performance critical.

        Don't want to add more almost duplicate functions than needed.
        */
        float min_ratio = Float.POSITIVE_INFINITY;

        for (int i=0;i<number_of_shapes;++i){
            min_ratio = Math.min(getRatio(i), min_ratio);
        }
        ratio = min_ratio;
    }

    private float getRatio(int i) {
        /* Gets ratio of a shape used for translation.

        See notes in getMinRatio();
        */
        Shape current_shape = saved_data.wall.panel_set.get(i);
        Translater translater = new Translater(top_buffer, 0, bottom_buffer, screen_height, screen_width, current_shape.get_height(), current_shape.get_width());
        return translater.getRatio();
    }
}