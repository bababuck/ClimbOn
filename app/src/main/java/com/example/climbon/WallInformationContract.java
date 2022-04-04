package com.example.climbon;

import android.provider.BaseColumns;

public class WallInformationContract {
    private WallInformationContract(){}

    public static final String DROP_TABLE_TEXT = "DROP TABLE IF EXISTS ";
    public static final String CREATE_TABLE_TEXT = "CREATE TABLE ";

    public static class WallEntry implements BaseColumns {
        public static final String TABLE_NAME = "walls";
        public static final String COLUMN_NAME_WALL_NAME = "wall_name";
        public static final String COLUMN_NAME_USER = "user";

        public static final String SQL_CREATE_TABLE =
                CREATE_TABLE_TEXT + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_WALL_NAME + " TEXT," +
                        COLUMN_NAME_USER + " TEXT)";

        public static final String SQL_DELETE_TABLE =
                DROP_TABLE_TEXT + TABLE_NAME;
    }

    public static class WallPanels implements BaseColumns {
        public static final String TABLE_NAME = "panels";
        public static final String COLUMN_NAME_WALL_NAME = "wall_name";
        public static final String COLUMN_NAME_PANEL_NUMBER = "panel_number";
        public static final String COLUMN_NAME_COORDINATES = "coordinates";
        public static final String COLUMN_NAME_COLOR = "color";

        public static final String SQL_CREATE_TABLE =
                CREATE_TABLE_TEXT + TABLE_NAME + " (" +
                        COLUMN_NAME_PANEL_NUMBER + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_WALL_NAME + " TEXT," +
                        COLUMN_NAME_COORDINATES + " TEXT," +
                        COLUMN_NAME_COLOR + " TEXT)";

        public static final String SQL_DELETE_TABLE =
                DROP_TABLE_TEXT + TABLE_NAME;
    }

    public static class WallHolds implements BaseColumns {
        public static final String TABLE_NAME = "holds";
        public static final String COLUMN_NAME_PANEL_NUMBER = "panel_number";
        public static final String COLUMN_NAME_HOLD_NUMBER = "hold_number";
        public static final String COLUMN_NAME_COORDINATES = "coordinates";
        public static final String COLUMN_NAME_COLOR = "color";

        public static final String SQL_CREATE_TABLE =
                CREATE_TABLE_TEXT + TABLE_NAME + " (" +
                        COLUMN_NAME_HOLD_NUMBER + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_PANEL_NUMBER + " INTEGER," +
                        COLUMN_NAME_COORDINATES + " TEXT," +
                        COLUMN_NAME_COLOR + " TEXT)";

        public static final String SQL_DELETE_TABLE =
                DROP_TABLE_TEXT + TABLE_NAME;
    }

    public static class HoldRouteJoinTable implements BaseColumns {
        public static final String TABLE_NAME = "hold_on";
        public static final String COLUMN_NAME_ROUTE_ID = "route_id";
        public static final String COLUMN_NAME_HOLD_ID = "hold_id";
        public static final String COLUMN_NAME_COLOR = "color";

        public static final String SQL_CREATE_TABLE =
                CREATE_TABLE_TEXT + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_COLOR + " TEXT," +
                        COLUMN_NAME_ROUTE_ID + " INTEGER REFERENCES" + RouteEntry.TABLE_NAME + ","+
                        COLUMN_NAME_HOLD_ID + " INTEGER REFERENCES" + WallHolds.TABLE_NAME + ")";


        public static final String SQL_DELETE_TABLE =
                DROP_TABLE_TEXT + TABLE_NAME;
    }

    public static class RouteEntry implements BaseColumns {
        public static final String TABLE_NAME = "routes";
        public static final String COLUMN_NAME_WALL_NAME = "wall_name";
        public static final String COLUMN_NAME_USER = "user";
        public static final String COLUMN_NAME_ROUTE_NAME = "route_name";
        public static final String COLUMN_NAME_ROUTE_TYPE = "route_type";
        public static final String COLUMN_NAME_RATING = "hold_number";

        public static final String SQL_CREATE_TABLE =
                CREATE_TABLE_TEXT + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY," +
                        COLUMN_NAME_WALL_NAME + " TEXT," +
                        COLUMN_NAME_USER + " TEXT," +
                        COLUMN_NAME_ROUTE_NAME + " TEXT," +
                        COLUMN_NAME_ROUTE_TYPE + " INTEGER," +
                        COLUMN_NAME_RATING + " INTEGER)";

        public static final String SQL_DELETE_TABLE =
                DROP_TABLE_TEXT + TABLE_NAME;
    }
}
