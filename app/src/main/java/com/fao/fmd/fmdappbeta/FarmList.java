package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
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

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_list);
        Spinner spinner = findViewById(R.id.farmSpinner);
        animalList = findViewById(R.id.animalList);

        Button addAnimal = findViewById(R.id.newAnimal);
        Button addTrace = findViewById(R.id.newTrace);
        Button farmTL = findViewById(R.id.farmTL);

        addAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FarmList.this, AnimalCreation.class);
                startActivity(intent);
            }
        });
        addTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Tracings in development",
                        Toast.LENGTH_SHORT).show();
            }
        });
        farmTL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Timeline in development",
                        Toast.LENGTH_SHORT).show();
            }
        });

        final List<String> farmList = new ArrayList<>();
        farmList.add(0,"Select a farm...");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference farmsRef = mDatabase.child("farms");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Farm farm = snapshot.getValue(Farm.class);
                    farmList.add(farm.name);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("DBError", "Failed to read value.", error.toException());
            }
        };
        farmsRef.addListenerForSingleValueEvent(valueEventListener);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,farmList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
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
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        final List<String> details = new ArrayList<String>();

        mDatabase = FirebaseDatabase.getInstance().getReference();
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
        }


    }



}
