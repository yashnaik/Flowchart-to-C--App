package com.example.shoaib.lecode_alpha_release;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import com.example.shoaib.lecode_alpha_release.Shape.Circle;
import com.example.shoaib.lecode_alpha_release.Shape.Polygon;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by ChaoLin on 4/13/2015.
 */
public class ImageProcessTest extends ActivityInstrumentationTestCase2<GenerateCodeUI>{
    private GenerateCodeUI activity;
    private FileOutputStream fileOutputStream;

    public ImageProcessTest(){
        super(GenerateCodeUI.class);
    }

    @Override
    public void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(false);
        activity = getActivity();
        storeSampleImages();

    }

    public void storeSampleImages(){
        String extStorageDir = Environment.getExternalStorageDirectory().toString();
        try{
            //Store one_circle.png to external storage
            Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.one_circle);
            File file = new File(extStorageDir+"/LeCoder_Image", "one_circle.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save one_rectangle.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.one_rectangle);
            file = new File(extStorageDir+"/LeCoder_Image", "one_rectangle.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save one_diamond.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.one_diamond);
            file = new File(extStorageDir+"/LeCoder_Image", "one_diamond.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save one_hexagon.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.one_hexagon);
            file = new File(extStorageDir+"/LeCoder_Image", "one_hexagon.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save one_customshape.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.one_customshape);
            file = new File(extStorageDir+"/LeCoder_Image", "one_customshape.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save start_point.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.start_point);
            file = new File(extStorageDir+"/LeCoder_Image", "start_point.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save statement_node.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.statement_node);
            file = new File(extStorageDir+"/LeCoder_Image", "statement_node.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save if_node.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.if_node);
            file = new File(extStorageDir+"/LeCoder_Image", "if_node.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch(Exception e){
            Log.v("Error", "Can not save files on ex");
        }

    }

    @MediumTest
    public void testOneCircle() throws Exception {
        Log.v(" ", Environment.getExternalStorageDirectory().toString());
        //Define the file's path for one circle picture
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/one_circle.png";
        Log.v("image path: ", imagePath);
        activity.loadImageFile(imagePath);
        Log.v("after", "after");
        activity.processImage();
        ImageProcess ip = activity.getImageProcess();
        //Check if the number of circles in the image is correct
        assertTrue(ip.getCircles().size() == 1);
    }

    @MediumTest
    public void testOneRectangle() throws Exception {
        Log.v(" ", Environment.getExternalStorageDirectory().toString());
        //Define the file's path for one rectangle picture
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/one_rectangle.png";
        Log.v("image path: ", imagePath);
        activity.loadImageFile(imagePath);
        Log.v("after", "after");
        activity.processImage();
        ImageProcess ip = activity.getImageProcess();
        //Check if the number of rectangles in the image is correct
        assertTrue(ip.getPolygons().get(0).size() == 1);
    }

    @MediumTest
    public void testOneDiamond() throws Exception {
        Log.v(" ", Environment.getExternalStorageDirectory().toString());
        //Define the file's path for one diamond picture
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/one_diamond.png";
        Log.v("image path: ", imagePath);
        activity.loadImageFile(imagePath);
        Log.v("after", "after");
        activity.processImage();
        ImageProcess ip = activity.getImageProcess();
        //Check if the number of diamonds in the image is correct
        assertTrue(ip.getPolygons().get(1).size() == 1);
    }

    @MediumTest
    public void testOneHexagon() throws Exception {
        Log.v(" ", Environment.getExternalStorageDirectory().toString());
        //Define the file's path for one hexagons picture
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/one_hexagon.png";
        Log.v("image path: ", imagePath);
        activity.loadImageFile(imagePath);
        Log.v("after", "after");
        activity.processImage();
        ImageProcess ip = activity.getImageProcess();
        //Check if the number of hexagons in the image is correct
        assertTrue(ip.getPolygons().get(2).size() == 1);
    }

    @MediumTest
    public void testOneCustomShape() throws Exception {
        Log.v(" ", Environment.getExternalStorageDirectory().toString());
        //Define the file's path for one custom shape picture
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/one_customshape.png";
        Log.v("image path: ", imagePath);
        activity.loadImageFile(imagePath);
        Log.v("after", "after");
        activity.processImage();
        ImageProcess ip = activity.getImageProcess();
        //Check if the number of custom shapes in the image is correct
        assertTrue(ip.getPolygons().get(3).size() == 1);
    }

    @MediumTest
    public void testStartPoint() throws Exception {
        Log.v(" ", Environment.getExternalStorageDirectory().toString());
        //Define the file's path for start point picture
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/start_point.png";
        Log.v("image path: ", imagePath);
        activity.loadImageFile(imagePath);
        Log.v("after", "after");
        activity.processImage();
        ImageProcess ip = activity.getImageProcess();
        ip.detectLines();
        //Check if it detected a circle
        assertTrue(ip.getCircles().size() == 1);
        Circle circle = ip.getCircles().get(0);
        Log.v("connect circle",""+ip.connectedLines(circle));
        //Check if there is at least one lines connect to the circle
        assertTrue(ip.connectedLines(circle) >= 1);
    }

    @MediumTest
    public void testStatementNode() throws Exception {
        Log.v(" ", Environment.getExternalStorageDirectory().toString());
        //Define the file's path for statement node picture
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/statement_node.png";
        Log.v("image path: ", imagePath);
        activity.loadImageFile(imagePath);
        Log.v("after", "after");
        activity.processImage();
        ImageProcess ip = activity.getImageProcess();
        ip.detectLines();
        //Check if it detect a rectangle
        assertTrue(ip.getPolygons().get(0).size() == 1);
        Polygon rectangle = ip.getPolygons().get(0).get(0);
        Log.v("connect rectangle",""+ip.connectedLines(rectangle));
        //Check if there are at least 2 lines connect to the rectangle
        assertTrue(ip.connectedLines(rectangle)>=2);
    }

    @MediumTest
    public void testIfNode() throws Exception {
        Log.v(" ", Environment.getExternalStorageDirectory().toString());
        //Define the file's path for if node picture
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/if_node.png";
        Log.v("image path: ", imagePath);
        activity.loadImageFile(imagePath);
        Log.v("after", "after");
        activity.processImage();
        ImageProcess ip = activity.getImageProcess();
        ip.detectLines();
        //Check if it detect a diamond
        assertTrue(ip.getPolygons().get(1).size() == 1);
        Polygon diamond = ip.getPolygons().get(1).get(0);
        Log.v("connect diamond: ",""+ip.connectedLines(diamond));
        //Check if there are at least 4 lines connect to the diamond
        assertTrue(ip.connectedLines(diamond)>=4);
    }
    /*@MediumTest
    public void testText() throws Exception {
        String path="path";
        assertEquals(recognizedtext,OcrConvert());        //Check this line
    }*/
    @MediumTest
    public void testRectangle() throws  Exception{

    }
}
