package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DiagnosticOptions extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnostic_options);

        Bundle b = getIntent().getExtras();
        String res = b.getString("res");

        ExpandableListView diagnList = findViewById(R.id.diagnList);
        TextView test = findViewById(R.id.test);

        List<String> listDataHeader = new ArrayList<>();
        HashMap<String, List<String>> listDataChild = new HashMap<>();

        if(res.equals("1-2") || res.equals("2-3")){

            listDataHeader.add("Vesicular fluid");
            listDataHeader.add("Epithelium samples");
            listDataHeader.add("Swabs");
            listDataHeader.add("Cardiac muscle");
            listDataHeader.add("Blood");

            List<String> ves = new ArrayList<>();
            ves.add("This is the richest source of virus and in a live animal but is difficult to get. If a vesicle is intact, you need to take a narrow gauge needle to extract the fluid which can be very challenging due to difficulties restraining the animal. Vesicular fluid may also be collected on a swab in a freshly ruptured vesicle so be ready when you begin examination just in case this happens!");
            ves.add("Vesicular fluid should be stored in a plain tube (no transport medium is necessary)");
            ves.add("Virus isolation, antigen ELISA or RT-PCR methods can be used to detect the presence of virus. You can also test the fluid on a lateral flow device (LFD)");

            List<String> epi = new ArrayList<>();
            epi.add("If a vesicle has burst, it is often possible to take a sample of epithelium. This is a very rich source of virus especially in 1-2 day old lesions.");
            epi.add("Good animal restrain is needed to take the sample and sedation may be needed.");
            epi.add("Sometimes epithelium can be pulled off with one’s fingers, but using tweezers/forceps and scissors is preferred if possible and safe to do.");
            epi.add("At least 2cm^2 of epithelium is optimal (roughly a thumbnail sized amount).");
            epi.add("Samples should be stored in appropriate transport medium and stored at 2-8°C or in the freezer (-20°C or -80°C)");
            epi.add("Virus isolation, antigen ELISA or RT-PCR methods can be used to detect the presence of virus. You can also test the fluid on a lateral flow device (LFD)");

            List<String> swa = new ArrayList<>();
            swa.add("If it is not possible to take vesicular fluid or epithelium samples, a plain swab directly applied to the lesion can be taken although this is not optimal due to lower concentrations of virus present compared to epithelium and vesicular fluid.");
            swa.add("Orophayngeal swabs can also be taken, in either case, collected into a small volume of transport medium or lysis buffer (for RT-PCR).");
            swa.add("Swabs should be stored at 2-8°C or in the freezer (-20°C or -80°C)");
            swa.add("You are unlikely to recover live virus.");
            swa.add("Virus isolation or RT-PCR methods can be used to detect the presence of virus.");

            List<String> car = new ArrayList<>();
            car.add("If the animal has died from myocarditis, virus can be detected in heart muscle");
            car.add("A piece of myocardium needs to be put in a plain pot (NOT formalin) in transport medium and sent immediately to the laboratory");
            car.add("The sample should be stored at 2-8°C");
            car.add("Virus isolation, antigen ELISA or RT-PCR methods can be used to detect the presence of virus.");

            List<String> blo = new ArrayList<>();
            blo.add("Note that blood samples are NOT the optimum sample for detecting virus due to low concentrations present");
            blo.add("RT-PCR methods are essential");

            listDataChild.put(listDataHeader.get(0), ves);
            listDataChild.put(listDataHeader.get(1), epi);
            listDataChild.put(listDataHeader.get(2), swa);
            listDataChild.put(listDataHeader.get(3), car);
            listDataChild.put(listDataHeader.get(4), blo);

            String text = "For an animal with a 1-2 day lesion, it is too early for an antibody response to be detected. Antibodies to structural proteins usually begin to appear with 3 day old lesions. Antibodies to non-structural proteins begin to appear with around 4 day old lesions.\n\n" +
                    "In an endemic country, the animal may already be positive due to previous infection or vaccination.";
            test.setText(text);
        }

        if(res.equals("3-4")){

            listDataHeader.add("Epithelium samples");
            listDataHeader.add("Swabs");
            listDataHeader.add("Blood");

            List<String> epi = new ArrayList<>();
            epi.add("With 3-4 day old lesions, if epithelium is present it is often starting to go necrotic and lower levels of virus are present compared to 1-2 day old lesions. However, if available (more likely from between the digits of the feet than in the mouth) it is still worth taking a sample");
            epi.add("Good animal restrain is needed to take the sample and sedation may be needed.");
            epi.add("Sometimes epithelium can be pulled off with one’s fingers, but using tweezers/forceps and scissors is preferred if possible and safe to do.");
            epi.add("At least 2cm^2 of epithelium is needed (roughly a thumbnail sized amount).");
            epi.add("Samples should be stored in appropriate transport medium and stored at 2-8°C or in the freezer (-20°C or -80°C)");
            epi.add("Virus isolation, antigen ELISA or RT-PCR methods can be used to detect the presence of virus. You can also test the fluid on a lateral flow device (LFD)");

            List<String> swa = new ArrayList<>();
            swa.add("If it is not possible to take epithelium samples, a plain swab directly applied to the lesion can be taken although virus levels tend to be lower");
            swa.add("Orophayngeal swabs can also be taken");
            swa.add("Swabs should be stored at 2-8°C or in the freezer (-20°C or -80°C)");
            swa.add("You are unlikely to recover live virus.");
            swa.add("Virus isolation or PCR methods can be used to detect the presence of virus.");

            List<String> blo = new ArrayList<>();
            blo.add("In animals with this age of lesion, blood samples are very unlikely to detect virus even with sensitive RT-PCR methods");

            listDataChild.put(listDataHeader.get(0), epi);
            listDataChild.put(listDataHeader.get(1), swa);
            listDataChild.put(listDataHeader.get(2), blo);

            String text = "For an animal with a 3-4 day lesion, you should be able to detect antibodies to structural proteins but maybe not to non-structural proteins which typically appear with 4 day old lesions but not earlier.\n\n" +
                    "In an endemic country, the animal may already be positive to either SP or NSP antibodies due to previous infection or vaccination.\n\n" +
                    "Take a plain blood tube (serum), and you can do a variety of structural protein antibody tests including:\n" +
                    "•\tVirus neutralisation tests (VNTs)\n" +
                    "•\tSolid-phase competition ELISA (SPCE)\n" +
                    "•\tLiquid-phase blocking ELISA (LPBE)\n" +
                    "•\tvarious commercial tests\n\n" +
                    "Note that VNTs are highly specialised tests that can only be done in laboratories equipped for cell culture work and for handling live FMD virus.";
            test.setText(text);
        }

        if(res.equals("4-5")){

            listDataHeader.add("Swabs");
            listDataHeader.add("Probang");
            listDataHeader.add("Blood");

            List<String> swa = new ArrayList<>();
            swa.add("A plain swab directly applied to the lesion can be taken and you may be able to detect virus");
            swa.add("Orophayngeal swabs can also be taken");
            swa.add("Swabs should be stored at 2-8°C or in the freezer (-20°C or -80°C)");
            swa.add("You are unlikely to recover live virus.");
            swa.add("Virus isolation or PCR methods can be used to detect the presence of virus. ");

            List<String> pro = new ArrayList<>();
            pro.add("Probang sampling involves taking a sample of mucus with cellular material from the oropharynx using a metal cup introduced on the end of a semi-flexible wire");
            pro.add("It is used where it is not possible to get vesicular fluid or epithelium but live virus is required (useful for some diagnostic tests)");
            pro.add("Probang sampling are also used to test for the presence of FMD virus carriers");
            pro.add("For animals with oral lesions, depending on the particular case, performing this test can cause a lot of distress and may be best suited for animals with older lesions");
            pro.add("The steps to do a probang are:\n" +
                    "I.\tEnsure adequate restraint\n" +
                    "II.\tMeasure the distance to the oropharynx on the outside of the animal and mark on the wire of the probang as a guide.\n" +
                    "III.\tIntroduce the probang into the mouth centrally- if it deviates to the side you will feel the animal chewing it\n" +
                    "IV.\tFeel/watch the outside of the larynx and upper oesophagus on the left side of the animal- you will feel/see as the probang reaches this area\n" +
                    "V.\tGently move the probang backwards and forwards five times in this region\n" +
                    "VI.\tGently withdraw the probang, aiming to keep the cup upright so as not to tip out the sample\n" +
                    "VII.\tTransfer the sample to probang transport medium");

            List<String> blo = new ArrayList<>();
            blo.add("You will not detect virus from blood of animals with lesions of this age");

            listDataChild.put(listDataHeader.get(0), swa);
            listDataChild.put(listDataHeader.get(1), pro);
            listDataChild.put(listDataHeader.get(2), blo);

            String text = "For an animal with a 4-5 day old lesion, you should be able to detect antibodies to structural and non-structural proteins. Antibodies to structural proteins usually begin to appear with 3 day old lesions. Antibodies to non-structural proteins begin to appear with 4 day old lesions.\n\n" +
                    "In an endemic country, the animal may still be positive to either SP or NSP antibodies due to previous infection or vaccination.\n\n" +
                    "Take a plain blood tube (serum), and you can do a variety of structural protein antibody tests including:\n" +
                    "•\tVirus neutralisation tests (VNTs)\n" +
                    "•\tSolid-phase competition ELISA (SPCE)\n" +
                    "•\tLiquid-phase blocking ELISA (LPBE)\n\n" +
                    "Note that VNTs are highly specialised tests that can only be done in specialised laboratories equipped for handling live FMD virus";
            test.setText(text);
        }

        if(res.equals("5-7")){

            listDataHeader.add("Swabs");
            listDataHeader.add("Probang");

            List<String> swa = new ArrayList<>();
            swa.add("With this age of lesion, swabs applied to the lesion may be able to detect virus using PCR methods. It is unlikely you will find live virus");
            swa.add("Orophayngeal swabs can also be taken");
            swa.add("Swabs should be stored at 2-8°C or in the freezer (-20°C or -80°C)");

            List<String> pro = new ArrayList<>();
            pro.add("Probang sampling involves taking a sample of cellular material from the oropharynx using a metal cup introduced on the end of a semi-flexible wire");
            pro.add("It is used where it is not possible to get vesicular fluid or epithelium but live virus is required (useful for some diagnostic tests)");
            pro.add("Probang sampling are also used to test for the presence of FMD virus carriers");
            pro.add("For animals with oral lesions, depending on the particular case, performing this test can cause a lot of distress and may be best suited for animals with older lesions or those that are fully recovered");
            pro.add("The steps to do a probang are:\n" +
                    "I.\tEnsure adequate restraint\n" +
                    "II.\tMeasure the distance to the oropharynx on the outside of the animal and mark on the wire of the probang as a guide.\n" +
                    "III.\tIntroduce the probang into the mouth centrally- if it deviates to the side you will feel the animal chewing it\n" +
                    "IV.\tFeel the outside of the larynx and upper oesophagus- you will feel as the probang reaches this area\n" +
                    "V.\tGently move the probang backwards and forwards five times in this region\n" +
                    "VI.\tGently withdraw the probang, aiming to keep the cup upright so as not to tip out the sample\n" +
                    "VII.\tTransfer the sample to probang transport medium");

            listDataChild.put(listDataHeader.get(0), swa);
            listDataChild.put(listDataHeader.get(1), pro);

            String text = "For an animal with a 5-7 day lesion, you should be able to detect antibodies to structural proteins and non-structural proteins. Antibodies to structural proteins usually begin to appear with 3 day old lesions. Antibodies to non-structural proteins begin to appear with 4 day old lesions.\n\n" +
                    "In an endemic country, the animal may still be positive to either SP or NSP antibodies due to previous infection or vaccination.\n\n" +
                    "Take a plain blood tube (serum), and you can do a variety of structural protein antibody tests including:\n" +
                    "•\tVirus neutralisation tests (VNTs)\n" +
                    "•\tSolid-phase competition ELISA (SPCE)\n" +
                    "•\tLiquid-phase blocking ELISA (LPBE)\n\n" +
                    "Note that VNTs are highly specialised tests that can only be done in specialised laboratories equipped for handling live FMD virus\n\n" +
                    "Serum from a plain blood tube can also be used for NSP antibody using a commercial NSP-ELISA test.";
            test.setText(text);
        }

        if(res.equals("7-10") || res.equals("10-14") || res.equals("14+")){

            listDataHeader.add("Probang");

            List<String> pro = new ArrayList<>();
            pro.add("Probang sampling involves taking a sample of cellular material from the oropharynx using a metal cup introduced on the end of a semi-flexible wire");
            pro.add("It is used where it is not possible to get vesicular fluid or epithelium but live virus is required (useful for some diagnostic tests)");
            pro.add("Probang sampling are also used to test for the presence of FMD virus carriers");
            pro.add("For animals with oral lesions, depending on the particular case, performing this test can cause a lot of distress and may be best suited for animals with older lesions");
            pro.add("The steps to do a probang are:\n" +
                    "I.\tEnsure adequate restraint\n" +
                    "II.\tMeasure the distance to the oropharynx on the outside of the animal and mark on the wire of the probang as a guide.\n" +
                    "III.\tIntroduce the probang into the mouth centrally- if it deviates to the side you will feel the animal chewing it\n" +
                    "IV.\tFeel the outside of the larynx and upper oesophagus- you will feel as the probang reaches this area\n" +
                    "V.\tGently move the probang backwards and forwards five times in this region\n" +
                    "VI.\tGently withdraw the probang, aiming to keep the cup upright so as not to tip out the sample\n" +
                    "VII.\tTransfer the sample to probang transport medium");

            listDataChild.put(listDataHeader.get(0), pro);

            String text = "For an animal with a ≥7 day old lesion, you should be able to detect antibodies to structural proteins and non-structural proteins. Antibodies to structural proteins usually begin to appear with 3 day old lesions. Antibodies to non-structural proteins begin to appear with 4 day old lesions.\n\n" +
                    "In an endemic country, the animal may still be positive to either SP or NSP antibodies due to previous infection or vaccination.\n\n" +
                    "Take a plain blood tube (serum), and you can do a variety of structural protein antibody tests including:\n" +
                    "•\tVirus neutralisation tests (VNTs)\n" +
                    "•\tSolid-phase competition ELISA (SPCE)\n" +
                    "•\tLiquid-phase blocking ELISA (LPBE)\n\n" +
                    "Note that VNTs are highly specialised tests that can only be done in specialised laboratories equipped for handling live FMD virus\n\n" +
                    "Serum from a plain blood tube can also be used for NSP antibody using a commercial NSP-ELISA test.";
            test.setText(text);
        }

        ExpandableListAdapter listAdapter = new TextAdapter(this, listDataHeader, listDataChild);
        diagnList.setAdapter(listAdapter);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
