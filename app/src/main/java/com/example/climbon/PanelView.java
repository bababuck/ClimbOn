package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;

import android.view.View;
import android.widget.AbsoluteLayout;

import java.util.ArrayList;

public class PanelView extends AppCompatActivity {
    /* View a panel with the holds as clickable items.

    Clicking on buttons will turn those holds on/off.
    TODO: Adjust button size based on zoom.
    TODO: Adjust centering of buttons over coordinate.
    */
    // Buffers (Arbitrary distances shape must stay away from edges of screen
    private final static int TOP_BUFFER = 10; // Need room for title, back button
    private final static int EDGE_BUFFER = 10;
    protected final static int BOTTOM_BUFFER = 300; // Big for now since buttons at bottom block screen in Emulator

    private Translater translater;
    protected int screen_height;
    protected int screen_width;
    private Shape current_shape;
    private ArrayList<Integer> current_hold_types;
    protected ArrayList<PanelViewHold> all_holds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* Performs actions to set up the screen.

        For now this is the only functionality in the class.
        Yes absolute layout is deprecated, I think I worked around it.
        TODO: Switch away from absolute layout.
        */
        super.onCreate(savedInstanceState);
        setXML();

        initializeData();
        getScreenInformation();
        initializeTranslater();
        drawShape();
        createButtons();
    }

    protected void setXML() {
        /* Selected xml file to use. */
        setContentView(R.layout.activity_panel_view);
    }

    private void getScreenInformation() {
        /* Gather screen dimensions to help with drawing. */
        DisplayMetrics display_metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display_metrics);
        screen_height = display_metrics.heightPixels;
        screen_width = display_metrics.widthPixels;
    }

    private void initializeData() {
        /* Load the current shape from the save data. */
        UniversalData saved_data;
        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;
        current_shape = saved_data.wall.panel_set.get(saved_data.current_shape);
        current_hold_types = current_shape.hold_types;
    }

    private void initializeTranslater() {
        /* Helper function to create the translater.

        Translater will help with positioning and scaling.
        */
        int shape_width = (int) current_shape.get_width();
        int shape_height = (int) current_shape.get_height();
        translater = new Translater(TOP_BUFFER,
                EDGE_BUFFER,
                BOTTOM_BUFFER,
                screen_height,
                screen_width,
                shape_height,
                shape_width);
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
            corners_adj.add(new Coordinate(translater.translateX((int) current_shape.corners.get(i).x),
                                            translater.translateY((int) current_shape.corners.get(i).y)));
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

    private void createButtons() {
        /* Creates buttons from the list of coordinates.

        Android sets (0,0) as top left, coordinate system is from bottom right
        Have to resolve this issue.
        */
        ArrayList<Coordinate> buttons = current_shape.hold_set;
        AbsoluteLayout layout = findViewById(R.id.PanelView);
        for (int i=0;i< buttons.size();++i) {
            PanelViewHold button = new PanelViewHold(this, current_hold_types.get(i),true);
            button.setId(i);
            // Set here, since we will reuse PanelViewHold later
            setOnCLickListener(button);
            int x = translater.translateX(buttons.get(i).x);
            int y = translater.translateY(buttons.get(i).y);
            int button_size = (int) (translater.getRatio() * Shape.DISTANCE_BETWEEN_HOLDS);
            AbsoluteLayout.LayoutParams params = new AbsoluteLayout.LayoutParams(button_size,button_size,x-button_size/2,y-button_size/2);
            layout.addView(button, params);
            all_holds.add(button);
        }
    }

    protected void setOnCLickListener(PanelViewHold button) {
        /* Sets the OnClickListener for a button

        By pulling out, we can override this function in other classes.
        */
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (button.hold_type != HOLD_TYPE.NO_HOLD) {
                    button.on = !button.on;
                    button.setHoldImage();
                }
            }
        });
    }
}