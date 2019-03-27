package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditAnimal extends Activity implements AdapterView.OnItemSelectedListener{

    Spinner spinner;
    Spinner sexSpin;
    EditText other;
    Button close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_animal);

        TextView light = findViewById(R.id.light);
        TextView dark = findViewById(R.id.dark);
        LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.00f
        );
        LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.00f
        );
        light.setLayoutParams(paramLight);
        dark.setLayoutParams(paramDark);

        ImageView addAnimal = findViewById(R.id.addAnimal);

        final EditText animal = findViewById(R.id.animalID);
        final EditText group = findViewById(R.id.groupID);
        other = findViewById(R.id.other);

        animal.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        group.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });
        other.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        });

        close = findViewById(R.id.close);
        close.setOnClickListener(v -> {
            spinner.setSelection(0);
            spinner.setVisibility(View.VISIBLE);
            other.setVisibility(View.INVISIBLE);
            close.setVisibility(View.INVISIBLE);
        });

        final String[] items = new String[]{"Cattle", "Sheep", "Goat", "Pig", "Other"};
        final String[] sex = new String[]{"M", "F"};
        final String[] years = new String[]{"0 year","1 year","2 years","3 years","4 years","5 years","6 years","7 years","8 years","9 years","10 years","11 years","12 years","13 years","14 years","15 years"};
        final String[] months = new String[]{"0 months","1 month","2 months","3 months","4 months","5 months","6 months","7 months","8 months","9 months","10 months","11 months"};
        final String[] vaccination = new String[]{"No", "≤6 months", ">6 months"};

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
        ImageView cal = findViewById(R.id.cal);
        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
            sign.setText(sdf.format(calendar.getTime()));
        };
        cal.setOnClickListener(v -> new DatePickerDialog(EditAnimal.this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        final Spinner vaccSpin = findViewById(R.id.vaccinated);
        ArrayAdapter<String> vaccAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vaccination);
        vaccAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccSpin.setAdapter(vaccAdapter);

        Bundle bundle = getIntent().getExtras();
        final int id;

        if(bundle != null){
            id = bundle.getInt("id");
        }else{
            id = 0;
        }

        //Retrieve animal information
        String selectQuery = "SELECT * FROM " + Animal.AnimalEntry.TABLE_NAME + " WHERE id=" + id;
        DatabaseHelper mDbHelper = new DatabaseHelper(EditAnimal.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        animal.setText(cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_NAME)));
        group.setText(cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_GROUP)));
        String age = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_AGE));
        yearsSpin.setSelection(yearsAdapter.getPosition(age.substring(0, age.indexOf('s')+1)));
        monthsSpin.setSelection(monthsAdapter.getPosition(age.substring(age.indexOf('&')+2)));
        String species = cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_SPECIES));
        spinner.setSelection(adapter.getPosition(species.substring(0, species.indexOf(' '))));
        sexSpin.setSelection(sexAdapter.getPosition(species.substring(species.indexOf('(')+1,species.indexOf(')'))));
        sign.setText(cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_REPORT)));
        vaccSpin.setSelection(vaccAdapter.getPosition(cursor.getString(cursor.getColumnIndex(Animal.AnimalEntry.COLUMN_VACCINATION))));

        cursor.close();

        addAnimal.setOnClickListener(v -> {

            String breed;
            if(spinner.getVisibility() == View.VISIBLE){
                breed = spinner.getSelectedItem().toString();
            }else{
                breed = other.getText().toString();
            }

            //PUT VALUES INTO DB
            ContentValues values = new ContentValues();
            values.put(Animal.AnimalEntry.COLUMN_NAME, animal.getText().toString());
            values.put(Animal.AnimalEntry.COLUMN_GROUP, group.getText().toString());
            values.put(Animal.AnimalEntry.COLUMN_AGE, yearsSpin.getSelectedItem().toString() + " & " + monthsSpin.getSelectedItem().toString());
            values.put(Animal.AnimalEntry.COLUMN_SPECIES, breed + " (" + sexSpin.getSelectedItem().toString() + ")");
            values.put(Animal.AnimalEntry.COLUMN_REPORT, sign.getText().toString());
            values.put(Animal.AnimalEntry.COLUMN_VACCINATION, vaccSpin.getSelectedItem().toString());

            long newRowId = db.update(Animal.AnimalEntry.TABLE_NAME, values, "id= ?",new String[]{Integer.toString(id)});

            if (newRowId == -1) {
                Toast.makeText(getBaseContext(), "Error in the DB", Toast.LENGTH_LONG).show();
                db.close();
            } else {
                Toast.makeText(getBaseContext(), "DB updated", Toast.LENGTH_SHORT).show();
                cursor.close();
                db.close();
                Intent intent = new Intent(EditAnimal.this, FarmList.class);
                startActivity(intent);
            }

        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if(parent.getItemAtPosition(pos) == "Other"){
            spinner.setVisibility(View.INVISIBLE);
            other.setVisibility(View.VISIBLE);
            close.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
