package com.fao.fmd.fmdappbeta;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class Q5Fragment extends Fragment implements View.OnClickListener{

    View view;
    Bundle bundle;

    int animal;

    public Q5Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_q5, container, false);

        bundle = this.getArguments();

        animal = getArguments().getInt("id");

        Button smallBtn = view.findViewById(R.id.smallBtn);
        Button lotBtn = view.findViewById(R.id.lotBtn);
        Button noBtn = view.findViewById(R.id.noBtn);
        Button smallExBtn = view.findViewById(R.id.smallExBtn);
        Button lotExBtn = view.findViewById(R.id.lotExBtn);
        Button noExBtn = view.findViewById(R.id.noExBtn);
        smallBtn.setOnClickListener(this);
        lotBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);
        smallExBtn.setOnClickListener(this);
        lotExBtn.setOnClickListener(this);
        noExBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.smallBtn:
                bundle.putString("Q5", "a" );
                endAlgorithm();
                break;
            case R.id.lotBtn:
                bundle.putString("Q5", "b" );
                endAlgorithm();
                break;
            case R.id.noBtn:
                bundle.putString("Q5", "c" );
                endAlgorithm();
                break;
            case R.id.smallExBtn:
                Toast.makeText(getActivity(), "No photos available", Toast.LENGTH_LONG).show();
                break;
            case R.id.lotExBtn:
                Toast.makeText(getActivity(), "No photos available", Toast.LENGTH_LONG).show();
                break;
            case R.id.noExBtn:
                Toast.makeText(getActivity(), "No photos available", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void endAlgorithm(){
        bundle.putInt("id",animal);
        Intent intent = new Intent(getActivity(), Suggestion.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
