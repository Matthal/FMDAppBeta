package com.fao.fmd.fmdappbeta;

import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public final class Farm {


    private Farm() {
    }

    /* Inner class that defines the table contents */
    public static class FarmEntry {
        public static final String TABLE_NAME = "farms";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_VET = "vet_name";
        public static final String COLUMN_OWNER = "owner_name";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_COUNTRY = "country";
        public static final String COLUMN_NAME = "village_name";
    }

    public static final String CREATE_FARM_TABLE =
            "CREATE TABLE " + FarmEntry.TABLE_NAME + " (" +
                    FarmEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FarmEntry.COLUMN_VET + " TEXT," +
                    FarmEntry.COLUMN_OWNER + " TEXT," +
                    FarmEntry.COLUMN_DATE + " TEXT," +
                    FarmEntry.COLUMN_LONGITUDE + " REAL," +
                    FarmEntry.COLUMN_LATITUDE + " REAL," +
                    FarmEntry.COLUMN_COUNTRY + " TEXT," +
                    FarmEntry.COLUMN_NAME + " TEXT)";

    public static final String DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FarmEntry.TABLE_NAME;
}
