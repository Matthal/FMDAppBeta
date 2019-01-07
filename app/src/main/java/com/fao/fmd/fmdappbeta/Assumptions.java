package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Assumptions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assumptions);

        TextView assumptions = findViewById(R.id.assumptions);

        String text = "1.\tConsidering cattle only\n" +
                "2.\tConsidering oral lesions only\n" +
                "3.\tOnset of clinical signs is day 0\n" +
                "4.\tOnset of lesions is day 1\n" +
                "5.\tFibrin appears from day 3 onwards\n" +
                "6.\tIf the operator says there is no fibrin present, it is assumed that this could have been wiped off so reported absence does not necessary indicate a fresh or old lesions\n" +
                "7.\tLesions start to appear pink at around 3 days old, but due to the obvious subjectivity the algorithm does not rigidly stick to this assumption\n" +
                "8.\tLesion edges start to appear rounded/smooth at 3 days of age but due to the obvious subjectivity the algorithm does not rigidly stick to this assumption\n" +
                "9.\tHealing around the edges of the lesion appears from 3 days onward and becomes complete between 5-7 days";

        assumptions.setText(text);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}
