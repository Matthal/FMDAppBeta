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
import android.widget.Spinner;
import android.widget.Toast;

public class AnimalTrackFragment extends Fragment {

    View view;

    public AnimalTrackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_animal_track, container, false);

        final int farm = getArguments().getInt("id");

        final EditText date = view.findViewById(R.id.date);
        final EditText notes = view.findViewById(R.id.note);

        String[] items = new String[]{"goat", "cow", "bull"};

        final Spinner species = view.findViewById(R.id.species);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        species.setAdapter(adapter);

        Button done = view.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper mDbHelper = new DatabaseHelper(getActivity());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Tracings.TracingEntry.COLUMN_FARM, farm);
                values.put(Tracings.TracingEntry.COLUMN_CATEGORY, "Animal Movements");
                values.put(Tracings.TracingEntry.COLUMN_SUB_CATEGORY, species.getSelectedItem().toString());
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
