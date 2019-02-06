package com.fao.fmd.fmdappbeta;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LesionAgeing extends Activity {

    private static final int CAMERA_PIC_REQUEST = 1;
    private String mCurrentPhotoPath;

    boolean lock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesion_ageing);

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

        //Get animal ID from previous page
        Bundle oldBundle = getIntent().getExtras();
        final int animal = oldBundle.getInt("id");

        final Bundle newBundle = new Bundle();

        final String[] vesItems = new String[]{"YES", "NO"};
        final String[] fibItems = new String[]{"YES", "NO"};
        final String[] edgeItems = new String[]{"SMOOTH/ROUNDED", "SHARP"};
        final String[] healItems = new String[]{"HEALING(SMALL)", "HEALING(A LOT)", "NO"};

        final Spinner vesSpin = findViewById(R.id.ves);
        ArrayAdapter<String> vesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, vesItems);
        vesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vesSpin.setAdapter(vesAdapter);

        final Spinner fibSpin = findViewById(R.id.fib);
        ArrayAdapter<String> fibAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, fibItems);
        fibAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fibSpin.setAdapter(fibAdapter);

        final Button red = findViewById(R.id.colRed);
        final boolean[] redClicked = {false};
        final boolean[] redSel = {false};
        final Button yellow = findViewById(R.id.colYellow);
        final boolean[] yellowSel = {false};
        final Button pink = findViewById(R.id.colPink);
        final boolean[] pinkSel = {false};
        final Button white = findViewById(R.id.colWhite);
        final boolean[] whiteSel = {false};

        final Spinner edgeSpin = findViewById(R.id.edge);
        ArrayAdapter<String> edgeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, edgeItems);
        edgeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edgeSpin.setAdapter(edgeAdapter);

        final Spinner healSpin = findViewById(R.id.heal);
        ArrayAdapter<String> healAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, healItems);
        healAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        healSpin.setAdapter(healAdapter);

        TextView textFib = findViewById(R.id.text_fib);
        TextView textColour = findViewById(R.id.text_colour);
        TextView textEdges = findViewById(R.id.text_edges);
        TextView textHeal = findViewById(R.id.text_heal);

        Button vesGalleryBtn = findViewById(R.id.vesGallery);
        Button fibGalleryBtn = findViewById(R.id.fibGallery);
        Button redGalleryBtn = findViewById(R.id.redGallery);
        Button pinkGalleryBtn = findViewById(R.id.pinkGallery);
        Button whiteGalleryBtn = findViewById(R.id.whiteGallery);

        //LISTENERS
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redSel[0] = !redSel[0];
                pinkSel[0] = false;
                yellowSel[0] = false;
                whiteSel[0] = false;
                red.setBackgroundResource(R.color.green);
                red.setTextColor(getResources().getColor(R.color.black));
                yellow.setBackgroundResource(R.color.white);
                yellow.setTextColor(getResources().getColor(R.color.grey));
                pink.setBackgroundResource(R.color.white);
                pink.setTextColor(getResources().getColor(R.color.grey));
                white.setBackgroundResource(R.color.white);
                white.setTextColor(getResources().getColor(R.color.grey));
                newBundle.putString("Q3","a");
                textEdges.setVisibility(View.VISIBLE);
                edgeSpin.setVisibility(View.VISIBLE);
                if(fibSpin.getSelectedItem().toString().equals("YES") && edgeSpin.getSelectedItem().toString().equals("SHARP")){
                    textHeal.setVisibility(View.INVISIBLE);
                    healSpin.setVisibility(View.INVISIBLE);
                }else{
                    redClicked[0] = true;
                    textHeal.setVisibility(View.VISIBLE);
                    healSpin.setVisibility(View.VISIBLE);
                }
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yellowSel[0] = !yellowSel[0];
                redClicked[0] = false;
                pinkSel[0] = false;
                redSel[0] = false;
                whiteSel[0] = false;
                red.setBackgroundResource(R.color.white);
                red.setTextColor(getResources().getColor(R.color.grey));
                yellow.setBackgroundResource(R.color.green);
                yellow.setTextColor(getResources().getColor(R.color.black));
                pink.setBackgroundResource(R.color.white);
                pink.setTextColor(getResources().getColor(R.color.grey));
                white.setBackgroundResource(R.color.white);
                white.setTextColor(getResources().getColor(R.color.grey));
                newBundle.putString("Q3","c");
                textEdges.setVisibility(View.INVISIBLE);
                edgeSpin.setVisibility(View.INVISIBLE);
                textHeal.setVisibility(View.INVISIBLE);
                healSpin.setVisibility(View.INVISIBLE);
            }
        });
        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinkSel[0] = !pinkSel[0];
                redClicked[0] = false;
                redSel[0] = false;
                yellowSel[0] = false;
                whiteSel[0] = false;
                red.setBackgroundResource(R.color.white);
                red.setTextColor(getResources().getColor(R.color.grey));
                yellow.setBackgroundResource(R.color.white);
                yellow.setTextColor(getResources().getColor(R.color.grey));
                pink.setBackgroundResource(R.color.green);
                pink.setTextColor(getResources().getColor(R.color.black));
                white.setBackgroundResource(R.color.white);
                white.setTextColor(getResources().getColor(R.color.grey));
                newBundle.putString("Q3","b");
                textEdges.setVisibility(View.VISIBLE);
                edgeSpin.setVisibility(View.VISIBLE);
                if(edgeSpin.getSelectedItem().toString().equals("SHARP")){
                    textHeal.setVisibility(View.INVISIBLE);
                    healSpin.setVisibility(View.INVISIBLE);
                }else{
                    textHeal.setVisibility(View.VISIBLE);
                    healSpin.setVisibility(View.VISIBLE);
                }
            }
        });
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whiteSel[0] = !whiteSel[0];
                redClicked[0] = false;
                pinkSel[0] = false;
                yellowSel[0] = false;
                redSel[0] = false;
                red.setBackgroundResource(R.color.white);
                red.setTextColor(getResources().getColor(R.color.grey));
                yellow.setBackgroundResource(R.color.white);
                yellow.setTextColor(getResources().getColor(R.color.grey));
                pink.setBackgroundResource(R.color.white);
                pink.setTextColor(getResources().getColor(R.color.grey));
                white.setBackgroundResource(R.color.green);
                white.setTextColor(getResources().getColor(R.color.black));
                newBundle.putString("Q3","d");
                textEdges.setVisibility(View.VISIBLE);
                edgeSpin.setVisibility(View.VISIBLE);
                if(edgeSpin.getSelectedItem().toString().equals("SHARP")){
                    textHeal.setVisibility(View.INVISIBLE);
                    healSpin.setVisibility(View.INVISIBLE);
                }else{
                    textHeal.setVisibility(View.VISIBLE);
                    healSpin.setVisibility(View.VISIBLE);
                }
            }
        });

        vesSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(vesSpin.getSelectedItem().toString().equals("YES")){
                    newBundle.putString("Q1", "a");
                    textFib.setVisibility(View.INVISIBLE);
                    fibSpin.setVisibility(View.INVISIBLE);
                    fibGalleryBtn.setVisibility(View.INVISIBLE);
                    textColour.setVisibility(View.INVISIBLE);
                    red.setVisibility(View.INVISIBLE);
                    redGalleryBtn.setVisibility(View.INVISIBLE);
                    yellow.setVisibility(View.INVISIBLE);
                    pink.setVisibility(View.INVISIBLE);
                    pinkGalleryBtn.setVisibility(View.INVISIBLE);
                    white.setVisibility(View.INVISIBLE);
                    whiteGalleryBtn.setVisibility(View.INVISIBLE);
                    textEdges.setVisibility(View.INVISIBLE);
                    edgeSpin.setVisibility(View.INVISIBLE);
                    textHeal.setVisibility(View.INVISIBLE);
                    healSpin.setVisibility(View.INVISIBLE);
                    red.setBackgroundResource(R.color.white);
                    red.setTextColor(getResources().getColor(R.color.grey));
                    yellow.setBackgroundResource(R.color.white);
                    yellow.setTextColor(getResources().getColor(R.color.grey));
                    pink.setBackgroundResource(R.color.white);
                    pink.setTextColor(getResources().getColor(R.color.grey));
                    white.setBackgroundResource(R.color.white);
                    white.setTextColor(getResources().getColor(R.color.grey));
                }else{
                    if(fibSpin.getSelectedItem().toString().equals("YES")){
                        yellow.setVisibility(View.INVISIBLE);
                        white.setVisibility(View.INVISIBLE);
                        whiteGalleryBtn.setVisibility(View.INVISIBLE);
                    }else{
                        yellow.setVisibility(View.VISIBLE);
                        white.setVisibility(View.VISIBLE);
                        whiteGalleryBtn.setVisibility(View.VISIBLE);
                    }
                    newBundle.putString("Q1", "b");
                    textFib.setVisibility(View.VISIBLE);
                    fibSpin.setVisibility(View.VISIBLE);
                    fibGalleryBtn.setVisibility(View.VISIBLE);
                    textColour.setVisibility(View.VISIBLE);
                    red.setVisibility(View.VISIBLE);
                    redGalleryBtn.setVisibility(View.VISIBLE);
                    pink.setVisibility(View.VISIBLE);
                    pinkGalleryBtn.setVisibility(View.VISIBLE);
                    textEdges.setVisibility(View.VISIBLE);
                    edgeSpin.setVisibility(View.VISIBLE);
                    textHeal.setVisibility(View.VISIBLE);
                    healSpin.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        fibSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(fibSpin.getSelectedItem().toString().equals("YES")){
                    newBundle.putString("Q2", "a");
                    yellow.setVisibility(View.INVISIBLE);
                    white.setVisibility(View.INVISIBLE);
                    whiteGalleryBtn.setVisibility(View.INVISIBLE);
                    yellow.setBackgroundResource(R.color.white);
                    yellow.setTextColor(getResources().getColor(R.color.grey));
                    white.setBackgroundResource(R.color.white);
                    white.setTextColor(getResources().getColor(R.color.grey));
                    if(vesSpin.getSelectedItem().toString().equals("YES")){
                        textEdges.setVisibility(View.INVISIBLE);
                        edgeSpin.setVisibility(View.INVISIBLE);
                        textHeal.setVisibility(View.INVISIBLE);
                        healSpin.setVisibility(View.INVISIBLE);
                    }else{
                        if(edgeSpin.getSelectedItem().toString().equals("SHARP")){
                            textHeal.setVisibility(View.INVISIBLE);
                            healSpin.setVisibility(View.INVISIBLE);
                        }else{
                            textHeal.setVisibility(View.VISIBLE);
                            healSpin.setVisibility(View.VISIBLE);
                        }
                        textEdges.setVisibility(View.VISIBLE);
                        edgeSpin.setVisibility(View.VISIBLE);
                    }
                }else{
                    newBundle.putString("Q2", "b");
                    yellow.setVisibility(View.VISIBLE);
                    white.setVisibility(View.VISIBLE);
                    whiteGalleryBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        edgeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(edgeSpin.getSelectedItem().toString().equals("SMOOTH/ROUNDED")){
                    newBundle.putString("Q4", "a");
                    if(vesSpin.getSelectedItem().toString().equals("YES")){
                        textHeal.setVisibility(View.INVISIBLE);
                        healSpin.setVisibility(View.INVISIBLE);
                    }else{
                        textHeal.setVisibility(View.VISIBLE);
                        healSpin.setVisibility(View.VISIBLE);
                    }
                }else{
                    newBundle.putString("Q4", "b");
                    if(!redClicked[0] || (redClicked[0] && fibSpin.getSelectedItem().toString().equals("YES"))){
                        textHeal.setVisibility(View.INVISIBLE);
                        healSpin.setVisibility(View.INVISIBLE);
                    }else{
                        textHeal.setVisibility(View.VISIBLE);
                        healSpin.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        healSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(healSpin.getSelectedItem().toString().equals("HEALING(SMALL)")){
                    newBundle.putString("Q5", "a");
                }else{
                    if(healSpin.getSelectedItem().toString().equals("HEALING(A LOT)")){
                        newBundle.putString("Q5", "b");
                    }else{
                        newBundle.putString("Q5", "c");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        Switch locker = findViewById(R.id.locker);
        locker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(vesSpin.isEnabled()){
                        vesSpin.setBackgroundResource(R.color.colorPrimary);
                    }
                    if(fibSpin.isEnabled()){
                        fibSpin.setBackgroundResource(R.color.colorPrimary);
                    }
                    if(redSel[0]){
                        red.setBackgroundResource(R.color.colorPrimary);
                        red.setTextColor(getResources().getColor(R.color.black));
                        lock = false;
                    }
                    if(yellowSel[0]){
                        yellow.setBackgroundResource(R.color.colorPrimary);
                        yellow.setTextColor(getResources().getColor(R.color.black));
                        lock = false;
                    }
                    if(pinkSel[0]){
                        pink.setBackgroundResource(R.color.colorPrimary);
                        pink.setTextColor(getResources().getColor(R.color.black));
                        lock = false;
                    }
                    if(whiteSel[0]){
                        white.setBackgroundResource(R.color.colorPrimary);
                        white.setTextColor(getResources().getColor(R.color.black));
                        lock = false;
                    }
                    if(!redSel[0] && !pinkSel[0] && !yellowSel[0] && !whiteSel[0] && (red.getVisibility() == View.VISIBLE)){
                        red.setBackgroundResource(R.color.TLyellow);
                        red.setTextColor(getResources().getColor(R.color.black));
                        yellow.setBackgroundResource(R.color.TLyellow);
                        yellow.setTextColor(getResources().getColor(R.color.black));
                        pink.setBackgroundResource(R.color.TLyellow);
                        pink.setTextColor(getResources().getColor(R.color.black));
                        white.setBackgroundResource(R.color.TLyellow);
                        white.setTextColor(getResources().getColor(R.color.black));
                        lock = true;
                    }
                    if(edgeSpin.isEnabled()){
                        edgeSpin.setBackgroundResource(R.color.colorPrimary);
                    }
                    if(healSpin.isEnabled()){
                        healSpin.setBackgroundResource(R.color.colorPrimary);
                    }
                    vesSpin.setEnabled(false);
                    fibSpin.setEnabled(false);
                    red.setEnabled(false);
                    yellow.setEnabled(false);
                    pink.setEnabled(false);
                    white.setEnabled(false);
                    edgeSpin.setEnabled(false);
                    healSpin.setEnabled(false);
                }else{
                    vesSpin.setBackgroundResource(R.color.white);
                    fibSpin.setBackgroundResource(R.color.white);
                    red.setBackgroundResource(R.color.white);
                    red.setTextColor(getResources().getColor(R.color.grey));
                    yellow.setBackgroundResource(R.color.white);
                    yellow.setTextColor(getResources().getColor(R.color.grey));
                    pink.setBackgroundResource(R.color.white);
                    pink.setTextColor(getResources().getColor(R.color.grey));
                    white.setBackgroundResource(R.color.white);
                    white.setTextColor(getResources().getColor(R.color.grey));
                    edgeSpin.setBackgroundResource(R.color.white);
                    healSpin.setBackgroundResource(R.color.white);
                    vesSpin.setEnabled(true);
                    fibSpin.setEnabled(true);
                    red.setEnabled(true);
                    pink.setEnabled(true);
                    yellow.setEnabled(true);
                    white.setEnabled(true);
                    edgeSpin.setEnabled(true);
                    healSpin.setEnabled(true);
                    redSel[0] = false;
                    pinkSel[0] = false;
                    yellowSel[0] = false;
                    whiteSel[0] = false;
                }
            }
        });

        Bundle g = new Bundle();

        vesGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g.putString("img","ves");
                Intent intent = new Intent(LesionAgeing.this, VesiclesGallery.class);
                intent.putExtras(g);
                startActivity(intent);
            }
        });

        fibGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g.putString("img","fib");
                Intent intent = new Intent(LesionAgeing.this, VesiclesGallery.class);
                intent.putExtras(g);
                startActivity(intent);
            }
        });

        redGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g.putString("img","red");
                Intent intent = new Intent(LesionAgeing.this, VesiclesGallery.class);
                intent.putExtras(g);
                startActivity(intent);
            }
        });

        pinkGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g.putString("img","pink");
                Intent intent = new Intent(LesionAgeing.this, VesiclesGallery.class);
                intent.putExtras(g);
                startActivity(intent);
            }
        });

        whiteGalleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g.putString("img","white");
                Intent intent = new Intent(LesionAgeing.this, VesiclesGallery.class);
                intent.putExtras(g);
                startActivity(intent);
            }
        });

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!locker.isChecked()){
                    Toast.makeText(getBaseContext(), "Lock information to proceed", Toast.LENGTH_LONG).show();
                    return;
                }
                if(locker.isChecked() && lock){
                    Toast.makeText(getBaseContext(), "There is an error in the item selections", Toast.LENGTH_LONG).show();
                    return;
                }

                newBundle.putInt("id",animal);
                Intent intent = new Intent(LesionAgeing.this, Suggestion.class);
                intent.putExtras(newBundle);
                startActivity(intent);
            }
        });

        ImageView camera = findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File f = null;
                try {
                    f = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Uri photoURI = FileProvider.getUriForFile(LesionAgeing.this, getBaseContext().getApplicationContext().getPackageName() + ".provider", f);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST && resultCode != 0) {
            /*DatabaseHelper mDbHelper = new DatabaseHelper(PostLesion.this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            Bitmap image = (Bitmap) data.getExtras().get("data");
            ContentValues cv = new ContentValues();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, out);
            cv.put(Photo.PhotoEntry.COLUMN_PHOTO, out.toByteArray());
            cv.put(Photo.PhotoEntry.COLUMN_LESION, getLesion());
            long newRowId = db.insert(Photo.PhotoEntry.TABLE_NAME, null, cv);
            if(newRowId == -1){
                Toast.makeText(getBaseContext(), "Error in the DB",
                        Toast.LENGTH_LONG).show();
                db.close();
            }else {
                Toast.makeText(getBaseContext(), "Photo added to the DB",
                        Toast.LENGTH_LONG).show();
                db.close();
            }*/
            galleryAddPic();
            Intent intent = new Intent(this, DrawOnBitmapActivity.class);
            String filePath = "file:" + mCurrentPhotoPath;
            intent.putExtra("image", filePath);
            startActivity(intent);
        }else{
            Toast.makeText(getBaseContext(), "Photo cancelled",
                    Toast.LENGTH_LONG).show();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.UK).format(new Date());
        String imageFileName = timeStamp + "_";
        File storageDir = new File(Environment.getExternalStorageDirectory(),"Pictures/FMD-DOI");
        storageDir.mkdirs();
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        System.out.println(mCurrentPhotoPath);
        return image;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
