package com.example.climbon;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Boundary extends Drawable {
    /* Creates a shape to be drawn in PanelView.

    Will be initialized to fill the screen.

    Draws only straight lines from corner to corner.

    TODO: Add functionality for curved lines
    - Path has an interface for arcs
    - Currently can be done roughly by lots of corners lol
    */
    public Path path;
    protected Paint paint;
    public int panel_color = Color.RED;

    public Boundary(ArrayList<Coordinate> corners) {
        /* Initialize the boundary from a set of coordinates.

        */
        path = new Path();
        paint = new Paint();
        path.moveTo( corners.get(corners.size()-1).x,
                     corners.get(corners.size()-1).y);
        for (int i=0;i<corners.size();++i) {
            path.lineTo( corners.get(i).x,
                         corners.get(i).y);
        }
        paint.setColor(panel_color);
        paint.setStrokeWidth(10);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        /* Draw the path to the canvas.

        This produces the filled in shape.
        TBH, not entirely sure why its filled in, maybe will adjust to not be?
        */
        canvas.drawPath(path, paint);
    }

    /* Trivial overrides of abstract methods.

    All of them just interact with the paint color
    */
    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return paint.getAlpha();
    }
}
