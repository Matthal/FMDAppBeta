package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FarmList extends Activity implements AdapterView.OnItemSelectedListener {

    ListView animalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_list);
        Spinner spinner = findViewById(R.id.farmSpinner);
        animalList = findViewById(R.id.animalList);

        List<String> Array = new ArrayList<>();
        Array.add("farm 1");
        Array.add("farm 2");
        Array.add("farm 3");

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,Array);
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


        if(farmName.equals("farm 2")){
            List<String> Array = new ArrayList<>();
            Array.add("animal 1");
            Array.add("animal 2");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getBaseContext(),
                    android.R.layout.simple_list_item_1, Array);
            animalList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }



}
