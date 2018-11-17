package com.fao.fmd.fmdappbeta;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {

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
                System.out.println(DatabaseHelper.getTableAsString(db, "lesions"));
                Toast.makeText(getBaseContext(), "Diagnostics in development",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
