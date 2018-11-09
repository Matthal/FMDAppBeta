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

public class Q3Fragment extends Fragment implements View.OnClickListener{

    View view;
    Bundle bundle;

    int animal;

    public Q3Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_q3, container, false);

        bundle = this.getArguments();

        Button redBtn = view.findViewById(R.id.redBtn);
        Button pinkBtn = view.findViewById(R.id.pinkBtn);
        Button yellowBtn = view.findViewById(R.id.yellowBtn);
        Button whiteBtn = view.findViewById(R.id.whiteBtn);
        redBtn.setOnClickListener(this);
        pinkBtn.setOnClickListener(this);
        yellowBtn.setOnClickListener(this);
        whiteBtn.setOnClickListener(this);

        animal = getArguments().getInt("id");

        if(bundle != null) {
            if (bundle.getString("Q2").equals("a")) {
                yellowBtn.setVisibility(View.INVISIBLE);
                whiteBtn.setVisibility(View.INVISIBLE);
                view.findViewById(R.id.yellowExBtn).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.whiteExBtn).setVisibility(View.INVISIBLE);
            }
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.redBtn:
                bundle.putString("Q3", "a" );
                changeFragment();
                break;
            case R.id.pinkBtn:
                bundle.putString("Q3", "b" );
                changeFragment();
                break;
            case R.id.yellowBtn:
                bundle.putString("Q3", "c" );
                endAlgorithm();
                break;
            case R.id.whiteBtn:
                bundle.putString("Q3", "d" );
                changeFragment();
                break;
        }
    }

    public void changeFragment(){
        bundle.putInt("id",animal);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new Q4Fragment();
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
