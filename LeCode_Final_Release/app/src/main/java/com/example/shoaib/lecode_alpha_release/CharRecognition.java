package com.example.shoaib.lecode_alpha_release;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

/**
 * Created by Yash on 23-Apr-15.
 */
public class CharRecognition extends ActionBarActivity {
    TextView Text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_recognition);
        //OcrConversion();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_char_recognition, menu);
        Text = (TextView)findViewById(R.id.Code);
        Text.setText("Char Recong");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }
    public String OcrConvert(String s,int shape, String path){

        String IMAGE_PATH= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/test1.jpg";
        //String IMAGE_PATH= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/comments.JPG";

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        options.inMutable= true;
        Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_PATH, options);
        android.util.Log.v("New Diamenesion", String.valueOf((bitmap.getHeight())));


        android.util.Log.e("path", IMAGE_PATH);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(IMAGE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL);

        int rotate = 0;

        switch (exifOrientation) {



            case ExifInterface.ORIENTATION_ROTATE_90:
                rotate = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotate = 270;
                break;
        }

        if (rotate != 0) {


            int w = bitmap.getWidth();
            int h = bitmap.getHeight();

            // Setting pre rotate


            Matrix mtx = new Matrix();
            mtx.preRotate(rotate);

            // Rotating Bitmap & convert to ARGB_8888, required by tess
            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, w-10,h-10 );
            String height1 = String.valueOf(bitmap1.getHeight());
            android.util.Log.v("New Diamenesion",height1);

        }
        String height = String.valueOf(bitmap.getHeight());
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/tesseract-ocr", "eng");
        //baseApi.init(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),"eng");
        baseApi.setImage(bitmap);
        String recognizedText = baseApi.getUTF8Text();

        android.util.Log.e("OCRtext", recognizedText);
        //Text.setText(recognizedText.toString());
        baseApi.end();
          return recognizedText;
    }
}
