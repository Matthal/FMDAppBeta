package com.fao.fmd.fmdappbeta;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Animal {

    public String type;
    public String farm;
    public String name;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Animal() {
    }

    public Animal(String type, String farm) {
        this.type = type;
        this.farm = farm;
    }
}
