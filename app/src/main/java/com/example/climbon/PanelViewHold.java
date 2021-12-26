package com.example.climbon;

import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;

public class PanelViewHold extends androidx.appcompat.widget.AppCompatImageButton {
    /* Will represent a single hold in panel view.

    Will be able to be choose from a variety of hold types.

    Holds will change colors (and update the hold_on array)
    - can't turn on holds that are 'missing'
    */
    public enum HOLD_TYPE {POCKET, JUG, MINI_JUG, CRIMP, FOOTHOLD, SLOPER, PINCH, NO_HOLD}

    public HOLD_TYPE hold_type;
    public boolean on;

    public PanelViewHold(@NonNull Context context, HOLD_TYPE hold_type, boolean on) {
        super(context);
        this.hold_type = hold_type;
        this.on = on;
        this.setBackgroundColor(Color.TRANSPARENT);
        setHoldImage();
    }

    private void setHoldImage() {
        /* Sets the image to be used for the hold.

        Different image for on/vs off

        Is there a better way to do this?
        Like set the path for seperate folers for on/off
        */
        if (on) {
            switch (hold_type) {
                case POCKET:
                    this.setImageResource(R.drawable.);
                case JUG:
                    this.setImageResource(R.drawable.);
                case MINI_JUG:
                    this.setImageResource(R.drawable.);
                case CRIMP:
                    this.setImageResource(R.drawable.);
                case FOOTHOLD:
                    this.setImageResource(R.drawable.);
                case SLOPER:
                    this.setImageResource(R.drawable.);
                case PINCH:
                    this.setImageResource(R.drawable.);
                case NO_HOLD:
                    this.setImageResource(R.drawable.);
            }
        } else { // off

        }
    }
}
