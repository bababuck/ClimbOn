package com.example.climbon;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Triangle {

    private FloatBuffer vertexBuffer;
    private final int mProgram;
    private int positionHandle;
    private int colorHandle;

    private int vPMatrixHandle;

    private final int vertexCount = 9 / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    float triangleCoords[];

    // Set color with red, green, blue and alpha (opacity) values
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    ArrayList<Line> edges = new ArrayList<>();

    public Triangle(float triangleCoords[]) {
        this.triangleCoords = triangleCoords;
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                triangleCoords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(triangleCoords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);
        mProgram = ProgramSingleton.getProgram();

        float edge1[] = {triangleCoords[0], triangleCoords[1], triangleCoords[2],
                         triangleCoords[3], triangleCoords[4], triangleCoords[5],};
        edges.add(new Line(edge1));

        float edge2[] =  {triangleCoords[3], triangleCoords[4], triangleCoords[5],
                          triangleCoords[6], triangleCoords[7], triangleCoords[8]};
        edges.add(new Line(edge2));

        float edge3[] =  {triangleCoords[6], triangleCoords[7], triangleCoords[8],
                          triangleCoords[0], triangleCoords[1], triangleCoords[2]};
        edges.add(new Line(edge3));
    }

    public void draw(float[] mvpMatrix) {
        for (Line line : edges) {
            line.draw(mvpMatrix);
        }
        GLES20.glUseProgram(mProgram);
        positionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        colorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        GLES20.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(positionHandle);


    }
}