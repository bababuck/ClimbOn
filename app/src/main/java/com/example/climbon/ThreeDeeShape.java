package com.example.climbon;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class ThreeDeeShape {
    protected FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private ShortBuffer lineListBuffer;
    protected final int mProgram;
    protected int positionHandle;
    protected int colorHandle;

    private int vPMatrixHandle;

    private final int vertexCount;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    static final int COORDS_PER_VERTEX = 3;
    float coordinates[];

    float baryocentric[];

    float color[];

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };
    private short square_line_order[] = {0, 1, 1, 2, 2, 3, 3, 1};
    private short triangle_line_order[] = {0, 1, 1, 2, 2, 1};
    private float normal[];

    float line_color[] = {0.0f, 0.0f, 0.0f, 1.0f};

    boolean square;

    public ThreeDeeShape(float coordinates[], float color[]) {
        mProgram = ProgramSingleton.getProgram();

        this.coordinates = coordinates;
        this.color = color;
        vertexCount = coordinates.length / COORDS_PER_VERTEX;
        setNormal(3, 6, 0);

        ByteBuffer bb = ByteBuffer.allocateDirect(coordinates.length * 4);
        bb.order(ByteOrder.nativeOrder());

        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(coordinates);
        vertexBuffer.position(0);

        square = (coordinates.length == 12);

        adjustColor();

        ByteBuffer llb = ByteBuffer.allocateDirect(vertexCount * 4);
        llb.order(ByteOrder.nativeOrder());
        lineListBuffer = llb.asShortBuffer();

        if (square) {
            ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
            dlb.order(ByteOrder.nativeOrder());
            drawListBuffer = dlb.asShortBuffer();
            drawListBuffer.put(drawOrder);
            drawListBuffer.position(0);

            lineListBuffer.put(square_line_order);
        } else {
            lineListBuffer.put(triangle_line_order);
        }
        lineListBuffer.position(0);

        findBaryocentric();
    }

    private void adjustColor() {
        float angle = angle(3, 6, 0);
        if (square) {
            float angle2 = angle(3, 9, 0);
            assert angle2-0.001 < angle && angle < angle2+0.001;
        }
        for (int i=0;i<3;i++) {
            color[i] = color[i] * (angle + 1f)/2f;
        }

    }

    public void draw(float[] mvpMatrix) {
        GLES20.glUseProgram(mProgram);
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        if (square) {
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length,GLES20.GL_UNSIGNED_SHORT, drawListBuffer);
        } else {
            GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        }
        GLES20.glDisableVertexAttribArray(positionHandle);

        draw_lines(mvpMatrix);
    }

    private void draw_lines(float[] mvpMatrix) {
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, lineListBuffer);

        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, line_color, 0);

        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawElements(GLES20.GL_LINES, vertexCount*2, GLES20.GL_UNSIGNED_SHORT, lineListBuffer);
    }

    public float angle(int start_loc_1, int start_loc_2, int start_loc_3) {
        float x = normal[0];
        float y = normal[1];
        float z = normal[2];

        return (float) (Math.acos(z/(Math.sqrt(x*x+y*y+z*z)))/(Math.PI));
    }

    public float setNormal(int start_loc_1, int start_loc_2, int start_loc_3) {
        normal[0] = (coordinates[start_loc_1+1]-coordinates[start_loc_3+1]) *
                (coordinates[start_loc_2+2]-coordinates[start_loc_3+2]) -
                (coordinates[start_loc_2+1]-coordinates[start_loc_3+1]) *
                        (coordinates[start_loc_1+2]-coordinates[start_loc_3+2]);
        normal[1] = (coordinates[start_loc_1+2]-coordinates[start_loc_3+2]) *
                (coordinates[start_loc_2]-coordinates[start_loc_3]) -
                (coordinates[start_loc_2+2]-coordinates[start_loc_3+2]) *
                        (coordinates[start_loc_1]-coordinates[start_loc_3]);
        normal[2] = (coordinates[start_loc_1]-coordinates[start_loc_3]) *
                (coordinates[start_loc_2+1]-coordinates[start_loc_3+1]) -
                (coordinates[start_loc_2]-coordinates[start_loc_3]) *
                        (coordinates[start_loc_1+1]-coordinates[start_loc_3+1]);
    }

    public boolean clicked(float[] click_vector, float[] eye_loc) {
        float denominator = click_vector[0] * normal[0] + click_vector[1] * normal[1] + click_vector[2] * normal[2];
        if (denominator <= 0.0f) return false;

        float d = ((click_vector[0] - coordinates[0]) * normal[0] + (click_vector[1] - coordinates[1]) * normal[1] + (click_vector[2] - coordinates[2]) * normal[2])/denominator;

        float intersection[] = new float[3];
        intersection[0] = eye_loc[0] + click_vector[0] * d;
        intersection[1] = eye_loc[0] + click_vector[0] * d;
        intersection[2] = eye_loc[0] + click_vector[0] * d;
        return inShape(intersection);
    }

    private void findBaryocentric() {
        baryocentric = new float[coordinates.length];
        for (int i=0;i<COORDS_PER_VERTEX;++i){
            baryocentric[i] = coordinates[i];
        }
        for (int i=COORDS_PER_VERTEX;i<coordinates.length;++i){
            baryocentric[i] = coordinates[i] - coordinates[i % COORDS_PER_VERTEX];
        }
    }

    private boolean inShape(float[] intersection) {
        float v2[] = intersection - coordinates[0:2];
        x = v2 . v0;
        y= v1 . v0;
        z = v0 . v0;

        x * a = u * z * a + v * y * a;
        w = u * y + v * a;

        (x * a - w * y) / (z * a - y * y) = u;

        (w - u * y)/a = v;

                v2 . v1 = u * (v0 . v1) + v * (v1 . v1)

        w = v2 . v1;
        a = v1 . v1;

        x - v * y = z * u;

        (z * w - (x * y))/(z * a + y * y)  = v;
    }
}
