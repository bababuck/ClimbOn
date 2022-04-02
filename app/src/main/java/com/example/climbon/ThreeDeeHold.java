package com.example.climbon;

import android.os.Build;

import androidx.annotation.RequiresApi;

public class ThreeDeeHold extends ThreeDeeShape{
    public float left_bound, right_bound, top_bound, bottom_bound;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ThreeDeeHold(float[] coordinates, float[] color) {
        super(coordinates, color);
        left_bound = Float.MAX_VALUE;
        right_bound = Float.MIN_VALUE;
        top_bound = Float.MIN_VALUE;
        bottom_bound = Float.MAX_VALUE;
        for (int i=0;i<vertexCount;++i) {
            left_bound = Float.min(rotated_coordinates[2*i], left_bound);
            right_bound = Float.max(rotated_coordinates[2*i], right_bound);
            bottom_bound = Float.min(rotated_coordinates[2*i+1], bottom_bound);
            top_bound = Float.max(rotated_coordinates[2*i+1], top_bound);
        }
    }

    public boolean contains2D(int click_x, int click_y) {
        for (int i;i<vertexCount;++i) {

        }
        return true;
    }
}
