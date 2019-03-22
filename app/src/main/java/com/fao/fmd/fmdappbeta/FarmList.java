package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FarmList extends Activity implements AdapterView.OnItemSelectedListener {

    AnimalListAdapter listAdapter;
    ExpandableListView animalList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    Button addAnimal;
    Button addTrace;
    Button farmTL;
    Button del;
    ImageView map;

    boolean lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_list);

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

        final Spinner spinner = findViewById(R.id.farmSpinner);
        animalList = findViewById(R.id.animalList);

        addAnimal = findViewById(R.id.newAnimal);
        addTrace = findViewById(R.id.newTrace);
        farmTL = findViewById(R.id.farmTL);
        map = findViewById(R.id.map);
        del = findViewById(R.id.del);

        addAnimal.setOnClickListener(v -> {
            int farm = spinner.getSelectedItemPosition();
            Intent intent = new Intent(FarmList.this, AnimalCreation.class);
            Bundle mBundle = new Bundle();
            mBundle.putInt("id",farm);
            intent.putExtras(mBundle);
            startActivity(intent);
        });
        addTrace.setOnClickListener(v -> {
            if(lock){
                Toast.makeText(FarmList.this, "You must add an animal in the farm before adding tracings", Toast.LENGTH_LONG).show();
            }else{
                int farm = spinner.getSelectedItemPosition();
                Intent intent = new Intent(FarmList.this, Tracing.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",farm);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        farmTL.setOnClickListener(v -> {
            if(lock){
                Toast.makeText(FarmList.this, "You must add an animal in the farm before view timeline", Toast.LENGTH_LONG).show();
            }else{
                List<Integer> animals = getAnimals(spinner.getSelectedItemPosition());
                List<Integer> lesions = getLesions(animals);
                if(lesions.isEmpty()){
                    Toast.makeText(FarmList.this, "There is no lesion related to the animals in the farms", Toast.LENGTH_LONG).show();
                    return;
                }
                int id = spinner.getSelectedItemPosition();
                Intent intent = new Intent(FarmList.this, Timeline.class);
                Bundle bundle = new Bundle();
                bundle.putInt("farm",id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        map.setOnClickListener(v -> {
            String selectQuery = "SELECT * FROM " + Farm.FarmEntry.TABLE_NAME + " WHERE id=" + spinner.getSelectedItemPosition();
            DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToFirst();
            double latitude = cursor.getDouble(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_LATITUDE));
            double longitude = cursor.getDouble(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_LONGITUDE));
            Uri uri = Uri.parse("https://www.google.com/maps/search/?api=1&query="+ latitude + "," + longitude);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            cursor.close();
            db.close();
        });

        final List<String> farmList = getAllFarms();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, farmList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        del = findViewById(R.id.del);
        del.setOnClickListener(v -> {
            int id = spinner.getSelectedItemPosition();
            DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            db.delete(Farm.FarmEntry.TABLE_NAME,Farm.FarmEntry.COLUMN_ID + "='" + id + "'",null);
            Toast.makeText(FarmList.this, "Farm deleted, refresh page", Toast.LENGTH_SHORT).show();
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        ImageView info = findViewById(R.id.qmark);
        info.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("tag","list");
            Intent intent = new Intent(FarmList.this, InfoPage.class);
            intent.putExtras(b);
            startActivity(intent);
        });

    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String farmName = (String) parent.getItemAtPosition(pos);

        if(farmName.equals("Select a farm...")){
            animalList.setVisibility(View.INVISIBLE);
            addAnimal.setVisibility(View.INVISIBLE);
            addTrace.setVisibility(View.INVISIBLE);
            farmTL.setVisibility(View.INVISIBLE);
            del.setVisibility(View.INVISIBLE);
            map.setVisibility(View.INVISIBLE);
        }else{
            animalList.setVisibility(View.VISIBLE);
            addAnimal.setVisibility(View.VISIBLE);
            addTrace.setVisibility(View.VISIBLE);
            farmTL.setVisibility(View.VISIBLE);
            del.setVisibility(View.VISIBLE);
            //map.setVisibility(View.VISIBLE);

            // preparing list data
            prepareListData(pos);

            listAdapter = new AnimalListAdapter(this, listDataHeader, listDataChild);

            // setting list adapter
            animalList.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    private void prepareListData(int pos) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        String selectQuery = "SELECT * FROM " + Animal.AnimalEntry.TABLE_NAME + " WHERE farm=" + pos;
        DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++){
            List<String> details = new ArrayList<>();
            listDataHeader.add("Animal " + cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_ID)));
            details.add("Name: " + cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_NAME)) + "\n" +
                    "Group ID: " + cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_GROUP)) +  "\n" +
                    "Species: " + cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_SPECIES)) + "\n" +
                    "Age: " + cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_AGE)));
            listDataChild.put(listDataHeader.get(i), details);
            cursor.moveToNext();
        }

        if(listDataHeader.isEmpty()){
            lock = true;
        }else{
            lock = false;
        }

        cursor.close();
        db.close();

    }

    public int getFarmCount() {
        String countQuery = "SELECT  * FROM " + Farm.FarmEntry.TABLE_NAME;
        DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public List<String> getAllFarms() {
        List<String> farms = new ArrayList<>();
        farms.add(0,"Select a farm...");

        // Select All Query
        String selectQuery = "SELECT * FROM " + Farm.FarmEntry.TABLE_NAME;

        DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String farm = cursor.getString(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_NAME));
                farms.add(farm);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return farms;
    }

    public List<Integer> getAnimals(int id) {
        List<Integer> animals = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Animal.AnimalEntry.TABLE_NAME + " WHERE farm=" + id;

        DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int animal = cursor.getInt(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_ID));
                animals.add(animal);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return animals;
    }

    public List<Integer> getLesions(List<Integer> animals) {
        List<Integer> lesions = new ArrayList<>();

        for(int i = 0; i < animals.size(); i++){
            String selectQuery = "SELECT * FROM " + Lesion.LesionEntry.TABLE_NAME + " WHERE animal=" + animals.get(i);

            DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    int lesion = cursor.getInt(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_ID));
                    lesions.add(lesion);
                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();
        }

        return lesions;
    }
}
