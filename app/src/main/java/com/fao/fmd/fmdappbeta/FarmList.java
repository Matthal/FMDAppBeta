package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
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
    ImageButton edit;

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
        del = findViewById(R.id.del);

        addAnimal.setOnClickListener(v -> {
            String farmName = (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
            String selectID = "SELECT * FROM " + Farm.FarmEntry.TABLE_NAME + " WHERE farm_name='" + farmName + "'";

            DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            Cursor cursorID = db.rawQuery(selectID, null);
            cursorID.moveToFirst();
            int id = cursorID.getInt(cursorID.getColumnIndex(Farm.FarmEntry.COLUMN_ID));
            cursorID.close();
            Intent intent = new Intent(FarmList.this, AnimalCreation.class);
            Bundle mBundle = new Bundle();
            mBundle.putInt("id",id);
            intent.putExtras(mBundle);
            startActivity(intent);
        });
        addTrace.setOnClickListener(v -> {
            if(lock){
                Toast.makeText(FarmList.this, "You must add an animal in the farm before adding tracings", Toast.LENGTH_LONG).show();
            }else{
                String farmName = (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
                String selectID = "SELECT * FROM " + Farm.FarmEntry.TABLE_NAME + " WHERE farm_name='" + farmName + "'";

                DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                Cursor cursorID = db.rawQuery(selectID, null);
                cursorID.moveToFirst();
                int id = cursorID.getInt(cursorID.getColumnIndex(Farm.FarmEntry.COLUMN_ID));
                cursorID.close();
                Intent intent = new Intent(FarmList.this, TracingOptions.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        farmTL.setOnClickListener(v -> {
            if(lock){
                Toast.makeText(FarmList.this, "You must add an animal in the farm before view timeline", Toast.LENGTH_LONG).show();
            }else{
                String farmName = (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
                String selectID = "SELECT * FROM " + Farm.FarmEntry.TABLE_NAME + " WHERE farm_name='" + farmName + "'";

                DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                Cursor cursorID = db.rawQuery(selectID, null);
                cursorID.moveToFirst();
                int id = cursorID.getInt(cursorID.getColumnIndex(Farm.FarmEntry.COLUMN_ID));
                cursorID.close();
                List<Integer> animals = getAnimals(id);
                List<Integer> lesions = getLesions(animals);
                if(lesions.isEmpty()){
                    Toast.makeText(FarmList.this, "There is no lesion related to the animals in the farms", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(FarmList.this, Timeline.class);
                Bundle bundle = new Bundle();
                bundle.putInt("farm",id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        final List<String> farmList = getAllFarms();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, farmList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        edit = findViewById(R.id.edit);
        edit.setOnClickListener(v -> {
            String farmName = (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
            String selectID = "SELECT * FROM " + Farm.FarmEntry.TABLE_NAME + " WHERE farm_name='" + farmName + "'";

            DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            Cursor cursorID = db.rawQuery(selectID, null);
            cursorID.moveToFirst();
            int id = cursorID.getInt(cursorID.getColumnIndex(Farm.FarmEntry.COLUMN_ID));
            cursorID.close();

            Bundle b = new Bundle();
            b.putInt("id", id);
            Intent editIntent = new Intent(FarmList.this, EditFarm.class);
            editIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            editIntent.putExtras(b);
            startActivity(editIntent);
        });

        del = findViewById(R.id.del);
        del.setOnClickListener(v -> {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            String farmName = (String) spinner.getItemAtPosition(spinner.getSelectedItemPosition());
                            DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
                            SQLiteDatabase db = mDbHelper.getWritableDatabase();
                            db.delete(Farm.FarmEntry.TABLE_NAME,Farm.FarmEntry.COLUMN_NAME + "='" + farmName + "'",null);
                            Toast.makeText(FarmList.this, "Farm deleted", Toast.LENGTH_SHORT).show();
                            //adapter.remove(farmName);
                            Intent delIntent = new Intent(FarmList.this, MainActivity.class);
                            startActivity(delIntent);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(FarmList.this);
            builder.setMessage("Are you sure to delete this farm?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent intent = new Intent(FarmList.this, MainActivity.class);
            startActivity(intent);
        });

        //QMARK ICON REMOVED FOR NO SPACE
        /*
        ImageView info = findViewById(R.id.qmark);
        info.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("tag","list");
            Intent intent = new Intent(FarmList.this, InfoPage.class);
            intent.putExtras(b);
            startActivity(intent);
        });*/

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
            edit.setVisibility(View.INVISIBLE);
            del.setVisibility(View.INVISIBLE);
        }else{
            animalList.setVisibility(View.VISIBLE);
            addAnimal.setVisibility(View.VISIBLE);
            addTrace.setVisibility(View.VISIBLE);
            farmTL.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
            del.setVisibility(View.VISIBLE);

            // preparing list data
            prepareListData(farmName);

            listAdapter = new AnimalListAdapter(this, listDataHeader, listDataChild);

            // setting list adapter
            animalList.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        }

    }

    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    private void prepareListData(String farmName) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        String selectID = "SELECT * FROM " + Farm.FarmEntry.TABLE_NAME + " WHERE farm_name='" + farmName + "'";

        DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursorID = db.rawQuery(selectID, null);
        cursorID.moveToFirst();
        int id = cursorID.getInt(cursorID.getColumnIndex(Farm.FarmEntry.COLUMN_ID));
        cursorID.close();

        String selectQuery = "SELECT * FROM " + Animal.AnimalEntry.TABLE_NAME + " WHERE farm=" + id;
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

    //SUPPORT METHOD
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
