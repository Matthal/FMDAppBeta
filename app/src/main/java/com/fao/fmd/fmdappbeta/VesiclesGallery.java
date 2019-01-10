package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class VesiclesGallery extends Activity {

    List<SectionedGridRecyclerViewAdapter.Section> sections;
    SectionedGridRecyclerViewAdapter mSectionedAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vesicles_gallery);

        //Your RecyclerView
        RecyclerView mRecyclerView = findViewById(R.id.recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(VesiclesGallery.this,4));

        //Your RecyclerView.Adapter
        SimpleAdapter mAdapter = new SimpleAdapter(VesiclesGallery.this);

        //This is the code to provide a sectioned grid
        sections = new ArrayList<>();

        //Sections
        sections.add(new SectionedGridRecyclerViewAdapter.Section(0,"Vesicles"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(7,"Fibrin"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(14,"Red Fibrin"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(21,"Pink Fibrin"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(28,"Red No Fibrin"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(34,"Pink No Fibrin"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(39,"White Smooth No Fibrin"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(45,"Red Smooth Fibrin"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(50,"Red Sharp Fibrin"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(52,"Pink Smooth Fibrin"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(57,"Red Smooth No Fibrin"));
        sections.add(new SectionedGridRecyclerViewAdapter.Section(59,"Red Sharp No Fibrin"));
        //sections.add(new SectionedGridRecyclerViewAdapter.Section(20,"Pink Smooth"));

        //Add your adapter to the sectionAdapter
        SectionedGridRecyclerViewAdapter.Section[] dummy = new SectionedGridRecyclerViewAdapter.Section[sections.size()];
        mSectionedAdapter = new SectionedGridRecyclerViewAdapter(this,R.layout.section,R.id.section_text,mRecyclerView,mAdapter);
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
