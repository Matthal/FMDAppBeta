package com.fao.fmd.fmdappbeta;


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
public class Q2Fragment extends Fragment implements View.OnClickListener{

    View view;
    Bundle bundle;

    int animal;

    public Q2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_q2, container, false);

        bundle = this.getArguments();

        animal = getArguments().getInt("id");
        System.out.println(animal);

        Button yesBtn = view.findViewById(R.id.yesBtn);
        Button noBtn = view.findViewById(R.id.noBtn);
        yesBtn.setOnClickListener(this);
        noBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        //do what you want to do when button is clicked
        switch (v.getId()) {
            case R.id.yesBtn:
                bundle.putString("Q2", "a" );
                changeFragment();
                break;
            case R.id.noBtn:
                bundle.putString("Q2", "b" );
                changeFragment();
                break;
        }
    }

    public void changeFragment(){
        bundle.putInt("id",animal);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new Q3Fragment();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.placeholder, fragment);
        fragmentTransaction.commit();
    }

}
