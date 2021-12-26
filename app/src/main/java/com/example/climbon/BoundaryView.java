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

    Path path;
    public BoundaryView(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public BoundaryView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
    }

    public BoundaryView(Context context, Path path){
        super(context);
        this.path = path;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint myPaint = new Paint();
        myPaint.setColor(Color.rgb(0, 0, 255));
        myPaint.setStrokeWidth(10);
        canvas.drawPath(path, myPaint);
    }
}
