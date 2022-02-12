package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {
    /* A simple menu displayed on startup.

    Allows for user to select mode.
    TODO: Create cooler starting screen
    TODO: Choose from available walls on startup or new one
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Startup activities for the main menu.

        Sets up the pathing of the buttons in the activity.
        All buttons in XML for now, may switch to programmatically.

        Will download the data and save it to the app.
        */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        routeButtons();

    }

    private void routeButtons() {
        /* Defines the activity on each buttons clicking. */
        Button panel_view = findViewById(R.id.panel_view);
        panel_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PanelView.class);

                view.getContext().startActivity(intent);}
        });

        Button create_wall = findViewById(R.id.create_wall);
        create_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CreateWallActivity.class);

                view.getContext().startActivity(intent);}
        });

        Button route_view = findViewById(R.id.create_route);
        route_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RouteView.class);

                view.getContext().startActivity(intent);}
        });
    }
}