package com.fao.fmd.fmdappbeta;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fao.fmd.fmdappbeta.DividerItemDecoration;
import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;
import java.util.List;

public class SamplesFragment extends Fragment {

    View view;

    public SamplesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_samples, container, false);

        final String res = getArguments().getString("days");

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        final List<ItemModel> data = new ArrayList<>();

        if(res.equals("1") || res.equals("2-3")){
            data.add(new ItemModel(
                    "Vesicular fluid",
                    "This is the richest source of virus and in a live animal but is difficult to get. If a vesicle is intact, you need to take a narrow gauge needle to extract the fluid which can be very challenging due to difficulties restraining the animal. Vesicular fluid may also be collected on a swab in a freshly ruptured vesicle so be ready when you begin examination just in case this happens!\n" +
                            "Vesicular fluid should be stored in a plain tube (no transport medium is necessary).\n" +
                            "Virus isolation, antigen ELISA or RT-PCR methods can be used to detect the presence of virus. You can also test the fluid on a lateral flow device (LFD).",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
            data.add(new ItemModel(
                    "Epithelium samples",
                    "If a vesicle has burst, it is often possible to take a sample of epithelium. This is a very rich source of virus especially in 1-2 day old lesions.\n"+
                            "Good animal restrain is needed to take the sample and sedation may be needed.\n"+
                            "Sometimes epithelium can be pulled off with one’s fingers, but using tweezers/forceps and scissors is preferred if possible and safe to do.\n"+
                            "At least 2cm^2 of epithelium is optimal (roughly a thumbnail sized amount).\n"+
                            "Samples should be stored in appropriate transport medium and stored at 2-8°C or in the freezer (-20°C or -80°C).\n"+
                            "Virus isolation, antigen ELISA or RT-PCR methods can be used to detect the presence of virus. You can also test the fluid on a lateral flow device (LFD).",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
            data.add(new ItemModel(
                    "Swabs",
                    "If it is not possible to take vesicular fluid or epithelium samples, a plain swab directly applied to the lesion can be taken although this is not optimal due to lower concentrations of virus present compared to epithelium and vesicular fluid.\n"+
                            "Orophayngeal swabs can also be taken, in either case, collected into a small volume of transport medium or lysis buffer (for RT-PCR).\n"+
                            "Swabs should be stored at 2-8°C or in the freezer (-20°C or -80°C).\n"+
                            "You are unlikely to recover live virus.\n"+
                            "Virus isolation or RT-PCR methods can be used to detect the presence of virus.",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
            data.add(new ItemModel(
                    "Cardiac muscle",
                    "If the animal has died from myocarditis, virus can be detected in heart muscle.\n"+
                            "A piece of myocardium needs to be put in a plain pot (NOT formalin) in transport medium and sent immediately to the laboratory.\n"+
                            "The sample should be stored at 2-8°C.\n"+
                            "Virus isolation, antigen ELISA or RT-PCR methods can be used to detect the presence of virus.",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
            data.add(new ItemModel(
                    "Blood",
                    "Note that blood samples are NOT the optimum sample for detecting virus due to low concentrations present.\n"+
                            "RT-PCR methods are essential.",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
        }

        if(res.equals("3-4")){
            data.add(new ItemModel(
                    "Epithelium samples",
                    "With 3-4 day old lesions, if epithelium is present it is often starting to go necrotic and lower levels of virus are present compared to 1-2 day old lesions. However, if available (more likely from between the digits of the feet than in the mouth) it is still worth taking a sample.\n"+
                            "Good animal restrain is needed to take the sample and sedation may be needed.\n"+
                            "Sometimes epithelium can be pulled off with one’s fingers, but using tweezers/forceps and scissors is preferred if possible and safe to do.\n"+
                            "At least 2cm^2 of epithelium is needed (roughly a thumbnail sized amount).\n"+
                            "Samples should be stored in appropriate transport medium and stored at 2-8°C or in the freezer (-20°C or -80°C).\n"+
                            "Virus isolation, antigen ELISA or RT-PCR methods can be used to detect the presence of virus. You can also test the fluid on a lateral flow device (LFD).",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
            data.add(new ItemModel(
                    "Swabs",
                    "If it is not possible to take epithelium samples, a plain swab directly applied to the lesion can be taken although virus levels tend to be lower.\n"+
                            "Orophayngeal swabs can also be taken.\n"+
                            "Swabs should be stored at 2-8°C or in the freezer (-20°C or -80°C).\n"+
                            "You are unlikely to recover live virus.\n"+
                            "Virus isolation or PCR methods can be used to detect the presence of virus.",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
            data.add(new ItemModel(
                    "Blood",
                    "In animals with this age of lesion, blood samples are very unlikely to detect virus even with sensitive RT-PCR methods.\n",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
        }

        if(res.equals("4-5")){
            data.add(new ItemModel(
                    "Swabs",
                    "A plain swab directly applied to the lesion can be taken and you may be able to detect virus.\n"+
                            "Orophayngeal swabs can also be taken.\n"+
                            "Swabs should be stored at 2-8°C or in the freezer (-20°C or -80°C).\n"+
                            "You are unlikely to recover live virus.\n"+
                            "Virus isolation or PCR methods can be used to detect the presence of virus.",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
            data.add(new ItemModel(
                    "Probang",
                    "Probang sampling involves taking a sample of mucus with cellular material from the oropharynx using a metal cup introduced on the end of a semi-flexible wire.\n"+
                            "It is used where it is not possible to get vesicular fluid or epithelium but live virus is required (useful for some diagnostic tests).\n"+
                            "Probang sampling are also used to test for the presence of FMD virus carriers.\n"+
                            "For animals with oral lesions, depending on the particular case, performing this test can cause a lot of distress and may be best suited for animals with older lesions.\n"+
                            "The steps to do a probang are:\n" +
                            "I.\tEnsure adequate restraint\n" +
                            "II.\tMeasure the distance to the oropharynx on the outside of the animal and mark on the wire of the probang as a guide.\n" +
                            "III.\tIntroduce the probang into the mouth centrally- if it deviates to the side you will feel the animal chewing it\n" +
                            "IV.\tFeel/watch the outside of the larynx and upper oesophagus on the left side of the animal- you will feel/see as the probang reaches this area\n" +
                            "V.\tGently move the probang backwards and forwards five times in this region\n" +
                            "VI.\tGently withdraw the probang, aiming to keep the cup upright so as not to tip out the sample\n" +
                            "VII.\tTransfer the sample to probang transport medium",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
            data.add(new ItemModel(
                    "Blood",
                    "You will not detect virus from blood of animals with lesions of this age.",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
        }

        if(res.equals("5-7")){
            data.add(new ItemModel(
                    "Swabs",
                    "With this age of lesion, swabs applied to the lesion may be able to detect virus using PCR methods. It is unlikely you will find live virus.\n"+
                            "Orophayngeal swabs can also be taken.\n"+
                            "Swabs should be stored at 2-8°C or in the freezer (-20°C or -80°C).",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
            data.add(new ItemModel(
                    "Probang",
                    "Probang sampling involves taking a sample of mucus with cellular material from the oropharynx using a metal cup introduced on the end of a semi-flexible wire.\n"+
                            "It is used where it is not possible to get vesicular fluid or epithelium but live virus is required (useful for some diagnostic tests).\n"+
                            "Probang sampling are also used to test for the presence of FMD virus carriers.\n"+
                            "For animals with oral lesions, depending on the particular case, performing this test can cause a lot of distress and may be best suited for animals with older lesions or those that are fully recovered.\n"+
                            "The steps to do a probang are:\n" +
                            "I.\tEnsure adequate restraint\n" +
                            "II.\tMeasure the distance to the oropharynx on the outside of the animal and mark on the wire of the probang as a guide.\n" +
                            "III.\tIntroduce the probang into the mouth centrally- if it deviates to the side you will feel the animal chewing it\n" +
                            "IV.\tFeel the outside of the larynx and upper oesophagus- you will feel as the probang reaches this area\n" +
                            "V.\tGently move the probang backwards and forwards five times in this region\n" +
                            "VI.\tGently withdraw the probang, aiming to keep the cup upright so as not to tip out the sample\n" +
                            "VII.\tTransfer the sample to probang transport medium",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
        }

        if(res.equals("7-10") || res.equals("10-14") || res.equals("14+")){
            data.add(new ItemModel(
                    "Probang",
                    "Probang sampling involves taking a sample of mucus with cellular material from the oropharynx using a metal cup introduced on the end of a semi-flexible wire.\n"+
                            "It is used where it is not possible to get vesicular fluid or epithelium but live virus is required (useful for some diagnostic tests).\n"+
                            "Probang sampling are also used to test for the presence of FMD virus carriers.\n"+
                            "For animals with oral lesions, depending on the particular case, performing this test can cause a lot of distress and may be best suited for animals with older lesions.\n"+
                            "The steps to do a probang are:\n" +
                            "I.\tEnsure adequate restraint\n" +
                            "II.\tMeasure the distance to the oropharynx on the outside of the animal and mark on the wire of the probang as a guide.\n" +
                            "III.\tIntroduce the probang into the mouth centrally- if it deviates to the side you will feel the animal chewing it\n" +
                            "IV.\tFeel the outside of the larynx and upper oesophagus- you will feel as the probang reaches this area\n" +
                            "V.\tGently move the probang backwards and forwards five times in this region\n" +
                            "VI.\tGently withdraw the probang, aiming to keep the cup upright so as not to tip out the sample\n" +
                            "VII.\tTransfer the sample to probang transport medium",
                    R.color.colorPrimary,
                    R.color.light_grey,
                    Utils.createInterpolator(Utils.ACCELERATE_DECELERATE_INTERPOLATOR)));
        }

        recyclerView.setAdapter(new RecyclerViewRecyclerAdapter(data));

        return view;
    }

}
