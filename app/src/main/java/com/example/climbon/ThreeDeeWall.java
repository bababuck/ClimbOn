package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.os.Bundle;
import android.util.Log;
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

        ConstraintLayout layout = findViewById(R.id.ThreeDeeWallLayout);
//        {
//            int GLVIEW_ID = 420;
//          gLView.setId(GLVIEW_ID);
//            ConstraintLayout.LayoutParams openGLParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
//            layout.addView(gLView, openGLParams);
//        }
        {
            int TEXT_ID = 420;
            text.setId(TEXT_ID);
            ConstraintLayout.LayoutParams openGLParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
            layout.addView(text, openGLParams);
        }
        {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.connect(text.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
//            constraintSet.connect(gLView.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
            constraintSet.connect(text.getId(),ConstraintSet.BOTTOM,ConstraintSet.PARENT_ID,ConstraintSet.BOTTOM,0);
//            constraintSet.connect(gLView.getId(),ConstraintSet.TOP,text.getId(),ConstraintSet.BOTTOM,0);
            constraintSet.applyTo(layout);
        }
        Log.e("ThreeDeeWall","Setting content view...");
//        setContentView(layout);
    }
}