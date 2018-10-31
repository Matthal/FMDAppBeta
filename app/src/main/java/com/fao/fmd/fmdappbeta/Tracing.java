package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Tracing extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing);

        Button animalTrack = findViewById(R.id.animalTrack);
        Button productTrack = findViewById(R.id.productTrack);
        Button peopleTrack = findViewById(R.id.peopleTrack);
        Button veichleTrack = findViewById(R.id.veichleTrack);

        animalTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout mainLayout= findViewById(R.id.main);
                mainLayout.setVisibility(RelativeLayout.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.placeholder, new AnimalTrackFragment());
                ft.commit();
            }
        });

        productTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout mainLayout= findViewById(R.id.main);
                mainLayout.setVisibility(RelativeLayout.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.placeholder, new ProductTrackFragment());
                ft.commit();
            }
        });

        peopleTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout mainLayout= findViewById(R.id.main);
                mainLayout.setVisibility(RelativeLayout.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.placeholder, new PeopleTrackFragment());
                ft.commit();
            }
        });

        veichleTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout mainLayout= findViewById(R.id.main);
                mainLayout.setVisibility(RelativeLayout.GONE);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.placeholder, new VehicleTrackFragment());
                ft.commit();
            }
        });

    }
}
