package com.fao.fmd.fmdappbeta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiagnosticOptions extends FragmentActivity {

    Button samples;
    Button antibody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_options);

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

        Bundle b = getIntent().getExtras();
        String res = b.getString("res");

        samples = findViewById(R.id.btn_samples);
        antibody = findViewById(R.id.btn_antibody);

        Bundle bundle = new Bundle();

        samples.setOnClickListener(v -> {
            bundle.putString("days", res);
            samples.setVisibility(RelativeLayout.GONE);
            antibody.setVisibility(RelativeLayout.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new SamplesFragment();
            fragment.setArguments(bundle);
            ft.replace(R.id.placeholder, fragment,"fragment");
            ft.commit();
        });

        antibody.setOnClickListener(v -> {
            if(res.equals("1") || res.equals("2-3")){
                bundle.putString("text","For an animal with a 1-2 day lesion, it is too early for an antibody response to be detected. Antibodies to structural proteins usually begin to appear with 3 day old lesions. Antibodies to non-structural proteins begin to appear with around 4 day old lesions.\n\n" +
                        "In an endemic country, the animal may already be positive due to previous infection or vaccination.");
            }
            if(res.equals("3-4")){
                bundle.putString("text","For an animal with a 3-4 day lesion, you should be able to detect antibodies to structural proteins but maybe not to non-structural proteins which typically appear with 4 day old lesions but not earlier.\n\n" +
                        "In an endemic country, the animal may already be positive to either SP or NSP antibodies due to previous infection or vaccination.\n\n" +
                        "Take a plain blood tube (serum), and you can do a variety of structural protein antibody tests including:\n" +
                        "•\tVirus neutralisation tests (VNTs)\n" +
                        "•\tSolid-phase competition ELISA (SPCE)\n" +
                        "•\tLiquid-phase blocking ELISA (LPBE)\n" +
                        "•\tvarious commercial tests\n\n" +
                        "Note that VNTs are highly specialised tests that can only be done in laboratories equipped for cell culture work and for handling live FMD virus.");
            }
            if(res.equals("4-5")){
                bundle.putString("text","For an animal with a 4-5 day old lesion, you should be able to detect antibodies to structural and non-structural proteins. Antibodies to structural proteins usually begin to appear with 3 day old lesions. Antibodies to non-structural proteins begin to appear with 4 day old lesions.\n\n" +
                        "In an endemic country, the animal may still be positive to either SP or NSP antibodies due to previous infection or vaccination.\n\n" +
                        "Take a plain blood tube (serum), and you can do a variety of structural protein antibody tests including:\n" +
                        "•\tVirus neutralisation tests (VNTs)\n" +
                        "•\tSolid-phase competition ELISA (SPCE)\n" +
                        "•\tLiquid-phase blocking ELISA (LPBE)\n\n" +
                        "Note that VNTs are highly specialised tests that can only be done in specialised laboratories equipped for handling live FMD virus");
            }
            if(res.equals("5-7")){
                bundle.putString("text","For an animal with a 5-7 day lesion, you should be able to detect antibodies to structural proteins and non-structural proteins. Antibodies to structural proteins usually begin to appear with 3 day old lesions. Antibodies to non-structural proteins begin to appear with 4 day old lesions.\n\n" +
                        "In an endemic country, the animal may still be positive to either SP or NSP antibodies due to previous infection or vaccination.\n\n" +
                        "Take a plain blood tube (serum), and you can do a variety of structural protein antibody tests including:\n" +
                        "•\tVirus neutralisation tests (VNTs)\n" +
                        "•\tSolid-phase competition ELISA (SPCE)\n" +
                        "•\tLiquid-phase blocking ELISA (LPBE)\n\n" +
                        "Note that VNTs are highly specialised tests that can only be done in specialised laboratories equipped for handling live FMD virus\n\n" +
                        "Serum from a plain blood tube can also be used for NSP antibody using a commercial NSP-ELISA test.");
            }
            if(res.equals("7-10") || res.equals("10-14") || res.equals("14+")){
                bundle.putString("text","For an animal with a ≥7 day old lesion, you should be able to detect antibodies to structural proteins and non-structural proteins. Antibodies to structural proteins usually begin to appear with 3 day old lesions. Antibodies to non-structural proteins begin to appear with 4 day old lesions.\n\n" +
                        "In an endemic country, the animal may still be positive to either SP or NSP antibodies due to previous infection or vaccination.\n\n" +
                        "Take a plain blood tube (serum), and you can do a variety of structural protein antibody tests including:\n" +
                        "•\tVirus neutralisation tests (VNTs)\n" +
                        "•\tSolid-phase competition ELISA (SPCE)\n" +
                        "•\tLiquid-phase blocking ELISA (LPBE)\n\n" +
                        "Note that VNTs are highly specialised tests that can only be done in specialised laboratories equipped for handling live FMD virus\n\n" +
                        "Serum from a plain blood tube can also be used for NSP antibody using a commercial NSP-ELISA test.");
            }
            samples.setVisibility(RelativeLayout.GONE);
            antibody.setVisibility(RelativeLayout.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new DisplayTextFragment();
            fragment.setArguments(bundle);
            ft.replace(R.id.placeholder, fragment,"fragment");
            ft.commit();
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

        ImageView info = findViewById(R.id.qmark);
        info.setOnClickListener(v -> {
            Bundle bu = new Bundle();
            bu.putString("tag","options");
            Intent intent = new Intent(DiagnosticOptions.this, InfoPage.class);
            intent.putExtras(bu);
            startActivity(intent);
        });
    }

    @Override
    public void onBackPressed() {
        if(samples.getVisibility() == View.GONE && antibody.getVisibility() == View.GONE){
            samples.setVisibility(RelativeLayout.VISIBLE);
            antibody.setVisibility(RelativeLayout.VISIBLE);
            Fragment frag = getSupportFragmentManager().findFragmentByTag("fragment");
            frag.getView().setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
}
