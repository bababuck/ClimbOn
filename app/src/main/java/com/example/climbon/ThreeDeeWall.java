package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

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

            renderer = new MyGLRenderer(context);

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

                        ThreeDeeHold hold = renderer.findClickedHold(result);
                        holdClicked(hold);
                        requestRender();
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

    public HashMap<Integer, float[]> selected_holds = new HashMap<>();
    public HashMap<Integer, float[]> deleted_holds = new HashMap<>();
    public ArrayList<Integer> previous_holds = new ArrayList<>();
    public int current_route_id = 69;
    public String route_name = "Generic Route";
    public int rating = 3;
    public int route_type = 2;

    private void holdClicked(ThreeDeeHold hold) {
        if (previous_holds.contains(hold.SQL_id)){
            if (deleted_holds.containsKey(hold.SQL_id)){
                hold.setColor(deleted_holds.get(hold.SQL_id));
                deleted_holds.remove(hold.SQL_id);
            } else {
                deleted_holds.put(hold.SQL_id, hold.color);
                hold.setColor(hold.base_color);
            }
        }
        if (selected_holds.containsKey(hold.SQL_id)) {
            hold.setColor(hold.base_color);
            selected_holds.remove(hold.SQL_id);
        } else {
            float color[] = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
            hold.setColor(color);
            selected_holds.put(hold.SQL_id, color);
        }
    }

    private void onSaveClicked() {
        WallInfoDbHelper dbHelper = new WallInfoDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int route_ID;

        ContentValues values = new ContentValues();
        values.put(WallInformationContract.RouteEntry.COLUMN_NAME_WALL_NAME, "Big Wall");
        values.put(WallInformationContract.RouteEntry.COLUMN_NAME_USER, "bababuck");
        values.put(WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_NAME, route_name);
        values.put(WallInformationContract.RouteEntry.COLUMN_NAME_RATING, rating);
        values.put(WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_TYPE, route_type);
        route_ID = (int) db.insert(WallInformationContract.RouteEntry.TABLE_NAME, null, values);
        values.clear();

        Iterator it = selected_holds.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            values.put(WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_HOLD_ID, (Integer) pair.getKey());
            values.put(WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_ROUTE_ID, route_ID);
            values.put(WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_COLOR, WallInfoDbHelper.coordinatesToSQLString((float[]) pair.getValue()));
            db.insert(WallInformationContract.HoldRouteJoinTable.TABLE_NAME, null, values);
            values.clear();
        }

        it = deleted_holds.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String selection = WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_HOLD_ID + " = ? AND " + WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_ROUTE_ID + " = ?";
            String[] selectionArgs = { Integer.toString((Integer) pair.getKey()), Integer.toString(route_ID) };
            db.delete(WallInformationContract.HoldRouteJoinTable.TABLE_NAME, selection, selectionArgs);
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