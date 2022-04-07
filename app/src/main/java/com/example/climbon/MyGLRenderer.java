package com.example.climbon;

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

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    public MyGLRenderer(Context context) {
        super();
        this.context = context;
    }

    private final ArrayList<ThreeDeeShape> shapes = new ArrayList<>();

    public float width, height;

    public volatile float[] eye_loc = {-4f, 0f, 0.0f, 0f};
    private final float[] view_loc = {4.0f, 0f, 0.0f,0f};

    public final float[] vPMatrix = new float[16];
    public final float[] projectionMatrix = new float[16];
    float[] invertProjection = new float[16];
    public final float[] viewMatrix = new float[16];

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        Log.e("MyGLRenderer", "Creating Surface");

        Log.e("MyGLRenderer", "Accessing DB...");
        WallInfoDbHelper dbHelper = new WallInfoDbHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {
                WallInformationContract.WallPanels.COLUMN_NAME_COORDINATES,
                WallInformationContract.WallPanels.COLUMN_NAME_COLOR,
                WallInformationContract.WallPanels.COLUMN_NAME_PANEL_NUMBER
        };
        Cursor cursor = db.query(
                WallInformationContract.WallPanels.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        int panel_number_index = cursor.getColumnIndex(WallInformationContract.WallPanels.COLUMN_NAME_PANEL_NUMBER);
        int color_index = cursor.getColumnIndex(WallInformationContract.WallPanels.COLUMN_NAME_COLOR);
        int coordinates_index = cursor.getColumnIndex(WallInformationContract.WallPanels.COLUMN_NAME_COORDINATES);
        ThreeDeeShape shape;
        while (cursor.moveToNext()) {
            shape = new ThreeDeeShape(WallInfoDbHelper.convertStringToList(cursor.getString(coordinates_index)),
                    WallInfoDbHelper.convertStringToList(cursor.getString(color_index)));
            String[] inner_projection = {
                    WallInformationContract.WallHolds.COLUMN_NAME_COORDINATES,
                    WallInformationContract.WallHolds.COLUMN_NAME_HOLD_NUMBER
            };
            // Filter results WHERE "title" = 'My Title'
            String selection =  WallInformationContract.WallHolds.COLUMN_NAME_PANEL_NUMBER + " = ?";
            String[] selectionArgs = { Integer.toString(cursor.getInt(panel_number_index)) };
            Cursor inner_cursor = db.query(
                    WallInformationContract.WallHolds.TABLE_NAME,   // The table to query
                    inner_projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs ,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );
            int inner_coordinates_index = inner_cursor.getColumnIndex(WallInformationContract.WallHolds.COLUMN_NAME_COORDINATES);
            int inner_id_index = inner_cursor.getColumnIndex(WallInformationContract.WallHolds.COLUMN_NAME_HOLD_NUMBER);
            while (inner_cursor.moveToNext()) {
                shape.addHold(WallInfoDbHelper.convertStringToList(inner_cursor.getString(inner_coordinates_index)), inner_cursor.getInt(inner_id_index));
            }
            inner_cursor.close();
            shapes.add(shape);
        }
        cursor.close();
        Log.e("MyGLRenderer", "Closing DB...");
    }

    public void addShape(float[] coordinates, float[] base_color){
        assert coordinates.length == 12 || coordinates.length == 9;
        assert base_color.length == 4;
        shapes.add(new ThreeDeeShape(coordinates, base_color));
    }

    public void onDrawFrame(GL10 unused) {
        unused.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, eye_loc[0], eye_loc[1], eye_loc[2], eye_loc[0]+view_loc[0], eye_loc[1]+view_loc[1], eye_loc[2]+view_loc[2], 0f, 0f, 1.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        Log.e("MyGLRenderer", "Drawing Shapes...");
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

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
        Matrix.invertM(invertProjection,0, projectionMatrix, 0);
    }

    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
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
            if (click_2D != null)
                return shape.hold_hash.findClickedHold(click_2D[0], click_2D[1]);
        }
        return null;
    }


}
