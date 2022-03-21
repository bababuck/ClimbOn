package com.example.climbon;

import android.provider.BaseColumns;

public class WallInformationContract {
    private WallInformationContract(){}

    public static class WallEntry implements BaseColumns {
        public static final String TABLE_NAME = "walls";
        public static final String COLUMN_NAME_WALL_NAME = "wall_name";
        public static final String COLUMN_NAME_USER = "user";
    }

    public static class WallPanels implements BaseColumns {
        public static final String TABLE_NAME = "panels";
        public static final String COLUMN_NAME_WALL_NAME = "wall_name";
        public static final String COLUMN_NAME_USER = "user";
    }

    public static class RouteEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
