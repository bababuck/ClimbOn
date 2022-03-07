package com.example.climbon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;

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

    public RouteViewPanel(Context context, Shape shape, ArrayList<Boolean> hold_status, Translater translater, int hold_offset){
        /* Create a panel to be added to a button for RouteView.

        Takes translator since that can only be calculated from entire set of panels.
        */
        super();
        ArrayList<Coordinate> coords = shape.hold_set;
        ArrayList<Integer> holds = shape.hold_types;

        // Translate the background shape to the new dimensions
        ArrayList<Coordinate> new_corners = new ArrayList<>();
        for (int j=0;j<shape.corners.size();++j) {
            new_corners.add(new Coordinate(translater.translateX(shape.corners.get(j).x), translater.translateY(shape.corners.get(j).y)));
        }
        boundary = new Boundary(new_corners);

         // If a hold is active, translate it and create
        for (int i=0;i<holds.size();++i){
            if (hold_status != null && hold_status.get(i+hold_offset)){
                this.holds.add(context.getResources().getDrawable(PanelViewHold.getImageId(holds.get(i))));
                int left = translater.translateX(coords.get(i).x);
                int top = translater.translateY(coords.get(i).y);
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

    // Methods needed for Drawables
    @Override
    public void setAlpha(int i) {
        boundary.setAlpha(i);
        for (int j=0; j<holds.size(); ++j){
            holds.get(j).setAlpha(i);
        }
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        boundary.setColorFilter(colorFilter);
        for (int j=0; j<holds.size(); ++j){
            holds.get(j).setColorFilter(colorFilter);
        }
    }

    @Override
    public int getOpacity() {
        return boundary.getOpacity(); // Not from holds??
    }
}
