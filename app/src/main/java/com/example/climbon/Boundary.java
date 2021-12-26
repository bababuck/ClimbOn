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
    Just an outline of the shape should be printed.
    */
    private Path path = new Path();
    private Paint paint = new Paint();

    public Boundary(ArrayList<Coordinate> corners) {
        /* Initialize the boundary from a set of coordinates.

        TODO:
        - make color a operand
        */
        path.moveTo( corners.get(corners.size()-1).x,
                     corners.get(corners.size()-1).y);
        for (int i=0;i<corners.size();++i) {
            path.lineTo( corners.get(i).x * 100,
                         corners.get(i).y *100);
        }
        paint.setStrokeWidth(20);
        paint.setColor(Color.BLACK);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawPath(path, paint);
    }

    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        // No idea??
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return paint.getAlpha();
    }
}
