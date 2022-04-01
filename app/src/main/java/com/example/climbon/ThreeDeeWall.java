package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ThreeDeeWall extends AppCompatActivity {

    private MyGLSurfaceView gLView;
    private ScaleGestureDetector mScaleGestureDetector;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScaleGestureDetector = new ScaleGestureDetector(this, new SpecialScaleGestureDetector());


        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        gLView = new MyGLSurfaceView(this);
        setContentView(gLView);
    }

    class MyGLSurfaceView extends GLSurfaceView {

        public final MyGLRenderer renderer;

        private final float ratio = 0.1f;
        private float previousX;
        private float previousY;

        public MyGLSurfaceView(Context context){
            super(context);

            // Create an OpenGL ES 2.0 context
            setEGLContextClientVersion(2);

            renderer = new MyGLRenderer();

            // Set the Renderer for drawing on the GLSurfaceView
            setRenderer(renderer);
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }

        private float mDownX;
        private float mDownY;
        private final float SCROLL_THRESHOLD = 100;
        private boolean isOnClick;

        float temp[] = new float[4];
        float result[] = new float[4];
        float invertView[] = new float[16];

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

                        renderer.rotate_view_loc(-dx*ratio, dy*ratio);
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
                        Log.e("ThreeDeeWall","Click");
                        float clickY = e.getY();
                        float clickX = e.getX();
                        float invertProjection[] = new float[16];
                        renderer.getInverseView(invertView);
                        invertProjection = renderer.invertProjection;
                        float click_loc[] = {(2.0f * clickX) / renderer.width - 1.0f, 1.0f - (2.0f * clickY) / renderer.height, -1.0f, 1.0f};

                        Matrix.multiplyMV(temp, 0, invertProjection, 0, click_loc, 0);
                        temp[2] = -1.0f;
                        temp[3] = 0.0f;
                        Matrix.multiplyMV(result, 0, invertView, 0, temp, 0);

                        ThreeDeeShape shape = renderer.findClickedShape(result);
                        if (shape != null) {
                            shape.setColor();
                            requestRender();
                        }
                    }
                    break;
                default:
                    break;
            }
            previousX = x;
            previousY = y;

            return true;
        }
    }

    private class SpecialScaleGestureDetector extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float zoom;
            zoom = scaleGestureDetector.getScaleFactor();
            gLView.renderer.zoom(zoom);
            Log.e("ThreeDeeWall","Scale");
            return true;
        }
    }
}