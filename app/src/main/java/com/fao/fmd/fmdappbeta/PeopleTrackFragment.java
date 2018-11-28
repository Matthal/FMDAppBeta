package com.fao.fmd.fmdappbeta;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class PeopleTrackFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    View view;
    Date dayZero;
    Spinner category;
    EditText other;

    public PeopleTrackFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_people_track, container, false);

        final int farm = getArguments().getInt("id");

        List<Integer> animals = getAnimals(farm);
        List<Integer> lesions = getLesions(animals);
        try {
            dayZero = getDayZero(lesions);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final Calendar calendar = Calendar.getInstance();
        final EditText date = view.findViewById(R.id.date);
        final DatePickerDialog.OnDateSetListener datepick = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                date.setText(sdf.format(calendar.getTime()));
            }

        };
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(getActivity(), datepick, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(subDays(dayZero,21).getTime());
                dialog.show();
            }
        });

        final EditText notes = view.findViewById(R.id.note);
        other = view.findViewById(R.id.other);

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

        String[] items = new String[]{"Family", "Vet", "Nutritionist", "Other"};

        category = view.findViewById(R.id.category);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);
        category.setOnItemSelectedListener(this);

        Button done = view.findViewById(R.id.done);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String catStr;
                if(category.getVisibility() == View.VISIBLE){
                    catStr = category.getSelectedItem().toString();
                }else{
                    catStr = other.getText().toString();
                }

                DatabaseHelper mDbHelper = new DatabaseHelper(getActivity());
                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Tracings.TracingEntry.COLUMN_FARM, farm);
                values.put(Tracings.TracingEntry.COLUMN_CATEGORY, "People" + radio[0]);
                values.put(Tracings.TracingEntry.COLUMN_SUB_CATEGORY, catStr);
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
                    Intent intent = new Intent(getActivity(), Tracing.class);
                    Bundle b = new Bundle();
                    b.putInt("id",farm);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    public List<Integer> getAnimals(int id) {
        List<Integer> animals = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Animal.AnimalEntry.TABLE_NAME + " WHERE farm=" + id;

        DatabaseHelper mDbHelper = new DatabaseHelper(getActivity());
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

            DatabaseHelper mDbHelper = new DatabaseHelper(getActivity());
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

    public Date getDayZero(List<Integer> lesions) throws ParseException {
        List<Date> dates = new ArrayList<>();

        for(int i = 0; i < lesions.size(); i++){
            String selectQuery = "SELECT * FROM " + Lesion.LesionEntry.TABLE_NAME + " WHERE id=" + lesions.get(i);

            DatabaseHelper mDbHelper = new DatabaseHelper(getActivity());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            int old;

            if (cursor.moveToFirst()) {
                do {
                    String age = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_AGE));
                    String diagnosis = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_SPR_MAX));
                    if(age.charAt(1) == '-'){
                        if(age.length() == 3){
                            old = Character.getNumericValue(age.charAt(2));
                        }else{
                            old = Integer.parseInt(age.substring(2,3));
                        }
                    }else{
                        if(age.charAt(2) == '-'){
                            old = Integer.parseInt(age.substring(3,4));
                        }else{
                            old = Integer.parseInt(age.substring(0,1));
                        }
                    }
                    Date date = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(diagnosis);
                    Date diagnDate = subDays(date,old);
                    dates.add(diagnDate);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }

        return Collections.max(dates);
    }

    public Date subDays(Date date, int days){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(parent.getItemAtPosition(pos) == "Other"){
            category.setVisibility(View.INVISIBLE);
            other.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

}
