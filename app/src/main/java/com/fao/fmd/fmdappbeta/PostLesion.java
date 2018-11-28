package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PostLesion extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_lesion);

        final Bundle bundle = getIntent().getExtras();

        Button addAnimal = findViewById(R.id.animal);
        Button addTracing = findViewById(R.id.tracing);
        Button viewTimeline = findViewById(R.id.tlBtn);

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
                int farm = getFarm(bundle.getInt("id"));
                Bundle b = new Bundle();
                b.putInt("id", farm);
                Intent intent = new Intent(PostLesion.this, Tracing.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
        viewTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostLesion.this, Timeline.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
}
