package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class PostLesion extends Activity {

    private static final int CAMERA_PIC_REQUEST = 1;
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
            DatabaseHelper mDbHelper = new DatabaseHelper(PostLesion.this);
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
            }
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
}
