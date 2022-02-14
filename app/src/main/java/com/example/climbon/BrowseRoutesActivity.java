package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BrowseRoutesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_routes);

        UniversalData saved_data;
        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;
        // Create button for each route
        for (RouteData route : saved_data.route_data) {
            String name = route.name;
        }
    }



}