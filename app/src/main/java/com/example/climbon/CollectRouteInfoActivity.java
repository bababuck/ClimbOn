package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class CollectRouteInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("CollectRouteInfo","Entering CollectRouteInfoActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_route_info);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        UniversalData saved_data = app.data;

        Button button = findViewById(R.id.create_route);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("CollectRouteInfo","Create Route button clicked...");
                EditText input = findViewById(R.id.v_rating);
                int v_rating = Integer.parseInt(String.valueOf(input.getText()));

                input = findViewById(R.id.routeName);
                String name = String.valueOf(input.getText());

                RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                String style = radioButton.getText().toString();

                ArrayList<Boolean> holds = saved_data.wall.getHolds();
                saved_data.current_route = new RouteData(v_rating,holds, style, name);
                saved_data.current_route_number = saved_data.routes.routes.size();
                Intent intent = new Intent(view.getContext(), SetRouteActivity.class);

                Log.e("CollectRouteInfo","Entering SetRouteActivity...");
                view.getContext().startActivity(intent);
            }
        });
    }
}