package com.example.climbon;

import android.content.Context;
import android.graphics.Color;
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

    public PanelViewHold(@NonNull Context context, int hold_type, boolean on) {
        super(context);
        this.hold_type = HOLD_TYPE.values()[hold_type];
        this.on = on;
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setScaleType(ImageView.ScaleType.CENTER_CROP);
        setHoldImage();
    }


    public static int getImageId(int hold_type) {
        return getImageId(HOLD_TYPE.values()[hold_type], true);
    }

    public static int getImageId(HOLD_TYPE hold_type, boolean on){
        /* Helper function that returns the drawable ID for a hold/on status. */
        if (on) {
            switch (hold_type) {
                case POCKET:
                    return R.drawable.pocket_on;
                case JUG:
                    return R.drawable.jug_on;
                case MINI_JUG:
                    return R.drawable.mini_jug_on;
                case CRIMP:
                    return R.drawable.crimp_on;
                case FOOTHOLD:
                    return R.drawable.foothold_on;
                case SLOPER:
                    return R.drawable.sloper_on;
                case PINCH:
                    return R.drawable.pinch_on;
                case NO_HOLD:
                    return R.drawable.no_hold;
            }
        } else { // off
            switch (hold_type) {
                case POCKET:
                    return R.drawable.pocket_off;
                case JUG:
                    return R.drawable.jug_off;
                case MINI_JUG:
                    return R.drawable.mini_jug_off;
                case CRIMP:
                    return R.drawable.crimp_off;
                case FOOTHOLD:
                    return R.drawable.foothold_off;
                case SLOPER:
                    return R.drawable.sloper_off;
                case PINCH:
                    return R.drawable.pinch_off;
                case NO_HOLD:
                    return R.drawable.no_hold;
            }
        }
        return 0; // Compiler being stoopid and getting mad at me for no return... idc about multiple returns
    }

    public void setHoldImage() {
        /* Sets the image to be used for the hold.

        Different image for on/vs off

        Is there a better way to do this?
        Like set the path for separate folders for on/off
        then:
        if (on) {path = R.drawable.on}
        ...
        this.setImageResource(path.pocket)
        */
        this.setImageResource(getImageId(hold_type, on));
    }
}
