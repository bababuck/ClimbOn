package com.example.climbon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;

public class PreviewShapeDrawable extends View {

    Shape shape;
    Translater translater;
    Boundary boundary;

    public PreviewShapeDrawable(Context context, ArrayList<Float> corners, Coordinate start_point, int width, int height){
        super(context);
        try{
        shape = new Shape(corners, start_point);
        } catch (Exception e){

        }
        translater = new Translater(50, 50, 0, height, width, shape.get_height(), shape.get_width());
        boundary = new Boundary(shape.corners, translater);
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
        boundary.draw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        for (Coordinate hold : shape.hold_set) {
            canvas.drawCircle(translater.translateX(hold.x), translater.translateY(hold.y), 10, paint);
        }

    }
}
