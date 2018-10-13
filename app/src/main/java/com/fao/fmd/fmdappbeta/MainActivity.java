package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cFarm = findViewById(R.id.newFarm);
        Button lFarm = findViewById(R.id.listFarm);

        cFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FarmCreation.class);
                startActivity(intent);
            }
        });

        lFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FarmList.class);
                startActivity(intent);
            }
        });
    }


}
