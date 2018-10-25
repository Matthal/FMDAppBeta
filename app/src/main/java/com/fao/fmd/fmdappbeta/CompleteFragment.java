package com.fao.fmd.fmdappbeta;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteFragment extends Fragment {

    View view;
    Bundle bundle;

    public CompleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_complete, container, false);

        bundle = this.getArguments();

        TextView text = view.findViewById(R.id.text);
        text.append(bundle.getString("res") + " days");

        return view;
    }

}
