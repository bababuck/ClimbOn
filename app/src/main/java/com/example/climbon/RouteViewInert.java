package com.example.climbon;

import android.view.View;
import android.widget.ImageButton;

public class RouteViewInert extends RouteView {

    protected void setNavigation(ImageButton button, int i) {
        /* Set the holds on the selected activity.

        For this one, we actually want the buttons to not do anything, purely just for viewing.
        Maybe should make Images, not image buttons, but this hack for now works.

        TODO: Make images instead of ImageButtons
        */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert true;
            }
        });
    }
}
