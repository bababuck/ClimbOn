package com.example.climbon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;

import java.util.ArrayList;

public class PanelViewer extends AppCompatActivity {

    // Buffers (in dp)
    int TOP_BUFFER = 100; // Need room for title, back button
    int EDGE_BUFFER = 5;
    int BOTTOM_BUFFER = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* */
        UniversalData saved_data = ((ClimbOnApplication) this.getApplication()).data;
        Panel current_panel = saved_data.panels.get(saved_data.current_panel);
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

    private int convertYCoordinateToLocation() {
        int width, height;

    }

    private int convertXCoordinateToLocation(float x_coordinate) {
        int screen_width, edge_buffer, shape_width_arb, offset, shape_width_px;
        edge_buffer = (int)  convertDpToPixel(EDGE_BUFFER, context);
        shape_width_px = screen_width - 2 * edge_buffer;
        offset = x_coordinate * (float) shape_width_px / (float) shape_width_arb;
        return edge_buffer + offset;
    }

    public static float convertDpToPixel(float dp, Context context){
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}