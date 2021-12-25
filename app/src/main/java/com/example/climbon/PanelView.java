package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
    int EDGE_BUFFER = 5;
    int BOTTOM_BUFFER = 5;
    Shape current_shape;
    int screen_height;
    int screen_width;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* */
        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        UniversalData saved_data = app.data;
        saved_data.current_shape = 0;
        ArrayList<Float> corners = new ArrayList<Float>(Arrays.<Float>asList(0f,0f,10f,0f,10f,8f,8f,10f,0f,10f));
        try {
            saved_data.shapes.add(new Shape(corners, new Coordinate(0.5f,0.5f)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        current_shape = saved_data.shapes.get(saved_data.current_shape);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel_view);
        createButtons();
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
            button.setBackgroundColor(Color.rgb(10*i, 0, 0));
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    button.setBackgroundColor(Color.rgb(0, 100, 0));
                }
            });
            int x = convertXCoordinateToLocation(buttons.get(i).x);
            int y = convertYCoordinateToLocation(buttons.get(i).y);
            AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(50,50,500+x*15,y);
            layout.addView(button, params);
            System.out.println(x);
        }
    }

    private int convertYCoordinateToLocation(float y_coordinate) {
        /* Calculates the exact y-pixel location of a hold on the screen.

        Should be robust against screen sizes but who knows-
        and also who cares since probably only will run on my phone.
        */
        int shape_height_px, top_buffer, bottom_buffer, offset;
        float shape_height_arb;

        shape_height_arb = current_shape.get_height();
        top_buffer = (int) convertDpToPixel(TOP_BUFFER, this);
        bottom_buffer = (int) convertDpToPixel(BOTTOM_BUFFER, this);
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

        shape_width_arb = current_shape.get_width();
        edge_buffer = (int) convertDpToPixel(EDGE_BUFFER, this);
        shape_width_px = screen_width - 2 * edge_buffer;
        offset = (int) (x_coordinate * (float) shape_width_px / shape_width_arb);
        return edge_buffer + offset;
    }

    public static float convertDpToPixel(float dp, PanelView context){
        context.getResources().getDisplayMetrics();
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}