package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    /* A simple menu displayed on startup.

    Allows for user to select mode.
    TODO: Create cooler starting screen

    For entire project
    TODO: SQL
    TODO: Bluetooth
    TODO: 3D
    TODO: Holds as paths
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Startup activities for the main menu.

        Sets up the pathing of the buttons in the activity.
        All buttons in XML for now, may switch to programmatically.

        Will download the data and save it to the app.
        */
        Log.e("Main Menu","Entering MainMenu activity...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        routeButtons();

    }

    private void routeButtons() {
        /* Defines the activity on each buttons clicking. */
        UniversalData saved_data;
        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;

        Button browse_routes = findViewById(R.id.browse_routes);
        browse_routes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Main Menu","'BrowseRoutes' button selected...");
                Intent intent = new Intent(view.getContext(), BrowseRoutesActivity.class);

                Log.e("Main Menu","Entering BrowseRoutes activity...");
                view.getContext().startActivity(intent);
            }
        });

        Button create_route = findViewById(R.id.create_route);
        create_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Main Menu","'CreateRoute' button selected...");
                Intent intent = new Intent(view.getContext(), CollectRouteInfoActivity.class);
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                
                Log.e("Main Menu","Entering CollectRouteInfoActivity activity...");
                view.getContext().startActivity(intent);
            }
        });

        Button edit_wall = findViewById(R.id.edit_wall);
        edit_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Main Menu","'EditWall' button selected...");
//                Intent intent = new Intent(view.getContext(), ThreeDeeWall.class);
//
//                Log.e("Main Menu","Entering EditWall activity...");
//                view.getContext().startActivity(intent);
            }
        });

        Button edit_holds = findViewById(R.id.edit_holds);
        edit_holds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Main Menu","'EditHolds' button selected...");
//                saved_data.saved = true;
//
//                Intent intent = new Intent(view.getContext(), SetHoldsOuter.class);
//
//                Log.e("Main Menu","Entering SetHoldsOuter activity...");
//                view.getContext().startActivity(intent);
            }
        });
    }
}