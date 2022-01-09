package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class CreateWallActivity extends AppCompatActivity {

    ArrayList<EditText> corner_inputs = new ArrayList<>();
    int bottom_id;
    LinearLayout bottom_buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wall);
        RelativeLayout relative_layout = findViewById(R.id.outer_layout);

        // Add Input field
        createCornerInput(true);


        // Add layout for bottom buttons
        bottom_buttons = new LinearLayout(this);
        bottom_buttons.setOrientation(LinearLayout.HORIZONTAL);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, bottom_id);
        relative_layout.addView(bottom_buttons, params);

        // Create Add Another and Done buttons
        {
            Button current_button = new Button(this);
            LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(0, 100, 1);
            current_button.setBackgroundColor(Color.BLUE);
            current_button.setText("Remove Corner");
            current_button.setTextColor(Color.BLACK);
            bottom_buttons.addView(current_button,button_params);
            current_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    removeCornerInput();
                }
            });
        }
        {
            Button current_button = new Button(this);
            LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(0, 100, 1);
            current_button.setBackgroundColor(Color.RED);
            current_button.setText("Add Corner");
            current_button.setTextColor(Color.BLACK);
            bottom_buttons.addView(current_button,button_params);
            current_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    createCornerInput(false);
                }
            });
        }
        {
            Button current_button = new Button(this);
            LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(0, 100, 1);
            current_button.setBackgroundColor(Color.GREEN);
            current_button.setText("Done");
            current_button.setTextColor(Color.BLACK);
            bottom_buttons.addView(current_button,button_params);
        }



    }

    private void removeCornerInput() {
        if (corner_inputs.size() > 0) {
            RelativeLayout relative_layout = findViewById(R.id.outer_layout);
            relative_layout.removeView(corner_inputs.get(corner_inputs.size()-1));
            corner_inputs.remove(corner_inputs.size()-1);
            bottom_id = corner_inputs.get(corner_inputs.size()-1).getId();

            RelativeLayout.LayoutParams button_params = (RelativeLayout.LayoutParams)bottom_buttons.getLayoutParams();
            button_params.removeRule(RelativeLayout.BELOW);
            button_params.addRule(RelativeLayout.BELOW, bottom_id);
            bottom_buttons.setLayoutParams(button_params);
        }
    }

    private void createCornerInput(boolean first) {
        if (corner_inputs.size() < 10) { // idk why more than 10
            EditText input = new EditText(this);
            RelativeLayout relative_layout = findViewById(R.id.outer_layout);
            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (first) {
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            } else {
                params.addRule(RelativeLayout.BELOW, bottom_id);
            }
            relative_layout.addView(input, params);
            input.setId(View.generateViewId());
            bottom_id = input.getId();
            if (!first) {
                RelativeLayout.LayoutParams button_params = (RelativeLayout.LayoutParams) bottom_buttons.getLayoutParams();
                button_params.removeRule(RelativeLayout.BELOW);
                button_params.addRule(RelativeLayout.BELOW, bottom_id);
                bottom_buttons.setLayoutParams(button_params);
            }
            input.setHint("Corner Location");
            corner_inputs.add(input);
        }
    }
}