package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class PhotoViewer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_viewer);

        Bundle b = getIntent().getExtras();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        SubsamplingScaleImageView imageView = findViewById(R.id.imgDisplay);

        imageView.setImage(ImageSource.resource(b.getInt("img")));

    }
}
