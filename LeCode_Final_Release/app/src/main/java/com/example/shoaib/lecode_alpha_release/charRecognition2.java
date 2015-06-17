package com.example.shoaib.lecode_alpha_release;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.IOException;

/**
 * Created by Yash on 23-Apr-15.
 */
public class charRecognition2 {
    public String OcrConvert(double x1,double y1,double x2,double y2,int shape, String IMAGE_PATH){  //Accept co-ordinates of shapes and image path for text recognition
        Log.d("OcrConvert","Called");
        // String IMAGE_PATH= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/test1.jpg";
        //String IMAGE_PATH= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/comments.JPG";
        x1=  x1/2;
        y1= y1/2;
        x2= x2/2;
        y2=y2/2;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        options.inMutable= true;
        Bitmap bitmap = BitmapFactory.decodeFile(IMAGE_PATH, options);//Create a mutable bitmape of the image
        android.util.Log.v("New Dimension", String.valueOf((bitmap.getHeight())));
        double width=x2-x1;                  //Get height and width of the window to be scanned using the co-ordinates accepted
        double height=y2-y1;

        if(shape==1&&width>40&&height>20)
        {
            width = width -20;
            height = height -10;
            x1=x1+10;
            y1=y1+10;
        }
        else if (shape==2)
        {    x1= x1+width/4;
            y1= y1+height/4;
            width = width*0.5;
            height=height/2;


        }
        else if (shape==3)
        {
            x1= x1 + width/5;
            y1= y1 + height/4;
            width = width*0.5;
            height=height/2;


        }
        Log.d("width", (x1)+" "+width+" "+ bitmap.getWidth()+ " " + shape);
        Log.d("height",(y1+" " +height)+" "+ bitmap.getHeight());

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

        //if (rotate != 0) {




        // Setting pre rotate


        Matrix mtx = new Matrix();
        mtx.preRotate(rotate);

        // Rotating Bitmap & convert to ARGB_8888, required by tess
        //Crop the source bitmap to only scan the received window area
        // bitmap = Bitmap.createBitmap(bitmap, (int)x1/2, (int)(y1/2), (int)(width),(int)(height));
        //bitmap = Bitmap.createBitmap(bitmap, 410,250, 80,40);
        //bitmap = Bitmap.createBitmap(bitmap, 660,480, 140,40);
        //bitmap = Bitmap.createBitmap(bitmap, 90,480, 130,40);
        if(shape==1 ||shape==2)
        {bitmap = Bitmap.createBitmap(bitmap, (int)x1, (int)(y1), (int)(width),(int)(height));}
        else
        {bitmap = Bitmap.createBitmap(bitmap, (int)x1, (int)(y1), (int)(width),(int)(height));}
        String height1 = String.valueOf(bitmap.getHeight());
        android.util.Log.v("New Dimension2",height1);

        //}
        //String height = String.valueOf(bitmap.getHeight());
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);

        //Have a predefined data to train tess library
        baseApi.init(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/tesseract-ocr", "eng");
        //baseApi.init(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath(),"eng");
        //Pass the bitmap image to the tess-two library to text detection
        baseApi.setImage(bitmap);

        //Store the recognized data in a string
        String recognizedText = baseApi.getUTF8Text();
        if(shape==1 || shape==2)
        {
            recognizedText= recognizedText.replaceAll(":","=");
            recognizedText= recognizedText.replace("(","<");
            recognizedText= recognizedText.replace(")",">");

            Log.d("recognized",recognizedText);

        }
        else if(shape==3)
        {
            for(int i=0;i<recognizedText.length();i++)
            {

                recognizedText= recognizedText.replace("(","<");
                recognizedText= recognizedText.replace(")",">");
                Log.d("recognized",recognizedText);


            }
        }
        Log.d("OCRtext", recognizedText);
        //Text.setText(recognizedText.toString());
        baseApi.end();
        //return the string
        return recognizedText;
    }
}
