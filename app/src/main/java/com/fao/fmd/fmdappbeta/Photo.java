package com.fao.fmd.fmdappbeta;

public class Photo {

    private Photo() {
    }

    /* Inner class that defines the table contents */
    public static class PhotoEntry {
        public static final String TABLE_NAME = "photos";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_PHOTO = "photo";
        public static final String COLUMN_LESION = "lesion";
    }

    public static final String CREATE_PHOTO_TABLE =
            "CREATE TABLE " + PhotoEntry.TABLE_NAME + " (" +
                    PhotoEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    PhotoEntry.COLUMN_PHOTO + " BLOB," +
                    PhotoEntry.COLUMN_LESION + " INTEGER," +
                    " FOREIGN KEY ("+PhotoEntry.COLUMN_LESION+") REFERENCES "+Lesion.LesionEntry.TABLE_NAME +"("+Lesion.LesionEntry.COLUMN_ID +"));";

    public static final String DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PhotoEntry.TABLE_NAME;
}
