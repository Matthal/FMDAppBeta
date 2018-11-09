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

public class Q4Fragment extends Fragment implements View.OnClickListener{

    View view;
    Bundle bundle;

    int animal;

    public Q4Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_q4, container, false);

        bundle = this.getArguments();

        Button roundBtn = view.findViewById(R.id.roundBtn);
        Button sharpBtn = view.findViewById(R.id.sharpBtn);
        roundBtn.setOnClickListener(this);
        sharpBtn.setOnClickListener(this);

        animal = getArguments().getInt("id");

        return view;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.roundBtn:
                bundle.putString("Q4", "a" );
                changeFragment();
                break;
            case R.id.sharpBtn:
                if(bundle.getString("Q3").equals("d")){
                    bundle.putString("Q4", "b" );
                    endAlgorithm();
                }else{
                    bundle.putString("Q4", "b" );
                    changeFragment();
                }
                break;
        }
    }

    public void changeFragment(){
        bundle.putInt("id",animal);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new Q5Fragment();
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
