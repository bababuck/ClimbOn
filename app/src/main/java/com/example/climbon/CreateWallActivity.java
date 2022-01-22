package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class CreateWallActivity extends AppCompatActivity {
    /* Create a new wall panel by panel.


    */
    ArrayList<LinearLayout> corner_rows = new ArrayList<>();
    ArrayList<EditText> corner_inputs = new ArrayList<>();
    int num_buttons = 0;
    LinearLayout scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wall);
        scroll = findViewById(R.id.ScrollLL);

        // Add Input field
        createCornerInput();

        // Create Add Another and Done buttons
        createBottomButtons();
    }

    private void createBottomButtons() {
        LinearLayout bottom_buttons = new LinearLayout(this);
        bottom_buttons.setOrientation(LinearLayout.HORIZONTAL);

        // Create buttons for handling number of corners
        genRemoveButton(bottom_buttons);
        genAddButton(bottom_buttons);
        genGenPreview(bottom_buttons);

        // Add lin layout to the scroll layout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        scroll.addView(bottom_buttons, params);

        genConfirm();
    }

    private void genConfirm() {
        LinearLayout bottom_buttons = new LinearLayout(this);
        bottom_buttons.setOrientation(LinearLayout.HORIZONTAL);

        Button current_button = new Button(this);
        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(0, 100, 1);
        current_button.setBackgroundColor(Color.GREEN);
        current_button.setText("Confirm Panel");
        current_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // Next
            }
        });
        current_button.setTextColor(Color.BLACK);
        bottom_buttons.addView(current_button, button_params);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        scroll.addView(bottom_buttons, params);
    }

    private void genGenPreview(LinearLayout bottom_buttons) {
        Button current_button = new Button(this);
        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(0, 100, 1);
        current_button.setBackgroundColor(Color.GREEN);
        current_button.setText("Generate Preview");
        current_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                generatePreview();
            }
        });
        current_button.setTextColor(Color.BLACK);
        bottom_buttons.addView(current_button, button_params);
    }

    private void generatePreview() {
        /* Take the corner inputs and generate a preview. */
        if (num_buttons >= 3) {
            ArrayList<Float> coordinates = new ArrayList<>();
            for (EditText input : corner_inputs) {
                coordinates.add(Float.parseFloat(input.getText().toString()));
            }

            DisplayMetrics displaymetrics = new DisplayMetrics();
            this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels;
            int height = 1500;

            PreviewShapeDrawable shape = new PreviewShapeDrawable(this, coordinates, new Coordinate(1,1), width, height);

            LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(width, 1500, 1);
            scroll.addView(shape, button_params);
        }
    }

    private void genAddButton(LinearLayout bottom_buttons) {
        Button current_button = new Button(this);
        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(0, 100, 1);
        current_button.setBackgroundColor(Color.RED);
        current_button.setText("Add Corner");
        current_button.setTextColor(Color.BLACK);
        bottom_buttons.addView(current_button,button_params);
        current_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                createCornerInput();
            }
        });
    }

    private void genRemoveButton(LinearLayout bottom_buttons) {

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

    private void removeCornerInput() {
        /* Remove an input from the scroll view.

        Removes the bottom input.
        */
        if (num_buttons > 0) {
            corner_rows.remove(corner_rows.size()-1);

            corner_inputs.remove(corner_inputs.size()-1);
            corner_inputs.remove(corner_inputs.size()-1);

            scroll.removeViewAt(num_buttons - 1);
            --num_buttons;
        }
    }

    private void createCornerInput() {
        /* Add new corner input row to the scroll view. */

        // Linear Layout that represents the current row
        LinearLayout current_row = new LinearLayout(this);
        current_row.setOrientation(LinearLayout.HORIZONTAL);
        corner_rows.add(current_row);

        // Params for adding EditTexts to the linear layout rows
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Input gathering for x-coordinate
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        input.setHint("Corner Location: X");
        current_row.addView(input, params);
        corner_inputs.add(input);

        // Input gathering for y-coordinate
        input = new EditText(this);
        input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        current_row.addView(input, params);
        input.setHint("Corner Location: Y");
        corner_inputs.add(input);

        // Add row to the scroll at proper locations
        scroll.addView(current_row, num_buttons);
        ++num_buttons;
    }

    /*
    TODO:
    Update preview and preview at bottom, then confirm

     */
}