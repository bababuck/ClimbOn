package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class BrowseRoutesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_routes);
        LinearLayout scroll = findViewById(R.id.ScrollLL);
        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        UniversalData saved_data;
        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;
        // Create button for each route
        for (RouteData route : saved_data.routes.routes) {
            String name = route.name;

            Button current_button = new Button(this);
            current_button.setBackgroundColor(Color.GREEN);
            current_button.setText(name);
            current_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    saved_data.current_route = route;
                    Intent intent = new Intent(view.getContext(), RouteViewInert.class);

                    view.getContext().startActivity(intent);
                }
            });
            current_button.setTextColor(Color.BLACK);
            scroll.addView(current_button, button_params);
        }

        setupBottomButtons();
    }

    private void setupBottomButtons() {
        /* Route the buttons to correct activities. */

        Button back = findViewById(R.id.BackButton);
        Button new_route = findViewById(R.id.NewRoute);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainMenu.class);
                view.getContext().startActivity(intent);
            }
        });

        new_route.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SetHoldsOuter.class);
                view.getContext().startActivity(intent);
            }
        });
    }
}