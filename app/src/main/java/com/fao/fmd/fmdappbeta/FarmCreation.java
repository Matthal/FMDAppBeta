package com.fao.fmd.fmdappbeta;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
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
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_creation);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        final String date = df.format(c);

        ImageView cFarm = findViewById(R.id.createFarm);

        final EditText name = findViewById(R.id.vetName);
        final EditText owner = findViewById(R.id.owner);
        final EditText farm = findViewById(R.id.farmName);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!name.getText().toString().trim().isEmpty()) {
                    name.setBackgroundResource(R.color.colorPrimary);
                } else {
                    name.setBackgroundResource(R.color.TLyellow);
                }
            }
        });
        owner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(name.getText().toString().trim().isEmpty()){
                    name.setBackgroundResource(R.color.TLyellow);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!owner.getText().toString().trim().isEmpty()) {
                    owner.setBackgroundResource(R.color.colorPrimary);
                } else {
                    owner.setBackgroundResource(R.color.TLyellow);
                }
            }
        });
        farm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(owner.getText().toString().trim().isEmpty()){
                    owner.setBackgroundResource(R.color.TLyellow);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!farm.getText().toString().trim().isEmpty()) {
                    farm.setBackgroundResource(R.color.colorPrimary);
                } else {
                    farm.setBackgroundResource(R.color.TLyellow);
                }
            }
        });

        final Spinner spinner = findViewById(R.id.country);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, CountryDetails.country);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TextView light = findViewById(R.id.light);
        TextView dark = findViewById(R.id.dark);
        LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.30f
        );
        LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.70f
        );
        light.setLayoutParams(paramLight);
        dark.setLayoutParams(paramDark);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                System.out.println("location not null");
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }else{
                System.out.println("location null");
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }else{
            Toast.makeText(getBaseContext(), "Please, enable GPS",
                    Toast.LENGTH_LONG).show();
        }
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

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

                if (newRowId == -1) {
                    Toast.makeText(getBaseContext(), "Error in the DB",
                            Toast.LENGTH_LONG).show();
                    db.close();
                } else {
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

        Switch locker = findViewById(R.id.locker);
        locker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    name.setEnabled(false);
                    owner.setEnabled(false);
                    farm.setEnabled(false);
                    spinner.setEnabled(false);
                }else{
                    name.setEnabled(true);
                    owner.setEnabled(true);
                    farm.setEnabled(true);
                    spinner.setEnabled(true);
                }
            }
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            System.out.println(longitude);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            Toast.makeText(getBaseContext(), "GPS enabled",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Toast.makeText(getBaseContext(), "Please, enable GPS",
                    Toast.LENGTH_LONG).show();
        }
    };

    private BroadcastReceiver mGpsSwitchStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                finish();
                startActivity(getIntent());
            }
        }
    };


}
