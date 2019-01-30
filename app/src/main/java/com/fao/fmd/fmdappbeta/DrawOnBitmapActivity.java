package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class DrawOnBitmapActivity extends FragmentActivity implements View.OnClickListener,PropertiesBSFragment.Properties{

    private static final int CAMERA_REQUEST = 52;

    PhotoEditorView mPhotoEditorView;
    PhotoEditor mPhotoEditor;

    private PropertiesBSFragment mPropertiesBSFragment;

    private String mCurrentPhotoPath;
    Bitmap mImageBitmap;

    String image;

    ImageView imgUndo;
    ImageView imgRedo;
    ImageView imgToolIcon;
    ImageView imgCamera;
    ImageView imgSave;
    ImageView imgClose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_on_bitmap);

        Intent intent = getIntent();
        image = intent.getStringExtra("image");

        mPhotoEditorView = findViewById(R.id.photoEditorView);

        try {
            mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(image));
            mPhotoEditorView.getSource().setImageBitmap(mImageBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgUndo = findViewById(R.id.imgUndo);
        imgUndo.setOnClickListener(this);

        imgRedo = findViewById(R.id.imgRedo);
        imgRedo.setOnClickListener(this);

        imgToolIcon = findViewById(R.id.imgToolIcon);
        imgToolIcon.setOnClickListener(this);

        imgCamera = findViewById(R.id.imgCamera);
        imgCamera.setOnClickListener(this);

        imgSave = findViewById(R.id.imgSave);
        imgSave.setOnClickListener(this);

        imgClose = findViewById(R.id.imgClose);
        imgClose.setOnClickListener(this);

        mPropertiesBSFragment = new PropertiesBSFragment();
        mPropertiesBSFragment.setPropertiesChangeListener(this);

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView)
                .setPinchTextScalable(true)
                .build();

        mPhotoEditor.setBrushDrawingMode(true);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgUndo:
                mPhotoEditor.undo();
                break;

            case R.id.imgRedo:
                mPhotoEditor.redo();
                break;

            case R.id.imgSave:
                //saveImage();
                break;

            case R.id.imgClose:
                onBackPressed();
                break;

            case R.id.imgCamera:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(image);
                System.out.println(image);
                if (f.delete()) {
                    System.out.println("file Deleted ");
                } else {
                    System.out.println("file not Deleted ");
                }
                try {
                    f = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri photoURI = FileProvider.getUriForFile(DrawOnBitmapActivity.this, getBaseContext().getApplicationContext().getPackageName() + ".provider", f);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;

            case R.id.imgToolIcon:
                mPhotoEditor.setBrushDrawingMode(true);
                mPropertiesBSFragment.show(getSupportFragmentManager(), mPropertiesBSFragment.getTag());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST:
                    mPhotoEditor.clearAllViews();
                    Bitmap newBitmap = null;
                    try {
                        newBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mPhotoEditorView.getSource().setImageBitmap(newBitmap);
                    break;
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
    }

    @Override
    public void onOpacityChanged(int opacity) {
        mPhotoEditor.setOpacity(opacity);
    }

    @Override
    public void onBrushSizeChanged(int brushSize) {
        mPhotoEditor.setBrushSize(brushSize);
    }

}