package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FarmList extends Activity implements AdapterView.OnItemSelectedListener {

    AnimalListAdapter listAdapter;
    ExpandableListView animalList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_list);
        Spinner spinner = findViewById(R.id.farmSpinner);
        animalList = findViewById(R.id.animalList);

        List<String> farmList = new ArrayList<>();
        farmList.add("farm 1");
        farmList.add("farm 2");
        farmList.add("farm 3");

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


        if(farmName.equals("farm 2")){
            // preparing list data
            prepareListData();

            listAdapter = new AnimalListAdapter(this, listDataHeader, listDataChild);

            // setting list adapter
            animalList.setAdapter(listAdapter);
            listAdapter.notifyDataSetChanged();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        String[] ID = {"201","202","203"};

        listDataHeader.add("Animal 1");
        listDataHeader.add("Animal 2");
        listDataHeader.add("Animal 3");

        for (int i = 0; i < ID.length; i++){
            List<String> top250 = new ArrayList<String>();
            // Adding child data ;
            top250.add("ID : " + ID[i] + "\n" +  "Name : " + "Boar");
            listDataChild.put(listDataHeader.get(i), top250);
        }

    }



}
