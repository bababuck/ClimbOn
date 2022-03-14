package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;

public class BrowseRoutesActivity extends AppCompatActivity {

    Boolean search_open = false;

    ArrayList<Button> route_buttons = new ArrayList<>();

    EditText rating;
    EditText name;
    Spinner type;

    UniversalData saved_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("BrowseRoutesActivity","Entering BrowseRoutesActivity...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_routes);

        LinearLayout scroll = findViewById(R.id.ScrollLL);
        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;

        createButtons(button_params, scroll, saved_data);
        setupSearchButtons();

        int index = 0;
        LinearLayout main = findViewById(R.id.main);
        rating = new EditText(this);
        rating.setHint("V-Rating");
        rating.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        main.addView(rating, index);
        ++index;
        name = new EditText(this);
        name.setHint("Route Name");
        main.addView(name, index);
        ++index;
        type = new Spinner(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                Arrays.asList("Route Type", "Jugs", "Crimps", "Slopers", "Pinches",  "Foot Stuff", "Mixed"));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        main.addView(type, index);
        ++index;
        hide_search_options();
    }

    private void createButtons(LinearLayout.LayoutParams button_params, LinearLayout scroll, UniversalData saved_data) {
        // Create button for each route
        Log.e("BrowseRoutesActivity","Creating buttons...");
        for (int i=0;i<saved_data.routes.routes.size();++i) {
            RouteData route = saved_data.routes.routes.get(i);
            String name = route.name;

            Button current_button = new Button(this);
            current_button.setBackgroundColor(Color.GREEN);
            current_button.setText(name);
            int finalI = i;
            current_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    saved_data.current_route = route;
                    saved_data.current_route_number = finalI;
                    Log.e("BrowseRoutesActivity","Route selected: " + route.name);
                    Intent intent = new Intent(view.getContext(), RouteViewInert.class);

                    view.getContext().startActivity(intent);
                }
            });
            current_button.setTextColor(Color.BLACK);

            Log.e("BrowseRoutesActivity","Adding button to linear layout...");
            scroll.addView(current_button, button_params);
            route_buttons.add(current_button);
        }
    }


    private void setupSearchButtons() {
        /* Route the buttons to correct activities. */
        Log.e("BrowseRoutesActivity","Routing bottom buttons...");
        Button search = findViewById(R.id.Search);

        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.e("BrowseRoutesActivity","Search button clicked");
                if (search_open) {
                    hide_search_options();
                    filter_routes();
                } else {
                    display_search_options();
                }
            }

            private void filter_routes() {
                String search_route_type = null;
                if (type.getSelectedItem().toString() != "Route Type")
                    search_route_type = type.getSelectedItem().toString();
                String search_name = null;
                if (!String.valueOf(name.getText()).isEmpty())
                    search_name = String.valueOf(name.getText());
                Integer search_rating = null;
                if (!String.valueOf(rating.getText()).isEmpty())
                    search_rating = Integer.parseInt(String.valueOf(rating.getText()));

                for (int i=0; i<saved_data.routes.routes.size();++i) {
                    if ((search_name == null ||
                            saved_data.routes.routes.get(i).name == search_name) &&
                            (search_route_type == null ||
                                    saved_data.routes.routes.get(i).type == search_route_type) &&
                            (search_rating == null ||
                                    saved_data.routes.routes.get(i).v_rating == search_rating)) {
                        route_buttons.get(i).setVisibility(View.VISIBLE);
                    } else {
                        route_buttons.get(i).setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void hide_search_options() {
        type.setVisibility(View.GONE);
        rating.setVisibility(View.GONE);
        name.setVisibility(View.GONE);
        search_open = false;
    }

    private void display_search_options() {
        type.setVisibility(View.VISIBLE);
        rating.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        search_open = true;
    }
}