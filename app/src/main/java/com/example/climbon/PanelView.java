package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Arrays;

public class PanelView extends AppCompatActivity {
    /* View a panel with the holds as clickable items.

    Clicking on buttons will turn those holds on/off.
    */
    // Buffers (in dp)
    int TOP_BUFFER = 100; // Need room for title, back button
    int EDGE_BUFFER = 40;
    int BOTTOM_BUFFER = 200;
    int edge_buffer, bottom_buffer, top_buffer;
    Shape current_shape;
    int screen_height;
    int screen_width;
    int extra_room_x, extra_room_y;
    int shape_height_px, shape_width_px;
    float ratio; // ratio of arb shape dimension to px
    float shape_height_arb;
    UniversalData saved_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_view);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;

        initializeSaveData(); // Needed for testing

        // Load the current shape from the save data.
        current_shape = saved_data.shapes.get(saved_data.current_shape);

        calculateBuffers();
        initializeDimensions();
        createButtons();
        drawShape();
    }

    private void drawShape() {
        /* Draws the shape of the panel to the screen in the proper location.

        Uses a custom view that will draw the path.
        For the boundary, need to convert the corner coordinates to screen locations.

        I chose to go the route of making the view the size of the full screen, and then
        converting the individual coordinates. Maybe more efficient slightly to go the route
        of making the view the size of the shape and offsetting within but would require
        making new utility function :(
        */
        ArrayList<Coordinate> corners_adj = new ArrayList<>();
        for (int i=0;i<current_shape.corners.size();++i){
            corners_adj.add(new Coordinate(convertCoordinateToLocation(true, current_shape.corners.get(i).x), convertCoordinateToLocation(false,current_shape.corners.get(i).y)));
        }
        Boundary boundary = new Boundary(corners_adj);
        AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(screen_width,
                screen_height,
                0,
                0);
        BoundaryView bnd_vw = new BoundaryView(this, boundary);
        AbsoluteLayout layout = findViewById(R.id.PanelView);
        layout.addView(bnd_vw, params);
    }

    private void initializeSaveData() {
        /* Fake function since no universal data yet since testing. */
        saved_data.current_shape = 0;
        ArrayList<Float> corners = new ArrayList<>(Arrays.asList(0f,0f,10f,0f,10f,8f,8f,10f,0f,10f));
        try {
            saved_data.shapes.add(new Shape(corners, new Coordinate(0.5f,0.5f)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculateBuffers() {
        /* Convert the buffers from DP to pixels.

        All things calculated here will be same for life of application.

        Could possible store the buffers/dims with the application since will be
        the same regardless of panel size, but not a huge speedup so maybe not.
        */
        DisplayMetrics display_metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display_metrics);
        screen_height = display_metrics.heightPixels;
        screen_width = display_metrics.widthPixels;

        edge_buffer = (int) convertDpToPixel(EDGE_BUFFER);
        top_buffer = (int) convertDpToPixel(TOP_BUFFER);
        bottom_buffer = (int) convertDpToPixel(BOTTOM_BUFFER);
    }

    private void initializeDimensions() {
        /* Calculates the rations/dimensions to be used for all buttons.

        These values depend on the screen size and panels, so will just
        calculate every time.
        */
        float shape_width_arb;
        float height_ratio, width_ratio;

        shape_height_arb = current_shape.get_height();
        shape_height_px = screen_height - (top_buffer + bottom_buffer);
        height_ratio = ((float) shape_height_px) / shape_height_arb;
        shape_width_arb = current_shape.get_width();
        shape_width_px = screen_width - 2 * edge_buffer;
        width_ratio = ((float) shape_width_px) / shape_width_arb;
        ratio = Math.min(width_ratio, height_ratio);
        extra_room_x = shape_width_px - (int) (shape_width_arb * ratio);
        extra_room_y = shape_height_px - (int) (shape_height_arb * ratio);
    }

    private void createButtons() {
        /* Creates buttons from the list of coordinates.

        Android sets (0,0) as top left, coordinate system is from bottom right
        Have to resolve this issue.
        */
        ArrayList<Coordinate> buttons = current_shape.hold_set;
        AbsoluteLayout layout = findViewById(R.id.PanelView);
        for (int i=0;i< buttons.size();++i) {
            Button button = new Button(this);
            button.setId(i);
            button.setBackgroundColor(Color.rgb(255, 0, 0));
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    button.setBackgroundColor(Color.rgb(0, 255, 0));
                }
            });
            int x = convertCoordinateToLocation(true, buttons.get(i).x);
            //Log.e("Help", "Coor:"+String.valueOf(buttons.get(i).y));
            int y = convertCoordinateToLocation(false, buttons.get(i).y);
            int button_size = 50;
            AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(button_size,button_size,x-button_size/2,y-button_size/2);
            layout.addView(button, params);
        }
    }

    private int convertCoordinateToLocation(boolean is_x, float coordinate) {
        /* Calculates the exact pixel location of a hold on the screen.

        Should be robust against screen sizes but who knows-
        and also who cares since probably only will run on my phone.

        Inputs:
        - is_x: True is an x-coordinate, false if y-coordinate
        - coordinate: a x or y coordinate in the 'Shape' plane
        */
        int offset;
        if (is_x){
            offset = (int) (coordinate * ratio) + extra_room_x / 2 + edge_buffer;
        } else {
            offset = (int) ((shape_height_arb - coordinate) * ratio) + extra_room_y / 2 + top_buffer;
        }
        return offset;
    }

    public float convertDpToPixel(float dp){
        this.getResources().getDisplayMetrics();
        return dp * ((float) this.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}