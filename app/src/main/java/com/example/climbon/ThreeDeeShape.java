package com.example.climbon;

import android.opengl.GLES20;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

// TODO: Will use bounding boxes for holds, and will sort each hold by its bounding box into quadrants (dynamically sized for how many), then only search to see if click is onto shape in certain quadrant
public class ThreeDeeShape {
    protected FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;
    private ShortBuffer lineListBuffer;
    protected final int mProgram;
    protected int positionHandle;
    protected int colorHandle;

    public ArrayList<ThreeDeeShape> holds = new ArrayList<>();

    public void addHold(float coordinates[]){
        float color[] = {0.0f, 0.0f,1.0f, 1.0f};
        ThreeDeeShape shape = new ThreeDeeShape(coordinates, color);
        for (int i=0;i<3;++i){
            rotate(coordinates, i, shape.rotated_coordinates, i);
        }
        holds.add(shape);
    }

    /* arraylist<arraylist<arraylist<holds>>>
    split in four, check bounding boxes
    SPLIT_STOP = 1.5;
    average = findAverage();
    horizontal_split = true;
    vertical_split = true;
    num_horizontal_splits = 0;
    num_vertical_splits = 0;
    while (horizontal_split || vertical_split)
    if (horizontal_split)
        ++num_horizontal_splits;
        new_average = split_horizontal
        if average/new_average < SPLIT_STOP
            horizontal_split  = false
        average = new_average

    if (vertical_split)
        new_average = split_vertical
        if average/new_average < SPLIT_STOP
            horizontal_split  = false
        average = new_average
        ++num_vertical_splits;

    for (int i; i<hold_bins.length;++i){
        for (int j; j<hold_bins[0].length;++j){ // Will this change?
            hold_bins[i].add(2*j+1, new ArrayList<ThreeDeeList>);
            float bin_size = (right_edge - left_edge) / Math.pow(2, num_horizontal_splits);
            float bin_left = left_edge + j * bin_size
            float bin_mid = bin_left + bin_size;
            float bin_right = bin_mid + bin_size;
            for (int k=0;k<hold_bins[i][2*j].length;++k) { // will this change
                ThreeDeeShape hold = hold_bins[i][2*j][k]
                if (!(hold.left_bound < bin_mid && hold.right_bound > bin_left)) {
                    hold_bins[i][2*j].delete(k);
                    --k;
                }
                if (hold.left_bound < bin_right && hold.right_bound > bin_mid) {
                    hold_bins[i][2*j+1].add(hold);
                }
            }
        }
    }

    for (int i; i<hold_bins.length;++i){ // will this change
        hold_bins.add(2*i+1, new ArrayList<ArrayList<ThreeDeeList>>);
        for (int j; j<hold_bins[0].length;++j){
            float bin_size = (top_edge - bottom_edge) / Math.pow(2, num_vertical_splits);
            float bin_top = top_edge - j * bin_size
            float bin_mid = bin_top - bin_size;
            float bin_bottom = bin_mid - bin_size;
            for (int k=0;k<hold_bins[2*i][j].length;++k) { // will this change
                ThreeDeeShape hold = hold_bins[2*i][j][k]
                if (!(hold.top_bound > bin_mid && hold.bottom_bound < bin_top)) {
                    hold_bins[2*i][j].delete(k);
                    --k;
                }
                if (hold.left_bound < bin_right && hold.right_bound > bin_mid) {
                    hold_bins[2*i+1][j].add(hold);
                }
            }
        }
    }


    click_region_x = (int) (click[0] - left_edge) / (right_edge - left_edge) * Math.pow(2, num_horizontal_splits)
    click_region_y = (int) (click[1] - bottom_edge) / (top_edge - bottom_edge) * Math.pow(2, num_vertical_splits)
    for (ThreeDeeShape hold : hold_bins[click_region_y][click_region_x]) {
        if (hold.contains(click)){
            hold.setColor("Blue")
            return true
        }
    }
    return false
     */

    private int vPMatrixHandle;

    private final int vertexCount;
    private final int vertexStride = COORDS_PER_VERTEX * 4;

    static final int COORDS_PER_VERTEX = 3;
    float coordinates[];
    float rotated_coordinates[];

    float baryocentric[] = new float[9];
    float v1dotv0;
    float v0dotv0;
    float v1dotv1;
    float v3dotv3;
    float v1dotv3;

    float color[];

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 };
    private short square_line_order[] = {0, 1, 1, 2, 2, 3, 3, 1};
    private short triangle_line_order[] = {0, 1, 1, 2, 2, 1};
    private float normal[] = new float[3];
    private float rotationMatrix[] = new float[9];
    float intersection[] = new float[3];



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
        getRotationMatrix();
    }

    private void getRotationMatrix() {
        // https://math.stackexchange.com/questions/1167717/transform-a-plane-to-the-xy-plane
        float a,b,c;
        a = normal[0];
        b = normal[1];
        c = normal[2];
        float sos = a*a+b*b+c*c;
        float ssos = ((float) Math.sqrt(sos));
        float u1 = b / ssos;
        float u2 = -a / ssos;
        float cos = c / ssos;
        float sin = ((float) Math.sqrt(a*a+b*b)) / ssos;

        rotationMatrix[0] = cos + u1*u1*(1-cos);
        rotationMatrix[1] = u1*u2*(1-cos);
        rotationMatrix[2] = -u2 * sin;
        rotationMatrix[3] = u1*u2*(1-cos);
        rotationMatrix[4] = cos + u2*u2*(1-cos);
        rotationMatrix[5] = u1 * sin;
        rotationMatrix[6] = u2 * sin;
        rotationMatrix[7] = -u1 * sin;
        rotationMatrix[8] = cos;
    }

    public void rotate(float coordinate[], int offset1, float return_vec[], int offset2) {
        // Z's will all be same so we don't care about em...
        return_vec[offset2] = rotationMatrix[0] * coordinate[offset1] + rotationMatrix[3] * coordinate[offset1+1] + rotationMatrix[6] * coordinate[offset1+2];
        return_vec[1+offset2] = rotationMatrix[1] * coordinate[offset1] + rotationMatrix[4] * coordinate[offset1+1] + rotationMatrix[5] * coordinate[offset1+2];
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
        for (ThreeDeeShape hold : holds) {
            hold.draw(mvpMatrix);
        }
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

    public void setNormal(int start_loc_1, int start_loc_2, int start_loc_3) {
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

        float d = ((coordinates[0]-eye_loc[0]) * normal[0] + (coordinates[1]-eye_loc[1]) * normal[1] + (coordinates[2]-eye_loc[2]) * normal[2])/denominator;


        intersection[0] = eye_loc[0] + click_vector[0] * d;
        intersection[1] = eye_loc[1] + click_vector[1] * d;
        intersection[2] = eye_loc[2] + click_vector[2] * d;
        Log.e("ThreeDeeShape",intersection[0] + "  " + intersection[1] + "  " + intersection[2]);
        return inShape();
    }

    private void findBaryocentric() {
        for (int i=0;i<((vertexCount-1)*COORDS_PER_VERTEX);++i){
            baryocentric[i] = coordinates[i + 3] - coordinates[i % COORDS_PER_VERTEX];
        }
        v1dotv0 = baryocentric[0] * baryocentric[3] +
                baryocentric[1] * baryocentric[4] +
                baryocentric[2] * baryocentric[5];
        v0dotv0 = baryocentric[0] * baryocentric[0] +
                baryocentric[1] * baryocentric[1] +
                baryocentric[2] * baryocentric[2];
        v1dotv1 = baryocentric[3] * baryocentric[3] +
                baryocentric[4] * baryocentric[4] +
                baryocentric[5] * baryocentric[5];
        if (vertexCount == 4){
            v3dotv3 = baryocentric[6] * baryocentric[6] +
                    baryocentric[7] * baryocentric[7] +
                    baryocentric[8] * baryocentric[8];
            v1dotv3 = baryocentric[3] * baryocentric[6] +
                    baryocentric[4] * baryocentric[7] +
                    baryocentric[5] * baryocentric[8];
        }
    }

    private boolean inShape() {
        float v2[] = {intersection[0] - coordinates[0], intersection[1] - coordinates[1], intersection[2] - coordinates[2]};
        float v2dotv0 = baryocentric[0] * v2[0] +
                baryocentric[1] * v2[1] +
                baryocentric[2] * v2[2];
        float v2dotv1 = baryocentric[3] * v2[0] +
                baryocentric[4] * v2[1] +
                baryocentric[5] * v2[2];
        float u = (v2dotv0 * v1dotv1 - v2dotv1 * v1dotv0)/(v0dotv0* v1dotv1 - v1dotv0 * v1dotv0);
        float v = (v2dotv1 - u * v1dotv0)/v1dotv1;
        Log.e("ThreeDeeShape",u + "  " + v);
        if ((u >= 0f) && (v >= 0f) && (u <= 1f) && (v <= 1f) && ((v + u) <= 1f)) {
            return true;
        }
        if (vertexCount == 4) {
            float v2dotv3 = baryocentric[6] * v2[0] +
                    baryocentric[7] * v2[1] +
                    baryocentric[8] * v2[2];
            u = (v2dotv3 * v1dotv1 - v2dotv1 * v1dotv3)/(v1dotv1* v3dotv3 - v1dotv3 * v1dotv3);
            v = (v2dotv1 - u * v1dotv3)/v1dotv1;
            Log.e("ThreeDeeShape",u + "  " + v);
            return (u >= 0f) && (v >= 0f) && (u <= 1f) && (v <= 1f) && ((v + u) <= 1f);
        }
        return false;
//        x = v2 . v0;
//        y= v1 . v0;
//        z = v0 . v0;
//
//        x * a = u * z * a + v * y * a;
//        w = u * y + v * a;
//
//        (x * a - w * y) / (z * a - y * y) = u;
//
//        (w - u * y)/a = v;
//
//                v2 . v1 = u * (v0 . v1) + v * (v1 . v1)
//
//        w = v2 . v1;
//        a = v1 . v1;
//
//        x - v * y = z * u;
//
//        (z * w - (x * y))/(z * a + y * y)  = v;
    }

    public void setColor() {
        color[0] = 1.0f;
        color[1] = 0.0f;
        color[2] = 0.0f;
    }
}
