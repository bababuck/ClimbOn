package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RouteViewThreeDee extends AppCompatActivity {

    private RouteSetGLSurfaceView gl_view;

    public int current_route_id = 69;
    public String route_name;
    public int rating;
    public int route_type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLayout();


    }

    private void setupLayout() {

        gl_view = new RouteSetGLSurfaceView(this);

        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        rating = app.data.current_route.getVRating();
        route_name = app.data.current_route.getName();
        route_type = app.data.current_route.getType();

        TextView text = new TextView(this);
        String route_description = route_name + ", V" + rating + " " + RouteTypesEnum.values()[route_type];
        text.setText(route_description);

        Button save_button = new Button(this);
        save_button.setText("Save");

        Button edit_button = new Button(this);
        edit_button.setText("Edit");

        ConstraintLayout layout = new ConstraintLayout(this);
        {
            int GLVIEW_ID = View.generateViewId();
            gl_view.setId(GLVIEW_ID);
            layout.addView(gl_view);
        }
        {
            int TEXT_ID = View.generateViewId();
            text.setId(TEXT_ID);
            layout.addView(text);
        }
        {
            int SAVE_ID = View.generateViewId();
            save_button.setId(SAVE_ID);
            layout.addView(save_button);
        }
        {
            int EDIT_ID = View.generateViewId();
            edit_button.setId(EDIT_ID);
            layout.addView(edit_button);
        }
        addConstraints(edit_button, text, gl_view, save_button, layout);
        setContentView(layout);

        routeEditButton(edit_button);
        routeSaveButton(save_button);
    }

    private void routeSaveButton(Button save_button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to save?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        onSaveClicked();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        assert true;
                    }
                });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("RouteViewThreeDee","Save button clicked.");

                if (!((ClimbOnApplication) getApplication()).data.saved) {
                    builder.create().show();
                } else {
                    Log.e("RouteViewThreeDee","Already saved.");
                }
            }
        });
    }

    private void routeEditButton(Button edit_button) {
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("RouteViewThreeDee","Edit button clicked.");
                if (!((ClimbOnApplication) getApplication()).data.saved){
                    Intent intent = new Intent(view.getContext(), EditRouteInfoActivity.class);
                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    private void addConstraints(Button edit_button, TextView text, RouteSetGLSurfaceView gLView, Button save_button, ConstraintLayout layout) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.constrainWidth(text.getId(), constraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(text.getId(), constraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(gLView.getId(), constraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(gLView.getId(), constraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(save_button.getId(), constraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(edit_button.getId(), constraintSet.WRAP_CONTENT);
        constraintSet.constrainWidth(edit_button.getId(), constraintSet.MATCH_CONSTRAINT);
        constraintSet.constrainHeight(save_button.getId(), constraintSet.WRAP_CONTENT);
        constraintSet.connect(text.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
        constraintSet.connect(gLView.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
        constraintSet.connect(text.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
        constraintSet.connect(gLView.getId(),ConstraintSet.TOP,text.getId(),ConstraintSet.BOTTOM,0);
        constraintSet.connect(gLView.getId(),ConstraintSet.TOP,save_button.getId(),ConstraintSet.BOTTOM,0);
        constraintSet.connect(save_button.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,0);
        constraintSet.connect(save_button.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
        constraintSet.connect(gLView.getId(),ConstraintSet.TOP,edit_button.getId(),ConstraintSet.BOTTOM,0);
        constraintSet.connect(save_button.getId(),ConstraintSet.LEFT,edit_button.getId(),ConstraintSet.RIGHT,0);
        constraintSet.connect(edit_button.getId(),ConstraintSet.LEFT,text.getId(),ConstraintSet.RIGHT,0);
        constraintSet.connect(edit_button.getId(),ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP,0);
        constraintSet.applyTo(layout);
    }

    class RouteSetGLSurfaceView extends MyGLSurfaceView {
        public RouteSetGLSurfaceView(Context context) {
            super(context);
        }

        protected void onClick(MotionEvent e) {
            float temp[] = new float[4];
            float result[] = new float[4];
            float invertView[] = new float[16];
            float invertProjection[];
            Log.e("RouteViewThreeDeeWall","Click");
            float clickY = e.getY();
            float clickX = e.getX();
            renderer.getInverseView(invertView);
            invertProjection = renderer.invertProjection;
            float click_loc[] = {(2.0f * clickX) / renderer.width - 1.0f, 1.0f - (2.0f * clickY) / renderer.height, -1.0f, 1.0f};

            Matrix.multiplyMV(temp, 0, invertProjection, 0, click_loc, 0);
            temp[2] = -1.0f;
            temp[3] = 0.0f;
            Matrix.multiplyMV(result, 0, invertView, 0, temp, 0);

            ThreeDeeHold hold = renderer.findClickedHold(result);
            if (hold != null) {
                holdClicked(hold);
                ((ClimbOnApplication) getApplication()).bluetoothService.write(renderer.getLedBits());
                requestRender();
            }
        }
    }

    public HashMap<Integer, float[]> selected_holds = new HashMap<>();
    public HashMap<Integer, float[]> deleted_holds = new HashMap<>();
    public ArrayList<Integer> previous_holds = new ArrayList<>();

    private void holdClicked(ThreeDeeHold hold) {
        if (previous_holds.contains(hold.SQL_id)){
            if (deleted_holds.containsKey(hold.SQL_id)){
                hold.setColor(deleted_holds.get(hold.SQL_id));
                deleted_holds.remove(hold.SQL_id);
            } else {
                deleted_holds.put(hold.SQL_id, hold.color);
                hold.setColor(hold.base_color);
            }
        } else {
            if (selected_holds.containsKey(hold.SQL_id)) {
                hold.setColor(hold.base_color);
                selected_holds.remove(hold.SQL_id);
            } else {
                float color[] = new float[]{1.0f, 1.0f, 0.0f, 1.0f};
                selected_holds.put(hold.SQL_id, hold.color);
                hold.setColor(color);
            }
        }
    }

    private void onSaveClicked() {
        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        app.data.saved = true;

        WallInfoDbHelper dbHelper = new WallInfoDbHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int route_ID;

        ContentValues values = new ContentValues();
        values.put(WallInformationContract.RouteEntry.COLUMN_NAME_WALL_NAME, app.data.current_wall);
        values.put(WallInformationContract.RouteEntry.COLUMN_NAME_USER, app.data.current_user);
        values.put(WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_NAME, route_name);
        values.put(WallInformationContract.RouteEntry.COLUMN_NAME_RATING, rating);
        values.put(WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_TYPE, route_type);
        route_ID = (int) db.insert(WallInformationContract.RouteEntry.TABLE_NAME, null, values);
        values.clear();

        Iterator<Map.Entry<Integer, float[]>> it = selected_holds.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, float[]> pair = (Map.Entry<Integer, float[]>)it.next();
            values.put(WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_HOLD_ID, (Integer) pair.getKey());
            values.put(WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_ROUTE_ID, route_ID);
            values.put(WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_COLOR, WallInfoDbHelper.coordinatesToSQLString((float[]) pair.getValue()));
            db.insert(WallInformationContract.HoldRouteJoinTable.TABLE_NAME, null, values);
            values.clear();
            previous_holds.add(pair.getKey());
        }

        it = deleted_holds.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, float[]> pair = it.next();
            String selection = WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_HOLD_ID + " = ? AND " + WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_ROUTE_ID + " = ?";
            String[] selectionArgs = { Integer.toString((Integer) pair.getKey()), Integer.toString(route_ID) };
            db.delete(WallInformationContract.HoldRouteJoinTable.TABLE_NAME, selection, selectionArgs);
            previous_holds.remove(pair.getKey());
        }

        deleted_holds.clear();
    }

    @Override
    public void onBackPressed() {
        if (!((ClimbOnApplication) getApplication()).data.saved) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure you want exit? All unsaved changes will be lost")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            RouteViewThreeDee.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            assert true;
                        }
                    });
            builder.create().show();
        } else {
            super.onBackPressed();
        }
    }
}