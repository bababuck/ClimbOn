package com.example.climbon;

import android.app.AlertDialog;
import android.app.Dialog;
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

public class SetHoldsOuter extends RouteView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Download data and then create the needed panels. */
        super.onCreate(savedInstanceState);
        Log.e("SetHoldsOuter","Entering SetHoldsOuter");

        ConstraintLayout main = findViewById(R.id.Main);
        saved_data.saved = true;

        Button save = new Button(this);
        save.setText("Save");
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        params.topToBottom = R.id.HoriScroll;
        main.addView(save, params);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to save route?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ClimbOnApplication app = (ClimbOnApplication) getApplication();
                        app.saveHoldTypes();
                        SetHoldsOuter.super.onBackPressed();
                        saved_data.saved = true;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        assert true;
                    }
                });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("SetHoldsOuter","Save button clicked.");

                if (!saved_data.saved)
                    builder.create().show();
            }
        });
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
                            SetHoldsOuter.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            assert true;
                        }
                    });
        } else {
            SetHoldsOuter.super.onBackPressed();
        }
    }
}
