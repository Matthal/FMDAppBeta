package com.fao.fmd.fmdappbeta;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ErrorFragment extends Fragment {

    View view;
    Bundle bundle;

    public ErrorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_error, container, false);

        bundle = this.getArguments();

        TextView light = view.findViewById(R.id.light);
        TextView dark = view.findViewById(R.id.dark);
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

        TextView text = view.findViewById(R.id.text);
        text.append(bundle.getString("res") + " Use the photos to guide you.");

        ImageView back = view.findViewById((R.id.back));

        back.setOnClickListener(v -> getActivity().onBackPressed());

        return view;
    }
}
