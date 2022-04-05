package com.example.climbon;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class WallInfoDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WallInfo.db";

    public WallInfoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(WallInformationContract.WallEntry.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(WallInformationContract.WallPanels.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(WallInformationContract.WallHolds.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(WallInformationContract.HoldRouteJoinTable.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(WallInformationContract.RouteEntry.SQL_CREATE_TABLE);

        addFakeData(sqLiteDatabase);
    }

    private void addFakeData(SQLiteDatabase sqLiteDatabase) {
        addFakeWallNames(sqLiteDatabase);
        addFakeWall(sqLiteDatabase);
        addFakeRoutes(sqLiteDatabase);
    }

    private void addFakeRoutes(SQLiteDatabase sqLiteDatabase) {
        int rowID;
        ContentValues values = new ContentValues();
        String[] route_names = {"Easy Route", "Hard Route", "Really Hard Route"};
        for (int i=0;i<route_names.length;++i) {
            values.put(WallInformationContract.RouteEntry.COLUMN_NAME_WALL_NAME, "Big Wall");
            values.put(WallInformationContract.RouteEntry.COLUMN_NAME_USER, "bababuck");
            values.put(WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_NAME, route_names[i]);
            values.put(WallInformationContract.RouteEntry.COLUMN_NAME_RATING, 3);
            values.put(WallInformationContract.RouteEntry.COLUMN_NAME_ROUTE_TYPE, i);
            rowID = (int) sqLiteDatabase.insert(WallInformationContract.RouteEntry.TABLE_NAME, null, values);
            values.clear();
            addFakeRouteInfo(sqLiteDatabase, rowID);
        }
    }

    private void addFakeRouteInfo(SQLiteDatabase sqLiteDatabase, int rowID) {
        int prev = -1;
        int randomNum = prev;
        ContentValues values = new ContentValues();
        for (int i=0;i<2;++i) {
            while (randomNum == prev)
                randomNum = ThreadLocalRandom.current().nextInt(0, 5);
            prev = randomNum;

            values.put(WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_HOLD_ID, hold_ids.get(randomNum));
            values.put(WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_ROUTE_ID, rowID);
            values.put(WallInformationContract.HoldRouteJoinTable.COLUMN_NAME_COLOR, coordinatesToSQLString(new float[] {1.0f, 1.0f, 1.0f, 1.0f}));
            sqLiteDatabase.insert(WallInformationContract.HoldRouteJoinTable.TABLE_NAME, null, values);
            values.clear();
        }
    }

    private void addFakeWall(SQLiteDatabase sqLiteDatabase) {
        String wall_name = "Big Wall";
        ContentValues values = new ContentValues();
        int rowID;
        float[] hold_coordinates[];

        float panels[] = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f,  1.0f, 0.0f,
                0.0f, 0.0f, 1.0f
        };
        float color[] = new float[]{0.0f, 1.0f, 1.0f, 1.0f};
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_WALL_NAME, wall_name);
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_COORDINATES, coordinatesToSQLString(panels));
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_COLOR, coordinatesToSQLString(color));
        rowID = (int) sqLiteDatabase.insert(WallInformationContract.WallPanels.TABLE_NAME, null, values);
        values.clear();
        
        hold_coordinates = new float[2][];
        hold_coordinates[0] = new float[]{
                0.0f, 0.5f, 0.0f,
                0.0f,  0.75f, 0.0f,
                0.0f, 0.0f, 0.25f
        };
        hold_coordinates[1] = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f,  0.25f, 0.0f,
                0.0f, 0.0f, 0.25f
        };
        addFakeHolds(sqLiteDatabase, rowID, hold_coordinates);

        panels = new float[]{
                0.0f, 0.0f, 1.0f,
                0.0f,  -1.0f, 0.0f,
                0.0f,  0.0f, 0.0f
        };
        color = new float[]{0.0f, 0.0f, 1.0f, 1.0f};
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_WALL_NAME, wall_name);
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_COORDINATES, coordinatesToSQLString(panels));
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_COLOR, coordinatesToSQLString(color));
        rowID = (int) sqLiteDatabase.insert(WallInformationContract.WallPanels.TABLE_NAME, null, values);
        values.clear();

        hold_coordinates[0] = new float[]{
                0.0f, -0.5f, 0.0f,
                0.0f,  -0.75f, 0.0f,
                0.0f, 0.0f, 0.25f
        };
        hold_coordinates[1] = new float[]{
                0.0f, 0.0f, 0.0f,
                0.0f,  -0.25f, 0.0f,
                0.0f, 0.0f, 0.25f
        };
        addFakeHolds(sqLiteDatabase, rowID, hold_coordinates);

        panels = new float[]{
                0.0f, 1.0f, 0.0f,
                0.0f, -1.0f, 0.0f,
                0.0f, -1.0f, -1.0f,
                0.0f, 1.0f, -1.0f,

        };
        color = new float[]{1.0f, 1.0f, 0.2f, 1.0f};
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_WALL_NAME, wall_name);
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_COORDINATES, coordinatesToSQLString(panels));
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_COLOR, coordinatesToSQLString(color));
        rowID = (int) sqLiteDatabase.insert(WallInformationContract.WallPanels.TABLE_NAME, null, values);
        values.clear();

        hold_coordinates = new float[1][];
        hold_coordinates[0] = new float[]{
                0.0f, 0.5f, 0.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, -0.5f
        };
        addFakeHolds(sqLiteDatabase, rowID, hold_coordinates);

        panels = new float[] {
                10.0f, 10.0f, -10.0f,
                10.0f, -10.0f, -10.0f,
                -10.0f, -10.0f, -10.0f,
                -10.0f, 10.0f, -10.0f,

        };
        color = new float[]{0.2f, 1.0f, 0.2f, 1.0f};
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_WALL_NAME, wall_name);
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_COORDINATES, coordinatesToSQLString(panels));
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_COLOR, coordinatesToSQLString(color));
        rowID = (int) sqLiteDatabase.insert(WallInformationContract.WallPanels.TABLE_NAME, null, values);
        values.clear();

        panels = new float[]{
                0.0f, 1.0f, -1.0f,
                0.0f, -1.0f, -1.0f,
                3.0f, -1.0f, -4.0f,
                3.0f, 1.0f, -4.0f,

        };
        color = new float[]{1.0f, 1.0f, 0.2f, 1.0f};
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_WALL_NAME, wall_name);
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_COORDINATES, coordinatesToSQLString(panels));
        values.put(WallInformationContract.WallPanels.COLUMN_NAME_COLOR, coordinatesToSQLString(color));
        rowID = (int) sqLiteDatabase.insert(WallInformationContract.WallPanels.TABLE_NAME, null, values);
    }

    private ArrayList<Integer> hold_ids = new ArrayList<>();

    private void addFakeHolds(SQLiteDatabase sqLiteDatabase, int rowID, float[][] hold_coordinates) {
        int rowID_hold;
        ContentValues values = new ContentValues();
        for (int i=0;i<hold_coordinates.length;++i) {
            values.put(WallInformationContract.WallHolds.COLUMN_NAME_PANEL_NUMBER, rowID);
            values.put(WallInformationContract.WallPanels.COLUMN_NAME_COORDINATES, coordinatesToSQLString(hold_coordinates[i]));
            values.put(WallInformationContract.WallPanels.COLUMN_NAME_COLOR, coordinatesToSQLString(new float[]{1.0f, 1.0f, 1.0f, 1.0f}));
            rowID_hold = (int) sqLiteDatabase.insert(WallInformationContract.WallPanels.TABLE_NAME, null, values);
            values.clear();
            hold_ids.add(rowID_hold);
        }
    }


    private static final String LIST_SEPARATOR = "__,__";

    public static String coordinatesToSQLString(float coordinates[]) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<coordinates.length;++i) {
            stringBuilder.append(Float.toString(coordinates[i])).append(LIST_SEPARATOR);
        }

        stringBuilder.setLength(stringBuilder.length() - LIST_SEPARATOR.length());

        return stringBuilder.toString();
    }

    public static float[] convertStringToList(String str) {
        String str_values[] = str.split(LIST_SEPARATOR);
        float flt_values[] = new float[str_values.length];
        for (int i=0;i<str_values.length;++i) {
            flt_values[i] = Float.parseFloat(str_values[i]);
        }
        return flt_values;
    }

    private void addFakeWallNames(SQLiteDatabase sqLiteDatabase) {
        String wall_name = "Big Wall";
        String user = "bababuck";
        ContentValues values = new ContentValues();
        values.put(WallInformationContract.WallEntry.COLUMN_NAME_WALL_NAME, wall_name);
        values.put(WallInformationContract.WallEntry.COLUMN_NAME_USER, user);
        sqLiteDatabase.insert(WallInformationContract.WallEntry.TABLE_NAME, null, values);

        values.clear();

        wall_name = "Fake Wall";
        values.put(WallInformationContract.WallEntry.COLUMN_NAME_WALL_NAME, wall_name);
        values.put(WallInformationContract.WallEntry.COLUMN_NAME_USER, user);
        sqLiteDatabase.insert(WallInformationContract.WallEntry.TABLE_NAME, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        assert (1 == 1);
    }
}
