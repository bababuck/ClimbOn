package com.example.climbon;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

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
        this.setScaleType(ImageView.ScaleType.CENTER_CROP);
        PanelViewHold button = this;
        this.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                button.on = !button.on;
                button.setHoldImage();
            }
        });
        setHoldImage();
    }

    private void setHoldImage() {
        /* Sets the image to be used for the hold.

        Different image for on/vs off

        Is there a better way to do this?
        Like set the path for seperate folers for on/off
        then:
        if (on) {path = R.drawable.on}
        ...
        this.setImageResource(path.pocket)
        */
        if (on) {
            switch (hold_type) {
                case POCKET:
                    this.setImageResource(R.drawable.pocket_on);
                    break;
                case JUG:
                    this.setImageResource(R.drawable.jug_on);
                    break;
                case MINI_JUG:
                    this.setImageResource(R.drawable.mini_jug_on);
                    break;
                case CRIMP:
                    this.setImageResource(R.drawable.crimp_on);
                    break;
                case FOOTHOLD:
                    this.setImageResource(R.drawable.foothold_on);
                    break;
                case SLOPER:
                    this.setImageResource(R.drawable.sloper_on);
                    break;
                case PINCH:
                    this.setImageResource(R.drawable.pinch_on);
                    break;
                case NO_HOLD:
                    this.setImageResource(R.drawable.no_hold);
                    break;
            }
        } else { // off
            switch (hold_type) {
                case POCKET:
                    this.setImageResource(R.drawable.pocket_off);
                    break;
                case JUG:
                    this.setImageResource(R.drawable.jug_off);
                    break;
                case MINI_JUG:
                    this.setImageResource(R.drawable.mini_jug_off);
                    break;
                case CRIMP:
                    this.setImageResource(R.drawable.crimp_off);
                    break;
                case FOOTHOLD:
                    this.setImageResource(R.drawable.foothold_off);
                    break;
                case SLOPER:
                    this.setImageResource(R.drawable.sloper_off);
                    break;
                case PINCH:
                    this.setImageResource(R.drawable.pinch_off);
                    break;
                case NO_HOLD:
                    this.setImageResource(R.drawable.no_hold);
                    break;
            }
        }
    }
}
