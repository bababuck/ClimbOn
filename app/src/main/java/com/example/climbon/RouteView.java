package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

        for (shape in saved_data.shape){

        }
        // Load the current shape from the save data.
        current_shape = saved_data.shapes.get(saved_data.current_shape);
        current_hold_types = saved_data.hold_types.get(saved_data.current_shape);

        calculateBuffers();
        initializeDimensions();
        drawShape();
        createButtons();
    }
}