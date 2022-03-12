package com.example.climbon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class RouteViewInert extends RouteView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Download data and then create the needed panels. */
        Log.e("RouteView","Entering RouteViewActivity");
        super.onCreate(savedInstanceState);

        routeEditButton();
    }

    public void saveData() {}

    private void routeEditButton() {
        Log.e("RouteViewInert","Changing content view to route view inert...");
        saved_data.saved = true;
//        setContentView(R.layout.activity_route_view_inert);
//        Button edit_button = findViewById(R.id.Edit);
//        edit_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("RouteViewInert","'Edit' button selected...");
//                Intent intent = new Intent(view.getContext(), SetRouteActivity.class);
//
//                Log.e("RouteViewInert","Entering SetRouteActivity");
//                view.getContext().startActivity(intent);
//            }
//        });
//
//        Log.e("RouteViewInert","Resetting content view");
//        setContentView(R.layout.activity_route_view);
    }

    @Override
    public void onRestart() {
        Log.e("RouteViewInert","Restarting activity...");
        super.onRestart();

        Log.e("RouteViewInert","Deleting buttons...");
        deleteButtons();
        Log.e("RouteViewInert","Creating buttons...");
        createButtons();
    }

    protected void setNavigation(ImageButton button, int i) {
        /* Set the holds on the selected activity.

        For this one, we actually want the buttons to not do anything, purely just for viewing.
        Maybe should make Images, not image buttons, but this hack for now works.

        TODO: Make images instead of ImageButtons
        */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("RouteViewInert","Inert button clicked...");
                assert true;
            }
        });
    }
}
