package com.example.climbon;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class SetHoldsOuter extends RouteView {
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
}
