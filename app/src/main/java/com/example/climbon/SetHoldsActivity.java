package com.example.climbon;

import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class SetHoldsActivity extends PanelView {
    /* View a single panel, but when clicking on holds, allows changing of the holds.

    TODO: add save button, on save click give warning about messing up routes and are you sure options
    TODO: maybe option to clear affected routes (eventually view them 1 by 1)
    */

    LinearLayout bottom_buttons;
    PanelViewHold currently_clicked;
    int hold_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Performs actions to set up the screen.

        For now this is the only functionality in the class.
        */
        super.onCreate(savedInstanceState);
        createBottomButtons();
        hideBottomButtons();
    }

    private void showBottomButtons() {
        /* Make bottom buttons visible/clickable

        Will be called when a button is clicked.
        */
        bottom_buttons.setVisibility(View.VISIBLE);
    }

    private void hideBottomButtons() {
        bottom_buttons.setVisibility(View.GONE);
    }

    private void createBottomButtons() {
        /* Will do here rather than in XML for maintainability.

        Allow for collection of
        */

        // Buttons along the bottom will be in a linear layout
        createLinLayout();

        // Add a button for every holds type
        int num_hold_types = HoldType.values().length;
        for (int j=0;j<num_hold_types;++j) {
            addButton(j);
        }

        // Add button for canceling selection
        addCancelButton();
    }

    private void addCancelButton() {
        /* Add an "X" button for canceling a hold selection. */
        ImageButton current_button = new ImageButton(this);
        current_button.setImageResource(android.R.drawable.ic_delete);
        current_button.setAdjustViewBounds(true);
        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams( 0 ,BOTTOM_BUFFER,1);
        current_button.setBackgroundColor(Color.TRANSPARENT);
        current_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                hideBottomButtons();
                currently_clicked.on = true;
                currently_clicked.setHoldImage();
                enableHolds();
            }
        });
        bottom_buttons.addView(current_button,button_params);
    }

    private void addButton(int j) {
        /* Add a button for a hold type to the bottom buttons. */
        ImageButton current_button = new ImageButton(this);
        current_button.setImageResource(PanelViewHold.getImageId(j));
        current_button.setAdjustViewBounds(true);
        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams( 0 ,BOTTOM_BUFFER,1);
        current_button.setBackgroundColor(Color.TRANSPARENT);
        setBottomButtonListener(current_button, j);
        bottom_buttons.addView(current_button,button_params);
    }

    private void createLinLayout() {
        /* Creates a linear layout for the bottom buttons.

        These buttons will appear when a hold is selected.
        */
        bottom_buttons = new LinearLayout(this);
        bottom_buttons.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, BOTTOM_BUFFER));
        bottom_buttons.setOrientation(LinearLayout.HORIZONTAL);

        AbsoluteLayout abs_layout = findViewById(R.id.PanelView);
        AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams( screen_width , BOTTOM_BUFFER,0,screen_height-BOTTOM_BUFFER);
        abs_layout.addView(bottom_buttons, params);
    }

    @Override
    protected void setOnCLickListener(PanelViewHold button, int i) {
        /* Sets the OnClickListener for a hold/button

        By pulling out, we can override this function in other classes.
        */
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                button.on = false;
                showBottomButtons();
                button.setHoldImage();
                currently_clicked = button;
                disableHolds();
                hold_number = i;
            }
        });
    }

    protected void setBottomButtonListener(ImageButton button, int i) {
        /* Sets the OnClickListener for a button

        By pulling out, we can override this function in other classes.
        */
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                hideBottomButtons();
                currently_clicked.hold_type = HoldType.values()[i];
                currently_clicked.on = true;
                currently_clicked.setHoldImage();
                current_hold_types.set(hold_number,i);
                enableHolds();
            }
        });
    }

    public void disableHolds() {
        /* Make all the holds non-clickable.

        Will want this when hold is selected for changing.
        Don't want to selected two at once.
        */
        for (int i = 0; i < all_holds.size(); ++i){
            all_holds.get(i).setEnabled(false);
        }
    }

    public void enableHolds() {
        /* Make all the holds clickable.

        Will want this when hold is no longer selected.
        This will occur if a selection is cancelled or changed.
        */
        for (int i = 0; i < all_holds.size(); ++i){
            all_holds.get(i).setEnabled(true);
        }
    }
}