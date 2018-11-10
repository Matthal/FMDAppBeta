package com.fao.fmd.fmdappbeta;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;


public class VehicleTrackFragment extends Fragment {

    View view;

    public VehicleTrackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vehicle_track, container, false);

        final int farm = getArguments().getInt("id");

        final EditText date = view.findViewById(R.id.date);
        final EditText notes = view.findViewById(R.id.note);

        RadioGroup rg = view.findViewById(R.id.check);
        final String[] radio = new String[1];
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.yes_check:
                        radio[0] = " (animals)";
                        break;
                    case R.id.no_chck:
                        radio[0] = " (not animals)";
                        break;
                }
            }
        });

        final String[] items = new String[]{"private car", "milk tanker", "feed truck"};

        final Spinner category = view.findViewById(R.id.category);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        Button done = view.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper mDbHelper = new DatabaseHelper(getActivity());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Tracings.TracingEntry.COLUMN_FARM, farm);
                values.put(Tracings.TracingEntry.COLUMN_CATEGORY, "Vehicle" + radio[0]);
                values.put(Tracings.TracingEntry.COLUMN_SUB_CATEGORY, category.getSelectedItem().toString());
                values.put(Tracings.TracingEntry.COLUMN_DATE, date.getText().toString());
                values.put(Tracings.TracingEntry.COLUMN_NOTES, notes.getText().toString());

                long newRowId = db.insert(Tracings.TracingEntry.TABLE_NAME, null, values);

                if(newRowId == -1){
                    Toast.makeText(getActivity(), "Error in the DB",
                            Toast.LENGTH_LONG).show();
                    db.close();
                }else {
                    Toast.makeText(getActivity(), "New entry added to the DB",
                            Toast.LENGTH_LONG).show();
                    db.close();
                }
            }
        });

        return view;
    }


}