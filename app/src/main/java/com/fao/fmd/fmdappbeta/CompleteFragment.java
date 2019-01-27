package com.fao.fmd.fmdappbeta;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

    public CompleteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_complete, container, false);

        bundle = this.getArguments();

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

        exBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle g = new Bundle();
                g.putString("img",res);
                Intent intent = new Intent(getActivity(), VesiclesGallery.class);
                intent.putExtras(g);
                startActivity(intent);
            }
        });
        lesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LesionAgeing.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        diagnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DiagnosticOptions.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        assumptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Assumptions.class);
                startActivity(intent);
            }
        });
        timelineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDBEntry();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mettere Controllo Timeline aggiunta a DB
                Intent intent = new Intent(getActivity(), PostLesion.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    public void addDBEntry(){
        DatabaseHelper mDbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String age = bundle.getString("res");
        int old;

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

        int like_inf_min = old + 6;
        int like_inf_max = old + 2;
        int pos_inf_min = old + 14;
        int pos_inf_max = old + 1;
        int like_spr_min = old + 1;
        int like_spr_max = old - 2;
        int pos_spr_min = old + 3;

        ContentValues values = new ContentValues();
        values.put(Lesion.LesionEntry.COLUMN_ANIMAL, bundle.getInt("id"));
        values.put(Lesion.LesionEntry.COLUMN_AGE, bundle.getString("res"));
        values.put(Lesion.LesionEntry.COLUMN_LIKE_INF_MIN, subDays(like_inf_min));
        values.put(Lesion.LesionEntry.COLUMN_LIKE_INF_MAX, subDays(like_inf_max));
        values.put(Lesion.LesionEntry.COLUMN_POSS_INF_MIN, subDays(pos_inf_min));
        values.put(Lesion.LesionEntry.COLUMN_POSS_INF_MAX, subDays(pos_inf_max));
        values.put(Lesion.LesionEntry.COLUMN_LIKE_SPR_MIN, subDays(like_spr_min));
        values.put(Lesion.LesionEntry.COLUMN_LIKE_SPR_MAX, subDays(like_spr_max));
        values.put(Lesion.LesionEntry.COLUMN_POSS_SPR_MIN, subDays(pos_spr_min));
        values.put(Lesion.LesionEntry.COLUMN_POSS_SPR_MAX, subDays(0));

        long newRowId = db.insert(Lesion.LesionEntry.TABLE_NAME, null, values);

        if(newRowId == -1){
            Toast.makeText(getActivity(), "Error in the DB",
                    Toast.LENGTH_LONG).show();
            db.close();
        }else {
            Toast.makeText(getActivity(), "New entry added to the DB",
                    Toast.LENGTH_LONG).show();
            db.close();
        }
    }

    public String subDays(int days){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        Date c = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy",Locale.UK);
        return df.format(c);
    }

}

