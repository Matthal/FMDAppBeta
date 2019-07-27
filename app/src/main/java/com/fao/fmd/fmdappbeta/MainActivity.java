package com.fao.fmd.fmdappbeta;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    String response;

    String syncDateKey = "com.fao.fmd.fmdappbeta.syncdate";

    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = this.getSharedPreferences("com.fao.fmd.fmdappbeta", Context.MODE_PRIVATE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Button cFarm = findViewById(R.id.newFarm);
        Button lFarm = findViewById(R.id.listFarm);
        Button dBtn = findViewById(R.id.diagn);
        Button bio = findViewById(R.id.bio);

        TextView syncDate = findViewById(R.id.syncDate);
        syncDate.setText(prefs.getString(syncDateKey,"Click to Sync DB"));

        //getApplicationContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);

        cFarm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FarmCreation.class);
            startActivity(intent);
        });

        lFarm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FarmList.class);
            startActivity(intent);
        });

        bio.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("tag","bio");
            Intent intent = new Intent(MainActivity.this, InfoPage.class);
            intent.putExtras(b);
            startActivity(intent);
            /*DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            getApplicationContext().deleteDatabase(DatabaseHelper.DATABASE_NAME);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure to delete the DB?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();*/
        });

        dBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DiagnosticsOverview.class);
            startActivity(intent);
        });

        ImageView sync = findViewById(R.id.sync);
        sync.setOnClickListener(v -> {
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enter your Name/ID");

            final EditText input = new EditText(this);

            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dial, int which) {
                    username = input.getText().toString();
                    new UploadFarms().execute();
                    new UploadAnimals().execute();
                    new UploadLesions().execute();
                    new UploadTracings().execute();
                    Date currentTime = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.UK);
                    prefs.edit().putString(syncDateKey, "Last update:\n" + df.format(currentTime)).apply();
                    syncDate.setText(prefs.getString(syncDateKey,"Click to Sync DB"));
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();

        });

        ImageView info = findViewById(R.id.qmark);
        info.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("tag","main");
            Intent intent = new Intent(MainActivity.this, InfoPage.class);
            intent.putExtras(b);
            startActivity(intent);
        });
    }

    public class UploadFarms extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://fmddoi.altervista.org/database/add_farm.php");

                DatabaseHelper mDbHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                Cursor cursor  = db.rawQuery("SELECT * FROM farms", null);
                if (cursor.moveToFirst() ) {
                    do {
                        final int id = cursor.getInt(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_ID));
                        String investigator = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_INVESTIGATOR));
                        String interviewee = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_INTERVIEWEE));
                        String position = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_POSITION));
                        String date = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_DATE));
                        Long lon = cursor.getLong(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_LONGITUDE));
                        Long lat = cursor.getLong(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_LATITUDE));
                        String country = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_COUNTRY));
                        String name = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_NAME));
                        String address = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_ADDRESS));

                        JSONObject postDataParams = new JSONObject();
                        postDataParams.put("user",username);
                        postDataParams.put("id", id);
                        postDataParams.put("investigator_name", investigator);
                        postDataParams.put("interviewee_name", interviewee);
                        postDataParams.put("interviewee_position", position);
                        postDataParams.put("date", date);
                        postDataParams.put("longitude", lon);
                        postDataParams.put("latitude", lat);
                        postDataParams.put("country", country);
                        postDataParams.put("farm_name", name);
                        postDataParams.put("farm_address", address);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(15000);
                        conn.setConnectTimeout(15000);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);

                        OutputStream os = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(getPostDataString(postDataParams));

                        writer.flush();
                        writer.close();
                        os.close();

                        int responseCode=conn.getResponseCode();

                        if (responseCode == HttpsURLConnection.HTTP_OK) {

                            BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                            StringBuffer sb = new StringBuffer("");
                            String line="";

                            while((line = in.readLine()) != null) {
                                sb.append(line);
                            }

                            in.close();
                            response = sb.toString();

                        }
                        else {
                            response = "false : "+responseCode;
                        }

                    }while (cursor.moveToNext());
                }
                cursor.close();
                db.close();

            }catch(Exception e){
                response = "Exception: " + e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public class UploadAnimals extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://fmddoi.altervista.org/database/add_animal.php");

                DatabaseHelper mDbHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                Cursor cursor  = db.rawQuery("SELECT * FROM animals", null);
                if (cursor.moveToFirst() ) {
                    do {
                        final int id = cursor.getInt(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_ID));
                        String name = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_NAME));
                        int farm = cursor.getInt(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_FARM));
                        String group = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_GROUP));
                        String age = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_AGE));
                        String species = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_SPECIES));
                        String report = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_REPORT));
                        String vaccination = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_VACCINATION));

                        JSONObject postDataParams = new JSONObject();
                        postDataParams.put("user",username);
                        postDataParams.put("id", id);
                        postDataParams.put("name",name);
                        postDataParams.put("farm", farm);
                        postDataParams.put("animal_group", group);
                        postDataParams.put("age", age);
                        postDataParams.put("species", species);
                        postDataParams.put("report", report);
                        postDataParams.put("vaccination_status", vaccination);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(15000);
                        conn.setConnectTimeout(15000);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);

                        OutputStream os = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(getPostDataString(postDataParams));

                        writer.flush();
                        writer.close();
                        os.close();

                        int responseCode=conn.getResponseCode();

                        if (responseCode == HttpsURLConnection.HTTP_OK) {

                            BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                            StringBuffer sb = new StringBuffer("");
                            String line="";

                            while((line = in.readLine()) != null) {
                                sb.append(line);
                            }

                            in.close();
                            response = sb.toString();

                        }
                        else {
                            response = "false : "+responseCode;
                        }

                    }while (cursor.moveToNext());
                }
                cursor.close();
                db.close();

            }catch(Exception e){
                response = "Exception: " + e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public class UploadLesions extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://fmddoi.altervista.org/database/add_lesion.php");

                DatabaseHelper mDbHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                Cursor cursor  = db.rawQuery("SELECT * FROM lesions", null);
                if (cursor.moveToFirst() ) {
                    do {
                        final int id = cursor.getInt(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_ID));
                        int animal = cursor.getInt(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_ANIMAL));
                        String age = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_AGE));
                        String date = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_VISIT_DATE));
                        String like_inf_min = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_INF_MIN));
                        String like_inf_max = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_INF_MAX));
                        String poss_inf_min = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_INF_MIN));
                        String poss_inf_max = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_INF_MAX));
                        String like_spr_min = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_SPR_MIN));
                        String like_spr_max = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_SPR_MAX));
                        String poss_spr_min = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_SPR_MIN));
                        String poss_spr_max = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_SPR_MAX));

                        JSONObject postDataParams = new JSONObject();
                        postDataParams.put("user",username);
                        postDataParams.put("id", id);
                        postDataParams.put("animal",animal);
                        postDataParams.put("age_of_lesion", age);
                        postDataParams.put("date_of_visit", date);
                        postDataParams.put("likely_infection_min", like_inf_min);
                        postDataParams.put("likely_infection_max", like_inf_max);
                        postDataParams.put("possible_infection_min", poss_inf_min);
                        postDataParams.put("possible_infection_max", poss_inf_max);
                        postDataParams.put("likely_spread_min", like_spr_min);
                        postDataParams.put("likely_spread_max", like_spr_max);
                        postDataParams.put("possible_spread_min", poss_spr_min);
                        postDataParams.put("possible_spread_max", poss_spr_max);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(15000);
                        conn.setConnectTimeout(15000);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);

                        OutputStream os = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(getPostDataString(postDataParams));

                        writer.flush();
                        writer.close();
                        os.close();

                        int responseCode=conn.getResponseCode();

                        if (responseCode == HttpsURLConnection.HTTP_OK) {

                            BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                            StringBuffer sb = new StringBuffer("");
                            String line="";

                            while((line = in.readLine()) != null) {
                                sb.append(line);
                            }

                            in.close();
                            response = sb.toString();

                        }
                        else {
                            response = "false : "+responseCode;
                        }

                    }while (cursor.moveToNext());
                }
                cursor.close();
                db.close();

            }catch(Exception e){
                response = "Exception: " + e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public class UploadTracings extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://fmddoi.altervista.org/database/add_tracing.php");

                DatabaseHelper mDbHelper = new DatabaseHelper(MainActivity.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                Cursor cursor  = db.rawQuery("SELECT * FROM tracings", null);
                if (cursor.moveToFirst() ) {
                    do {
                        final int id = cursor.getInt(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_ID));
                        int farm = cursor.getInt(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_FARM));
                        String category = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_CATEGORY));
                        String sub = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_SUB_CATEGORY));
                        String date = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_DATE));
                        String notes = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_NOTES));

                        JSONObject postDataParams = new JSONObject();
                        postDataParams.put("user",username);
                        postDataParams.put("id", id);
                        postDataParams.put("category", category);
                        postDataParams.put("sub_category", sub);
                        postDataParams.put("date", date);
                        postDataParams.put("notes", notes);
                        postDataParams.put("farm", farm);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(15000);
                        conn.setConnectTimeout(15000);
                        conn.setRequestMethod("POST");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);

                        OutputStream os = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                        writer.write(getPostDataString(postDataParams));

                        writer.flush();
                        writer.close();
                        os.close();

                        int responseCode=conn.getResponseCode();

                        if (responseCode == HttpsURLConnection.HTTP_OK) {

                            BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));

                            StringBuffer sb = new StringBuffer("");
                            String line="";

                            while((line = in.readLine()) != null) {
                                sb.append(line);
                            }

                            in.close();
                            response = sb.toString();

                        }
                        else {
                            response = "false : "+responseCode;
                        }

                    }while (cursor.moveToNext());
                }
                cursor.close();
                db.close();

            }catch(Exception e){
                response = "Exception: " + e.getMessage();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

}
