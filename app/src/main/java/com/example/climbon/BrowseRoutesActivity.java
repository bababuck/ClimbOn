package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class BrowseRoutesActivity extends AppCompatActivity {

    Boolean search_open = false;

    EditText rating;
    EditText name;
    Spinner type;

    LinearLayout.LayoutParams button_params;
    LinearLayout scroll;

    UniversalData saved_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("BrowseRoutesActivity","Entering BrowseRoutesActivity...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_routes);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;

        scroll = findViewById(R.id.ScrollLL);
        button_params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        createButtons(null, null,null);
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

        ArrayAdapter<RouteTypesEnum> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                RouteTypesEnum.values());

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapter);
        main.addView(type, index);
        ++index;
        hide_search_options();
    }

    private void createButtons(String type, String rating, String name) {
        // Create button for each route
        Log.e("BrowseRoutesActivity","Creating buttons...");
        WallInfoDbHelper dbHelper = new WallInfoDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        ArrayList<String> projection_list = new ArrayList<>();
        String[] query_params = {type, rating, name};
        StringBuilder selection = new StringBuilder();
        boolean previous = false;
        for (int i=0;i<query_params.length;++i){
            if (query_params[i] != null) {
                projection_list.add(query_params[i]);
                if (!previous){
                    previous = true;
                } else {
                    selection.append(" AND ");
                }
                if (i == 0){
                    selection.append(WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_TYPE + " = ? ");
                }
                if (i ==1){
                    selection.append(WallInformationContract.RouteEntry.COLUMN_NAME_RATING + " = ? ");
                }
                if (i ==2){
                    selection.append(WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_NAME + " LIKE ? ");
                }
            }
        }
        String[] selectionArgs = projection_list.toArray(new String[0]);


        String[] projection = {
                WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_TYPE,
                WallInformationContract.RouteEntry.COLUMN_NAME_RATING,
                WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_NAME,
                WallInformationContract.RouteEntry._ID
        };

        Cursor cursor = db.query(
                WallInformationContract.RouteEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection.toString(),              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        int route_type_index = cursor.getColumnIndex(WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_TYPE);
        int route_rating_index = cursor.getColumnIndex(WallInformationContract.RouteEntry.COLUMN_NAME_RATING);
        int route_name_index = cursor.getColumnIndex(WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_NAME);
        int route_id_index = cursor.getColumnIndex(WallInformationContract.RouteEntry._ID);
        int i=0;
        while (cursor.moveToNext() && i < 15) {
            ++i;
            String route_name = cursor.getString(route_name_index);
            Integer route_rating = cursor.getInt(route_rating_index);
            Integer route_type = cursor.getInt(route_type_index);
            Integer route_id = cursor.getInt(route_id_index);
            Button current_button = new Button(this);
            current_button.setBackgroundColor(Color.GREEN);
            current_button.setText(route_name);
            current_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    saved_data.current_route = new RouteData(route_rating, route_type, route_name, route_id);
                    Log.e("BrowseRoutesActivity","Route selected: " + route_name);
                    Intent intent = new Intent(view.getContext(), ThreeDeeWall.class);

                    view.getContext().startActivity(intent);
                }
            });
            current_button.setTextColor(Color.BLACK);

            Log.e("BrowseRoutesActivity","Adding button to linear layout...");
            scroll.addView(current_button, button_params);
        }
        cursor.close();
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
                    deleteButtons();
                    filter_routes();
                } else {
                    display_search_options();
                }
            }

            private void filter_routes() {
                String search_route_type = null;
                if (type.getSelectedItem().toString().equals("Route Type"))
                    search_route_type = ((Integer) RouteTypesEnum.valueOf(type.getSelectedItem().toString()).ordinal()).toString();
                String search_name = null;
                if (!String.valueOf(name.getText()).isEmpty())
                    search_name = String.valueOf(name.getText());
                String search_rating = null;
                if (!String.valueOf(rating.getText()).isEmpty())
                    search_rating = String.valueOf(rating.getText());

                createButtons(search_route_type, search_rating, search_name);
            }

            private void deleteButtons() {
                scroll.removeAllViews();
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