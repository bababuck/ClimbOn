package com.example.climbon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class ChooseWallActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("ChooseWall","Entering ChooseWallActivity...");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_wall);

        UniversalData saved_data;
        ClimbOnApplication app = (ClimbOnApplication) getApplication();
        saved_data = app.data;

        LinearLayout main = findViewById(R.id.LL);

        // Params for adding EditTexts to the linear layout rows
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        ArrayList<String> wall_names = loadWallNames(app.db);
        for (String wall_name : wall_names) {
            Button current_button = new Button(this);
            current_button.setBackgroundColor(Color.GREEN);
            current_button.setText(wall_name);
            current_button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Log.e("ChooseWall","Wall by name of '" + wall_name + "' selected...");
                    saved_data.current_wall = wall_name;
                    Intent intent = new Intent(view.getContext(), MainMenu.class);
                    app.loadWall(wall_name);
                    Log.e("ChooseWall","Entering MainMenu activity...");
                    view.getContext().startActivity(intent);
                }
            });
            current_button.setTextColor(Color.BLACK);
            main.addView(current_button, params);
        }

        EditText input = new EditText(this);
        input.setHint("New Wall Name");
        main.addView(input, params);

        Button current_button = new Button(this);
        current_button.setBackgroundColor(Color.GREEN);
        current_button.setText("New Wall");
        current_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                // TODO: Make sure name is not reused
                String new_name = input.getText().toString();
                if (!wall_names.contains(new_name) && !new_name.isEmpty()) {
                    Log.e("ChooseWall", "New wall to be created by the name of '" + new_name + "'...");
                    saved_data.current_wall = new_name;
                    Intent intent = new Intent(view.getContext(), CreateWallActivity.class);
                    intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    Log.e("ChooseWall", "Entering CreateWallActivity activity...");
                    view.getContext().startActivity(intent);
                }
            }
        });
        current_button.setTextColor(Color.BLACK);
        main.addView(current_button, params);
    }

    private ArrayList<String> loadWallNames(SQLiteDatabase db) {
        String[] projection = {
                WallInformationContract.WallEntry.COLUMN_NAME_WALL_NAME
        };
        Cursor cursor = db.query(
                WallInformationContract.WallEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        ArrayList<String> return_list = new ArrayList<>();
        int wall_column_index = cursor.getColumnIndex(WallInformationContract.WallEntry.COLUMN_NAME_WALL_NAME);
        while (cursor.moveToNext()) {
            return_list.add(cursor.getString(wall_column_index));
        }
        cursor.close();
        return return_list;
    }
}