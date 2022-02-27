package com.example.climbon;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class SetHoldsOuter extends RouteView {
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


    @Override
    public void onRestart() {
        super.onRestart();

        LinearLayout layout = findViewById(R.id.RouteViewLL);
        layout.removeViewAt(saved_data.current_shape);
        createButton(saved_data.current_shape);
    }
}
