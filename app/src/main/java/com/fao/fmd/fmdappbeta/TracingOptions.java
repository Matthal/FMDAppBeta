package com.fao.fmd.fmdappbeta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TracingOptions extends AppCompatActivity {

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracing_options);

        bundle = getIntent().getExtras();

        int farm = bundle.getInt("id");

        TextView light = findViewById(R.id.light);
        TextView dark = findViewById(R.id.dark);
        LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.90f
        );
        LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.10f
        );
        light.setLayoutParams(paramLight);
        dark.setLayoutParams(paramDark);

        Button addTracing = findViewById(R.id.newTracing);
        Button viewTracing = findViewById(R.id.listTracing);

        ImageView back = findViewById(R.id.back);

        addTracing.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putInt("id", farm);
            Intent intent = new Intent(TracingOptions.this, Tracing.class);
            intent.putExtras(b);
            startActivity(intent);
        });

        viewTracing.setOnClickListener(v -> {
            Bundle b = new Bundle();
            b.putInt("id", farm);
            Intent intent = new Intent(TracingOptions.this, TracingList.class);
            intent.putExtras(b);
            startActivity(intent);
        });

        back.setOnClickListener(v -> onBackPressed());
    }
}
