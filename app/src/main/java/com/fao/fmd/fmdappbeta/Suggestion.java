package com.fao.fmd.fmdappbeta;

import android.content.Intent;
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

        animal = bundle.getInt("id");
        System.out.println(animal);

        String val1 = bundle.getString("Q1");
        String val3 = bundle.getString("Q3");
        String val4 = bundle.getString("Q4");

        if(val1.equals("a")){
            Bundle complete = new Bundle();
            bundle.putString("res","1-2");
            fragmentComplete(bundle);
        }else{
            if(val3.equals("c")){
                Bundle complete = new Bundle();
                bundle.putString("res","A yellow colour to the lesion is evidence of fibrin deposition.");
                fragmentError(bundle);
            }else{
                if(val3.equals("d") && val4.equals("b")){
                    Bundle complete = new Bundle();
                    bundle.putString("res","If a lesion has a withish appearance this usually indicates extensive healing and scar tissue formation so the edges are very unlikely to be sharp.");
                    fragmentError(bundle);
                }else{
                    algorithmResult(bundle);
                }
            }
        }

    }

    public void algorithmResult(Bundle bundle){

        String val2 = bundle.getString("Q2");
        String val3 = bundle.getString("Q3");
        String val4 = bundle.getString("Q4");
        String val5 = bundle.getString("Q5");

        if(val4.equals("b") && val5.equals("a")){
            Bundle complete = new Bundle();
            bundle.putString("res","3-4");
            fragmentComplete(bundle);
        }

        if(val3.equals("a") && val4.equals("a") && val5.equals("b")){
            Bundle complete = new Bundle();
            bundle.putString("res","4-5");
            fragmentComplete(bundle);
        }

        if(val3.equals("a") && val4.equals("a") && (val5.equals("a") || val5.equals("c"))){
            Bundle complete = new Bundle();
            complete.putString("res","3-4");
            fragmentComplete(complete);
        }

        if(val2.equals("a")){
            algorithmA(bundle);
        }else{
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

    public void fragmentError(Bundle complete){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ErrorFragment fragment = new ErrorFragment();
        fragment.setArguments(complete);
        ft.replace(R.id.placeholder, fragment);
        ft.commit();
    }

    public void algorithmA(Bundle bundle){

        String val3 = bundle.getString("Q3");
        String val4 = bundle.getString("Q4");
        String val5 = bundle.getString("Q5");


        if(val4.equals("b") && val5.equals("c")){
            Bundle complete = new Bundle();
            complete.putString("res","3-4");
            fragmentComplete(complete);
        }

        if(val3.equals("b") && val4.equals("a") && val5.equals("a")){
            Bundle complete = new Bundle();
            complete.putString("res","4-5");
            fragmentComplete(complete);
        }

        if(val3.equals("b") && val4.equals("a") && val5.equals("b")){
            Bundle complete = new Bundle();
            complete.putString("res","6-7");
            fragmentComplete(complete);
        }

        if(val3.equals("b") && val4.equals("a") && val5.equals("c")){
            Bundle complete = new Bundle();
            complete.putString("res","Usually pink coloured lesions with smooth edges have evidence of healing around the edges.");
            fragmentError(complete);
        }

        if(val3.equals("a") && val4.equals("b") && val5.equals("b")){
            Bundle complete = new Bundle();
            complete.putString("res","Usually red lesions with sharp edges don’t have evidence of healing.");
            fragmentError(complete);
        }

        if(val3.equals("b") && val4.equals("b") && val5.equals("b")){
            Bundle complete = new Bundle();
            complete.putString("res","Usually pink lesions with evidence of a lot of healing have smooth rather than sharp edges.");
            fragmentError(complete);
        }

    }

    public void algorithmB(Bundle bundle){

        String val3 = bundle.getString("Q3");
        String val4 = bundle.getString("Q4");
        String val5 = bundle.getString("Q5");


        if(val4.equals("b") && val5.equals("c")){
            Bundle complete = new Bundle();
            complete.putString("res","2-3");
            fragmentComplete(complete);
        }

        if(val3.equals("b") && val4.equals("a") && val5.equals("a")){
            Bundle complete = new Bundle();
            complete.putString("res","5-7");
            fragmentComplete(complete);
        }

        if(val3.equals("b") && val4.equals("a") && val5.equals("b")){
            Bundle complete = new Bundle();
            complete.putString("res","7-10");
            fragmentComplete(complete);
        }

        if(val3.equals("d") && val5.equals("a")){
            Bundle complete = new Bundle();
            complete.putString("res","10-14");
            fragmentComplete(complete);
        }

        if(val3.equals("d") && val5.equals("b")){
            Bundle complete = new Bundle();
            complete.putString("res","14+");
            fragmentComplete(complete);
        }

        if(val3.equals("a") && val4.equals("b") && val5.equals("b")){
            Bundle complete = new Bundle();
            complete.putString("res","Red lesions with sharp edges are usually fresh and would usually only have no or limited amount of healing/epithelisation around the edge of the lesion.");
            fragmentError(complete);
        }

        if(val3.equals("b") && val4.equals("a") && val5.equals("c")){
            Bundle complete = new Bundle();
            complete.putString("res","Smooth/rounded lesion edges with a pink colour usually have evidence of healing or epithelisation around the periphery of the lesion.");
            fragmentError(complete);
        }

        if(val3.equals("b") && val4.equals("b") && val5.equals("b")){
            Bundle complete = new Bundle();
            complete.putString("res","Sharp edged lesions usually have only a limited amount of healing or epithelisation.");
            fragmentError(complete);
        }

        if(val3.equals("d") && val4.equals("a") && val5.equals("c")){
            Bundle complete = new Bundle();
            complete.putString("res","If a lesion has a whitish appearance this usually indicates extensive healing and scar tissue formation.");
            fragmentError(complete);
        }

    }
}
