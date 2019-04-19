package com.fao.fmd.fmdappbeta;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TracingList extends AppCompatActivity {

    TracingListAdapter listAdapter;
    ExpandableListView tracingList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing_list);

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

        bundle = getIntent().getExtras();

        int farm = bundle.getInt("id");

        tracingList = findViewById(R.id.tracingList);
        ImageView back = findViewById(R.id.back);

        back.setOnClickListener(v -> onBackPressed());

        prepareListData(farm);

        listAdapter = new TracingListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        tracingList.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    private void prepareListData(int farm) {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        DatabaseHelper mDbHelper = new DatabaseHelper(TracingList.this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selectQuery = "SELECT * FROM " + Tracings.TracingEntry.TABLE_NAME + " WHERE farm=" + farm;
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        for(int i = 0; i < cursor.getCount(); i++){
            List<String> details = new ArrayList<>();
            listDataHeader.add("Tracing " + cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_ID)));
            details.add("Category: " + cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_CATEGORY)) + "\n" +
                    "Sub-Category: " + cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_SUB_CATEGORY)) +  "\n" +
                    "Date: " + cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_DATE)) + "\n" +
                    "Notes: " + cursor.getString(cursor.getColumnIndex(Tracings.TracingEntry.COLUMN_NOTES)));
            listDataChild.put(listDataHeader.get(i), details);
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

    }
}
