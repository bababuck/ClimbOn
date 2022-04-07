package com.example.climbon;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

public class ClimbOnApplication extends Application {
    /* Holds global data for storage.

    TODO: Add null checks when gathering data since can be deleted
    - Try/catch statements
    */
    public UniversalData data = new UniversalData();
    public SQLiteDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        WallInfoDbHelper dbHelper = new WallInfoDbHelper(this);
        db = dbHelper.getReadableDatabase();
    }
}