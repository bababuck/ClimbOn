package com.example.climbon;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class SetRouteActivity extends RouteView {

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
        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        Log.e("SetRouteActivity","Updating all routes...");
        if (saved_data.routes.routes.contains(saved_data.current_route)) {
            saved_data.routes.routes.add(saved_data.current_route);
        }
        app.updateAllRoutes();
        super.onBackPressed();
    }
}