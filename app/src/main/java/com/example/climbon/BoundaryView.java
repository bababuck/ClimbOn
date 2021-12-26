package com.example.climbon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class BoundaryView  extends View {

    public BoundaryView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    protected void onDraw(Canvas canvas, Boundary boundary) {
        super.onDraw(canvas);
        boundary.draw(canvas);

        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(0, 0, 0));
        myPaint.setStrokeWidth(10);
        canvas.drawCircle(500,500,500, myPaint);
    }
}
