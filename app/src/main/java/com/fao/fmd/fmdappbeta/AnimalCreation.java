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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AnimalCreation extends Activity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;
    Spinner sexSpin;
    EditText other;

    boolean lock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_creation);

        TextView light = findViewById(R.id.light);
        TextView dark = findViewById(R.id.dark);
        LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.45f
        );
        LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.55f
        );
        light.setLayoutParams(paramLight);
        dark.setLayoutParams(paramDark);

        ImageView addAnimal = findViewById(R.id.addAnimal);

        final EditText animal = findViewById(R.id.animalID);
        final EditText group = findViewById(R.id.groupID);
        other = findViewById(R.id.other);

        final String[] items = new String[]{"Cattle", "Sheep", "Goat", "Pig", "Other"};
        final String[] sex = new String[]{"M", "F"};
        final String[] years = new String[]{"0 year","1 year","2 years","3 years","4 years","5 years","6 years","7 years","8 years","9 years","10 years","11 years","12 years","13 years","14 years","15 years"};
        final String[] months = new String[]{"1 month","2 months","3 months","4 months","5 months","6 months","7 months","8 months","9 months","10 months","11 months","12 months"};
        final String[] vaccination = new String[]{"≤6 months", ">6 months"};

        final Spinner yearsSpin = findViewById(R.id.animalYears);
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsSpin.setAdapter(yearsAdapter);

        final Spinner monthsSpin = findViewById(R.id.animalMonths);
        ArrayAdapter<String> monthsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthsSpin.setAdapter(monthsAdapter);

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

        Switch locker = findViewById(R.id.locker);
        locker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if (!animal.getText().toString().trim().isEmpty()) {
                        animal.setBackgroundResource(R.color.colorPrimary);
                        lock = false;
                    } else {
                        animal.setBackgroundResource(R.color.TLyellow);
                        lock = true;
                    }
                    if (!group.getText().toString().trim().isEmpty()) {
                        group.setBackgroundResource(R.color.colorPrimary);
                        lock = false;
                    } else {
                        group.setBackgroundResource(R.color.TLyellow);
                        lock = true;
                    }
                    yearsSpin.setBackgroundResource(R.color.colorPrimary);
                    monthsSpin.setBackgroundResource(R.color.colorPrimary);
                    spinner.setBackgroundResource(R.color.colorPrimary);
                    if (!other.getText().toString().trim().isEmpty()) {
                        other.setBackgroundResource(R.color.colorPrimary);
                        lock = false;
                    } else {
                        other.setBackgroundResource(R.color.TLyellow);
                        lock = true;
                    }
                    sexSpin.setBackgroundResource(R.color.colorPrimary);
                    if (!sign.getText().toString().trim().isEmpty()) {
                        sign.setBackgroundResource(R.color.colorPrimary);
                        lock = false;
                    } else {
                        sign.setBackgroundResource(R.color.TLyellow);
                        lock = true;
                    }
                    vaccSpin.setBackgroundResource(R.color.colorPrimary);
                    animal.setEnabled(false);
                    group.setEnabled(false);
                    yearsSpin.setEnabled(false);
                    monthsSpin.setEnabled(false);
                    spinner.setEnabled(false);
                    sexSpin.setEnabled(false);
                    sign.setEnabled(false);
                    vaccSpin.setEnabled(false);
                }else{
                    animal.setBackgroundResource(R.color.white);
                    group.setBackgroundResource(R.color.white);
                    yearsSpin.setBackgroundResource(R.color.white);
                    monthsSpin.setBackgroundResource(R.color.white);
                    spinner.setBackgroundResource(R.color.white);
                    other.setBackgroundResource(R.color.white);
                    sexSpin.setBackgroundResource(R.color.white);
                    sign.setBackgroundResource(R.color.white);
                    vaccSpin.setBackgroundResource(R.color.white);
                    animal.setEnabled(true);
                    group.setEnabled(true);
                    yearsSpin.setEnabled(true);
                    monthsSpin.setEnabled(true);
                    spinner.setEnabled(true);
                    sexSpin.setEnabled(true);
                    sign.setEnabled(true);
                    vaccSpin.setEnabled(true);
                }
            }
        });

        addAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!locker.isChecked()){
                    Toast.makeText(getBaseContext(), "Lock the switch before proceed", Toast.LENGTH_LONG).show();
                    return;
                }

                if(locker.isChecked() && lock){
                    Toast.makeText(getBaseContext(), "You need to fill all the information", Toast.LENGTH_LONG).show();
                    return;
                }

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
                values.put(Animal.AnimalEntry.COLUMN_AGE, yearsSpin.getSelectedItem().toString() + " & " + monthsSpin.getSelectedItem().toString());
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
