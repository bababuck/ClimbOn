package com.example.climbon;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;


class MyGLSurfaceView extends GLSurfaceView {

    private final ScaleGestureDetector mScaleGestureDetector;

    public final MyGLRenderer renderer;

    public MyGLSurfaceView(Context context){
        super(context);
        setEGLContextClientVersion(2);

        renderer = new MyGLRenderer(context);
        mScaleGestureDetector = new ScaleGestureDetector(context, new SpecialScaleGestureDetector());

        setRenderer(renderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private float mDownX;
    private float mDownY;
    private final float SCROLL_THRESHOLD = 100;
    private boolean isOnClick;

    private final float ROTATION_RATIO = 0.1f;
    private float previousX;
    private float previousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        mScaleGestureDetector.onTouchEvent(e);

        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (!isOnClick || ((Math.abs(mDownX - e.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - e.getY()) > SCROLL_THRESHOLD) && ((e.getEventTime() - e.getDownTime()) > 1000))) {

                    float dx = x - previousX;
                    float dy = y - previousY;

                    renderer.rotate_view_loc(-dx* ROTATION_RATIO, dy* ROTATION_RATIO);
                    requestRender();
                    Log.e("ThreeDeeWall","Rotate Wall");
                    isOnClick = false;
                }
                break;
            case MotionEvent.ACTION_DOWN:
                mDownX = e.getX();
                mDownY = e.getY();
                isOnClick = true;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isOnClick) {
                    isOnClick = false;
                    onClick(e);
                }
                break;
            default:
                break;
        }
        previousX = x;
        previousY = y;

        return true;
    }

    protected void onClick(MotionEvent e) {
    }

    private class SpecialScaleGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float zoom;
            zoom = scaleGestureDetector.getScaleFactor();
            renderer.zoom(zoom);
            Log.e("ThreeDeeWall","Scale");
            return true;
        }
    }
}
