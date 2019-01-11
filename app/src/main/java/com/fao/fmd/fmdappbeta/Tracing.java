package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class Tracing extends Activity implements AdapterView.OnItemSelectedListener{

    private PopupWindow mPopupWindow;

    Date dayZero;
    Spinner spinner;
    EditText other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing);

        final Bundle bundle = getIntent().getExtras();
        final int farm = bundle.getInt("id");

        List<Integer> animals = getAnimals(farm);
        List<Integer> lesions = getLesions(animals);
        try {
            dayZero = getDayZero(lesions);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final RelativeLayout rel = findViewById(R.id.relative);

        Button animalTrack = findViewById(R.id.animalTrack);
        Button productTrack = findViewById(R.id.productTrack);
        Button peopleTrack = findViewById(R.id.peopleTrack);
        Button veichleTrack = findViewById(R.id.veichleTrack);

        animalTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_animal_track,(ViewGroup) findViewById(R.id.animal_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        true
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                final Calendar calendar = Calendar.getInstance();
                final EditText date = customView.findViewById(R.id.date);
                final DatePickerDialog.OnDateSetListener picker = new DatePickerDialog.OnDateSetListener() {
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
                        new DatePickerDialog(Tracing.this, picker, calendar
                                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                String[] items = new String[]{"Cattle", "Sheep", "Goat", "Pig", "Other"};
                spinner = customView.findViewById(R.id.species);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(Tracing.this);

                other = customView.findViewById(R.id.other);

                final EditText notes = customView.findViewById(R.id.note);

                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);
            }
        });

        productTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_product_track,(ViewGroup) findViewById(R.id.product_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        true
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                final Calendar calendar = Calendar.getInstance();
                final EditText date = customView.findViewById(R.id.date);
                final DatePickerDialog.OnDateSetListener picker = new DatePickerDialog.OnDateSetListener() {
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
                        new DatePickerDialog(Tracing.this, picker, calendar
                                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                String[] items = new String[]{"Milk", "Meat", "Feed", "Other"};
                spinner = customView.findViewById(R.id.category);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(Tracing.this);

                other = customView.findViewById(R.id.other);

                final EditText notes = customView.findViewById(R.id.note);

                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);
            }
        });

        peopleTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_people_track,(ViewGroup) findViewById(R.id.people_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        true
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                final Calendar calendar = Calendar.getInstance();
                final EditText date = customView.findViewById(R.id.date);
                final DatePickerDialog.OnDateSetListener picker = new DatePickerDialog.OnDateSetListener() {
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
                        new DatePickerDialog(Tracing.this, picker, calendar
                                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                String[] items = new String[]{"Family", "Vet", "Nutritionist", "Other"};
                spinner = customView.findViewById(R.id.category);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(Tracing.this);

                String[] cont = new String[]{"Direct contact", "Indirect contact"};
                Spinner contact = customView.findViewById(R.id.contact);
                ArrayAdapter<String> contAdapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, cont);
                contAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                contact.setAdapter(contAdapter);

                other = customView.findViewById(R.id.other);

                final EditText notes = customView.findViewById(R.id.note);

                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);
            }
        });

        veichleTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_vehicle_track,(ViewGroup) findViewById(R.id.vehicle_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        true
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                final Calendar calendar = Calendar.getInstance();
                final EditText date = customView.findViewById(R.id.date);
                final DatePickerDialog.OnDateSetListener picker = new DatePickerDialog.OnDateSetListener() {
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
                        new DatePickerDialog(Tracing.this, picker, calendar
                                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                final String[] items = new String[]{"Private car", "Milk tanker", "Feed truck", "Other"};
                spinner = customView.findViewById(R.id.category);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(Tracing.this);

                String[] cont = new String[]{"Direct contact", "Indirect contact"};
                Spinner contact = customView.findViewById(R.id.contact);
                ArrayAdapter<String> contAdapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, cont);
                contAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                contact.setAdapter(contAdapter);

                other = customView.findViewById(R.id.other);

                final EditText notes = customView.findViewById(R.id.note);

                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);
            }
        });

    }

    public List<Integer> getAnimals(int id) {
        List<Integer> animals = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Animal.AnimalEntry.TABLE_NAME + " WHERE farm=" + id;

        DatabaseHelper mDbHelper = new DatabaseHelper(this);
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

            DatabaseHelper mDbHelper = new DatabaseHelper(this);
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

            DatabaseHelper mDbHelper = new DatabaseHelper(this);
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
            spinner.setVisibility(View.INVISIBLE);
            other.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

}
