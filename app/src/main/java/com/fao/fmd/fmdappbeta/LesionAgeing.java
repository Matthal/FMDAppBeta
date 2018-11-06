package com.fao.fmd.fmdappbeta;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class LesionAgeing extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesion_ageing);

        Bundle bundle = getIntent().getExtras();

        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Q1Fragment fragment = new Q1Fragment();
        fragment.setArguments(bundle);
        ft.replace(R.id.placeholder, fragment);
        ft.commit();

    }
}
