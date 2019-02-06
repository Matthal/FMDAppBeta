package com.fao.fmd.fmdappbeta;

public class Tracings {

    private Tracings() {
    }

    /* Inner class that defines the table contents */
    public static class TracingEntry {
        public static final String TABLE_NAME = "tracings";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_FARM = "farm";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_SUB_CATEGORY = "sub_category";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_NOTES = "notes";
    }

    public static final String CREATE_TRACINGS_TABLE =
            "CREATE TABLE " + TracingEntry.TABLE_NAME + " (" +
                    TracingEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TracingEntry.COLUMN_FARM + " INTEGER," +
                    TracingEntry.COLUMN_CATEGORY + " TEXT," +
                    TracingEntry.COLUMN_SUB_CATEGORY + " TEXT," +
                    TracingEntry.COLUMN_DATE + " TEXT," +
                    TracingEntry.COLUMN_NOTES + " TEXT," +
                    " FOREIGN KEY ("+TracingEntry.COLUMN_FARM+") REFERENCES "+Farm.FarmEntry.TABLE_NAME +"("+Farm.FarmEntry.COLUMN_ID +") ON DELETE CASCADE)";

    public static final String DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TracingEntry.TABLE_NAME;
}
