package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LesionAgeingGallery extends Activity {

    List<SectionedGridRecyclerViewAdapter.Section> sections;
    SectionedGridRecyclerViewAdapter mSectionedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesion_ageing_gallery);

        TextView light = findViewById(R.id.light);
        TextView dark = findViewById(R.id.dark);
        LinearLayout.LayoutParams paramLight = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.75f
        );
        LinearLayout.LayoutParams paramDark = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.25f
        );
        light.setLayoutParams(paramLight);
        dark.setLayoutParams(paramDark);

        //Your RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(LesionAgeingGallery.this, 4));

        //Your RecyclerView.Adapter
        LesionAgeingAdapter mAdapter = new LesionAgeingAdapter(LesionAgeingGallery.this);

        //This is the code to provide a sectioned grid
        sections = new ArrayList<>();

        //Sections
        sections.add(new SectionedGridRecyclerViewAdapter.Section(0, "1 day"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(6, "2-3 days"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(17, "3-4 days"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(25, "4-5 days"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(41, "5-7 days"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(58, "7-10 days"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(59, "10-14 days"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(62, "14+ days"));

        //Add your adapter to the sectionAdapter
        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
        mSectionedAdapter = new SectionedGridRecyclerViewAdapter(this, R.layout.section, R.id.section_text, mRecyclerView, mAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        mRecyclerView.setAdapter(mSectionedAdapter);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
