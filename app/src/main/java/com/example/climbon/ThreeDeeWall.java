package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThreeDeeWall extends AppCompatActivity {

    public String route_name;
    public int rating;
    ClimbOnApplication app;
    int GLVIEW_ID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.e("ThreeDeeWall","Entering ThreeDeeWall activity...");
        super.onCreate(savedInstanceState);

        app = (ClimbOnApplication) getApplication();
        rating = app.data.current_route.getVRating();
        route_name = app.data.current_route.getName();

        MyGLSurfaceView gLView = new MyGLSurfaceView(this);
        TextView text = new TextView(this);
        String route_description = route_name + ", V" + rating;
        text.setText(route_description);

        Button copy_button = new Button(this);
        copy_button.setText("Clone");

        ConstraintLayout layout = new ConstraintLayout(this);
        {
            GLVIEW_ID = View.generateViewId();
            gLView.setId(GLVIEW_ID);
            layout.addView(gLView);
        }
        {
            int TEXT_ID = View.generateViewId();
            text.setId(TEXT_ID);
            layout.addView(text);
        }
        {
            int COPY_ID = View.generateViewId();
            copy_button.setId(COPY_ID);
            layout.addView(copy_button);
        }
        {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.constrainWidth(text.getId(), constraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(text.getId(), constraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(gLView.getId(), constraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(gLView.getId(), constraintSet.WRAP_CONTENT);
            constraintSet.constrainWidth(copy_button.getId(), constraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(copy_button.getId(), constraintSet.WRAP_CONTENT);
            constraintSet.connect(text.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
            constraintSet.connect(gLView.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
            constraintSet.connect(text.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
            constraintSet.connect(gLView.getId(),ConstraintSet.TOP,text.getId(),ConstraintSet.BOTTOM,0);

            constraintSet.connect(gLView.getId(),ConstraintSet.TOP,copy_button.getId(),ConstraintSet.BOTTOM,0);
            constraintSet.connect(copy_button.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,0);
            constraintSet.connect(copy_button.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
            constraintSet.applyTo(layout);
        }

        copy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ThreeDeeWall","Copy button clicked...");
                Intent intent = new Intent(view.getContext(), RouteViewThreeDee.class);
                view.getContext().startActivity(intent);
            }
        });

        setContentView(layout);

        gLView.requestRender();
    }

}