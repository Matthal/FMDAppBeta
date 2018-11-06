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


/**
 * A simple {@link Fragment} subclass.
 */
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
        System.out.println(animal);

        Button smallBtn = view.findViewById(R.id.smallBtn);
        Button lotBtn = view.findViewById(R.id.lotBtn);
        Button noBtn = view.findViewById(R.id.noBtn);
        smallBtn.setOnClickListener(this);
        lotBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);

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
        }
    }

    public void endAlgorithm(){
        bundle.putInt("id",animal);
        Intent intent = new Intent(getActivity(), Suggestion.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
