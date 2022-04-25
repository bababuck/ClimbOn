package com.example.climbon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class EditRouteInfoActivity extends CollectRouteInfoActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UniversalData saved_data = ((ClimbOnApplication) getApplication()).data;

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        mySpinner.setSelection(saved_data.current_route.getType());

        EditText input_rating = findViewById(R.id.v_rating);
        input_rating.setText(Integer.toString(saved_data.current_route.getVRating()));

        EditText input_name = findViewById(R.id.routeName);
        input_name.setText(saved_data.current_route.getName());

        Button button = findViewById(R.id.create_route);
        button.setText("Update Route Info");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("EditRouteInfo","Create Route button clicked...");
                EditText rating_input = findViewById(R.id.v_rating);
                EditText name_input = findViewById(R.id.routeName);
                if (saveButtonClicked(name_input, rating_input, mySpinner)) {
                    saved_data.saved = false;

                    Intent intent = new Intent(view.getContext(), RouteViewThreeDee.class);
                    Log.e("CollectRouteInfo","Entering SetRouteActivity...");
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

}