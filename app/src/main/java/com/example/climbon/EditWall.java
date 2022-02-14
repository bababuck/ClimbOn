package com.example.climbon;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class EditWall extends RouteView {

    protected void setNavigation(ImageButton button, int i) {
        /* Set the holds on the selected activity. */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saved_data.current_shape = i;
                Intent intent = new Intent(view.getContext(), CreateWallActivity.class);
                // TODO: Load create wall with data to start with
                view.getContext().startActivity(intent);
            }
        });
    }
}