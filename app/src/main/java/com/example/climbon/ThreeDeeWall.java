package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class ThreeDeeWall extends AppCompatActivity {

    public String route_name = "Generic Route";
    public int rating = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("ThreeDeeWall","Entering ThreeDeeWall activity...");
        super.onCreate(savedInstanceState);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        rating = app.data.current_route.getVRating();
        route_name = app.data.current_route.getName();

        MyGLSurfaceView gLView = new MyGLSurfaceView(this);
        TextView text = new TextView(this);
        String route_description = route_name + ", V" + rating;
        text.setText(route_description);

        ConstraintLayout layout = new ConstraintLayout(this);
        {
            int GLVIEW_ID = View.generateViewId();
            gLView.setId(GLVIEW_ID);
            layout.addView(gLView);
        }
        {
            int TEXT_ID = View.generateViewId();
            text.setId(TEXT_ID);
            layout.addView(text);
        }
        {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.constrainWidth(text.getId(), constraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(text.getId(), constraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(gLView.getId(), constraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(gLView.getId(), constraintSet.WRAP_CONTENT);
            constraintSet.connect(text.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
            constraintSet.connect(gLView.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
            constraintSet.connect(text.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
            constraintSet.connect(gLView.getId(),ConstraintSet.TOP,text.getId(),ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(layout);
        }

        setContentView(layout);

        gLView.requestRender();
    }
}