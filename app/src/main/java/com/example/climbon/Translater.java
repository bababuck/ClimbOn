package com.example.climbon;

public class Translater {
    /* Translates coordinates from one dimension to another.

    Designed for scaling shapes

    First flips the shape vertically.

    Linear transformations only.
    y = (ratio) * x + offset
    */
    private final int top_offset, left_offset;
    private final float ratio, shape_height;

    public Translater(int top_buffer, int edge_buffer, int bottom_buffer, int screen_height, int screen_width, float shape_height, float shape_width){
        /* Initializes the class, setting up offsets and ratios.

        */
        int usable_height, usable_width;
        float height_ratio, width_ratio;
        this.shape_height = shape_height;
        usable_height = screen_height - (top_buffer + bottom_buffer);
        usable_width = screen_width - 2 * edge_buffer;

        height_ratio = ((float) usable_height) / shape_height;
        width_ratio = ((float) usable_width) / shape_width;
        ratio = Math.min(width_ratio, height_ratio);

        left_offset = edge_buffer + (usable_width - (int) (shape_width * ratio))/2;
        top_offset = top_buffer + (usable_height - (int) (shape_height * ratio))/2;
    }

    public Translater(int top_buffer, int edge_buffer, int bottom_buffer, int screen_height, int screen_width, float shape_height, float shape_width, float ratio){
        /* Initializes the class, using a given ratio.

        Theres gotta be a better way than this -> maybe use builder if i get around to it.
         */
        int usable_height, usable_width;
        this.shape_height = shape_height;
        usable_height = screen_height - (top_buffer + bottom_buffer);
        usable_width = screen_width - 2 * edge_buffer;

        this.ratio = ratio;

        left_offset = edge_buffer + (usable_width - (int) (shape_width * ratio))/2;
        top_offset = top_buffer + (usable_height - (int) (shape_height * ratio))/2;
    }

    public int translateX(float x) {
        /* Translate an X coordinate according to scheme. */
        return left_offset + (int) (ratio * x);
    }

    public int translateY(float y) {
        /* Translate an X coordinate according to scheme. */
        return top_offset + (int) (ratio * (shape_height - y));
    }

    public float getRatio(){
        return ratio;
    }
}
