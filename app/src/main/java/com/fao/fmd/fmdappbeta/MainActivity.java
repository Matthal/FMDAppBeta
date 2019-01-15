package com.fao.fmd.fmdappbeta;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //Parse Initialization
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        /*FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("farms");
        scoresRef.keepSynced(true);*/

        Button cFarm = findViewById(R.id.newFarm);
        Button lFarm = findViewById(R.id.listFarm);
        Button dBtn = findViewById(R.id.diagn);
        Button bio = findViewById(R.id.bio);

        //getApplicationContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);

        cFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FarmCreation.class);
                startActivity(intent);
            }
        });

        lFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FarmList.class);
                startActivity(intent);
            }
        });

        bio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper mDbHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                System.out.println(DatabaseHelper.getTableAsString(db, "lesions"));
                /*Intent intent = new Intent(MainActivity.this, VesiclesGallery.class);
                startActivity(intent);*/
            }
        });

        dBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DiagnosticsOverview.class);
                startActivity(intent);
            }
        });

        Button sync = findViewById(R.id.sync);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadDB();
            }
        });
    }

    public void UploadDB(){
        DatabaseHelper mDbHelper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor  = db.rawQuery("SELECT * FROM farms", null);
        if (cursor.moveToFirst() ){
            do {
                final int id = cursor.getInt(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_ID));
                String vet = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_VET));
                String owner = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_OWNER));
                String date = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_DATE));
                Long lon = cursor.getLong(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_LONGITUDE));
                Long lat = cursor.getLong(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_LATITUDE));
                String country = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_COUNTRY));
                String name = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_NAME));

                ParseObject entity = new ParseObject("Farms");

                entity.put("ID", id);
                entity.put("Vet_name", vet);
                entity.put("Owner_name", owner);
                entity.put("Date", date);
                entity.put("Longitude", lon);
                entity.put("Latitude", lat);
                entity.put("Country", country);
                entity.put("Farm_name", name);

                // Saves the new object.
                // Notice that the SaveCallback is totally optional!
                entity.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        // Here you can handle errors, if thrown. Otherwise, "e" should be null
                    }
                });

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
