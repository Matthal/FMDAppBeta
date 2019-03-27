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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class EditFarm extends Activity implements AdapterView.OnItemSelectedListener{

    LocationManager mLocationManager;

    double longitude;
    double latitude;
    LocationManager lm;

    Spinner position;
    EditText other;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_farm);

        Bundle bundle = getIntent().getExtras();
        final int id;

        if(bundle != null){
            id = bundle.getInt("id");
        }else{
            id = 0;
        }

        final EditText name = findViewById(R.id.vetName);
        final EditText owner = findViewById(R.id.owner);
        final EditText farm = findViewById(R.id.farmName);
        final EditText address = findViewById(R.id.address);

        name.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        owner.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        farm.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        address.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        position = findViewById(R.id.position);
        final Spinner spinner = findViewById(R.id.country);

        other = findViewById(R.id.other);

        other.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

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
                1.00f
        );
        LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.00f
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
            text_gps.setText(getString(R.string.lat_long, lat, lon));
            text_gps.setVisibility(View.VISIBLE);
        });

        //Retrieve farm information
        String selectQuery = "SELECT * FROM " + Farm.FarmEntry.TABLE_NAME + " WHERE id=" + id;
        DatabaseHelper mDbHelper = new DatabaseHelper(EditFarm.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        name.setText(cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_INVESTIGATOR)));
        owner.setText(cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_INTERVIEWEE)));
        position.setSelection(posAdapter.getPosition(cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_POSITION))));
        farm.setText(cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_NAME)));
        address.setText(cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_ADDRESS)));
        spinner.setSelection(adapter.getPosition(cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_COUNTRY))));

        cursor.close();

        ImageView cFarm = findViewById(R.id.createFarm);
        cFarm.setOnClickListener(v -> {

            String role;
            if(position.getVisibility() == View.VISIBLE){
                role = position.getSelectedItem().toString();
            }else{
                role = other.getText().toString();
            }

            //PUT VALUES INTO DB
            ContentValues values = new ContentValues();
            values.put(Farm.FarmEntry.COLUMN_INVESTIGATOR, name.getText().toString());
            values.put(Farm.FarmEntry.COLUMN_INTERVIEWEE, owner.getText().toString());
            values.put(Farm.FarmEntry.COLUMN_POSITION, role);
            if(latitude != 0 && longitude != 0){
                values.put(Farm.FarmEntry.COLUMN_LONGITUDE, longitude);
                values.put(Farm.FarmEntry.COLUMN_LATITUDE, latitude);
            }
            values.put(Farm.FarmEntry.COLUMN_COUNTRY, spinner.getSelectedItem().toString());
            values.put(Farm.FarmEntry.COLUMN_NAME, farm.getText().toString());
            values.put(Farm.FarmEntry.COLUMN_ADDRESS, address.getText().toString());

            long newRowId = db.update(Farm.FarmEntry.TABLE_NAME, values, "id= ?",new String[]{Integer.toString(id)});

            if (newRowId == -1) {
                Toast.makeText(getBaseContext(), "Error in the DB", Toast.LENGTH_LONG).show();
                db.close();
            } else {
                Toast.makeText(getBaseContext(), "DB updated", Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
                Intent intent = new Intent(EditFarm.this, FarmList.class);
                startActivity(intent);
            }

        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
