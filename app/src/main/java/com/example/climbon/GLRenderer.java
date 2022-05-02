package com.example.climbon;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.BitSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class GLRenderer implements GLSurfaceView.Renderer {
    /* Handles renderer things.

    TODO: width, height seem misplaced in this class
    TODO: Provide option to set the default eye_loc and view_loc
    */
    private final ClimbOnApplication application;
    private BetterBitSet ledsOn;

    private final String LOG_TAG = "GLRenderer";

    private final ArrayList<ThreeDeeShape> shapes = new ArrayList<>();
    public float width, height;

    public volatile float[] eye_loc = {-4f, 0f, 0.0f, 0f};
    private final float[] view_loc = {4.0f, 0f, 0.0f,0f};

    public final float[] vPMatrix = new float[16];
    public final float[] projectionMatrix = new float[16];
    float[] invertProjection = new float[16];
    public final float[] viewMatrix = new float[16];

    public GLRenderer(Context context) {
        super();
        Log.e(LOG_TAG, "Creating renderer");
        application = ((ClimbOnApplication) ((Activity) context).getApplication());
        ledsOn = new BetterBitSet(application.data.total_bits);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Log.e(LOG_TAG, "Creating Surface");
        loadWall();
        loadHoldSettings();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadWall() {
        Log.e(LOG_TAG, "Accessing DB...");
        SQLiteDatabase db = application.db;

        String[] projection = {
                WallInformationContract.WallPanels.COLUMN_NAME_COORDINATES,
                WallInformationContract.WallPanels.COLUMN_NAME_COLOR,
                WallInformationContract.WallPanels.COLUMN_NAME_PANEL_NUMBER
        };
        String selection =  WallInformationContract.WallPanels.COLUMN_NAME_WALL_NAME + " = ?";
        String[] selectionArgs = { application.data.current_wall };
        Cursor cursor = db.query(
                WallInformationContract.WallPanels.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        while (cursor.moveToNext()) {
            getHolds(cursor);
        }
        cursor.close();
        Log.e(LOG_TAG, "Closing DB...");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getHolds(Cursor cursor) {
        int panel_number_index = cursor.getColumnIndex(WallInformationContract.WallPanels.COLUMN_NAME_PANEL_NUMBER);
        int color_index = cursor.getColumnIndex(WallInformationContract.WallPanels.COLUMN_NAME_COLOR);
        int coordinates_index = cursor.getColumnIndex(WallInformationContract.WallPanels.COLUMN_NAME_COORDINATES);
        Log.e(LOG_TAG, "Shape id:" + cursor.getInt(panel_number_index));

        ThreeDeeShape shape = new ThreeDeeShape(WallInfoDbHelper.convertStringToList(cursor.getString(coordinates_index)),
                WallInfoDbHelper.convertStringToList(cursor.getString(color_index)),
                cursor.getInt(panel_number_index));
        String[] inner_projection = {
                WallInformationContract.WallHolds.COLUMN_NAME_COORDINATES,
                WallInformationContract.WallHolds.COLUMN_NAME_HOLD_NUMBER,
                WallInformationContract.WallHolds.COLUMN_NAME_LED_ID
        };
        String inner_selection =  WallInformationContract.WallHolds.COLUMN_NAME_PANEL_NUMBER + " = ?";
        String[] inner_selectionArgs = { Integer.toString(cursor.getInt(panel_number_index)) };
        Cursor inner_cursor = application.db.query(
                WallInformationContract.WallHolds.TABLE_NAME,   // The table to query
                inner_projection,             // The array of columns to return (pass null to get all)
                inner_selection,              // The columns for the WHERE clause
                inner_selectionArgs ,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        int inner_coordinates_index = inner_cursor.getColumnIndex(WallInformationContract.WallHolds.COLUMN_NAME_COORDINATES);
        int inner_id_index = inner_cursor.getColumnIndex(WallInformationContract.WallHolds.COLUMN_NAME_HOLD_NUMBER);
        int inner_led_id_index = inner_cursor.getColumnIndex(WallInformationContract.WallHolds.COLUMN_NAME_LED_ID);
        while (inner_cursor.moveToNext()) {
            shape.addHold(WallInfoDbHelper.convertStringToList(inner_cursor.getString(inner_coordinates_index)), inner_cursor.getInt(inner_id_index), inner_cursor.getInt(inner_led_id_index));
        }
        inner_cursor.close();
        shapes.add(shape);
    }

    public void onDrawFrame(GL10 unused) {
        unused.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        Matrix.setLookAtM(viewMatrix, 0, eye_loc[0], eye_loc[1], eye_loc[2], eye_loc[0]+view_loc[0], eye_loc[1]+view_loc[1], eye_loc[2]+view_loc[2], 0f, 0f, 1.0f);
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        Log.e(LOG_TAG, "Drawing Shapes...");
        for (ThreeDeeShape shape: shapes ){
            shape.draw(vPMatrix);
        }
    }

    public void getInverseView(float[] return_matrix) {
        Matrix.invertM(return_matrix,0, viewMatrix, 0);
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        this.width = width;
        this.height = height;

        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
        Matrix.invertM(invertProjection,0, projectionMatrix, 0);
    }

    public static int loadShader(int type, String shaderCode){

        int shader = GLES20.glCreateShader(type);

        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    public void move_eye_loc(float x, float y, float z) {
        eye_loc[0] += x;
        eye_loc[1] += y;
        eye_loc[2] += z;
    }

    public void rotate_view_loc(float horizontal, float vertical) {
        float[] rotation_matrix = new float[16];
        float x_ratio = view_loc[1]/(view_loc[0] +view_loc[1]);
        float y_ratio = view_loc[0]/(view_loc[0] +view_loc[1]);
        Matrix.setRotateEulerM(rotation_matrix, 0, vertical*x_ratio, vertical*y_ratio, horizontal);
        Matrix.multiplyMV(view_loc, 0, rotation_matrix, 0, view_loc, 0);
    }

    public void zoom(float ratio) {
        view_loc[0] *= ratio;
        view_loc[1] *= ratio;
        view_loc[2] *= ratio;
    }

    public ThreeDeeShape findClickedShape(float[] click_vector) {
        for (ThreeDeeShape shape : shapes) {
            if (shape.clicked(click_vector, eye_loc) != null)
                return shape;
        }
        return null;
    }

    public ThreeDeeHold findClickedHold(float[] click_vector) {
        for (ThreeDeeShape shape : shapes) {
            float[] click_2D = shape.clicked(click_vector, eye_loc);
            if (click_2D != null) {
                ThreeDeeHold clicked_hold = shape.hold_hash.findClickedHold(click_2D[0], click_2D[1]);
                ledsOn.xor(clicked_hold.LED_id);
                return clicked_hold;
            }
        }
        return null;
    }


    private void loadHoldSettings() {
        int route_id = application.data.current_route.getID();
        Cursor cursor = queryHoldIDs(route_id);
        loadHoldIDs(cursor);
    }

    private void loadHoldIDs(Cursor cursor) {
        int color_index = cursor.getColumnIndex(WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_COLOR);
        int hold_id_index = cursor.getColumnIndex(WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_HOLD_ID);
        while (cursor.moveToNext()) {
            int hold_id = cursor.getInt(hold_id_index);
            float[] color = WallInfoDbHelper.convertStringToList(cursor.getString(color_index));
            loadHoldLed(hold_id, color);
        }
        cursor.close();
    }

    private Cursor queryHoldIDs(int route_id) {
        String[] projection = {
                WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_COLOR,
                WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_HOLD_ID
        };
        String selection =  WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_ROUTE_ID + " = ?";
        String[] selectionArgs = { Integer.toString(route_id) };
        Cursor cursor = application.db.query(
                WallInformationContract.HoldRouteJoinTable.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        return cursor;
    }

    private void loadHoldLed(int hold_id, float[] color) {
        Cursor cursor = queryHoldLed(hold_id);
        int panel_number_index = cursor.getColumnIndex(WallInformationContract.WallHolds.COLUMN_NAME_PANEL_NUMBER);
        int led_id_index = cursor.getColumnIndex(WallInformationContract.WallHolds.COLUMN_NAME_LED_ID);
        if (cursor.moveToNext()) {
            Log.e(LOG_TAG, "Setting hold color, panel_id="+cursor.getInt(panel_number_index));
            int panel_number = cursor.getInt(panel_number_index);
            turnOnHoldVirtually(panel_number, hold_id, color);

            int led_id = cursor.getInt(led_id_index);
            turnOnHoldPhysically(led_id);
        }
        cursor.close();
    }

    private Cursor queryHoldLed(int hold_id) {
        String[] inner_projection = {
                WallInformationContract.WallHolds.COLUMN_NAME_PANEL_NUMBER,
                WallInformationContract.WallHolds.COLUMN_NAME_LED_ID
        };
        String inner_selection =  WallInformationContract.WallHolds.COLUMN_NAME_HOLD_NUMBER + " = ?";
        String[] inner_selectionArgs = { Integer.toString(hold_id) };
        Cursor cursor = application.db.query(
                WallInformationContract.WallHolds.TABLE_NAME,   // The table to query
                inner_projection,             // The array of columns to return (pass null to get all)
                inner_selection,              // The columns for the WHERE clause
                inner_selectionArgs ,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        return cursor;
    }

    private void turnOnHoldPhysically(int led_id) {
        ledsOn.set(led_id);
    }

    private void turnOnHoldVirtually(int panel_number, int hold_id, float[] color) {
        setHoldColor(panel_number, hold_id, color);
    }

    public void setHoldColor(int panel_id, int hold_id, float[] color) {
        for (ThreeDeeShape shape : shapes) {
            if (shape.panel_id == panel_id) {
                for (ThreeDeeHold hold : shape.holds) {
                    if (hold_id == hold.SQL_id) {
                        hold.color = color;
                        hold.color = new float[]{1.0f, 1.0f, 1.0f, 1.0f};
                        return;
                    }
                }
            }
        }
        assert (false);
    }

    public byte[] getLedBits() {
        return ledsOn.toByteArray();
    }

    private class BetterBitSet extends BitSet {
        public BetterBitSet(int nbits){
            super(nbits);
        }

        public void xor(int bitIndex) {
            if (ledsOn.get(bitIndex)) ledsOn.set(bitIndex, false);
            else ledsOn.set(bitIndex, true);
        }
    }
}
