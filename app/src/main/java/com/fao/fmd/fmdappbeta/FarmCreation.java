package com.fao.fmd.fmdappbeta;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FarmCreation extends Activity {

    //private DatabaseReference mDatabase;
    double longitude;
    double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_creation);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        final String date = df.format(c);

        Button cFarm = findViewById(R.id.createFarm);

        final EditText name = findViewById(R.id.vetName);
        final EditText owner = findViewById(R.id.owner);
        final EditText farm = findViewById(R.id.farmName);

        final Spinner spinner = findViewById(R.id.country);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CountryDetails.country);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            longitude = 0.0;
            latitude = 0.0;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        cFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*mDatabase = FirebaseDatabase.getInstance().getReference("farms");
                String farmId = mDatabase.push().getKey();
                Farm farm = new Farm(name.getText().toString(), Integer.parseInt(animals.getText().toString()));
                mDatabase.child(farmId).setValue(farm);

                mDatabase = FirebaseDatabase.getInstance().getReference("animals");
                String farmId = mDatabase.push().getKey();
                Animal farm = new Animal("boar","-LP0QEUlxfiIIHWybnXY");
                mDatabase.child(farmId).setValue(farm);

                Intent intent = new Intent(FarmCreation.this, AnimalCreation.class);
                startActivity(intent);*/

                //PUT VALUES INTO DB
                DatabaseHelper mDbHelper = new DatabaseHelper(FarmCreation.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Farm.FarmEntry.COLUMN_VET, name.getText().toString());
                values.put(Farm.FarmEntry.COLUMN_OWNER, owner.getText().toString());
                values.put(Farm.FarmEntry.COLUMN_DATE, date);
                values.put(Farm.FarmEntry.COLUMN_LONGITUDE, longitude);
                values.put(Farm.FarmEntry.COLUMN_LATITUDE, latitude);
                values.put(Farm.FarmEntry.COLUMN_COUNTRY, spinner.getSelectedItem().toString());
                values.put(Farm.FarmEntry.COLUMN_NAME, farm.getText().toString());

                long newRowId = db.insert(Farm.FarmEntry.TABLE_NAME, null, values);

                if(newRowId == -1){
                    Toast.makeText(getBaseContext(), "Error in the DB",
                            Toast.LENGTH_LONG).show();
                    db.close();
                }else{
                    Toast.makeText(getBaseContext(), "New entry added to the DB",
                            Toast.LENGTH_LONG).show();
                    String selectQuery = "SELECT  * FROM " + Farm.FarmEntry.TABLE_NAME;
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    cursor.moveToLast();
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    cursor.close();
                    db.close();
                    Intent mIntent = new Intent(FarmCreation.this, AnimalCreation.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("id", id);
                    mIntent.putExtras(mBundle);
                    startActivity(mIntent);
                }

            }
        });

    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(getBaseContext(), "Please, enable GPS",
                    Toast.LENGTH_LONG).show();
        }
    };
}
