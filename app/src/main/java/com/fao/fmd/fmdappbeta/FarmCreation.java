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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FarmCreation extends Activity implements AdapterView.OnItemSelectedListener {

    LocationManager mLocationManager;

    double longitude;
    double latitude;
    LocationManager lm;

    Spinner position;
    EditText other;
    Button close;

    boolean lock = false;

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
        final EditText address = findViewById(R.id.address);

        position = findViewById(R.id.position);
        final Spinner spinner = findViewById(R.id.country);

        other = findViewById(R.id.other);
        close = findViewById(R.id.close);
        close.setOnClickListener(v -> {
            position.setSelection(0);
            position.setVisibility(View.VISIBLE);
            other.setVisibility(View.INVISIBLE);
            close.setVisibility(View.INVISIBLE);
        });

        final String[] pos = new String[]{"Owner", "Farm Manager", "Farm Worker", "Other"};
        ArrayAdapter<String> posAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, pos);
        posAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        position.setAdapter(posAdapter);
        position.setOnItemSelectedListener(this);

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
            Location location = getLastKnownLocation();
            if (location != null) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            } else {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        } else {
            Toast.makeText(getBaseContext(), "Please, enable GPS",
                    Toast.LENGTH_LONG).show();
        }
        registerReceiver(mGpsSwitchStateReceiver, new IntentFilter(LocationManager.PROVIDERS_CHANGED_ACTION));

        Button gps = findViewById(R.id.gps);
        TextView text_gps = findViewById(R.id.text_gps);
        String lat = String.format(Locale.UK,"%.2f", latitude);
        String lon = String.format(Locale.UK,"%.2f", longitude);
        gps.setOnClickListener(v -> {
            text_gps.setText("Lat: " + lat + "\nLong: " + lon);
            text_gps.setVisibility(View.VISIBLE);
        });

        Switch locker = findViewById(R.id.locker);
        locker.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!name.getText().toString().trim().isEmpty()) {
                    name.setBackgroundResource(R.color.colorPrimary);
                    lock = false;
                } else {
                    name.setBackgroundResource(R.color.TLyellow);
                    lock = true;
                }
                if (!owner.getText().toString().trim().isEmpty()) {
                    owner.setBackgroundResource(R.color.colorPrimary);
                    lock = false;
                } else {
                    owner.setBackgroundResource(R.color.TLyellow);
                    lock = true;
                }
                position.setBackgroundResource(R.color.colorPrimary);
                if (!other.getText().toString().trim().isEmpty()) {
                    other.setBackgroundResource(R.color.colorPrimary);
                    lock = false;
                } else {
                    other.setBackgroundResource(R.color.TLyellow);
                    lock = true;
                }
                if (!farm.getText().toString().trim().isEmpty()) {
                    farm.setBackgroundResource(R.color.colorPrimary);
                    lock = false;
                } else {
                    farm.setBackgroundResource(R.color.TLyellow);
                    lock = true;
                }
                if (!address.getText().toString().trim().isEmpty()) {
                    address.setBackgroundResource(R.color.colorPrimary);
                    lock = false;
                } else {
                    address.setBackgroundResource(R.color.TLyellow);
                    lock = true;
                }
                spinner.setBackgroundResource(R.color.colorPrimary);
                name.setEnabled(false);
                owner.setEnabled(false);
                position.setEnabled(false);
                farm.setEnabled(false);
                address.setEnabled(false);
                spinner.setEnabled(false);
            } else {
                name.setBackgroundResource(R.color.white);
                owner.setBackgroundResource(R.color.white);
                position.setBackgroundResource(R.color.white);
                other.setBackgroundResource(R.color.white);
                farm.setBackgroundResource(R.color.white);
                address.setBackgroundResource(R.color.white);
                spinner.setBackgroundResource(R.color.white);
                name.setEnabled(true);
                owner.setEnabled(true);
                position.setEnabled(true);
                farm.setEnabled(true);
                address.setEnabled(true);
                spinner.setEnabled(true);
            }
        });

        cFarm.setOnClickListener(v -> {

            if (!locker.isChecked()) {
                Toast.makeText(getBaseContext(), "Lock information to proceed", Toast.LENGTH_LONG).show();
                return;
            }

            if (locker.isChecked() && lock) {
                Toast.makeText(getBaseContext(), "You need to fill all the information", Toast.LENGTH_LONG).show();
                return;
            }

            String role;
            if(position.getVisibility() == View.VISIBLE){
                role = position.getSelectedItem().toString();
            }else{
                role = other.getText().toString();
            }

            //PUT VALUES INTO DB
            DatabaseHelper mDbHelper = new DatabaseHelper(FarmCreation.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Farm.FarmEntry.COLUMN_INVESTIGATOR, name.getText().toString());
            values.put(Farm.FarmEntry.COLUMN_INTERVIEWEE, owner.getText().toString());
            values.put(Farm.FarmEntry.COLUMN_POSITION, role);
            values.put(Farm.FarmEntry.COLUMN_DATE, date);
            values.put(Farm.FarmEntry.COLUMN_LONGITUDE, longitude);
            values.put(Farm.FarmEntry.COLUMN_LATITUDE, latitude);
            values.put(Farm.FarmEntry.COLUMN_COUNTRY, spinner.getSelectedItem().toString());
            values.put(Farm.FarmEntry.COLUMN_NAME, farm.getText().toString());
            values.put(Farm.FarmEntry.COLUMN_ADDRESS, address.getText().toString());

            long newRowId = db.insert(Farm.FarmEntry.TABLE_NAME, null, values);

            if (newRowId == -1) {
                Toast.makeText(getBaseContext(), "Error in the DB", Toast.LENGTH_LONG).show();
                db.close();
            } else {
                Toast.makeText(getBaseContext(), "New entry added to the DB", Toast.LENGTH_SHORT).show();
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

        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            //System.out.println(longitude);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (parent.getItemAtPosition(pos) == "Other") {
            position.setVisibility(View.INVISIBLE);
            other.setVisibility(View.VISIBLE);
            close.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

}
