package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class CollectRouteInfoActivity extends AppCompatActivity {

    UniversalData saved_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("CollectRouteInfo","Entering CollectRouteInfoActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_route_info);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;


        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        mySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, RouteTypesEnum.values()));


        Button button = findViewById(R.id.create_route);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CollectRouteInfo","Create Route button clicked...");
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

    protected boolean saveButtonClicked(EditText name, EditText v_rating, Spinner type) {
        String name_value = String.valueOf(name.getText());
        int type_value = RouteTypesEnum.valueOf(type.getSelectedItem().toString()).ordinal();
        if (type_value != 0 && !String.valueOf(v_rating.getText()).equals("") && !name_value.equals("")) {
            int v_rating_value = Integer.parseInt(String.valueOf(v_rating.getText()));
            saved_data.current_route = new RouteData(v_rating_value,
                    type_value,
                    name_value,
                    -1);
            return true;
        } else {
            return false;
        }


    }
}