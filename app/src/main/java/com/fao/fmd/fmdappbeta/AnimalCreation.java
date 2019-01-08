package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AnimalCreation extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    Spinner sexSpin;
    EditText other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_creation);

        ImageView addAnimal = findViewById(R.id.addAnimal);

        final EditText animal = findViewById(R.id.animalID);
        final EditText group = findViewById(R.id.groupID);
        final EditText years = findViewById(R.id.animalYears);
        final EditText months = findViewById(R.id.animalMonths);
        other = findViewById(R.id.other);

        final String[] items = new String[]{"Cattle", "Sheep", "Goat", "Pig", "Other"};
        final String[] sex = new String[]{"M", "F"};

        spinner = findViewById(R.id.breed);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        sexSpin = findViewById(R.id.sex);
        ArrayAdapter<String> sexAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, sex);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexSpin.setAdapter(sexAdapter);

        final Calendar calendar = Calendar.getInstance();
        final EditText sign = findViewById(R.id.sign);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
                sign.setText(sdf.format(calendar.getTime()));
            }

        };
        sign.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(AnimalCreation.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final String[] vaccination = new String[]{"<6 months", ">6 months"};
        final Spinner vaccSpin = findViewById(R.id.vaccinated);
        ArrayAdapter<String> vaccAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vaccination);
        vaccAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccSpin.setAdapter(vaccAdapter);

        Bundle bundle = getIntent().getExtras();
        final int farm;

        if(bundle != null){
            farm = bundle.getInt("id");
        }else{
            farm = 0;
        }

        addAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHelper mDbHelper = new DatabaseHelper(AnimalCreation.this);
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

                String breed;
                if(spinner.getVisibility() == View.VISIBLE){
                    breed = spinner.getSelectedItem().toString();
                }else{
                    breed = other.getText().toString();
                }

                int vaccined;
                if(vaccSpin.getSelectedItem().toString().equals("<6 months")){
                    vaccined = 1;
                }else{
                    vaccined = 0;
                }

                ContentValues values = new ContentValues();
                values.put(Animal.AnimalEntry.COLUMN_NAME, animal.getText().toString());
                values.put(Animal.AnimalEntry.COLUMN_FARM, farm);
                values.put(Animal.AnimalEntry.COLUMN_GROUP, group.getText().toString());
                values.put(Animal.AnimalEntry.COLUMN_AGE, years.getText().toString() + " & " + months.getText().toString());
                values.put(Animal.AnimalEntry.COLUMN_BREED, breed + " (" + sexSpin.getSelectedItem().toString() + ")");
                values.put(Animal.AnimalEntry.COLUMN_REPORT, sign.getText().toString());
                values.put(Animal.AnimalEntry.COLUMN_VACCINATED, vaccined);

                long newRowId = db.insert(Animal.AnimalEntry.TABLE_NAME, null, values);

                if(newRowId == -1){
                    Toast.makeText(getBaseContext(), "Error in the DB",
                            Toast.LENGTH_LONG).show();
                    db.close();
                }else{
                    Toast.makeText(getBaseContext(), "New entry added to the DB",
                            Toast.LENGTH_LONG).show();
                    String selectQuery = "SELECT  * FROM " + Animal.AnimalEntry.TABLE_NAME;
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    cursor.moveToLast();
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    cursor.close();
                    db.close();
                    Intent intent = new Intent(AnimalCreation.this, LesionAgeing.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putInt("id", id);
                    intent.putExtras(mBundle);
                    startActivity(intent);
                }
            }
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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
