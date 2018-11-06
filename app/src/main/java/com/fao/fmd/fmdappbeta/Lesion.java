package com.fao.fmd.fmdappbeta;

public class Lesion {

    private Lesion() {
    }

    /* Inner class that defines the table contents */
    public static class LesionEntry {
        public static final String TABLE_NAME = "lesions";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ANIMAL = "animal";
        public static final String COLUMN_AGE = "age_of_lesion";
        public static final String COLUMN_LIKE_INF_MIN = "likely_infection_min";
        public static final String COLUMN_LIKE_INF_MAX = "likely_infection_max";
        public static final String COLUMN_POSS_INF_MIN = "possible_infection_min";
        public static final String COLUMN_POSS_INF_MAX = "possible_infection_max";
        public static final String COLUMN_LIKE_SPR_MIN = "likely_spread_min";
        public static final String COLUMN_LIKE_SPR_MAX = "likely_spread_max";
        public static final String COLUMN_POSS_SPR_MIN = "possible_spread_min";
        public static final String COLUMN_POSS_SPR_MAX = "possible_spread_max";
    }

    public static final String CREATE_LESION_TABLE =
            "CREATE TABLE " + LesionEntry.TABLE_NAME + " (" +
                    LesionEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    LesionEntry.COLUMN_ANIMAL + " INTEGER," +
                    LesionEntry.COLUMN_AGE + " TEXT," +
                    LesionEntry.COLUMN_LIKE_INF_MIN + " TEXT," +
                    LesionEntry.COLUMN_LIKE_INF_MAX + " TEXT," +
                    LesionEntry.COLUMN_POSS_INF_MIN + " TEXT," +
                    LesionEntry.COLUMN_POSS_INF_MAX + " TEXT," +
                    LesionEntry.COLUMN_LIKE_SPR_MIN + " TEXT," +
                    LesionEntry.COLUMN_LIKE_SPR_MAX + " TEXT," +
                    LesionEntry.COLUMN_POSS_SPR_MIN + " TEXT," +
                    LesionEntry.COLUMN_POSS_SPR_MAX + " TEXT)";

    public static final String DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + LesionEntry.TABLE_NAME;
}
