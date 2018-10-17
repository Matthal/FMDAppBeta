package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

public class FarmCreation extends Activity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm_creation);

        Button cFarm = findViewById(R.id.createFarm);

        final EditText name = findViewById(R.id.farmName);
        final EditText animals = findViewById(R.id.nAnimals);

        cFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*mDatabase = FirebaseDatabase.getInstance().getReference("farms");
                String farmId = mDatabase.push().getKey();
                Farm farm = new Farm(name.getText().toString(), Integer.parseInt(animals.getText().toString()));
                mDatabase.child(farmId).setValue(farm);*/

                mDatabase = FirebaseDatabase.getInstance().getReference("animals");
                String farmId = mDatabase.push().getKey();
                Animal farm = new Animal("boar","-LP0QEUlxfiIIHWybnXY");
                mDatabase.child(farmId).setValue(farm);

                Toast.makeText(getBaseContext(), "Diagnosis algorithm in development",
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}
