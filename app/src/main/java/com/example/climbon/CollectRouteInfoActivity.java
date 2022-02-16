package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CollectRouteInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_route_info);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        UniversalData saved_data = app.data;

        EditText input = findViewById(R.id.v_rating);
        int v_rating = Integer.parseInt(String.valueOf(input.getText()));

        input = findViewById(R.id.v_rating);
        String name = String.valueOf(input.getText());

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = (RadioButton) findViewById(selectedId);
        String style = radioButton.getText().toString();


        Button button = findViewById(R.id.create_route);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saved_data.current_route = new RouteData(v_rating,null, style, name);
                Intent intent = new Intent(view.getContext(), SetRouteActivity.class);

                view.getContext().startActivity(intent);
            }
        });

        button = findViewById(R.id.abort);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), EditWall.class);
//
//                view.getContext().startActivity(intent);
            }
        });


    }


}