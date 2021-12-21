package com.example.climbon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;

import java.util.ArrayList;

public class PanelView extends AppCompatActivity {
    /* View a panel with the holds as clickable items.

    Clicking on buttons will turn those holds on/off.
    */
    // Buffers (in dp)
    int TOP_BUFFER = 100; // Need room for title, back button
    int EDGE_BUFFER = 5;
    int BOTTOM_BUFFER = 5;
    Panel current_panel;
    int screen_height;
    int screen_width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* */
        UniversalData saved_data = ((ClimbOnApplication) this.getApplication()).data;
        current_panel = saved_data.panels.get(saved_data.current_panel);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_viewer);

    }

    private ArrayList<Button> createButtons() {
        /* Creates buttons from the list of coordinates.

        Android sets (0,0) as top left, coordinate system is from bottom right
        Have to resolve this issue.
        */
        int width, height;

    }

    private int convertYCoordinateToLocation(float y_coordinate) {
        /* Calculates the exact y-pixel location of a hold on the screen.

        Should be robust against screen sizes but who knows-
        and also who cares since probably only will run on my phone.
        */
        int shape_height_px, top_buffer, bottom_buffer, offset;
        float shape_height_arb;

        shape_height_arb = current_panel.shape.get_height();
        top_buffer = (int) convertDpToPixel(TOP_BUFFER, context);
        bottom_buffer = (int) convertDpToPixel(BOTTOM_BUFFER, context);
        shape_height_px = screen_height - (top_buffer + bottom_buffer);
        offset = (int) (y_coordinate * (float) shape_height_px / shape_height_arb);
        return top_buffer + offset;
    }

    private int convertXCoordinateToLocation(float x_coordinate) {
        /* Calculates the exact x-pixel location of a hold on the screen.

        Should be robust against screen sizes but who knows-
        and also who cares since probably only will run on my phone.
        */
        int edge_buffer, offset, shape_width_px;
        float shape_width_arb;

        shape_width_arb = current_panel.shape.get_width();
        edge_buffer = (int) convertDpToPixel(EDGE_BUFFER, context);
        shape_width_px = screen_width - 2 * edge_buffer;
        offset = (int) (x_coordinate * (float) shape_width_px / shape_width_arb);
        return edge_buffer + offset;
    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}