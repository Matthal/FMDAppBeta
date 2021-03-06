package com.fao.fmd.fmdappbeta;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CompleteFragment extends Fragment {

    View view;
    Bundle bundle;

    boolean added = false;

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.UK);

    public CompleteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_complete, container, false);

        bundle = this.getArguments();

        System.out.println(bundle);

        TextView light = view.findViewById(R.id.light);
        TextView dark = view.findViewById(R.id.dark);
        LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.75f
        );
        LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.25f
        );
        light.setLayoutParams(paramLight);
        dark.setLayoutParams(paramDark);

        TextView id = view.findViewById(R.id.lesID);
        id.append(Integer.toString(bundle.getInt("id")));

        String res = bundle.getString("res");
        TextView text = view.findViewById(R.id.days);
        text.append(res);

        Button exBtn = view.findViewById(R.id.exBtn);
        Button diagnBtn = view.findViewById(R.id.diagnBtn);
        Button lesBtn = view.findViewById(R.id.lesBtn);
        Button timelineBtn = view.findViewById(R.id.timelineBtn);
        Button assumptBtn = view.findViewById(R.id.assumptBtn);

        ImageView next = view.findViewById(R.id.next);
        ImageView back = view.findViewById(R.id.back);

        exBtn.setOnClickListener(v -> {
            Bundle g = new Bundle();
            g.putString("img",res);
            Intent intent = new Intent(getActivity(), VesiclesGallery.class);
            intent.putExtras(g);
            startActivity(intent);
        });
        lesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LesionAgeing.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        diagnBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DiagnosticOptions.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });
        assumptBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Assumptions.class);
            startActivity(intent);
        });
        timelineBtn.setOnClickListener(v -> addDBEntry());

        next.setOnClickListener(v -> {
            if(!added){
                Toast.makeText(getActivity(), "You must add the lesion to the farm timeline before continuing", Toast.LENGTH_LONG).show();
                return;
            }
            Intent intent = new Intent(getActivity(), PostLesion.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        back.setOnClickListener(v -> getActivity().onBackPressed());

        ImageView info = view.findViewById(R.id.qmark);
        info.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putString("tag","complete");
            Intent intent = new Intent(getActivity(), InfoPage.class);
            intent.putExtras(b);
            startActivity(intent);
        });

        return view;
    }

    public void addDBEntry(){
        DatabaseHelper mDbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String age = bundle.getString("res");
        System.out.println(age);
        int old;

        if(age.length() < 2){
            old = Integer.parseInt(age);
        }else{
            if(age.charAt(1) == '-'){
                if(age.length() == 3){
                    old = Character.getNumericValue(age.charAt(2));
                }else{
                    old = Integer.parseInt(age.substring(2,4));
                }
            }else{
                old = Integer.parseInt(age.substring(0,2));
            }
        }

        int like_inf_min = old + 6;
        int like_inf_max = old + 2;
        int pos_inf_min = old + 14;
        int pos_inf_max = old + 1;
        int like_spr_min = old;
        int like_spr_max = old - 3;
        int pos_spr_min = old + 2;
        int pos_spr_max = old - 14;

        ContentValues values = new ContentValues();
        values.put(Lesion.LesionEntry.COLUMN_ANIMAL, bundle.getInt("id"));
        values.put(Lesion.LesionEntry.COLUMN_AGE, bundle.getString("res"));
        values.put(Lesion.LesionEntry.COLUMN_VISIT_DATE, df.format(Calendar.getInstance().getTime()));
        values.put(Lesion.LesionEntry.COLUMN_LIKE_INF_MIN, subDays(like_inf_min));
        values.put(Lesion.LesionEntry.COLUMN_LIKE_INF_MAX, subDays(like_inf_max));
        values.put(Lesion.LesionEntry.COLUMN_POSS_INF_MIN, subDays(pos_inf_min));
        values.put(Lesion.LesionEntry.COLUMN_POSS_INF_MAX, subDays(pos_inf_max));
        values.put(Lesion.LesionEntry.COLUMN_LIKE_SPR_MIN, subDays(like_spr_min));
        values.put(Lesion.LesionEntry.COLUMN_LIKE_SPR_MAX, subDays(like_spr_max));
        values.put(Lesion.LesionEntry.COLUMN_POSS_SPR_MIN, subDays(pos_spr_min));
        values.put(Lesion.LesionEntry.COLUMN_POSS_SPR_MAX, subDays(pos_spr_max));

        String query = "Select * from " + Lesion.LesionEntry.TABLE_NAME + " where " + Lesion.LesionEntry.COLUMN_ANIMAL + " = " + bundle.getInt("id");
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToLast();

        if(cursor.getCount()>0){
            long newRowId = db.update(Lesion.LesionEntry.TABLE_NAME, values, "id= ?",new String[]{cursor.getString(cursor.getColumnIndex(Lesion.LesionEntry.COLUMN_ID))});
            if(newRowId == -1){
                Toast.makeText(getActivity(), "Error in the DB", Toast.LENGTH_SHORT).show();
                db.close();
            }else {
                Toast.makeText(getActivity(), "Lesion successfully updated", Toast.LENGTH_SHORT).show();
                added = true;
                db.close();
            }
        }else{
            long newRowId = db.insert(Lesion.LesionEntry.TABLE_NAME, null, values);
            if(newRowId == -1){
                Toast.makeText(getActivity(), "Error in the DB", Toast.LENGTH_SHORT).show();
                db.close();
            }else {
                Toast.makeText(getActivity(), "New lesion added to the DB", Toast.LENGTH_SHORT).show();
                added = true;
                db.close();
            }
        }
        cursor.close();

    }

    public String subDays(int days){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        Date c = calendar.getTime();
        return df.format(c);
    }

}

