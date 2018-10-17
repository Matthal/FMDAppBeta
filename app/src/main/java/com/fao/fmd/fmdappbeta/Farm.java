package com.fao.fmd.fmdappbeta;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Farm {

    public String name;
    public int animals;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Farm() {
    }

    public Farm(String name, int animals) {
        this.name = name;
        this.animals = animals;
    }
}
