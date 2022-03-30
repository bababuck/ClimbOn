package com.example.climbon;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private ThreeDeeShape mTriangle,mTriangle2, mSquare, ground;

    private ArrayList<ThreeDeeShape> shapes = new ArrayList<>();

    private float vertAngle, horiAngle;

    public float width, height;

    public volatile float[] eye_loc = {-4f, 0f, 1.0f, 0f};
    private volatile float[] view_loc = {4.0f, 0f, -1.0f,0f};

    private final float[] vPMatrix = new float[16];
    public final float[] projectionMatrix = new float[16];
    public final float[] viewMatrix = new float[16];

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        float coords [] = {   // in counterclockwise order:
                0.0f, 0.0f, 0.0f,
                0.0f,  1.0f, 0.0f,
                0.0f, 0.0f, 1.0f
        };

        float color [] = {0.0f, 1.0f, 1.0f, 1.0f};
        shapes.add(new ThreeDeeShape(coords,color));

        float coords2 [] = {   // in counterclockwise order:
                0.0f, 0.0f, 1.0f, // top
                0.0f,  -1.0f, 0.0f,  // bottom right
                0.0f,  0.0f, 0.0f // bottom left
        };
        float color2 [] = {0.0f, 0.0f, 1.0f, 1.0f};
        shapes.add(new ThreeDeeShape(coords2, color2));

        float coords3 [] = {   // in counterclockwise order:
                0.0f, 1.0f, 0.0f,  // top right
                0.0f, -1.0f, 0.0f, // top left
                0.0f, -1.0f, -1.0f,  // top right
                0.0f, 1.0f, -1.0f, // top left

        };
        float color3 [] = {1.0f, 1.0f, 0.2f, 1.0f};
        shapes.add(new ThreeDeeShape(coords3, color3));

        float groundCoors [] = {   // in counterclockwise order:
                10.0f, 10.0f, -10.0f,  // top right
                10.0f, -10.0f, -10.0f, // top left
                -10.0f, -10.0f, -10.0f,  // top right
                -10.0f, 10.0f, -10.0f, // top left

        };
        float groundColor [] = {0.2f, 1.0f, 0.2f, 1.0f};
        shapes.add(new ThreeDeeShape(groundCoors, groundColor));

        float coords4 [] = {   // in counterclockwise order:
                0.0f, 1.0f, -1.0f,
                0.0f, -1.0f, -1.0f,
                3.0f, -1.0f, -4.0f,
                3.0f, 1.0f, -4.0f,

        };
        float color4 [] = {1.0f, 1.0f, 0.2f, 1.0f};
        shapes.add(new ThreeDeeShape(coords4, color4));
    }

    public void addShape(float coordinates[], float base_color[]){
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

        // Redraw background color
        for (ThreeDeeShape shape: shapes ){
            shape.draw(vPMatrix);
        }
    }

    public void getInverseProjection(float return_matrix[]) {
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, eye_loc[0], eye_loc[1], eye_loc[2], eye_loc[0]+view_loc[0], eye_loc[1]+view_loc[1], eye_loc[2]+view_loc[2], 0f, 0f, 1.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        Matrix.invertM(return_matrix,0, vPMatrix, 0);

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
        float rotation_matrix[] = new float[16];
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

    public ThreeDeeShape findClickedShape(float click_vector[]) {
        for (ThreeDeeShape shape : shapes) {
            if (shape.clicked(click_vector, eye_loc))
                return shape;
        }
        return null;
    }
}
