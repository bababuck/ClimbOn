package com.example.climbon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import java.util.ArrayList;

public class RouteViewPanel extends BoundaryView {
    /* Displays a panel with only the activated holds.

    To be used when viewing the entire route in route view.
    First draws the panel, then the holds.
    Draws holds smaller than when in panel view.

    Not sure if there is better way to do this since I had similar
    functionality in the panel view, but not sure if I could reuse here.
    */
    Boundary boundary;
    public RouteViewPanel(Context context) {
        super(context);
    }

    public RouteViewPanel(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public RouteViewPanel(Context context, Boundary boundary, ArrayList<Integer> holds, ArrayList<Boolean> hold_status){
        super(context, boundary);
        holds[i] = context.getResources().getDrawable(R.drawable.my_image);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /* Draws the shape and holds to canvas.

        */
        super.onDraw(canvas);

        for (int i=0; i<holds.size(); ++i){
            if (hold_status[i]){
                holds[i].setBounds(imageBounds);
                holds[i].draw(canvas);
            }
        }
    }
