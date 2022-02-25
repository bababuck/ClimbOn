package com.example.climbon;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

public class SetHoldsOuter extends RouteView {
// SET CURRENT_ROUTE_TO_OFF (in RouteView, set attrs to null?)
    protected void setNavigation(ImageButton button, int i) {
        /* Set the holds on the selected activity. */
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saved_data.current_shape = i;
                Intent intent = new Intent(view.getContext(), SetHoldsActivity.class);

                view.getContext().startActivity(intent);
            }
        });
    }
}
