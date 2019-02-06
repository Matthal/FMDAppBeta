package com.fao.fmd.fmdappbeta;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class MainActivity extends AppCompatActivity {

    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        //Parse Initialization
        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();

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
                System.out.println(DatabaseHelper.getTableAsString(db, "tracings"));
                /*Intent intent = new Intent(MainActivity.this, DiagnosticOptions.class);
                startActivity(intent);*/
                /*Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File f = null;
                try {
                    f = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri photoURI = FileProvider.getUriForFile(MainActivity.this, getBaseContext().getApplicationContext().getPackageName() + ".provider", f);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 1);*/
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
                UploadFarms();
                UploadAnimals();
                UploadLesions();
                UploadTracings();
            }
        });
    }

    public void UploadFarms(){
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

    public void UploadAnimals(){
        DatabaseHelper mDbHelper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor  = db.rawQuery("SELECT * FROM animals", null);
        if (cursor.moveToFirst() ){
            do {
                final int id = cursor.getInt(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_NAME));
                int farm = cursor.getInt(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_FARM));
                String group = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_GROUP));
                String age = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_AGE));
                String breed = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_BREED));
                String report = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_REPORT));
                int vaccinated = cursor.getInt(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_VACCINATED));

                ParseObject entity = new ParseObject("Animals");

                entity.put("ID", id);
                entity.put("Name", name);
                entity.put("Farm", farm);
                entity.put("Group", group);
                entity.put("Age", age);
                entity.put("Breed", breed);
                entity.put("Report", report);
                entity.put("Vaccinated", vaccinated);

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

    public void UploadLesions(){
        DatabaseHelper mDbHelper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor  = db.rawQuery("SELECT * FROM lesions", null);
        if (cursor.moveToFirst() ){
            do {
                final int id = cursor.getInt(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_ID));
                int animal = cursor.getInt(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_ANIMAL));
                String age = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_AGE));
                String like_inf_min = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_INF_MIN));
                String like_inf_max = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_INF_MAX));
                String poss_inf_min = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_INF_MIN));
                String poss_inf_max = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_INF_MAX));
                String like_spr_min = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_SPR_MIN));
                String like_spr_max = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_SPR_MAX));
                String poss_spr_min = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_SPR_MIN));
                String poss_spr_max = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_SPR_MAX));

                ParseObject entity = new ParseObject("Lesions");

                entity.put("ID", id);
                entity.put("Animal", animal);
                entity.put("Age", age);
                entity.put("Likely_Infection_min", like_inf_min);
                entity.put("Likely_Infection_max", like_inf_max);
                entity.put("Possible_Infection_min", poss_inf_min);
                entity.put("Possible_Infection_max", poss_inf_max);
                entity.put("Likely_Spread_min", like_spr_min);
                entity.put("Likely_Spread_max", like_spr_max);
                entity.put("Possible_Spread_min", poss_spr_min);
                entity.put("Possible_Spread_max", poss_spr_max);

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

    public void UploadTracings(){
        DatabaseHelper mDbHelper = new DatabaseHelper(MainActivity.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor  = db.rawQuery("SELECT * FROM tracings", null);
        if (cursor.moveToFirst() ){
            do {
                final int id = cursor.getInt(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_ID));
                int farm = cursor.getInt(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_FARM));
                String category = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_CATEGORY));
                String sub = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_SUB_CATEGORY));
                String date = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_DATE));
                String notes = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_NOTES));

                ParseObject entity = new ParseObject("Tracings");

                entity.put("ID", id);
                entity.put("Farm", farm);
                entity.put("Category", category);
                entity.put("Sub_Category", sub);
                entity.put("Date", date);
                entity.put("Notes", notes);

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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory(),"Pictures/FMD-DOI");
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        System.out.println(mCurrentPhotoPath);
        return image;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode != 0) {
            galleryAddPic();
            Intent intent = new Intent(this, DrawOnBitmapActivity.class);
            String filePath = "file:" + mCurrentPhotoPath;
            intent.putExtra("image", filePath);
            startActivity(intent);
        }else{
            Toast.makeText(getBaseContext(), "Photo cancelled",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
