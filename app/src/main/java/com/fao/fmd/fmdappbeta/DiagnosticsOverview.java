package com.fao.fmd.fmdappbeta;

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

public class DiagnosticsOverview extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostics_overview);

        Button vesicular = findViewById(R.id.vesicular);
        Button epithelium = findViewById(R.id.epithelium);
        Button swabs = findViewById(R.id.swabs);
        Button probang = findViewById(R.id.probang);

        Button structural = findViewById(R.id.structural);
        Button nonStructural = findViewById(R.id.non_structural);

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

        final Bundle b = new Bundle();

        vesicular.setOnClickListener(v -> {
            b.putString("text","\nThis is the richest source of virus and in a live animal but is difficult to get. If a vesicle is intact, you need to take a narrow gauge needle to extract the fluid which can be very challenging due to difficulties restraining the animal. Vesicular fluid may also be collected on a swab in a freshly ruptured vesicle so be ready when you begin examination just in case this happens!\n\n" +
                    "Vesicular fluid should be stored in a plain tube (no transport medium is necessary).\n\n" +
                    "Virus isolation, antigen ELISA or PCR methods can be used to detect the presence of virus. You can also test the fluid on a lateral flow device (LFD)");
            RelativeLayout mainLayout= findViewById(R.id.main);
            mainLayout.setVisibility(RelativeLayout.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new DisplayTextFragment();
            fragment.setArguments(b);
            ft.replace(R.id.placeholder, fragment,"fragment");
            ft.commit();
        });
        epithelium.setOnClickListener(v -> {
            b.putString("text","\nIf a vesicle has burst, it is often possible to take a sample of epithelium. This is a very rich source of virus especially in 1-2 day old lesions but you can also get positive results from 3-4 day lesions although this is less likely.\n\n" +
                    "Good animal restrain is needed to take the sample and sedation may be needed.\n\n" +
                    "Sometimes epithelium can be pulled off with one’s fingers, but using tweezers/forceps and scissors is preferred if possible and safe to do.\n\n" +
                    "At least 2cm^2 of epithelium is needed (roughly a thumbnail sized amount).\n\n" +
                    "Samples should be stored in appropriate transport medium and stored at 2-8°C or in the freezer (-20°C or -80°C).\n\n" +
                    "Virus isolation, antigen ELISA or PCR methods can be used to detect the presence of virus. You can also test the fluid on a lateral flow device (LFD)");
            RelativeLayout mainLayout= findViewById(R.id.main);
            mainLayout.setVisibility(RelativeLayout.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new DisplayTextFragment();
            fragment.setArguments(b);
            ft.replace(R.id.placeholder, fragment,"fragment");
            ft.commit();
        });
        swabs.setOnClickListener(v -> {
            b.putString("text","\nIf it is not possible to take vesicular fluid or epithelium samples, a plain swab directly applied to the lesion can be taken for lesions up to several days of age.\n\n" +
                    "Orophayngeal swabs can also be taken.\n\n" +
                    "Swabs should be stored at 2-8°C or in the freezer (-20°C or -80°C).\n\n" +
                    "You are unlikely to recover live virus, particularly in older lesions.\n\n" +
                    "Virus isolation, antigen ELISA or PCR methods can be used to detect the presence of virus.");
            RelativeLayout mainLayout= findViewById(R.id.main);
            mainLayout.setVisibility(RelativeLayout.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new DisplayTextFragment();
            fragment.setArguments(b);
            ft.replace(R.id.placeholder, fragment,"fragment");
            ft.commit();
        });
        probang.setOnClickListener(v -> {
            b.putString("text","\nProbang sampling involves taking a sample of cellular material from the oropharynx using a metal cup introduced on the end of a semi-flexible wire.\n\n" +
                    "It is used where it is not possible to get vesicular fluid or epithelium but live virus is required (useful for some diagnostic tests).\n\n" +
                    "Probang sampling are also used to test for the presence of FMD virus carriers\n\n" +
                    "For animals with oral lesions, depending on the particular case, performing this test can cause a lot of distress and may be best suited for animals with older lesions or those that are fully recovered\n\n" +
                    "The steps to do a probang are:\n" +
                    "I.\tEnsure adequate restraint\n" +
                    "II.\tMeasure the distance to the oropharynx on the outside of the animal and mark on the wire of the probang as a guide.\n" +
                    "III.\tIntroduce the probang into the mouth centrally- if it deviates to the side you will feel the animal chewing it\n" +
                    "IV.\tFeel the outside of the larynx and upper oesophagus- you will feel as the probang reaches this area\n" +
                    "V.\tGently move the probang backwards and forwards five times in this region\n" +
                    "VI.\tGently withdraw the probang, aiming to keep the cup upright so as not to tip out the sample\n" +
                    "VII.\tTransfer the sample to probang transport medium");
            RelativeLayout mainLayout= findViewById(R.id.main);
            mainLayout.setVisibility(RelativeLayout.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new DisplayTextFragment();
            fragment.setArguments(b);
            ft.replace(R.id.placeholder, fragment,"fragment");
            ft.commit();
        });

        structural.setOnClickListener(v -> {
            b.putString("text","Appear alongside 3-4 day lesions.\n\n" +
                    "Serotype specific.\n\n" +
                    "Induced by all FMD vaccines.\n\n" +
                    "In an endemic country, a suspected clinical case may be positive to SP antibodies due to previous infection or vaccination.\n\n" +
                    "Take a plain blood tube (serum), and you can do a variety of structural protein antibody tests including:\n" +
                    "o\tVirus neutralisation tests (VNTs)\n" +
                    "o\tSolid-phase competition ELISA (SPCE)\n" +
                    "o\tLiquid-phase blocking ELISA (LPBE)\n\n" +
                    "Note that VNTs are highly specialised tests that can only be done in specialised laboratories equipped for handling live FMD virus.\n\n" +
                    "Can get cross-reactivity between different serotypes making the results challenging to interpret particular in endemic countries with multiple serotypes circulating.");
            RelativeLayout mainLayout= findViewById(R.id.main);
            mainLayout.setVisibility(RelativeLayout.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new DisplayTextFragment();
            fragment.setArguments(b);
            ft.replace(R.id.placeholder, fragment,"fragment");
            ft.commit();
        });
        nonStructural.setOnClickListener(v -> {
            b.putString("text","\nAppear alongside 6-7 day lesions.\n\n" +
                    "Pan-serotype (this test does not distinguish between serotypes).\n\n" +
                    "Some vaccines are purified of NSPs, so you can distinguish between infected and vaccinated animals (“DIVA”).\n\n" +
                    "In an endemic country, a suspected clinical case may be positive to NSP antibodies due to previous infection or vaccination.\n\n" +
                    "Serum from a plain blood tube can be used for NSP antibody testing using a commercial NSP-ELISA test.");
            RelativeLayout mainLayout= findViewById(R.id.main);
            mainLayout.setVisibility(RelativeLayout.GONE);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment fragment = new DisplayTextFragment();
            fragment.setArguments(b);
            ft.replace(R.id.placeholder, fragment,"fragment");
            ft.commit();
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        RelativeLayout mainLayout = findViewById(R.id.main);
        if(mainLayout.getVisibility() == View.GONE){
            mainLayout.setVisibility(RelativeLayout.VISIBLE);
            Fragment frag = getSupportFragmentManager().findFragmentByTag("fragment");
            frag.getView().setVisibility(View.GONE);
        }else{
            super.onBackPressed();
        }
    }
}
