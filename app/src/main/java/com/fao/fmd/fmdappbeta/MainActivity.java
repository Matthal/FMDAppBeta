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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;

public class MainActivity extends Activity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        /*FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        DatabaseReference scoresRef = FirebaseDatabase.getInstance().getReference("farms");
        scoresRef.keepSynced(true);*/

        Button cFarm = findViewById(R.id.newFarm);
        Button lFarm = findViewById(R.id.listFarm);
        Button dBtn = findViewById(R.id.diagn);

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

        dBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper mDbHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                System.out.println(DatabaseHelper.getTableAsString(db, "animals"));
                //Intent intent = new Intent(MainActivity.this, DiagnosticsOverview.class);
                //startActivity(intent);
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

                final String[] onlineID = {Integer.toString(id)};
                mDatabase = FirebaseDatabase.getInstance().getReference("farms");
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if(snapshot.child(onlineID[0]).exists()){
                                onlineID[0] = "2";
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("DBError", "Failed to read value.", error.toException());
                    }
                };
                mDatabase.addListenerForSingleValueEvent(valueEventListener);
                //mDatabase.child("id").setValue(id);
                mDatabase.child(onlineID[0]).child("vet_name").setValue(vet);
                mDatabase.child(onlineID[0]).child("owner_name").setValue(owner);
                mDatabase.child(onlineID[0]).child("date").setValue(date);
                mDatabase.child(onlineID[0]).child("longitude").setValue(lon);
                mDatabase.child(onlineID[0]).child("latitude").setValue(lat);
                mDatabase.child(onlineID[0]).child("country").setValue(country);
                mDatabase.child(onlineID[0]).child("village_name").setValue(name);

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
