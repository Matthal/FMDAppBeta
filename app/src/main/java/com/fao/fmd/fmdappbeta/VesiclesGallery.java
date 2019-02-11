package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class VesiclesGallery extends Activity {

    private ArrayList<Integer> ImagesArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vesicles_gallery);

        TextView light = findViewById(R.id.light);
        TextView dark = findViewById(R.id.dark);
        LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.60f
        );
        LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.40f
        );
        light.setLayoutParams(paramLight);
        dark.setLayoutParams(paramDark);

        Bundle g = getIntent().getExtras();

        init(g.getString("img"));

        GridView gridView = findViewById(R.id.galleryGridView);

        // Instance of ImageAdapter Class
        gridView.setAdapter(new ImageAdapter(this, ImagesArray));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent i = new Intent(getApplicationContext(), PhotoViewer.class);
                i.putExtra("img", ImagesArray.get(position));
                startActivity(i);
            }
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> onBackPressed());

    }

    private void init(String id) {

        if(id.equals("1")){
            final Integer[] IMAGES = {R.drawable.onetwo_photo_1,R.drawable.onetwo_photo_2,R.drawable.onetwo_photo_3,R.drawable.onetwo_photo_4,R.drawable.onetwo_photo_5,R.drawable.onetwo_photo_6};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("2-3")){
            final Integer[] IMAGES = {R.drawable.twothree_photo_1,R.drawable.twothree_photo_2,R.drawable.twothree_photo_3,R.drawable.twothree_photo_4,R.drawable.twothree_photo_5,R.drawable.twothree_photo_6,R.drawable.twothree_photo_7,R.drawable.twothree_photo_8,R.drawable.twothree_photo_9,R.drawable.twothree_photo_10,R.drawable.twothree_photo_11};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("3-4")){
            final Integer[] IMAGES = {R.drawable.threefour_photo_1,R.drawable.threefour_photo_2,R.drawable.threefour_photo_3,R.drawable.threefour_photo_4,R.drawable.threefour_photo_5,R.drawable.threefour_photo_6,R.drawable.threefour_photo_7,R.drawable.threefour_photo_8};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("4-5")){
            final Integer[] IMAGES = {R.drawable.fourfive_photo_1,R.drawable.fourfive_photo_2,R.drawable.fourfive_photo_3,R.drawable.fourfive_photo_4,R.drawable.fourfive_photo_5,R.drawable.fourfive_photo_6,R.drawable.fourfive_photo_7,R.drawable.fourfive_photo_8,R.drawable.fourfive_photo_9,R.drawable.fourfive_photo_10,R.drawable.fourfive_photo_11,R.drawable.fourfive_photo_12,R.drawable.fourfive_photo_13,R.drawable.fourfive_photo_14,R.drawable.fourfive_photo_15,R.drawable.fourfive_photo_16};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("5-7")){
            final Integer[] IMAGES = {R.drawable.fiveseven_photo_1,R.drawable.fiveseven_photo_2,R.drawable.fiveseven_photo_3,R.drawable.fiveseven_photo_4,R.drawable.fiveseven_photo_5,R.drawable.fiveseven_photo_6,R.drawable.fiveseven_photo_7,R.drawable.fiveseven_photo_8,R.drawable.fiveseven_photo_9,R.drawable.fiveseven_photo_10,R.drawable.fiveseven_photo_11,R.drawable.fiveseven_photo_12,R.drawable.fiveseven_photo_13,R.drawable.fiveseven_photo_14,R.drawable.fiveseven_photo_15,R.drawable.fiveseven_photo_16,R.drawable.fiveseven_photo_17};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("7-10")){
            final Integer[] IMAGES = {R.drawable.seventen_photo_1};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("10-14")){
            final Integer[] IMAGES = {R.drawable.tenfourteen_photo_1,R.drawable.tenfourteen_photo_2,R.drawable.tenfourteen_photo_3,R.drawable.tenfourteen_photo_4,R.drawable.tenfourteen_photo_5};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("14+")){
            final Integer[] IMAGES = {R.drawable.fourteenplus_photo_1,R.drawable.fourteenplus_photo_2,R.drawable.fourteenplus_photo_3};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }

        if(id.equals("ves")){
            final Integer[] IMAGES = {R.drawable.ves_photo_1,R.drawable.ves_photo_2,R.drawable.ves_photo_3,R.drawable.ves_photo_4,R.drawable.ves_photo_5,R.drawable.ves_photo_6,R.drawable.ves_photo_7};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("fib")){
            final Integer[] IMAGES = {R.drawable.fib_photo_1,R.drawable.fib_photo_2,R.drawable.fib_photo_3,R.drawable.fib_photo_4,R.drawable.fib_photo_5,R.drawable.fib_photo_6,R.drawable.fib_photo_7};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("red")){
            final Integer[] IMAGES = {R.drawable.redyes_photo_1,R.drawable.redyes_photo_2,R.drawable.redyes_photo_3,R.drawable.redyes_photo_4,R.drawable.redyes_photo_5,R.drawable.redyes_photo_6,R.drawable.redyes_photo_7,R.drawable.redno_photo_1,R.drawable.redno_photo_2,R.drawable.redno_photo_3,R.drawable.redno_photo_4,R.drawable.redno_photo_5,R.drawable.redno_photo_6,R.drawable.redyesround_photo_2,R.drawable.redyesround_photo_3,R.drawable.redyesround_photo_4,R.drawable.redyesround_photo_5,R.drawable.redyessharp_photo_2,R.drawable.rednosharp_photo_1,R.drawable.rednosharp_photo_3,R.drawable.rednosharp_photo_4};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("pink")){
            final Integer[] IMAGES = {R.drawable.pinkyes_photo_1,R.drawable.pinkyes_photo_2,R.drawable.pinkyes_photo_3,R.drawable.pinkyes_photo_4,R.drawable.pinkyes_photo_5,R.drawable.pinkyes_photo_6,R.drawable.pinkyes_photo_7,R.drawable.pinkno_photo_1,R.drawable.pinkno_photo_2,R.drawable.pinkno_photo_3,R.drawable.pinkno_photo_4,R.drawable.pinkno_photo_5,R.drawable.pinkyesround_photo_1,R.drawable.pinkyesround_photo_2,R.drawable.pinkyesround_photo_3,R.drawable.pinkyesround_photo_4};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("white")){
            final Integer[] IMAGES = {R.drawable.white_photo_1,R.drawable.white_photo_2,R.drawable.white_photo_3,R.drawable.white_photo_4,R.drawable.white_photo_5,R.drawable.white_photo_6};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
    }
}
