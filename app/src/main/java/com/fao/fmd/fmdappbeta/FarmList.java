package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_list);
        final Spinner spinner = findViewById(R.id.farmSpinner);
        animalList = findViewById(R.id.animalList);

        addAnimal = findViewById(R.id.newAnimal);
        addTrace = findViewById(R.id.newTrace);
        farmTL = findViewById(R.id.farmTL);

        addAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = spinner.getSelectedItemPosition();
                Intent intent = new Intent(FarmList.this, AnimalCreation.class);
                Bundle mBundle = new Bundle();
                mBundle.putInt("id",id);
                intent.putExtras(mBundle);
                startActivity(intent);
            }
        });
        addTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = spinner.getSelectedItemPosition();
                Intent intent = new Intent(FarmList.this, Tracing.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        farmTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Timeline in development",
                        Toast.LENGTH_SHORT).show();
            }
        });

        final List<String> farmList = getAllFarms();

        /*mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference farmsRef = mDatabase.child("farms");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Farm farm = snapshot.getValue(Farm.class);
                    farmList.add("placeholder");//WRONG
                    //A ROW MISSED
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DBError", "Failed to read value.", error.toException());
            }
        };
        farmsRef.addListenerForSingleValueEvent(valueEventListener);*/

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,farmList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        String farmName = (String) parent.getItemAtPosition(pos);

        if(farmName.equals("Select a farm...")){
            animalList.setVisibility(View.INVISIBLE);
        }else{
            animalList.setVisibility(View.VISIBLE);
            addAnimal.setVisibility(View.VISIBLE);
            addTrace.setVisibility(View.VISIBLE);
            farmTL.setVisibility(View.VISIBLE);

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
        final List<String> details = new ArrayList<String>();

        String selectQuery = "SELECT * FROM " + Animal.AnimalEntry.TABLE_NAME + " WHERE farm=" + pos;
        DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++){
            listDataHeader.add("Animal " + cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_ID)));
            details.add("Group ID : " + cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_GROUP)) +  "\n" +  "Breed : " + cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_BREED)));
            listDataChild.put(listDataHeader.get(0), details);
        }

        db.close();
        cursor.close();

        /*mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference farmsRef = mDatabase.child("animals");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Animal animal = snapshot.getValue(Animal.class);
                    listDataHeader.add(animal.name);
                    details.add("ID : " + snapshot.getKey() +  "\n" +  "Name : " + animal.type);
                    listDataChild.put(listDataHeader.get(0), details);
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DBError", "Failed to read value.", error.toException());
            }
        };
        farmsRef.addListenerForSingleValueEvent(valueEventListener);

        for (int i = 0; i < 10; i++){
            listDataHeader.add("Animal " + Integer.toString(i+1));
            listDataChild.put(listDataHeader.get(i), details);
        }*/


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
        String selectQuery = "SELECT  * FROM " + Farm.FarmEntry.TABLE_NAME;

        DatabaseHelper mDbHelper = new DatabaseHelper(FarmList.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String farm = "Farm" + cursor.getInt(cursor.getColumnIndex(Farm.FarmEntry.COLUMN_ID));
                farms.add(farm);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return farms;
    }



}
