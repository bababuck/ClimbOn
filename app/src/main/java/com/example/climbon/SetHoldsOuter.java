package com.example.climbon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class SetHoldsOuter extends RouteView {

    ArrayList<ArrayList<Integer>> saved_hold_types;
    /* For now, save upon exiting this activity.

    If changes discarded, have to revert back to wall upon entering.

    TODO: Move all saving to the sSetHoldsActivity
    This will allow us to not save everything which could cause issues
    if activity data is cleared before backing up theoretically.
    Also more intuitive I think.
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Download data and then create the needed panels. */
        super.onCreate(savedInstanceState);
        Log.e("SetHoldsOuter","Entering SetHoldsOuter");

        saved_hold_types = saved_data.wall.copyHolds();

        createSaveButton();
    }

    public void saveData() {
        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        app.saveHoldTypes();
        saved_hold_types = saved_data.wall.copyHolds();
    }

    protected void setNavigation(ImageButton button, int i) {
        /* Set the holds on the selected activity. */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("SetHoldsOuter","Button clicked: " + i);
                saved_data.current_shape = i;
                Intent intent = new Intent(view.getContext(), SetHoldsActivity.class);

                Log.e("SetHoldsOuter","Entering SetHoldsActivity");
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();

        Log.e("SetHoldsOuter","Restarting SetHoldsOuter...");
        LinearLayout layout = findViewById(R.id.RouteViewLL);
        Log.e("SetHoldsOuter","Removing view at: " + saved_data.current_shape);
        layout.removeViewAt(saved_data.current_shape);

        Log.e("SetHoldsOuter","Creating button at: " + saved_data.current_shape);
        createButton(saved_data.current_shape);
    }

    @Override
    public void onBackPressed() {
        if (!saved_data.saved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want exit? All unsaved changes will be lost")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            saved_data.wall.setHolds(saved_hold_types);
                            SetHoldsOuter.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            assert true;
                        }
                    });
            builder.create().show();
        } else {
            super.onBackPressed();
        }
    }
}
