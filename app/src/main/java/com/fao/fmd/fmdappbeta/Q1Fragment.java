package com.fao.fmd.fmdappbeta;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class Q1Fragment extends Fragment implements View.OnClickListener {

    View view;
    Bundle bundle = new Bundle();

    int animal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.fragment_q1, parent, false);

        Button yesBtn = view.findViewById(R.id.yesBtn);
        Button noBtn = view.findViewById(R.id.noBtn);
        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);

        animal = getArguments().getInt("id");

        System.out.println(animal);

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    }



    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.yesBtn:
                bundle.putString("Q1", "a" );
                endAlgorithm();
                break;
            case R.id.noBtn:
                bundle.putString("Q1", "b" );
                changeFragment();
                break;
        }
    }

    public void changeFragment(){
        bundle.putInt("id",animal);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new Q2Fragment();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.placeholder, fragment);
        fragmentTransaction.commit();
    }

    public void endAlgorithm(){
        Intent intent = new Intent(getActivity(), Suggestion.class);
        bundle.putInt("id",animal);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
