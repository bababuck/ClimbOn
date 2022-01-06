package com.example.climbon;

import android.view.View;

public class SetHoldsPanelView extends PanelView {
    /* View a single panel, but when clicking on holds, allows changing of the holds */

    private void setOnCLickListener(PanelViewHold button) {
        /* Sets the OnClickListener for a button

        By pulling out, we can override this function in other classes.
        */
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (button.hold_type != PanelViewHold.HOLD_TYPE.NO_HOLD) {
                    button.on = !button.on;
                    button.setHoldImage();
                }
            }
        });
    }

    // produce all holds side by side on the bottom, along wiht x button, on click chagne hold type of prev selected
    // oN oirigonal click change red so know what one selected


    // add save button, on sae click give warning about messing up routes and are you sure options
    // maybe option to clear effeted routes (eventually view them 1 by 1)
}
