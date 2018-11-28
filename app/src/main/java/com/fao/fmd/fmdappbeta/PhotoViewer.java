package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class PhotoViewer extends Activity {

    //private static final Integer[] IMAGES = {R.drawable.onetwo_photo_1,R.drawable.onetwo_photo_2,R.drawable.onetwo_photo_3,R.drawable.onetwo_photo_4};
    private ArrayList<Integer> ImagesArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        Bundle b = getIntent().getExtras();
        if(b != null){
            if(b.getString("tag").equals("complete")){
                String days = b.getString("res");
                init(days);
            }else{
                String tag = b.getString("tag");
                init(tag);
            }
        }
    }

    private void init(String id) {

        if(id.equals("1-2")){
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
        if(id.equals("redYes")){
            final Integer[] IMAGES = {R.drawable.redyes_photo_1,R.drawable.redyes_photo_2,R.drawable.redyes_photo_3,R.drawable.redyes_photo_4,R.drawable.redyes_photo_5,R.drawable.redyes_photo_6,R.drawable.redyes_photo_7};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("pinkYes")){
            final Integer[] IMAGES = {R.drawable.pinkyes_photo_1,R.drawable.pinkyes_photo_2,R.drawable.pinkyes_photo_3,R.drawable.pinkyes_photo_4,R.drawable.pinkyes_photo_5,R.drawable.pinkyes_photo_6,R.drawable.pinkyes_photo_7};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("redNo")){
            final Integer[] IMAGES = {R.drawable.redno_photo_1,R.drawable.redno_photo_2,R.drawable.redno_photo_3,R.drawable.redno_photo_4,R.drawable.redno_photo_5,R.drawable.redno_photo_6};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("pinkNo")){
            final Integer[] IMAGES = {R.drawable.pinkno_photo_1,R.drawable.pinkno_photo_2,R.drawable.pinkno_photo_3,R.drawable.pinkno_photo_4,R.drawable.pinkno_photo_5};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("white")){
            final Integer[] IMAGES = {R.drawable.white_photo_1,R.drawable.white_photo_2,R.drawable.white_photo_3,R.drawable.white_photo_4,R.drawable.white_photo_5,R.drawable.white_photo_6};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("redYesRound")){
            final Integer[] IMAGES = {R.drawable.redyesround_photo_1,R.drawable.redyesround_photo_2,R.drawable.redyesround_photo_3,R.drawable.redyesround_photo_4,R.drawable.redyesround_photo_5};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("redYesSharp")){
            final Integer[] IMAGES = {R.drawable.redyessharp_photo_1,R.drawable.redyessharp_photo_2};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("pinkYesRound")){
            final Integer[] IMAGES = {R.drawable.pinkyesround_photo_1,R.drawable.pinkyesround_photo_2,R.drawable.pinkyesround_photo_3,R.drawable.pinkyesround_photo_4,R.drawable.pinkyesround_photo_5};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("redNoRound")){
            final Integer[] IMAGES = {R.drawable.rednoround_photo_1,R.drawable.rednoround_photo_2};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }
        if(id.equals("redNoSharp")){
            final Integer[] IMAGES = {R.drawable.rednosharp_photo_1,R.drawable.rednosharp_photo_2,R.drawable.rednosharp_photo_3,R.drawable.rednosharp_photo_4,R.drawable.rednosharp_photo_5};
            ImagesArray.addAll(Arrays.asList(IMAGES));
        }

        ViewPager mPager = findViewById(R.id.pager);

        mPager.setAdapter(new ImageAdapter(PhotoViewer.this,ImagesArray));

    }
}
