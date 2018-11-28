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
import android.widget.Toast;

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
        Button roundExBtn = view.findViewById(R.id.roundExBtn);
        Button sharpExBtn = view.findViewById(R.id.sharpExBtn);
        roundBtn.setOnClickListener(this);
        sharpBtn.setOnClickListener(this);
        roundExBtn.setOnClickListener(this);
        sharpExBtn.setOnClickListener(this);

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
                if((bundle.getString("Q2").equals("a") && bundle.getString("Q3").equals("a")) || (bundle.getString("Q2").equals("a") && bundle.getString("Q3").equals("b")) || bundle.getString("Q3").equals("b") || bundle.getString("Q3").equals("d")){
                    bundle.putString("Q4", "b" );
                    endAlgorithm();
                }else{
                    bundle.putString("Q4", "b" );
                    changeFragment();
                }
                break;
            case R.id.roundExBtn:
                if(bundle.getString("Q2").equals("a") && bundle.getString("Q3").equals("a")){
                    bundle.putString("tag","redYesRound");
                }
                if(bundle.getString("Q2").equals("a") && bundle.getString("Q3").equals("b")){
                    bundle.putString("tag","pinkYesRound");
                }
                if(bundle.getString("Q2").equals("b") && bundle.getString("Q3").equals("a")){
                    bundle.putString("tag","redNoRound");
                }
                if(bundle.getString("Q2").equals("b") && bundle.getString("Q3").equals("b")){
                    //bundle.putString("tag","pinkNoRound");
                    Toast.makeText(getActivity(), "No photos watermarked yet", Toast.LENGTH_LONG).show();
                    break;
                }
                if(bundle.getString("Q3").equals("d")){
                    bundle.putString("tag","white");
                }
                Intent intentRound = new Intent(getActivity(), PhotoViewer.class);
                intentRound.putExtras(bundle);
                startActivity(intentRound);
                break;
            case R.id.sharpExBtn:
                if(bundle.getString("Q2").equals("a") && bundle.getString("Q3").equals("a")){
                    bundle.putString("tag","redYesSharp");
                }else{
                    if(bundle.getString("Q2").equals("b") && bundle.getString("Q3").equals("a")){
                        bundle.putString("tag","redNoSharp");
                    }else{
                        Toast.makeText(getActivity(), "No photos available", Toast.LENGTH_LONG).show();
                        break;
                    }
                }
                Intent intentSharp = new Intent(getActivity(), PhotoViewer.class);
                intentSharp.putExtras(bundle);
                startActivity(intentSharp);
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
        fragmentTransaction.addToBackStack("Q4");
        fragmentTransaction.commit();
    }

    public void endAlgorithm(){
        Intent intent = new Intent(getActivity(), Suggestion.class);
        bundle.putInt("id",animal);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
