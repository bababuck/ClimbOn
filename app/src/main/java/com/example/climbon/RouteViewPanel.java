package com.example.climbon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.Arrays;

public class RouteViewPanel extends androidx.appcompat.widget.AppCompatImageButton {
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
    public RouteViewPanel(Context context) {
        super(context);
    }

    public RouteViewPanel(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public RouteViewPanel(Context context, Boundary boundary, ArrayList<Coordinate> coords, ArrayList<Integer> holds, ArrayList<Boolean> hold_status){
        super(context);
        this.boundary = boundary;
        for (int i=0;i<holds.size();++i){
            if (hold_status.get(i)){
                this.holds.add(context.getResources().getDrawable(PanelViewHold.getImageId(holds.get(i))));
                bounds.add(new Rect(0,0,5,5));
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /* Draws the boundary and holds to canvas.

        Everything needed is initialized in the constructor.
        */
        super.onDraw(canvas);
        boundary.draw(canvas);
        for (int i=0; i<holds.size(); ++i){
            holds.get(i).setBounds(bounds.get(i));
            holds.get(i).draw(canvas);
        }
    }
}
