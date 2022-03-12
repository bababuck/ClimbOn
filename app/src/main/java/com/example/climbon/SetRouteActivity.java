package com.example.climbon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class SetRouteActivity extends RouteView {

    /* TODO: Make class that is parnet of both SetRoute and SetHolds
    Will put save button stuff in that class*/

    RouteData saved_route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Download data and then create the needed panels. */
        super.onCreate(savedInstanceState);
        Log.e("SetRouteActivity","Entering SetRouteActivity");
        createSaveButton();

        Log.e("SetRouteActivity","Copying route");
        saved_route = new RouteData(saved_data.current_route);
    }

    protected void setNavigation(ImageButton button, int i) {
        /* Set the holds on the selected activity. */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("SetRouteActivity","Button clicked: " + i);
                saved_data.current_shape = i;
                Intent intent = new Intent(view.getContext(), PanelView.class);
                Log.e("SetRouteActivity","Entering PanelView");
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void saveData() {
        Log.e("SetRouteActivity","Updating all routes...");
        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        if (saved_data.current_route_number >= saved_data.routes.routes.size()) {
            saved_data.routes.routes.add(saved_data.current_route);
        }
        app.updateAllRoutes();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        Log.e("SetRouteActivity","Restarting SetRouteActivity...");
        deleteButtons();
        createButtons();
    }

    @Override
    public void onBackPressed() {
        /* Save state when back button pressed. */
        Log.e("SetRouteActivity","Back button pressed...");

        if (!saved_data.saved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want exit? All unsaved changes will be lost")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (saved_data.current_route_number < saved_data.routes.routes.size()) {
                                saved_data.routes.routes.remove(saved_data.current_route_number);
                                saved_data.routes.routes.add(saved_data.current_route_number, saved_data.current_route);
                            }
                            SetRouteActivity.super.onBackPressed();
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