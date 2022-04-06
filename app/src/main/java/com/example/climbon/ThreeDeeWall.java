package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ThreeDeeWall extends AppCompatActivity {

    private MyGLSurfaceView gLView;

    public HashMap<Integer, float[]> selected_holds = new HashMap<>();
    public HashMap<Integer, float[]> deleted_holds = new HashMap<>();
    public ArrayList<Integer> previous_holds = new ArrayList<>();
    public String route_name = "Generic Route";
    public int rating = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("ThreeDeeWall","Entering ThreeDeeWall activity...");
        super.onCreate(savedInstanceState);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        rating = app.data.current_route.getVRating();
        route_name = app.data.current_route.getName();

        gLView = new MyGLSurfaceView(this);
        TextView text = new TextView(this);
        text.setText(route_name + ", V" + rating);

        ConstraintLayout layout = new ConstraintLayout(this);
        {
            int GLVIEW_ID = 420;
            ConstraintLayout.LayoutParams openGLParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
            layout.addView(gLView, openGLParams);
            gLView.setId(GLVIEW_ID);
        }
        {
            int TEXT_ID = 420;
            ConstraintLayout.LayoutParams openGLParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
            layout.addView(text, openGLParams);
            text.setId(TEXT_ID);
        }
        {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.connect(gLView.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
            constraintSet.connect(text.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
            constraintSet.connect(gLView.getId(),ConstraintSet.TOP,text.getId(),ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(layout);
        }
        Log.e("ThreeDeeWall","Setting content view...");
        setContentView(layout);
    }
}