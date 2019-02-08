package com.fao.fmd.fmdappbeta;

public final class Farm {

    private Farm() {
    }

    /* Inner class that defines the table contents */
    public static class FarmEntry {
        public static final String TABLE_NAME = "farms";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_INVESTIGATOR = "investigator_name";
        public static final String COLUMN_INTERVIEWEE = "interviewee_name";
        public static final String COLUMN_POSITION = "interviewee_position";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_NAME = "farm_name";
        public static final String COLUMN_ADDRESS = "farm_address";
    }

    public static final String CREATE_FARM_TABLE =
            "CREATE TABLE " + FarmEntry.TABLE_NAME + " (" +
                    FarmEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FarmEntry.COLUMN_INVESTIGATOR + " TEXT," +
                    FarmEntry.COLUMN_INTERVIEWEE + " TEXT," +
                    FarmEntry.COLUMN_POSITION + " TEXT," +
                    FarmEntry.COLUMN_DATE + " TEXT," +
                    FarmEntry.COLUMN_LONGITUDE + " REAL," +
                    FarmEntry.COLUMN_LATITUDE + " REAL," +
                    FarmEntry.COLUMN_COUNTRY + " TEXT," +
                    FarmEntry.COLUMN_NAME + " TEXT," +
                    FarmEntry.COLUMN_ADDRESS + " TEXT)";

    public static final String DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FarmEntry.TABLE_NAME;
}
