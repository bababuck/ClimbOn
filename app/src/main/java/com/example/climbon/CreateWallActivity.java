//package com.example.climbon;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.text.InputType;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class CreateWallActivity extends AppCompatActivity {
//    /* Create a new wall panel by panel.
//
//    TODO: Update pathing to/from this activity
//    TODO:Edit Panel option (start with numbers filled)
//    TODO: Add more descriptive text
//    TODO: Clean up code
//    TODO: Make button height param
//    */
//    ArrayList<EditText> corner_inputs = new ArrayList<>();
//    int num_buttons = 0;
//    /* Keep tracks of all the corner input boxes */
//
//    ArrayList<EditText> start_inputs = new ArrayList<>();
//    /* Input boxes for the start hold coordinate. */
//
//    boolean generated = false;
//    /* Check on if a boundary has already been generated.
//
//    Probably other ways to check this but easiest to store Bool.
//    */
//
//    LinearLayout scroll;
//    /* Main linear layout to which Views will be added. */
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        /* Initialize environment.
//
//        This includes:
//        - Selecting the xml
//        - Initializing the main linear layout
//        - Add 1 corner input field set
//        - Add start hold input field set
//        - Add buttons at bottom for add/remove/gen/confirm
//        */
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_wall);
//        scroll = findViewById(R.id.ScrollLL);
//
//        createCornerInput();
//        createStartHoldInput();
//        createBottomButtons();
//    }
//
//    private void createStartHoldInput() {
//        /* Add start_hold input row to the scroll view.
//
//        Add it below the corner holds.
//        */
//
//        // Linear Layout that represents the current row (need input for x and y)
//        LinearLayout current_row = new LinearLayout(this);
//        current_row.setOrientation(LinearLayout.HORIZONTAL);
//
//        // Params for adding EditTexts to the linear layout rows
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        // Input box creation
//        for (String dimension : new ArrayList<String>(){{add("X"); add("Y");}}){
//            EditText input = new EditText(this);
//            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            input.setHint(String.format("Hold Location: %s", dimension));
//            current_row.addView(input, params);
//            start_inputs.add(input);
//        }
//
//        // Add row to the scroll below existing corner inputs
//        scroll.addView(current_row, num_buttons);
//    }
//
//    private void createBottomButtons() {
//        /* Create the bottom buttons.
//
//        These include:
//        - Add/Remove/Preview buttons
//        - Confirm (exit) button
//        */
//
//        // The first row of buttons will all go together in a Linear Layout
//        LinearLayout bottom_buttons = new LinearLayout(this);
//        bottom_buttons.setOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
//        genRemoveButton(bottom_buttons);
//        genAddButton(bottom_buttons);
//        genGenPreview(bottom_buttons);
//        scroll.addView(bottom_buttons, params);
//
//        // The bottom confirm button will be added separately here
//        genConfirm();
//    }
//
//    private void genConfirm() {
//        /* Generate the button to confirm Edits/Creation.
//
//        TODO: Write the panel data out to file on confirm
//        TODO: Wipe routes if number of holds changed?
//        TODO: Is LL needed?
//        */
//        ClimbOnApplication app = (ClimbOnApplication) getApplication();
//        UniversalData saved_data = app.data;
//
//        LinearLayout button_layout = new LinearLayout(this);
//        button_layout.setOrientation(LinearLayout.HORIZONTAL);
//        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(0, 100, 1);
//
//        {
//            Button current_button = new Button(this);
//            current_button.setBackgroundColor(Color.GREEN);
//            current_button.setText("Confirm and Add Another");
//            current_button.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    Log.e("CreateWall","'Confirm and Add Another' button selected...");
//                    doConfirmStuff();
//                    saved_data.current_shape++;
//
//                    Intent intent = new Intent(view.getContext(), CreateWallActivity.class);
//                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    Log.e("CreateWall","Heading to CreateWall activity...");
//                    view.getContext().startActivity(intent);
//                }
//            });
//            current_button.setTextColor(Color.BLACK);
//            button_layout.addView(current_button, button_params);
//        }
//        {
//            Button current_button = new Button(this);
//            current_button.setBackgroundColor(Color.GREEN);
//            current_button.setText("Confirm and Exit");
//            current_button.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View view) {
//                    Log.e("CreateWall","'Confirm and Exit' button selected...");
//                    doConfirmStuff();
//
//                    app.saveWallPanels();
//                    app.saveHoldTypes();
//
//                    Intent intent = new Intent(view.getContext(), MainMenu.class);
//
//                    Log.e("CreateWall","Heading to Main Menu...");
//                    view.getContext().startActivity(intent);
//                }
//            });
//            current_button.setTextColor(Color.BLACK);
//            button_layout.addView(current_button, button_params);
//        }
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        scroll.addView(button_layout, params);
//    }
//
//    private void doConfirmStuff() {
//        /* Save the wall panel into the cache as a Shape and into memory in save format.
//
//        For now will save even if still adding another panel.
//        */
//        Log.e("CreateWall","Entering doConfirmStuff");
//        ClimbOnApplication app = (ClimbOnApplication) getApplication();
//        UniversalData saved_data = app.data;
//
//        try {
//            Log.e("CreateWall","Creating Shape...");
//            Shape shape = new Shape(getCorners(), getStartHoldLoc());
//            saved_data.wall.panel_set.add(saved_data.current_shape, shape);
//        } catch (Exception E) {
//            assert true;
//        }
//    }
//
//    private void genGenPreview(LinearLayout bottom_buttons) {
//        /* Create button to generate/update preview.
//
//        Preview will allow for viewing of panel and make adjustments.
//        Particularly useful for adjusting the hold location grid.
//        */
//        Button current_button = new Button(this);
//        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(0, 100, 1);
//        current_button.setBackgroundColor(Color.GREEN);
//        current_button.setText("Generate Preview");
//        current_button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Log.e("CreateWall","'GeneratePreview' button selected...");
//                generatePreview();
//            }
//        });
//        current_button.setTextColor(Color.BLACK);
//        bottom_buttons.addView(current_button, button_params);
//    }
//
//    private void generatePreview() {
//        /* Take the corner inputs and generate a preview.
//
//        TODO: Print message if less than 3 holds
//        TODO: Check to make sure inputs are valid
//        TODO: Change shape constructor to take list of Coordinates
//        TODO: Only use a much vertical space as required (no vertical white space on wide shapes)
//        */
//        Log.e("CreateWall","Generating preview...");
//        // If previous preview exists, remove it
//        if (generated) {
//            scroll.removeViewAt(num_buttons + 2);
//        }
//
//        // Need at least 3 corners for a valid panel.
//        if (num_buttons >= 3) {
//            generated = true;
//
//            ArrayList<Float> coordinates = getCorners();
//
//            // Get height/width of use-able area
//            DisplayMetrics displaymetrics = new DisplayMetrics();
//            this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//            int screen_width = displaymetrics.widthPixels;
//            int box_height = 1500;
//
//            Coordinate start_hold = getStartHoldLoc();
//
//            PreviewShapeDrawable shape = new PreviewShapeDrawable(this,
//                                                                  coordinates,
//                                                                  start_hold,
//                                                                  screen_width,
//                                                                  box_height);
//
//            LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(screen_width, box_height, 1);
//            scroll.addView(shape, num_buttons + 2, button_params);
//            Log.e("CreateWall","Preview successfully generated...");
//        }
//        else {
//            Log.e("CreateWall","Not enough corners for a valid panel");
//        }
//    }
//
//    private ArrayList<Float> getCorners() {
//        /* Get the corners from the inputs. */
//        ArrayList<Float> coordinates = new ArrayList<>();
//        for (int i=0; i < num_buttons * 2; ++i) {
//            EditText input = corner_inputs.get(i);
//            coordinates.add(Float.parseFloat(input.getText().toString()));
//        }
//        return coordinates;
//    }
//
//    private Coordinate getStartHoldLoc() {
//        /* Find the start hold location. */
//        return new Coordinate(Float.parseFloat(start_inputs.get(0).getText().toString()),
//                Float.parseFloat(start_inputs.get(1).getText().toString()));
//    }
//
//    private void genAddButton(LinearLayout bottom_buttons) {
//        /* Create button that allows for adding more corners. */
//        Button current_button = new Button(this);
//        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(0, 100, 1);
//        current_button.setBackgroundColor(Color.RED);
//        current_button.setText("Add Corner");
//        current_button.setTextColor(Color.BLACK);
//        bottom_buttons.addView(current_button,button_params);
//        current_button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                createCornerInput();
//            }
//        });
//    }
//
//    private void genRemoveButton(LinearLayout bottom_buttons) {
//        /* Create button that allows for removing corners. */
//        Button current_button = new Button(this);
//        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(0, 100, 1);
//        current_button.setBackgroundColor(Color.BLUE);
//        current_button.setText("Remove Corner");
//        current_button.setTextColor(Color.BLACK);
//        bottom_buttons.addView(current_button,button_params);
//        current_button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                removeCornerInput();
//            }
//        });
//    }
//
//    private void removeCornerInput() {
//        /* Remove an input from the scroll view.
//
//        Removes the bottom input.
//        TODO: Remove a blank input, otherwise bottom?
//        */
//        if (num_buttons > 0) {
//            corner_inputs.remove(corner_inputs.size()-1);
//            corner_inputs.remove(corner_inputs.size()-1);
//
//            scroll.removeViewAt(num_buttons - 1);
//            --num_buttons;
//        }
//    }
//
//    private void createCornerInput() {
//        /* Add new corner input row to the scroll view. */
//
//        // Linear Layout that represents the current row
//        LinearLayout current_row = new LinearLayout(this);
//        current_row.setOrientation(LinearLayout.HORIZONTAL);
//
//        // Params for adding EditTexts to the linear layout rows
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        // Input box creation
//        for (String dimension : new ArrayList<String>(){{add("X"); add("Y");}}){
//            EditText input = new EditText(this);
//            input.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            input.setHint(String.format("Hold Location: %s", dimension));
//            current_row.addView(input, params);
//            corner_inputs.add(input);
//        }
//
//        // Add row to the scroll at proper locations
//        scroll.addView(current_row, num_buttons);
//        ++num_buttons;
//    }
//}