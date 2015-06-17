package com.example.shoaib.lecode_alpha_release;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class StartScreenUI extends ActionBarActivity {
    public TextView Tess_Info;
    public int ready =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen_ui);
        Tess_Info=(TextView)findViewById(R.id.textView2);
        TrainedDataAvailable();
    }

    public void TrainedDataAvailable(){
        File path =  new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"/tesseract-ocr");
        if(!path.exists())
        {   ready=0;
            Tess_Info.setText(" Train Data Unavailable \n Please download the train data for tesseract library from the following link. \n 'https://code.google.com/p/tesseract-ocr/downloads/detail?name=tesseract-ocr-3.02.eng.tar.gz&can=2&q=' \n " +
                    " \n Once Downloaded extract and save the 'tesseract-ocr'  inside the pictures folder in your device. \n" +
                    "Once you have done this press 'Trained Data' button again to make sure it is detected.   ");
            Toast.makeText(getApplicationContext(), "Trained Data not found.Please refer to the above message",Toast.LENGTH_LONG).show();
        }
        else {
            ready = 1;
            Toast.makeText(getApplicationContext(), "Trained Data Available app ready to run", Toast.LENGTH_LONG).show();
            Tess_Info.setText("");
        }
    }


    public void TestTesseractData(View view){

        TrainedDataAvailable();

    }

    public void captureFlowchartPressed(View view){
        Intent intent = new Intent(this, CaptureFlowchartUI.class);
        startActivity(intent);
    }

    public void helpPressed(View view){

    }
}
