package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Timeline extends Activity {

    Date minDate;
    Date dayZero;
    long days;
    int id;
    Map<String,Date> infectionDates;
    Map<String,Date> spreadDates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        TableLayout timeline = findViewById(R.id.timeline);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            id = bundle.getInt("id");
        }

        List<Integer> animals = getAnimals(id);
        List<Integer> lesions = getLesions(animals);
        List<Integer> tracings = getTracings(id);

        try {
            Date maxDate = getMaxDate(lesions);
            minDate = getMinDate(lesions,tracings);
            long diff = maxDate.getTime() - minDate.getTime();
            days = (diff / (1000*60*60*24));
            dayZero = getDayZero(lesions);
            infectionDates = getInfectionDates(animals);
            spreadDates = getSpreadDates(animals);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Map<String,List<String>> trackAttr = getTracingAttr(tracings);

        for (int i = 0; i < days+1; i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);

            TextView date = new TextView(this);
            Date dayDate = addDays(minDate, i);
            SimpleDateFormat mdyFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.UK);
            String strDate = mdyFormat.format(dayDate);
            Date newDate = null;
            try {
                newDate = mdyFormat.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            date.append(strDate);

            TextView lesion = new TextView(this);
            lesion.setGravity(Gravity.CENTER);
            if(newDate.compareTo(dayZero) == 0){
                lesion.append("0");
            }else{
                if(newDate.compareTo(dayZero) < 0){
                    long diff = dayZero.getTime() - newDate.getTime();
                    long days = -(diff / (1000*60*60*24));
                    lesion.append(Long.toString(days));
                }else{
                    long diff = newDate.getTime() - dayZero.getTime();
                    long days = (diff / (1000*60*60*24));
                    lesion.append(Long.toString(days));
                }
            }

            TextView infection = new TextView(this);
            if(newDate.compareTo(infectionDates.get("likely_inf_min")) == 0 || (newDate.compareTo(infectionDates.get("likely_inf_min")) > 0 && newDate.compareTo(infectionDates.get("likely_inf_max")) < 0) || newDate.compareTo(infectionDates.get("likely_inf_max")) == 0){
                infection.setBackgroundColor(Color.GREEN);
            }else{
                if(newDate.compareTo(infectionDates.get("poss_inf_min")) == 0 || (newDate.compareTo(infectionDates.get("poss_inf_min")) > 0 && newDate.compareTo(infectionDates.get("poss_inf_max")) < 0) || newDate.compareTo(infectionDates.get("poss_inf_max")) == 0){
                    infection.setBackgroundColor(Color.RED);
                }
            }

            TextView spread = new TextView(this);
            if(newDate.compareTo(spreadDates.get("likely_spr_min")) == 0 || (newDate.compareTo(spreadDates.get("likely_spr_min")) > 0 && newDate.compareTo(spreadDates.get("likely_spr_max")) < 0) || newDate.compareTo(spreadDates.get("likely_spr_max")) == 0){
                spread.setBackgroundColor(Color.GREEN);
            }else{
                if(newDate.compareTo(spreadDates.get("poss_spr_min")) == 0 || (newDate.compareTo(spreadDates.get("poss_spr_min")) > 0 && newDate.compareTo(spreadDates.get("poss_spr_max")) < 0) || newDate.compareTo(spreadDates.get("poss_spr_max")) == 0){
                    spread.setBackgroundColor(Color.RED);
                }
            }

            TextView category = new TextView(this);
            category.setGravity(Gravity.CENTER);
            TextView subCat = new TextView(this);
            subCat.setGravity(Gravity.CENTER);
            TextView notes = new TextView(this);

            for (Map.Entry<String,List<String>> entry : trackAttr.entrySet()) {
                List<String> values = entry.getValue();
                try {
                    Date date1 = new SimpleDateFormat("dd/MM/yy",Locale.UK).parse(values.get(3));
                    if(newDate.compareTo(date1) == 0){
                        category.append(values.get(0));
                        subCat.append(values.get(1));
                        notes.append(values.get(2));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            row.addView(date);
            row.addView(lesion);
            row.addView(infection);
            row.addView(spread);
            row.addView(category);
            row.addView(subCat);
            row.addView(notes);
            timeline.addView(row,i+1);
        }
    }

    public List<Integer> getAnimals(int id) {
        List<Integer> animals = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Animal.AnimalEntry.TABLE_NAME + " WHERE farm=" + id;

        DatabaseHelper mDbHelper = new DatabaseHelper(Timeline.this);
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

            DatabaseHelper mDbHelper = new DatabaseHelper(Timeline.this);
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

    public Map<String,Date> getInfectionDates(List<Integer> animals) throws ParseException {
        Map<String,Date> lesions = new HashMap<>();

        DatabaseHelper mDbHelper = new DatabaseHelper(Timeline.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        for(int i = 0; i < animals.size(); i++){
            String selectQuery = "SELECT * FROM " + Lesion.LesionEntry.TABLE_NAME + " WHERE animal=" + animals.get(i);

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    String strDate = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_INF_MIN));
                    Date date = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(strDate);
                    int id = cursor.getInt(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_ID));
                    lesions.put(Integer.toString(id),date);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        Date oldLesion = Collections.min(lesions.values());

        String key = null;

        for(Map.Entry entry: lesions.entrySet()){
            if(oldLesion.equals(entry.getValue())){
                key = (String) entry.getKey();
                break; //breaking because its one to one map
            }
        }

        Map<String,Date> dates = new HashMap<>();
        String newQuery = "SELECT * FROM " + Lesion.LesionEntry.TABLE_NAME + " WHERE id=" + Integer.parseInt(key);

        Cursor cursor = db.rawQuery(newQuery, null);
        cursor.moveToFirst();
        String likeInfMin = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_INF_MIN));
        String likeInfMax = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_INF_MAX));
        String possInfMin = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_INF_MIN));
        String possInfMax = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_INF_MAX));
        Date likeInfMinDate = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(likeInfMin);
        Date likeInfMaxDate = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(likeInfMax);
        Date possInfMinDate = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(possInfMin);
        Date possInfMaxDate = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(possInfMax);
        dates.put("likely_inf_min",likeInfMinDate);
        dates.put("likely_inf_max",likeInfMaxDate);
        dates.put("poss_inf_min",possInfMinDate);
        dates.put("poss_inf_max",possInfMaxDate);
        cursor.close();


        return dates;

    }

    public Map<String,Date> getSpreadDates(List<Integer> animals) throws ParseException {
        Map<String,Date> lesions = new HashMap<>();

        DatabaseHelper mDbHelper = new DatabaseHelper(Timeline.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        for(int i = 0; i < animals.size(); i++){
            String selectQuery = "SELECT * FROM " + Lesion.LesionEntry.TABLE_NAME + " WHERE animal=" + animals.get(i);

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    String strDate = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_INF_MIN));
                    Date date = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(strDate);
                    int id = cursor.getInt(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_ID));
                    lesions.put(Integer.toString(id),date);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        Date oldLesion = Collections.min(lesions.values());
        Date youngLesion = Collections.max(lesions.values());

        String oldKey = null;
        String youngKey = null;

        for(Map.Entry entry: lesions.entrySet()){
            if(oldLesion.equals(entry.getValue())){
                oldKey = (String) entry.getKey();
                break; //breaking because its one to one map
            }
        }

        for(Map.Entry entry: lesions.entrySet()){
            if(youngLesion.equals(entry.getValue())){
                youngKey = (String) entry.getKey();
                break; //breaking because its one to one map
            }
        }

        Map<String,Date> dates = new HashMap<>();

        String oldQuery = "SELECT * FROM " + Lesion.LesionEntry.TABLE_NAME + " WHERE id=" + Integer.parseInt(oldKey);
        Cursor cursor = db.rawQuery(oldQuery, null);
        cursor.moveToFirst();
        String likeSprMin = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_SPR_MIN));
        String possSprMin = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_SPR_MIN));
        Date likeSprMinDate = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(likeSprMin);
        Date possSprMinDate = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(possSprMin);
        dates.put("likely_spr_min",likeSprMinDate);
        dates.put("poss_spr_min",possSprMinDate);
        cursor.close();

        String youngQuery = "SELECT * FROM " + Lesion.LesionEntry.TABLE_NAME + " WHERE id=" + Integer.parseInt(youngKey);
        cursor = db.rawQuery(youngQuery, null);
        cursor.moveToFirst();
        String likeSprMax = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_LIKE_SPR_MAX));
        String possSprMax = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_SPR_MAX));
        Date likeSprMaxDate = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(likeSprMax);
        Date possSprMaxDate = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(possSprMax);
        dates.put("likely_spr_max",likeSprMaxDate);
        dates.put("poss_spr_max",possSprMaxDate);
        cursor.close();

        return dates;

    }

    public List<Integer> getTracings(int id){
        List<Integer> tracings = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Tracings.TracingEntry.TABLE_NAME + " WHERE farm=" + id;

        DatabaseHelper mDbHelper = new DatabaseHelper(Timeline.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int tracing = cursor.getInt(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_ID));
                tracings.add(tracing);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return tracings;
    }

    public Map<String,List<String>> getTracingAttr(List<Integer> tracings){
        Map<String,List<String>> track = new HashMap<>();


        DatabaseHelper mDbHelper = new DatabaseHelper(Timeline.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        for(int i = 0; i < tracings.size(); i++){
            String selectQuery = "SELECT * FROM " + Tracings.TracingEntry.TABLE_NAME + " WHERE id=" + tracings.get(i);

            Cursor cursor = db.rawQuery(selectQuery, null);
            List<String> trackAttr = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    String cat;
                    String subCat;
                    String notes;
                    String date;
                    if(i != tracings.size() - 1){
                        cat = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_CATEGORY)) + "\n";
                        subCat = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_SUB_CATEGORY)) + "\n";
                        notes = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_NOTES)) + "\n";
                        date = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_DATE)) + "\n";
                    }else{
                        cat = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_CATEGORY));
                        subCat = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_SUB_CATEGORY));
                        notes = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_NOTES));
                        date = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_DATE));
                    }
                    trackAttr.add(cat);
                    trackAttr.add(subCat);
                    trackAttr.add(notes);
                    trackAttr.add(date);
                } while (cursor.moveToNext());
            }
            track.put(Integer.toString(tracings.get(i)),trackAttr);

            cursor.close();

        }
        db.close();

        return track;
    }

    public Date getMaxDate(List<Integer> lesions) throws ParseException {
        List<Date> dates = new ArrayList<>();

        for(int i = 0; i < lesions.size(); i++){
            String selectQuery = "SELECT * FROM " + Lesion.LesionEntry.TABLE_NAME + " WHERE id=" + lesions.get(i);

            DatabaseHelper mDbHelper = new DatabaseHelper(Timeline.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    String strDate = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_SPR_MAX));
                    Date date = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(strDate);
                    dates.add(date);
                    System.out.println(dates);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }

        return Collections.max(dates);
    }

    public Date getMinDate(List<Integer> lesions, List<Integer> tracings) throws ParseException {
        List<Date> dates = new ArrayList<>();
        List<Date> trackDates = new ArrayList<>();

        for(int i = 0; i < lesions.size(); i++){
            String selectQuery = "SELECT * FROM " + Lesion.LesionEntry.TABLE_NAME + " WHERE id=" + lesions.get(i);

            DatabaseHelper mDbHelper = new DatabaseHelper(Timeline.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    String strDate = cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_POSS_INF_MIN));
                    Date date = new SimpleDateFormat("dd-MM-yyyy",Locale.UK).parse(strDate);
                    dates.add(date);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }

        Date firstDate = Collections.min(dates);

        for(int i = 0; i < tracings.size(); i++){
            String selectQuery = "SELECT * FROM " + Tracings.TracingEntry.TABLE_NAME + " WHERE id=" + tracings.get(i);

            DatabaseHelper mDbHelper = new DatabaseHelper(Timeline.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    String strDate = cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_DATE));
                    Date date = new SimpleDateFormat("dd/MM/yy",Locale.UK).parse(strDate);
                    trackDates.add(date);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }

        Date secondDate = null;
        if(trackDates.size() > 0){
            secondDate = Collections.min(trackDates);
        }


        if(tracings.size() > 0){
            if(firstDate.compareTo(secondDate) < 0){
                return firstDate;
            }else{
                return  secondDate;
            }
        }else{
            return firstDate;
        }


    }

    public Date getDayZero(List<Integer> lesions) throws ParseException {
        List<Date> dates = new ArrayList<>();

        for(int i = 0; i < lesions.size(); i++){
            String selectQuery = "SELECT * FROM " + Lesion.LesionEntry.TABLE_NAME + " WHERE id=" + lesions.get(i);

            DatabaseHelper mDbHelper = new DatabaseHelper(Timeline.this);
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

    public static Date addDays(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public Date subDays(Date date, int days){
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, -days);
        return cal.getTime();
    }

    @Override
    public void onBackPressed(){
        Intent a = new Intent(Timeline.this, FarmList.class);
        startActivity(a);
    }

}
