package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PostLesion extends Activity {

    private static final int CAMERA_PIC_REQUEST = 1;
    private String mCurrentPhotoPath;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_lesion);

        bundle = getIntent().getExtras();

        int farm = getFarm(bundle.getInt("id"));
        bundle.putInt("farm",farm);

        TextView light = findViewById(R.id.light);
        TextView dark = findViewById(R.id.dark);
        LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.90f
        );
        LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.10f
        );
        light.setLayoutParams(paramLight);
        dark.setLayoutParams(paramDark);

        Button addAnimal = findViewById(R.id.animal);
        Button addTracing = findViewById(R.id.tracing);
        Button takePhoto = findViewById(R.id.photo);
        Button timeline = findViewById(R.id.timeline);

        ImageView next = findViewById(R.id.next);
        ImageView back = findViewById(R.id.back);

        addAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostLesion.this, AnimalCreation.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        addTracing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putInt("id", farm);
                Intent intent = new Intent(PostLesion.this, Tracing.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File f = null;
                try {
                    f = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri photoURI = FileProvider.getUriForFile(PostLesion.this, getBaseContext().getApplicationContext().getPackageName() + ".provider", f);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });
        timeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostLesion.this, Timeline.class);
                if(bundle.isEmpty()){
                    Toast.makeText(getBaseContext(), "Problem showing timeline", Toast.LENGTH_LONG).show();
                    return;
                }else{
                    intent.putExtras(bundle);
                }
                startActivity(intent);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostLesion.this, MainActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode != 0) {
            /*DatabaseHelper mDbHelper = new DatabaseHelper(PostLesion.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ContentValues cv = new ContentValues();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
            cv.put(Photo.PhotoEntry.COLUMN_PHOTO, out.toByteArray());
            cv.put(Photo.PhotoEntry.COLUMN_LESION, getLesion());
            long newRowId = db.insert(Photo.PhotoEntry.TABLE_NAME, null, cv);
            if(newRowId == -1){
                Toast.makeText(getBaseContext(), "Error in the DB",
                        Toast.LENGTH_LONG).show();
                db.close();
            }else {
                Toast.makeText(getBaseContext(), "Photo added to the DB",
                        Toast.LENGTH_LONG).show();
                db.close();
            }*/
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

    public int getFarm(int animal){
        String selectQuery = "SELECT * FROM " + Animal.AnimalEntry.TABLE_NAME + " WHERE id=" + animal;
        DatabaseHelper mDbHelper = new DatabaseHelper(PostLesion.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int farm = cursor.getInt(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_FARM));
        cursor.close();
        db.close();
        return farm;
    }

    public int getLesion(){
        String selectQuery = "SELECT * FROM " + Lesion.LesionEntry.TABLE_NAME;
        DatabaseHelper mDbHelper = new DatabaseHelper(PostLesion.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToLast();
        int lesion = cursor.getInt(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_ID));
        cursor.close();
        db.close();
        return lesion;
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

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
