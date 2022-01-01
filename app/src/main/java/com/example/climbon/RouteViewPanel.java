package com.example.climbon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;

public class RouteViewPanel extends Drawable {
    /* Displays a panel with only the activated holds.

    To be used when viewing the entire route in route view.
    First draws the panel, then the holds.
    Draws holds smaller than when in panel view.

    Not sure if there is better way to do this since I had similar
    functionality in the panel view, but not sure if I could reuse here.
    */
    Boundary boundary;
    ArrayList<Drawable> holds = new ArrayList<>();
    ArrayList<Rect> bounds = new ArrayList<>();
    int HOLD_SIZE = 50;

    public RouteViewPanel(Context context, Boundary boundary, ArrayList<Coordinate> coords, ArrayList<Integer> holds, ArrayList<Boolean> hold_status, Translater translater){
        super();
        this.boundary = boundary;
        for (int i=0;i<holds.size();++i){
            if (hold_status.get(i)){
                this.holds.add(context.getResources().getDrawable(PanelViewHold.getImageId(holds.get(i))));
                int left = translater.translateX((int) coords.get(i).x);
                int top = translater.translateY((int) coords.get(i).y);
                bounds.add(new Rect(left-50,top-50,left + 50,top + 50));
            }
        }
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        /* Draws the boundary and holds to canvas.

        Everything needed is initialized in the constructor.
        */
        boundary.draw(canvas);
        for (int i=0; i<holds.size(); ++i){
            holds.get(i).setBounds(bounds.get(i));
            holds.get(i).draw(canvas);
        }
    }

    @Override
    public void setAlpha(int i) {
        boundary.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        boundary.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return boundary.getOpacity();
    }
}
