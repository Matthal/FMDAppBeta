package com.fao.fmd.fmdappbeta;

public class Animal {

    private Animal() {
    }

    /* Inner class that defines the table contents */
    public static class AnimalEntry {
        public static final String TABLE_NAME = "animals";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FARM = "farm";
        public static final String COLUMN_GROUP = "group_id";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_BREED = "breed";
        public static final String COLUMN_REPORT = "report";
        public static final String COLUMN_VACCINATED = "vaccinated";
    }

    public static final String CREATE_ANIMAL_TABLE =
            "CREATE TABLE " + AnimalEntry.TABLE_NAME + " (" +
                    AnimalEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AnimalEntry.COLUMN_NAME + " TEXT," +
                    AnimalEntry.COLUMN_FARM + " INTEGER," +
                    AnimalEntry.COLUMN_GROUP + " INTEGER," +
                    AnimalEntry.COLUMN_AGE + " TEXT," +
                    AnimalEntry.COLUMN_BREED + " TEXT," +
                    AnimalEntry.COLUMN_REPORT + " TEXT," +
                    AnimalEntry.COLUMN_VACCINATED + " INTEGER)";

    public static final String DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AnimalEntry.TABLE_NAME;

}
