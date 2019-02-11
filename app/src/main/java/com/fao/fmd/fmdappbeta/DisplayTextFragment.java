package com.fao.fmd.fmdappbeta;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DisplayTextFragment extends Fragment {

    View view;

    public DisplayTextFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_display_text, container, false);

        String text = getArguments().getString("text");

        TextView dispaly = view.findViewById(R.id.display);
        dispaly.setText(text);

        return view;
    }

}
