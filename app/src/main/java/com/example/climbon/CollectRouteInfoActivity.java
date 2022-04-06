package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.ArrayList;

public class CollectRouteInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("CollectRouteInfo","Entering CollectRouteInfoActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_route_info);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        UniversalData saved_data = app.data;


        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        mySpinner.setAdapter(new ArrayAdapter<RouteTypesEnum>(this, android.R.layout.simple_spinner_item, RouteTypesEnum.values()));


        Button button = findViewById(R.id.create_route);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CollectRouteInfo","Create Route button clicked...");
                EditText input = findViewById(R.id.v_rating);
                int v_rating = Integer.parseInt(String.valueOf(input.getText()));

                input = findViewById(R.id.routeName);
                String name = String.valueOf(input.getText());

                int type = RouteTypesEnum.valueOf(mySpinner.getSelectedItem().toString()).ordinal();
                saved_data.current_route = new RouteData(v_rating,type, name, -1);
                saved_data.saved = false;

                Intent intent = new Intent(view.getContext(), RouteViewThreeDee.class);
                Log.e("CollectRouteInfo","Entering SetRouteActivity...");
                view.getContext().startActivity(intent);
            }
        });
    }
}