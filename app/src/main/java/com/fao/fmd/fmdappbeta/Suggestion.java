package com.fao.fmd.fmdappbeta;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class Suggestion extends FragmentActivity {

    Bundle bundle;

    int animal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        bundle = getIntent().getExtras();

        if(bundle.getString("skip") != null){
            fragmentSkip(bundle);
            return;
        }

        animal = bundle.getInt("id");

        String val1 = bundle.getString("Q1");

        if(val1.equals("a")){
            bundle.putString("res","1");
            fragmentComplete(bundle);
        }else{
            algorithmResult(bundle);
        }

    }

    public void algorithmResult(Bundle bundle){

        String val2 = bundle.getString("Q2");
        String val3 = bundle.getString("Q3");
        String val4 = bundle.getString("Q4");
        String val5 = bundle.getString("Q5");

        if(val2.equals("a") && val3.equals("a") && val4.equals("a")){
            bundle.putString("res","3-5");
            fragmentComplete(bundle);
        }

        if(val2.equals("a") && val3.equals("a") && val4.equals("b")){
            bundle.putString("res","3-4");
            fragmentComplete(bundle);
        }

        if(val2.equals("b") && val3.equals("a") && val4.equals("a")){
            bundle.putString("res","3-4");
            fragmentComplete(bundle);
        }

        if(val2.equals("b") && val3.equals("a") && val4.equals("b")){
            bundle.putString("res","2-3");
            fragmentComplete(bundle);
        }

        if(val3.equals("b") && val4.equals("a") && val5.equals("a")){
            bundle.putString("res","4-5");
            fragmentComplete(bundle);
        }

        if(val3.equals("b") && val4.equals("a") && val5.equals("b")){
            bundle.putString("res","5-7");
            fragmentComplete(bundle);
        }

        if(val2.equals("b")){
            algorithmB(bundle);
        }

    }

    public void fragmentComplete(Bundle complete){
        complete.putInt("id",animal);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CompleteFragment fragment = new CompleteFragment();
        fragment.setArguments(complete);
        ft.replace(R.id.placeholder, fragment);
        ft.commit();
    }

    public void fragmentSkip(Bundle complete){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        CompleteFragment fragment = new CompleteFragment();
        fragment.setArguments(complete);
        ft.replace(R.id.placeholder, fragment);
        ft.commit();
    }

    public void algorithmB(Bundle bundle){

        String val3 = bundle.getString("Q3");
        String val4 = bundle.getString("Q4");
        String val5 = bundle.getString("Q5");

        if(val3.equals("c") && val4.equals("a") && val5.equals("a")){
            bundle.putString("res","7-10");
            fragmentComplete(bundle);
        }

        if(val3.equals("c") && val4.equals("a") && val5.equals("b")){
            bundle.putString("res","10+");
            fragmentComplete(bundle);
        }
    }
}
