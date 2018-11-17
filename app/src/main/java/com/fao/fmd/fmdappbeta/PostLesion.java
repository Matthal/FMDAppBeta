package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PostLesion extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_lesion);

        final Bundle bundle = getIntent().getExtras();

        Button addAnimal = findViewById(R.id.animal);
        Button addTracing = findViewById(R.id.tracing);
        Button viewTimeline = findViewById(R.id.tlBtn);

        addAnimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostLesion.this, AnimalCreation.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        addTracing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostLesion.this, Tracing.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        viewTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostLesion.this, Timeline.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
