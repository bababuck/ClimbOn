package com.example.climbon;

import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.climbon.databinding.ActivitySetHoldsBinding;

import java.util.ArrayList;

public class SetHoldsActivity extends PanelView {
    /* View a single panel, but when clicking on holds, allows changing of the holds */

    LinearLayout bottom_buttons;
    PanelViewHold currently_clicked;

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
        bottom_buttons.setVisibility(View.VISIBLE);
    }

    private void hideBottomButtons() {
        bottom_buttons.setVisibility(View.GONE);
    }


    private void createBottomButtons() {
        /* Will do here rather than in XML for maintainability.

        Allow for collection of
        */

        bottom_buttons = new LinearLayout(this);

        bottom_buttons.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150));
        bottom_buttons.setOrientation(LinearLayout.HORIZONTAL);

        AbsoluteLayout abs_layout = findViewById(R.id.PanelView);
        DisplayMetrics display_metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display_metrics);
        int screen_width = display_metrics.widthPixels;
        int screen_height = display_metrics.heightPixels;
        AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams( screen_width , BOTTOM_BUFFER,0,screen_height-BOTTOM_BUFFER);
        abs_layout.addView(bottom_buttons, params);

        int num_hold_types = HOLD_TYPE.values().length;
        for (int j=0;j<num_hold_types;++j) {
            ImageButton current_button = new ImageButton(this);
            current_button.setImageResource(PanelViewHold.getImageId(j));
            current_button.setAdjustViewBounds(true);
            LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams( 0 ,BOTTOM_BUFFER,1);
            current_button.setBackgroundColor(Color.TRANSPARENT);
            setBottomButtonListener(current_button, j);
            bottom_buttons.addView(current_button,button_params);
        }
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

    @Override
    protected void setOnCLickListener(PanelViewHold button) {
        /* Sets the OnClickListener for a button

        By pulling out, we can override this function in other classes.
        */
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                button.on = false;
                showBottomButtons();
                button.setHoldImage();
                currently_clicked = button;
                disableHolds();
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
                currently_clicked.hold_type = HOLD_TYPE.values()[i];
                currently_clicked.on = true;
                currently_clicked.setHoldImage();
                enableHolds();
            }
        });
    }

    public void disableHolds() {
        for (int i = 0; i < all_holds.size(); ++i){
            all_holds.get(i).setEnabled(false);
        }
    }

    public void enableHolds() {
        for (int i = 0; i < all_holds.size(); ++i){
            all_holds.get(i).setEnabled(true);
        }
    }


    // add save button, on sae click give warning about messing up routes and are you sure options
    // maybe option to clear effeted routes (eventually view them 1 by 1)
}