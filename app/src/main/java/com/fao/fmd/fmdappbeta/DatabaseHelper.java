package com.fao.fmd.fmdappbeta;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "fmdapp_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create db tables
        db.execSQL(Farm.CREATE_FARM_TABLE);
        db.execSQL(Animal.CREATE_ANIMAL_TABLE);
        db.execSQL(Lesion.CREATE_LESION_TABLE);
        db.execSQL(Tracings.CREATE_TRACINGS_TABLE);
        db.execSQL(Photo.CREATE_PHOTO_TABLE);

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL(Farm.DELETE_ENTRIES);
        db.execSQL(Animal.DELETE_ENTRIES);
        db.execSQL(Lesion.DELETE_ENTRIES);
        db.execSQL(Tracings.DELETE_ENTRIES);
        db.execSQL(Photo.DELETE_ENTRIES);

        // Create tables again
        onCreate(db);
    }

    //SUPPORT METHOD
    public static String getTableAsString(SQLiteDatabase db, String tableName) {
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }
}
