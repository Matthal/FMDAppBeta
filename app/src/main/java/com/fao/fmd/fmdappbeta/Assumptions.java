package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Assumptions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assumptions);

        TextView light = findViewById(R.id.light);
        TextView dark = findViewById(R.id.dark);
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

        TextView assumptions = findViewById(R.id.assumptions);

        String text = "The following is a list of assumptions used in estimating the age of the lesion based on the information provided in the calculator:\n\n" +
                "1.\tConsidering cattle only\n" +
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
        back.setOnClickListener(v -> onBackPressed());

    }
}
