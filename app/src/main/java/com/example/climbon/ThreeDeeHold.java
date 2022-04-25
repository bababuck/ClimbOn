package com.example.climbon;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class ThreeDeeHold extends ThreeDeeShape{
    public float left_bound, right_bound, top_bound, bottom_bound;
    public int SQL_id;
    public int LED_id;
    public float[] base_color;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ThreeDeeHold(float[] coordinates, float[] color, int SQL_id, int LED_id) {
        super(coordinates, color, -1);
        this.SQL_id = SQL_id;
        this.LED_id = LED_id;
        left_bound = Float.MAX_VALUE;
        right_bound = -Float.MAX_VALUE;
        top_bound = -Float.MAX_VALUE;
        bottom_bound = Float.MAX_VALUE;
        for (int i=0;i<vertexCount;++i) {
            left_bound = Float.min(rotated_coordinates[2*i], left_bound);
            right_bound = Float.max(rotated_coordinates[2*i], right_bound);
            bottom_bound = Float.min(rotated_coordinates[2*i+1], bottom_bound);
            top_bound = Float.max(rotated_coordinates[2*i+1], top_bound);
//            Log.e("ThreeDeeHold", "counts: "+vertexCount+" "+i);
        }
        base_color = color;
    }

    public boolean contains2D(float click_x, float click_y) {
        ArrayList<Float> rot = new ArrayList<>();
        for (int i=0;i<vertexCount;++i) {
            rot.add(rotated_coordinates[2*i]);
            rot.add(rotated_coordinates[2*i+1]);
        }
        try {
            Shape shape = new Shape(rot);
            return shape.isInside(new Coordinate(click_x-left_bound, click_y-bottom_bound));
        } catch (Exception E) {
            Log.e("ThreeDeeHold", E.toString());
        }
        return false;
    }

    public void setColor(float[] new_color) {
        color = new_color;
        Log.e("ThreeDeeHold", "Set hold color");
    }
}
