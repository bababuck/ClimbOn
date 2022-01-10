package com.example.climbon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

public class BoundaryView extends View {
    /* Class to allow for adding a panel shape to layout

    Expects to receive the pre-generated boundary.
    */
    Boundary boundary;
    int PANEL_COLOR = Color.BLUE;
    public BoundaryView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public BoundaryView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public BoundaryView(Context context, Boundary boundary){
        super(context);
        this.boundary = boundary;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /* Draws the path to canvas.

        TODO: draw the edges in black over top the blue.
        - Do this by two separate boundaries
        - Would copy the first shape and then change paint
        - would save this copy so only generated once
        */
        super.onDraw(canvas);
        boundary.panel_color = PANEL_COLOR;
        boundary.draw(canvas);
    }
}
