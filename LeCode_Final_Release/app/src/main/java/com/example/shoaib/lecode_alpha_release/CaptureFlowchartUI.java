package com.example.shoaib.lecode_alpha_release;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import java.io.File;

/**
 * Created by ChaoLin on 3/10/2015.
 */

public class CaptureFlowchartUI extends ActionBarActivity {

    private File imageFileDir;
    private File imageFile;
    private Uri imageUri;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_flowchart_ui);
        //Initiate the image file and its Uri
       // Log.d("Holla ", " C1");
        initialImageUri();
        //Log.d("Holla ", " C2");
        // Create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Log.d("Holla ", " C3");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        //Log.d("Holla ", " C4");
        // Start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
       // Log.d("Holla ", " C5");
    }

    private void initialImageUri() {
        //Make a directory for this app
        imageFileDir = new File(Environment.getExternalStorageDirectory(), "LeCoder_Image");
        //If the imageFileDir does not exist, create one
        if (! imageFileDir.exists()){
            if (! imageFileDir.mkdirs()){
                Log.d("Lecoder_Image", "failed to cre   ate directory");
            }
        }
        //Get the time stamp for the file's name
       // String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        imageFile = new File(imageFileDir.getPath()+File.separator+"FlowchartImage.jpg");
        //Set the Uri
        imageUri = Uri.parse("file://"+imageFile.getAbsolutePath());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Intent intent = new Intent(this, GenerateCodeUI.class);
                intent.putExtra("File Path", imageFile.getAbsolutePath());
                startActivity(intent);

            } else if (resultCode == RESULT_CANCELED) {

            } else {

            }
        }
    }

}
