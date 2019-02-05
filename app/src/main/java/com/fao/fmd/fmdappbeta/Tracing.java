package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.DatePicker;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;

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
    Button close;

    EditText date;

    int farm;

    ArrayList<String> animalsSub = new ArrayList<>();
    ArrayList<String> animalsDate = new ArrayList<>();
    ArrayList<String> animalsNote = new ArrayList<>();

    ArrayList<String> productsSub = new ArrayList<>();
    ArrayList<String> productsDate = new ArrayList<>();
    ArrayList<String> productsNote = new ArrayList<>();

    ArrayList<String> peoplesSub = new ArrayList<>();
    ArrayList<String> peoplesDate = new ArrayList<>();
    ArrayList<String> peoplesNote = new ArrayList<>();

    ArrayList<String> vehiclesSub = new ArrayList<>();
    ArrayList<String> vehiclesDate = new ArrayList<>();
    ArrayList<String> vehiclesNote = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing);

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

        final Bundle bundle = getIntent().getExtras();
        farm = bundle.getInt("id");

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

        final TextView animalNum = findViewById(R.id.animalNum);
        final int[] animalCount = {0};
        final TextView productNum = findViewById(R.id.productNum);
        final int[] productCount = {0};
        final TextView peopleNum = findViewById(R.id.peopleNum);
        final int[] peopleCount = {0};
        final TextView vehicleNum = findViewById(R.id.vehicleNum);
        final int[] vehicleCount = {0};

        animalTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_animal_track, findViewById(R.id.animal_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.setFocusable(true);


                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                date = customView.findViewById(R.id.date);
                final DatePickerBuilder picker = new DatePickerBuilder(Tracing.this, listener).minimumDate(subDays(dayZero,21)).pickerType(CalendarView.MANY_DAYS_PICKER).headerColor(R.color.colorPrimary).selectionColor(R.color.colorPrimary).todayLabelColor(R.color.green_color_picker);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker datePicker = picker.build();
                        datePicker.show();
                    }
                });

                String[] items = new String[]{"Cattle", "Sheep", "Goat", "Pig", "Other"};
                spinner = customView.findViewById(R.id.species);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(Tracing.this);

                other = customView.findViewById(R.id.other);
                close = customView.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spinner.setSelection(0);
                        spinner.setVisibility(View.VISIBLE);
                        other.setVisibility(View.INVISIBLE);
                        close.setVisibility(View.INVISIBLE);
                    }
                });

                final EditText notes = customView.findViewById(R.id.note);

                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                Button add = customView.findViewById(R.id.done);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(date.getText().toString().length() > 9) {
                            int num = date.getText().toString().length() / 9;
                            int end = 8;
                            int start = 0;
                            for (int i = 0; i < num; i++) {
                                animalsDate.add(date.getText().toString().substring(start,end));
                                start = end + 1;
                                end = end + 9;
                                if (spinner.getVisibility() == View.VISIBLE) {
                                    animalsSub.add(spinner.getSelectedItem().toString());
                                } else {
                                    animalsSub.add(other.getText().toString());
                                }
                                animalsNote.add(notes.getText().toString());
                                animalCount[0]++;
                                String count = Integer.toString(animalCount[0]);
                                animalNum.setText(count);
                            }
                        }else{
                            animalsDate.add(date.getText().toString().substring(0,8));
                            if (spinner.getVisibility() == View.VISIBLE) {
                                animalsSub.add(spinner.getSelectedItem().toString());
                            } else {
                                animalsSub.add(other.getText().toString());
                            }
                            animalsNote.add(notes.getText().toString());
                            animalCount[0]++;
                            String count = Integer.toString(animalCount[0]);
                            animalNum.setText(count);
                        }
                        Toast.makeText(getBaseContext(), "Tracing saved", Toast.LENGTH_SHORT).show();
                        mPopupWindow.dismiss();
                    }
                });

                Button esc = customView.findViewById(R.id.esc);
                esc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
            }
        });

        productTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_product_track, findViewById(R.id.product_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.setFocusable(true);

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                date = customView.findViewById(R.id.date);
                final DatePickerBuilder picker = new DatePickerBuilder(Tracing.this, listener).minimumDate(subDays(dayZero,21)).pickerType(CalendarView.MANY_DAYS_PICKER).headerColor(R.color.colorPrimary).selectionColor(R.color.colorPrimary).todayLabelColor(R.color.green_color_picker);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker datePicker = picker.build();
                        datePicker.show();
                    }
                });

                String[] items = new String[]{"Milk", "Meat", "Feed", "Other"};
                spinner = customView.findViewById(R.id.category);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(Tracing.this);

                other = customView.findViewById(R.id.other);
                close = customView.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spinner.setSelection(0);
                        spinner.setVisibility(View.VISIBLE);
                        other.setVisibility(View.INVISIBLE);
                        close.setVisibility(View.INVISIBLE);
                    }
                });

                final EditText notes = customView.findViewById(R.id.note);

                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                Button add = customView.findViewById(R.id.done);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(date.getText().toString().length() > 9) {
                            int num = date.getText().toString().length() / 9;
                            int end = 8;
                            int start = 0;
                            for (int i = 0; i < num; i++) {
                                productsDate.add(date.getText().toString().substring(start,end));
                                start = end + 1;
                                end = end + 9;
                                if(spinner.getVisibility() == View.VISIBLE){
                                    productsSub.add(spinner.getSelectedItem().toString());
                                }else{
                                    productsSub.add(other.getText().toString());
                                }
                                productsNote.add(notes.getText().toString());
                                productCount[0]++;
                                String count = Integer.toString(productCount[0]);
                                productNum.setText(count);
                            }
                        }else{
                            productsDate.add(date.getText().toString().substring(0,8));
                            if(spinner.getVisibility() == View.VISIBLE){
                                productsSub.add(spinner.getSelectedItem().toString());
                            }else{
                                productsSub.add(other.getText().toString());
                            }
                            productsNote.add(notes.getText().toString());
                            productCount[0]++;
                            String count = Integer.toString(productCount[0]);
                            productNum.setText(count);
                        }
                        Toast.makeText(getBaseContext(), "Tracing saved", Toast.LENGTH_SHORT).show();
                        mPopupWindow.dismiss();
                    }
                });

                Button esc = customView.findViewById(R.id.esc);
                esc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
            }
        });

        peopleTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_people_track, findViewById(R.id.people_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.setFocusable(true);

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                date = customView.findViewById(R.id.date);
                final DatePickerBuilder picker = new DatePickerBuilder(Tracing.this, listener).minimumDate(subDays(dayZero,21)).pickerType(CalendarView.MANY_DAYS_PICKER).headerColor(R.color.colorPrimary).selectionColor(R.color.colorPrimary).todayLabelColor(R.color.green_color_picker);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker datePicker = picker.build();
                        datePicker.show();
                    }
                });

                String[] items = new String[]{"Family", "Vet", "Nutritionist", "Other"};
                spinner = customView.findViewById(R.id.category);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(Tracing.this);

                String[] cont = new String[]{"Direct contact", "Indirect contact"};
                final Spinner contact = customView.findViewById(R.id.contact);
                ArrayAdapter<String> contAdapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, cont);
                contAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                contact.setAdapter(contAdapter);

                other = customView.findViewById(R.id.other);
                close = customView.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spinner.setSelection(0);
                        spinner.setVisibility(View.VISIBLE);
                        other.setVisibility(View.INVISIBLE);
                        close.setVisibility(View.INVISIBLE);
                    }
                });

                final EditText notes = customView.findViewById(R.id.note);

                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                Button add = customView.findViewById(R.id.done);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(date.getText().toString().length() > 9) {
                            int num = date.getText().toString().length() / 9;
                            int end = 8;
                            int start = 0;
                            for (int i = 0; i < num; i++) {
                                peoplesDate.add(date.getText().toString().substring(start,end));
                                start = end + 1;
                                end = end + 9;
                                if(spinner.getVisibility() == View.VISIBLE){
                                    peoplesSub.add(spinner.getSelectedItem().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                }else{
                                    peoplesSub.add(other.getText().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                }
                                peoplesNote.add(notes.getText().toString());
                                peopleCount[0]++;
                                String count = Integer.toString(peopleCount[0]);
                                peopleNum.setText(count);
                            }
                        }else{
                            peoplesDate.add(date.getText().toString().substring(0,8));
                            if(spinner.getVisibility() == View.VISIBLE){
                                peoplesSub.add(spinner.getSelectedItem().toString() + "(" + contact.getSelectedItem().toString() + ")");
                            }else{
                                peoplesSub.add(other.getText().toString() + "(" + contact.getSelectedItem().toString() + ")");
                            }
                            peoplesNote.add(notes.getText().toString());
                            peopleCount[0]++;
                            String count = Integer.toString(peopleCount[0]);
                            peopleNum.setText(count);
                        }
                        Toast.makeText(getBaseContext(), "Tracing saved", Toast.LENGTH_SHORT).show();
                        mPopupWindow.dismiss();
                    }
                });

                Button esc = customView.findViewById(R.id.esc);
                esc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
            }
        });

        veichleTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_vehicle_track, findViewById(R.id.vehicle_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.setFocusable(true);

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                date = customView.findViewById(R.id.date);
                final DatePickerBuilder picker = new DatePickerBuilder(Tracing.this, listener).minimumDate(subDays(dayZero,21)).pickerType(CalendarView.MANY_DAYS_PICKER).headerColor(R.color.colorPrimary).selectionColor(R.color.colorPrimary).todayLabelColor(R.color.green_color_picker);
                date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker datePicker = picker.build();
                        datePicker.show();
                    }
                });

                final String[] items = new String[]{"Private car", "Milk tanker", "Feed truck", "Other"};
                spinner = customView.findViewById(R.id.category);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(Tracing.this);

                String[] cont = new String[]{"Direct contact", "Indirect contact"};
                final Spinner contact = customView.findViewById(R.id.contact);
                ArrayAdapter<String> contAdapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, cont);
                contAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                contact.setAdapter(contAdapter);

                other = customView.findViewById(R.id.other);
                close = customView.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        spinner.setSelection(0);
                        spinner.setVisibility(View.VISIBLE);
                        other.setVisibility(View.INVISIBLE);
                        close.setVisibility(View.INVISIBLE);
                    }
                });

                final EditText notes = customView.findViewById(R.id.note);

                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                Button add = customView.findViewById(R.id.done);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(date.getText().toString().length() > 9) {
                            int num = date.getText().toString().length() / 9;
                            System.out.println(num);
                            int end = 8;
                            int start = 0;
                            for (int i = 0; i < num; i++) {
                                vehiclesDate.add(date.getText().toString().substring(start,end));
                                start = end + 1;
                                end = end + 9;
                                if(spinner.getVisibility() == View.VISIBLE){
                                    vehiclesSub.add(spinner.getSelectedItem().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                }else{
                                    vehiclesSub.add(other.getText().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                }
                                vehiclesNote.add(notes.getText().toString());
                                vehicleCount[0]++;
                                String count = Integer.toString(vehicleCount[0]);
                                vehicleNum.setText(count);
                            }
                        }else{
                            vehiclesDate.add(date.getText().toString().substring(0,8));
                            if(spinner.getVisibility() == View.VISIBLE){
                                vehiclesSub.add(spinner.getSelectedItem().toString() + "(" + contact.getSelectedItem().toString() + ")");
                            }else{
                                vehiclesSub.add(other.getText().toString() + "(" + contact.getSelectedItem().toString() + ")");
                            }
                            vehiclesNote.add(notes.getText().toString());
                            vehicleCount[0]++;
                            String count = Integer.toString(vehicleCount[0]);
                            vehicleNum.setText(count);
                        }
                        Toast.makeText(getBaseContext(), "Tracing saved", Toast.LENGTH_SHORT).show();
                        mPopupWindow.dismiss();
                    }
                });

                Button esc = customView.findViewById(R.id.esc);
                esc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
            }
        });

        animalNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_lesions_list, findViewById(R.id.lesion_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.setFocusable(true);

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                RecyclerView mRecyclerView = customView.findViewById(R.id.my_recycler_view);
                mRecyclerView.setHasFixedSize(true);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Tracing.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
                mRecyclerView.addItemDecoration(dividerItemDecoration);

                RecyclerView.Adapter mAdapter = new RecyclerAdapter(animalsSub);
                ((RecyclerAdapter)mAdapter).setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position, String myData) {
                        View customView = inflater.inflate(R.layout.fragment_animal_track, findViewById(R.id.animal_popup));
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

                        mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                        date = customView.findViewById(R.id.date);
                        date.setText(animalsDate.get(position));
                        final DatePickerBuilder picker = new DatePickerBuilder(Tracing.this, listener).minimumDate(subDays(dayZero,21)).pickerType(CalendarView.MANY_DAYS_PICKER).headerColor(R.color.colorPrimary).selectionColor(R.color.colorPrimary).todayLabelColor(R.color.green_color_picker);
                        date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePicker datePicker = picker.build();
                                datePicker.show();
                            }
                        });

                        String[] items = new String[]{"Cattle", "Sheep", "Goat", "Pig", "Other"};
                        spinner = customView.findViewById(R.id.species);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(Tracing.this);

                        other = customView.findViewById(R.id.other);
                        close = customView.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                spinner.setSelection(0);
                                spinner.setVisibility(View.VISIBLE);
                                other.setVisibility(View.INVISIBLE);
                                close.setVisibility(View.INVISIBLE);
                            }
                        });

                        final EditText notes = customView.findViewById(R.id.note);
                        notes.setText(animalsNote.get(position));

                        Button add = customView.findViewById(R.id.done);
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                animalsDate.remove(position);
                                animalsSub.remove(position);
                                animalsNote.remove(position);
                                if(date.getText().toString().length() > 9) {
                                    int num = date.getText().toString().length() / 9;
                                    int end = 8;
                                    for (int i = 0; i < num; i++) {
                                        animalsDate.add(date.getText().toString().substring(0,end));
                                        end = end + 9;
                                        if (spinner.getVisibility() == View.VISIBLE) {
                                            animalsSub.add(spinner.getSelectedItem().toString());
                                        } else {
                                            animalsSub.add(other.getText().toString());
                                        }
                                        animalsNote.add(notes.getText().toString());
                                    }
                                }else{
                                    animalsDate.add(date.getText().toString().substring(0,8));
                                    if (spinner.getVisibility() == View.VISIBLE) {
                                        animalsSub.add(spinner.getSelectedItem().toString());
                                    } else {
                                        animalsSub.add(other.getText().toString());
                                    }
                                    animalsNote.add(notes.getText().toString());
                                }
                                Toast.makeText(getBaseContext(), "Tracing modified", Toast.LENGTH_SHORT).show();
                                mPopupWindow.dismiss();
                            }
                        });
                        Button esc = customView.findViewById(R.id.esc);
                        esc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopupWindow.dismiss();
                            }
                        });
                    }
                });
                mRecyclerView.setAdapter(mAdapter);

            }
        });

        productNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_lesions_list, findViewById(R.id.lesion_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.setFocusable(true);

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }


                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                RecyclerView mRecyclerView = customView.findViewById(R.id.my_recycler_view);
                mRecyclerView.setHasFixedSize(true);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Tracing.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
                mRecyclerView.addItemDecoration(dividerItemDecoration);

                RecyclerView.Adapter mAdapter = new RecyclerAdapter(productsSub);

                ((RecyclerAdapter)mAdapter).setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position, String myData) {
                        View customView = inflater.inflate(R.layout.fragment_product_track, findViewById(R.id.product_popup));
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

                        mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                        date = customView.findViewById(R.id.date);
                        date.setText(productsDate.get(position));
                        final DatePickerBuilder picker = new DatePickerBuilder(Tracing.this, listener).minimumDate(subDays(dayZero,21)).pickerType(CalendarView.MANY_DAYS_PICKER).headerColor(R.color.colorPrimary).selectionColor(R.color.colorPrimary).todayLabelColor(R.color.green_color_picker);
                        date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePicker datePicker = picker.build();
                                datePicker.show();
                            }
                        });

                        String[] items = new String[]{"Milk", "Meat", "Feed", "Other"};
                        spinner = customView.findViewById(R.id.category);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(Tracing.this);

                        other = customView.findViewById(R.id.other);
                        close = customView.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                spinner.setSelection(0);
                                spinner.setVisibility(View.VISIBLE);
                                other.setVisibility(View.INVISIBLE);
                                close.setVisibility(View.INVISIBLE);
                            }
                        });

                        final EditText notes = customView.findViewById(R.id.note);
                        notes.setText(productsNote.get(position));

                        Button add = customView.findViewById(R.id.done);
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                productsDate.remove(position);
                                productsSub.remove(position);
                                productsNote.remove(position);
                                if(date.getText().toString().length() > 9) {
                                    int num = date.getText().toString().length() / 9;
                                    int end = 8;
                                    for (int i = 0; i < num; i++) {
                                        productsDate.add(date.getText().toString().substring(0,end));
                                        end = end + 9;
                                        if(spinner.getVisibility() == View.VISIBLE){
                                            productsSub.add(spinner.getSelectedItem().toString());
                                        }else{
                                            productsSub.add(other.getText().toString());
                                        }
                                        productsNote.add(notes.getText().toString());
                                    }
                                }else{
                                    productsDate.add(date.getText().toString().substring(0,8));
                                    if(spinner.getVisibility() == View.VISIBLE){
                                        productsSub.add(spinner.getSelectedItem().toString());
                                    }else{
                                        productsSub.add(other.getText().toString());
                                    }
                                    productsNote.add(notes.getText().toString());
                                }
                                Toast.makeText(getBaseContext(), "Tracing modified", Toast.LENGTH_SHORT).show();
                                mPopupWindow.dismiss();
                            }
                        });
                        Button esc = customView.findViewById(R.id.esc);
                        esc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopupWindow.dismiss();
                            }
                        });
                    }
                });

                mRecyclerView.setAdapter(mAdapter);

            }
        });

        peopleNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_lesions_list, findViewById(R.id.lesion_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.setFocusable(true);

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                RecyclerView mRecyclerView = customView.findViewById(R.id.my_recycler_view);
                mRecyclerView.setHasFixedSize(true);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Tracing.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
                mRecyclerView.addItemDecoration(dividerItemDecoration);

                RecyclerView.Adapter mAdapter = new RecyclerAdapter(peoplesSub);

                ((RecyclerAdapter)mAdapter).setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position, String myData) {
                        View customView = inflater.inflate(R.layout.fragment_people_track, findViewById(R.id.people_popup));
                        mPopupWindow = new PopupWindow(
                                customView,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );
                        mPopupWindow.setFocusable(true);

                        // Set an elevation value for popup window
                        // Call requires API level 21
                        if(Build.VERSION.SDK_INT>=21){
                            mPopupWindow.setElevation(5.0f);
                        }

                        mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                        date = customView.findViewById(R.id.date);
                        date.setText(peoplesDate.get(position));
                        final DatePickerBuilder picker = new DatePickerBuilder(Tracing.this, listener).minimumDate(subDays(dayZero,21)).pickerType(CalendarView.MANY_DAYS_PICKER).headerColor(R.color.colorPrimary).selectionColor(R.color.colorPrimary).todayLabelColor(R.color.green_color_picker);
                        date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePicker datePicker = picker.build();
                                datePicker.show();
                            }
                        });

                        String[] items = new String[]{"Family", "Vet", "Nutritionist", "Other"};
                        spinner = customView.findViewById(R.id.category);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(Tracing.this);

                        String[] cont = new String[]{"Direct contact", "Indirect contact"};
                        final Spinner contact = customView.findViewById(R.id.contact);
                        ArrayAdapter<String> contAdapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, cont);
                        contAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        contact.setAdapter(contAdapter);

                        other = customView.findViewById(R.id.other);
                        close = customView.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                spinner.setSelection(0);
                                spinner.setVisibility(View.VISIBLE);
                                other.setVisibility(View.INVISIBLE);
                                close.setVisibility(View.INVISIBLE);
                            }
                        });

                        final EditText notes = customView.findViewById(R.id.note);
                        notes.setText(peoplesNote.get(position));

                        Button add = customView.findViewById(R.id.done);
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                peoplesDate.remove(position);
                                peoplesSub.remove(position);
                                peoplesNote.remove(position);
                                if(date.getText().toString().length() > 9) {
                                    int num = date.getText().toString().length() / 9;
                                    int end = 8;
                                    for (int i = 0; i < num; i++) {
                                        peoplesDate.add(date.getText().toString().substring(0,end));
                                        end = end + 9;
                                        if(spinner.getVisibility() == View.VISIBLE){
                                            peoplesSub.add(spinner.getSelectedItem().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                        }else{
                                            peoplesSub.add(other.getText().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                        }
                                        peoplesNote.add(notes.getText().toString());
                                    }
                                }else{
                                    peoplesDate.add(date.getText().toString().substring(0,8));
                                    if(spinner.getVisibility() == View.VISIBLE){
                                        peoplesSub.add(spinner.getSelectedItem().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                    }else{
                                        peoplesSub.add(other.getText().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                    }
                                    peoplesNote.add(notes.getText().toString());
                                }
                                Toast.makeText(getBaseContext(), "Tracing modified", Toast.LENGTH_SHORT).show();
                                mPopupWindow.dismiss();
                            }
                        });
                        Button esc = customView.findViewById(R.id.esc);
                        esc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopupWindow.dismiss();
                            }
                        });
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
            }
        });

        vehicleNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.fragment_lesions_list, findViewById(R.id.lesion_popup));
                mPopupWindow = new PopupWindow(
                        customView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                mPopupWindow.setFocusable(true);

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                RecyclerView mRecyclerView = customView.findViewById(R.id.my_recycler_view);
                mRecyclerView.setHasFixedSize(true);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Tracing.this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
                mRecyclerView.addItemDecoration(dividerItemDecoration);

                RecyclerView.Adapter mAdapter = new RecyclerAdapter(vehiclesSub);
                ((RecyclerAdapter)mAdapter).setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClickListener(View view, int position, String myData) {
                        View customView = inflater.inflate(R.layout.fragment_vehicle_track, findViewById(R.id.vehicle_popup));
                        mPopupWindow = new PopupWindow(
                                customView,
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                        );

                        // Set an elevation value for popup window
                        // Call requires API level 21
                        if(Build.VERSION.SDK_INT>=21){
                            mPopupWindow.setElevation(5.0f);
                        }

                        mPopupWindow.showAtLocation(rel, Gravity.CENTER,0,0);

                        date = customView.findViewById(R.id.date);
                        date.setText(vehiclesDate.get(position));
                        final DatePickerBuilder picker = new DatePickerBuilder(Tracing.this, listener).minimumDate(subDays(dayZero,21)).pickerType(CalendarView.MANY_DAYS_PICKER).headerColor(R.color.colorPrimary).selectionColor(R.color.colorPrimary).todayLabelColor(R.color.green_color_picker);
                        date.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DatePicker datePicker = picker.build();
                                datePicker.show();
                            }
                        });

                        final String[] items = new String[]{"Private car", "Milk tanker", "Feed truck", "Other"};
                        spinner = customView.findViewById(R.id.category);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, items);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(Tracing.this);

                        String[] cont = new String[]{"Direct contact", "Indirect contact"};
                        final Spinner contact = customView.findViewById(R.id.contact);
                        ArrayAdapter<String> contAdapter = new ArrayAdapter<>(Tracing.this, android.R.layout.simple_spinner_dropdown_item, cont);
                        contAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        contact.setAdapter(contAdapter);

                        other = customView.findViewById(R.id.other);
                        close = customView.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                spinner.setSelection(0);
                                spinner.setVisibility(View.VISIBLE);
                                other.setVisibility(View.INVISIBLE);
                                close.setVisibility(View.INVISIBLE);
                            }
                        });

                        final EditText notes = customView.findViewById(R.id.note);
                        notes.setText(vehiclesNote.get(position));
                        Button add = customView.findViewById(R.id.done);
                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                vehiclesDate.remove(position);
                                vehiclesSub.remove(position);
                                vehiclesNote.remove(position);
                                if(date.getText().toString().length() > 9) {
                                    int num = date.getText().toString().length() / 9;
                                    int end = 8;
                                    for (int i = 0; i < num; i++) {
                                        vehiclesDate.add(date.getText().toString().substring(0,end));
                                        end = end + 9;
                                        if(spinner.getVisibility() == View.VISIBLE){
                                            vehiclesSub.add(spinner.getSelectedItem().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                        }else{
                                            vehiclesSub.add(other.getText().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                        }
                                        vehiclesNote.add(notes.getText().toString());
                                    }
                                }else{
                                    vehiclesDate.add(date.getText().toString().substring(0,8));
                                    if(spinner.getVisibility() == View.VISIBLE){
                                        vehiclesSub.add(spinner.getSelectedItem().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                    }else{
                                        vehiclesSub.add(other.getText().toString() + "(" + contact.getSelectedItem().toString() + ")");
                                    }
                                    vehiclesNote.add(notes.getText().toString());
                                }
                                Toast.makeText(getBaseContext(), "Tracing modified", Toast.LENGTH_SHORT).show();
                                mPopupWindow.dismiss();
                            }
                        });
                        Button esc = customView.findViewById(R.id.esc);
                        esc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPopupWindow.dismiss();
                            }
                        });
                    }
                });

                mRecyclerView.setAdapter(mAdapter);


            }
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadToDB();
            }
        });

    }

    public void UploadToDB(){
        DatabaseHelper mDbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        for(int i = 0; i < animalsSub.size(); i++){
            values.put(Tracings.TracingEntry.COLUMN_FARM, farm);
            values.put(Tracings.TracingEntry.COLUMN_CATEGORY, "Animal");
            values.put(Tracings.TracingEntry.COLUMN_SUB_CATEGORY, animalsSub.get(i));
            values.put(Tracings.TracingEntry.COLUMN_DATE, animalsDate.get(i));
            values.put(Tracings.TracingEntry.COLUMN_NOTES, animalsNote.get(i));
            long newRowId = db.insert(Tracings.TracingEntry.TABLE_NAME, null, values);
            if(newRowId == -1) {
                Toast.makeText(this, "Error in the DB", Toast.LENGTH_LONG).show();
                db.close();
                return;
            }
        }

        for(int i = 0; i < productsSub.size(); i++){
            values.put(Tracings.TracingEntry.COLUMN_FARM, farm);
            values.put(Tracings.TracingEntry.COLUMN_CATEGORY, "Product");
            values.put(Tracings.TracingEntry.COLUMN_SUB_CATEGORY, productsSub.get(i));
            values.put(Tracings.TracingEntry.COLUMN_DATE, productsDate.get(i));
            values.put(Tracings.TracingEntry.COLUMN_NOTES, productsNote.get(i));
            long newRowId = db.insert(Tracings.TracingEntry.TABLE_NAME, null, values);
            if(newRowId == -1) {
                Toast.makeText(this, "Error in the DB", Toast.LENGTH_LONG).show();
                db.close();
                return;
            }
        }

        for(int i = 0; i < peoplesSub.size(); i++){
            values.put(Tracings.TracingEntry.COLUMN_FARM, farm);
            values.put(Tracings.TracingEntry.COLUMN_CATEGORY, "People");
            values.put(Tracings.TracingEntry.COLUMN_SUB_CATEGORY, peoplesSub.get(i));
            values.put(Tracings.TracingEntry.COLUMN_DATE, peoplesDate.get(i));
            values.put(Tracings.TracingEntry.COLUMN_NOTES, peoplesNote.get(i));
            long newRowId = db.insert(Tracings.TracingEntry.TABLE_NAME, null, values);
            if(newRowId == -1) {
                Toast.makeText(this, "Error in the DB", Toast.LENGTH_LONG).show();
                db.close();
                return;
            }
        }


        for(int i = 0; i < vehiclesSub.size(); i++){
            values.put(Tracings.TracingEntry.COLUMN_FARM, farm);
            values.put(Tracings.TracingEntry.COLUMN_CATEGORY, "Vehicle");
            values.put(Tracings.TracingEntry.COLUMN_SUB_CATEGORY, vehiclesSub.get(i));
            values.put(Tracings.TracingEntry.COLUMN_DATE, vehiclesDate.get(i));
            values.put(Tracings.TracingEntry.COLUMN_NOTES, vehiclesNote.get(i));
            long newRowId = db.insert(Tracings.TracingEntry.TABLE_NAME, null, values);
            if(newRowId == -1) {
                Toast.makeText(this, "Error in the DB", Toast.LENGTH_LONG).show();
                db.close();
                return;
            }
        }

        Toast.makeText(this, "Tracing added to farm timeline", Toast.LENGTH_LONG).show();
        db.close();
        Intent intent = new Intent(Tracing.this, MainActivity.class);
        startActivity(intent);
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
                    if(age.length() < 2){
                        old = Integer.parseInt(age);
                    }else{
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
                    }
                    Date date = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(diagnosis);
                    Date diagnDate = subDays(date,old).getTime();
                    dates.add(diagnDate);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }

        return Collections.max(dates);
    }

    public Calendar subDays(Date date, int days){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        return cal;
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

    private OnSelectDateListener listener = new OnSelectDateListener() {
        @Override
        public void onSelect(List<Calendar> calendars) {
            String x = "";
            String myFormat = "dd/MM/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
            for (int i = 0; i < calendars.size(); i++){
                String selectedDate = sdf.format(calendars.get(i).getTime());
                x += selectedDate + " ";
            }
            date.setText(x);
        }
    };

}
