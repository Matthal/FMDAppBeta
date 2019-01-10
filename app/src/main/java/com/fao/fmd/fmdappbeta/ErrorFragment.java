package com.fao.fmd.fmdappbeta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ErrorFragment extends Fragment {

    View view;
    Bundle bundle;

    public ErrorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_error, container, false);

        bundle = this.getArguments();

        TextView text = view.findViewById(R.id.text);
        text.append(bundle.getString("res") + " Use the photos to guide you.");

        ImageView back = view.findViewById((R.id.back));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }
}
