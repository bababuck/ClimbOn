package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ChooseWallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_wall);

        UniversalData saved_data;
        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;

        LinearLayout main = new LinearLayout(this);
        main = findViewById(R.id.LL);

        // Params for adding EditTexts to the linear layout rows
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        ArrayList<String> wall_names = saved_data.wall_names;
        for (String wall_name : wall_names) {
            Button current_button = new Button(this);
            current_button.setBackgroundColor(Color.GREEN);
            current_button.setText(wall_name);
            current_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    saved_data.current_wall = wall_name;
                    Intent intent = new Intent(view.getContext(), MainMenu.class);
                    view.getContext().startActivity(intent);
                }
            });
            current_button.setTextColor(Color.BLACK);
            main.addView(current_button, params);
        }

        EditText input = new EditText(this);
        input.setHint("New Wall Name");
        main.addView(input, params);

        Button current_button = new Button(this);
        current_button.setBackgroundColor(Color.GREEN);
        current_button.setText("New Wall");
        current_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                saved_data.current_wall = input.getText().toString();
                Intent intent = new Intent(view.getContext(), MainMenu.class);
                view.getContext().startActivity(intent);
            }
        });
        current_button.setTextColor(Color.BLACK);
        main.addView(current_button, params);
    }
}