package com.example.shoaib.lecode_alpha_release;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import com.example.shoaib.lecode_alpha_release.Shape.Polygon;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by Yash on 23-Apr-15.
 */

public class TestTextRecognition extends ActivityInstrumentationTestCase2<GenerateCodeUI> {
    private GenerateCodeUI activity;
    private FileOutputStream fileOutputStream;
    public TestTextRecognition() {
        super(GenerateCodeUI.class);
    }

    @Override
    public void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(false);
        activity= getActivity();
        storeSampleImages();
    }

    public void  storeSampleImages()
    {
        String extStorageDir = Environment.getExternalStorageDirectory().toString();
        try {
            Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.one_text_diamond);

            File file = new File(extStorageDir+"/LeCoder_Image", "test_diamond.jpg");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Bitmap bm1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.one_rectangle_text2);
            File file2 = new File(extStorageDir+"/LeCoder_Image", "one_rectangle_text2.jpg");
            fileOutputStream = new FileOutputStream(file2);
            bm1.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            Bitmap bm2 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.one_while_text);
            File file3 = new File(extStorageDir+"/LeCoder_Image", "one_hexagon_text2.jpg");
            fileOutputStream = new FileOutputStream(file3);
            bm2.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();


        }catch(Exception e){
            Log.v("Error", "Can not save files on ex");
        }

    }

    @MediumTest
    public void testOneRectangleText() throws Exception {
        Log.v(" ", Environment.getExternalStorageDirectory().toString());
        //Define the file's path for one rectangle picture
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/one_rectangle_text2.jpg";
        Log.v("image path: ", imagePath);
        activity.loadImageFile(imagePath);
        Log.v("after", "after");
        activity.processImage();
        ImageProcess ip = activity.getImageProcess();

        activity.textDetection();
        ArrayList<Polygon> rectangle = ip.getPolygons().get(0);
        if(rectangle.size()>0){
            String content = rectangle.get(0).getContent();
            Log.v("after", "Rectangle Detected "+content+" found   ");
            assertEquals(content,"X=5");
        }
        //Check if the number of rectangles in the image is correct

    }

    @MediumTest
    public void testOneDiamondText() throws Exception {
        Log.v(" ", Environment.getExternalStorageDirectory().toString());
        //Define the file's path for one rectangle picture
        String imagePath = Environment.getExternalStorageDirectory() + "/LeCoder_Image/test_diamond.jpg";
        Log.v("image path: ", imagePath);
        activity.loadImageFile(imagePath);
        Log.v("after", "diamond");
        activity.processImage();
        ImageProcess ip = activity.getImageProcess();

        activity.textDetection();
        ArrayList<Polygon> diamond = ip.getPolygons().get(1);
        if (diamond.size() > 0) {
            String content = diamond.get(0).getContent();
            assertTrue(content.equals("X==1") ||content.equals("x==1"));
        }
        //Check if the number of rectangles in the image is correct

    }

    @MediumTest
    public void testOneHexagonText() throws Exception {
        Log.v(" ", Environment.getExternalStorageDirectory().toString());
        //Define the file's path for one rectangle picture
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/one_hexagon_text2.jpg";
        Log.v("image path: ", imagePath);
        activity.loadImageFile(imagePath);
        Log.v("after", "after");
        activity.processImage();
        ImageProcess ip = activity.getImageProcess();

        activity.textDetection();
        ArrayList<Polygon> hexagons = ip.getPolygons().get(2);
        if(hexagons.size()>0){
            String content = hexagons.get(0).getContent();
            Log.v("after", "Hexagons Detected "+content+" found   ");
            assertEquals(content,"k>=2");
        }
        //Check if the number of rectangles in the image is correct

    }

}
