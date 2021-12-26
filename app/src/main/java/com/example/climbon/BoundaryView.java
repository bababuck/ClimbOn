package com.example.climbon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class BoundaryView extends View {

    Boundary boundary;
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
        /* Draws the path to canvas in Blue.

        Might change to just draw the edges since then I could
        represent stacked panels as one. Better yet, I should
        draw the edges in black over top the blue.
        */
        super.onDraw(canvas);
        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(0, 0, 255));
        myPaint.setStrokeWidth(10);
        canvas.drawPath(boundary.path, myPaint);
    }
}
