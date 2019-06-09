package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AnimalCreation extends Activity implements AdapterView.OnItemSelectedListener {

    EditText animal;
    EditText group;
    Spinner yearsSpin;
    Spinner monthsSpin;
    Spinner spinner;
    Spinner sexSpin;
    EditText other;
    Button close;
    Spinner vaccSpin;
    EditText sign;

    CheckBox ageCheck;
    CheckBox vaccCheck;

    int farm;

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

        animal = findViewById(R.id.animalID);
        group = findViewById(R.id.groupID);
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
        final String[] vaccination = new String[]{"Never", "≤6 months", ">6 months"};

        yearsSpin = findViewById(R.id.animalYears);
        ArrayAdapter<String> yearsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearsSpin.setAdapter(yearsAdapter);

        monthsSpin = findViewById(R.id.animalMonths);
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
        sign = findViewById(R.id.sign);
        ImageView cal = findViewById(R.id.cal);
        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String myFormat = "dd/MM/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
            sign.setText(sdf.format(calendar.getTime()));
        };
        cal.setOnClickListener(v -> new DatePickerDialog(AnimalCreation.this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());

        vaccSpin = findViewById(R.id.vaccinated);
        ArrayAdapter<String> vaccAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vaccination);
        vaccAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vaccSpin.setAdapter(vaccAdapter);

        ageCheck = findViewById(R.id.checkbox_age);
        vaccCheck = findViewById(R.id.checkbox_vacc);

        Bundle bundle = getIntent().getExtras();

        if(bundle != null){
            farm = bundle.getInt("id");
        }else{
            farm = 0;
        }

        addAnimal.setOnClickListener(v -> {

            if(animal.getText().toString().trim().isEmpty()){
                animal.setBackgroundResource(R.color.TLyellow);
                Toast.makeText(getBaseContext(), "Animal name/ID is a mandatory information", Toast.LENGTH_LONG).show();
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

            ContentValues values = new ContentValues();
            values.put(Animal.AnimalEntry.COLUMN_NAME, animal.getText().toString());
            values.put(Animal.AnimalEntry.COLUMN_FARM, farm);
            values.put(Animal.AnimalEntry.COLUMN_GROUP, group.getText().toString());
            if(!ageCheck.isChecked()){
                values.put(Animal.AnimalEntry.COLUMN_AGE, yearsSpin.getSelectedItem().toString() + " & " + monthsSpin.getSelectedItem().toString());
            }else{
                values.put(Animal.AnimalEntry.COLUMN_AGE,"");
            }
            values.put(Animal.AnimalEntry.COLUMN_SPECIES, breed + " (" + sexSpin.getSelectedItem().toString() + ")");
            values.put(Animal.AnimalEntry.COLUMN_REPORT, sign.getText().toString());
            if(!vaccCheck.isChecked()){
                values.put(Animal.AnimalEntry.COLUMN_VACCINATION, vaccSpin.getSelectedItem().toString());
            }else{
                values.put(Animal.AnimalEntry.COLUMN_VACCINATION, "");
            }

            long newRowId = db.insert(Animal.AnimalEntry.TABLE_NAME, null, values);

            if(newRowId == -1){
                Toast.makeText(getBaseContext(), "Error in the DB", Toast.LENGTH_LONG).show();
                db.close();
            }else{
                Toast.makeText(getBaseContext(), "New animal added to the DB", Toast.LENGTH_SHORT).show();
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
        });

        Button fast = findViewById(R.id.fast_track);
        fast.setOnClickListener(v -> {

            if(animal.getText().toString().trim().isEmpty()){
                animal.setBackgroundResource(R.color.TLyellow);
                Toast.makeText(getBaseContext(), "Animal name/ID is a mandatory information", Toast.LENGTH_LONG).show();
                return;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(AnimalCreation.this);
            builder.setTitle("Fast Track");
            String[] types = {"1 day", "2-3 days", "3-4 days", "3-5 days", "4-5 days", "5-7 days", "7-10 days", ">10 days"};
            builder.setItems(types, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: // 1
                            fastTrack("1");
                            break;
                        case 1: // 2-3
                            fastTrack("2-3");
                            break;
                        case 2: // 3-4
                            fastTrack("3-4");
                            break;
                        case 3: // 3-5
                            fastTrack("3-5");
                            break;
                        case 4: // 4-5
                            fastTrack("4-5");
                            break;
                        case 5: // 5-7
                            fastTrack("5-7");
                            break;
                        case 6: // 7-10
                            fastTrack("7-10");
                            break;
                        case 7: // 10+
                            fastTrack("10+");
                            break;
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        ImageView info = findViewById(R.id.qmark);
        info.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("tag","animal");
            Intent intent = new Intent(AnimalCreation.this, InfoPage.class);
            intent.putExtras(b);
            startActivity(intent);
        });

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

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_age:
                if (checked) {
                    yearsSpin.setEnabled(false);
                    monthsSpin.setEnabled(false);
                }else{
                    yearsSpin.setEnabled(true);
                    monthsSpin.setEnabled(true);
                }
                break;
            case R.id.checkbox_vacc:
                if (checked) {
                    vaccSpin.setEnabled(false);
                }else{
                    vaccSpin.setEnabled(true);
                }
                break;
        }
    }

    public void fastTrack(String days){
        DatabaseHelper mDbHelper = new DatabaseHelper(AnimalCreation.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String breed;
        if(spinner.getVisibility() == View.VISIBLE){
            breed = spinner.getSelectedItem().toString();
        }else{
            breed = other.getText().toString();
        }

        ContentValues values = new ContentValues();
        values.put(Animal.AnimalEntry.COLUMN_NAME, animal.getText().toString());
        values.put(Animal.AnimalEntry.COLUMN_FARM, farm);
        values.put(Animal.AnimalEntry.COLUMN_GROUP, group.getText().toString());
        if(!ageCheck.isChecked()){
            values.put(Animal.AnimalEntry.COLUMN_AGE, yearsSpin.getSelectedItem().toString() + " & " + monthsSpin.getSelectedItem().toString());
        }else{
            values.put(Animal.AnimalEntry.COLUMN_AGE,"");
        }
        values.put(Animal.AnimalEntry.COLUMN_SPECIES, breed + " (" + sexSpin.getSelectedItem().toString() + ")");
        values.put(Animal.AnimalEntry.COLUMN_REPORT, sign.getText().toString());
        if(!vaccCheck.isChecked()){
            values.put(Animal.AnimalEntry.COLUMN_VACCINATION, vaccSpin.getSelectedItem().toString());
        }else{
            values.put(Animal.AnimalEntry.COLUMN_VACCINATION, "");
        }

        long newRowId = db.insert(Animal.AnimalEntry.TABLE_NAME, null, values);

        if(newRowId == -1){
            Toast.makeText(getBaseContext(), "Error in the DB", Toast.LENGTH_LONG).show();
            db.close();
        }else{
            Toast.makeText(getBaseContext(), "New animal added to the DB", Toast.LENGTH_SHORT).show();
            String selectQuery = "SELECT  * FROM " + Animal.AnimalEntry.TABLE_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            cursor.moveToLast();
            int idAnimal = cursor.getInt(cursor.getColumnIndex("id"));
            cursor.close();
            db.close();
            Intent intent = new Intent(AnimalCreation.this, Suggestion.class);
            Bundle mBundle = new Bundle();
            mBundle.putInt("id", idAnimal);
            mBundle.putString("res", days);
            mBundle.putString("skip", "yes");
            intent.putExtras(mBundle);
            startActivity(intent);
        }
    }

}
