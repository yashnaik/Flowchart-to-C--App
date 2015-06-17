package com.example.shoaib.lecode_alpha_release;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.MediumTest;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by ChaoLin on 5/1/2015.
 */
public class TestSyntaxError extends ActivityInstrumentationTestCase2<GenerateCodeUI> {
    private GenerateCodeUI activity;
    private FileOutputStream fileOutputStream;

    public TestSyntaxError() {
        super(GenerateCodeUI.class);
    }

    @Override
    public void setUp() throws Exception{
        super.setUp();
        setActivityInitialTouchMode(false);
        activity = getActivity();
        storeSampleImages();
    }

    private void storeSampleImages() {
        String extStorageDir = Environment.getExternalStorageDirectory().toString();
        try{
            //Store if_without_false.png to external storage
            Bitmap bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.if_without_false);
            File file = new File(extStorageDir+"/LeCoder_Image", "if_without_false.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save if_without_true.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.if_without_true);
            file = new File(extStorageDir+"/LeCoder_Image", "if_without_true.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save if_without_next.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.if_without_next);
            file = new File(extStorageDir+"/LeCoder_Image", "if_without_next.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save no_start.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.no_start);
            file = new File(extStorageDir+"/LeCoder_Image", "no_start.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save statement_without_prev.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.statement_without_prev);
            file = new File(extStorageDir+"/LeCoder_Image", "statement_without_prev.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save while_without_loop.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.while_without_loop);
            file = new File(extStorageDir+"/LeCoder_Image", "while_without_loop.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            //Save while_without_next.png to external storage
            bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.while_without_next);
            file = new File(extStorageDir+"/LeCoder_Image", "while_without_next.png");
            fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch(Exception e){
            Log.v("Error", "Can not save files on ex");
        }
    }

    @MediumTest
    public void test_1314_1() throws Exception {
        //Generate the file's path
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/no_start.png";
        //Load the file and process it
        activity.loadImageFile(imagePath);
        activity.processImage();
        //Generate the nodes and construct the graph
        activity.generateNodes();
        activity.generateGraph();
        //Print all nodes
        activity.printAllNodes();
        //Check whether the flowchart has no start point
        assertFalse(activity.checkAllNodes());
    }

    @MediumTest
    public void test_1314_2() throws Exception {
        //Generate the file's path
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/statement_without_prev.png";
        //Load the file and process it
        activity.loadImageFile(imagePath);
        activity.processImage();
        //Generate the nodes and construct the graph
        activity.generateNodes();
        activity.generateGraph();
        //Print all nodes
        activity.printAllNodes();
        //Check whether the flowchart has no start point
        assertFalse(activity.checkAllNodes());
    }

    @MediumTest
    public void test_1314_3() throws Exception {
        //Generate the file's path
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/if_without_false.png";
        //Load the file and process it
        activity.loadImageFile(imagePath);
        activity.processImage();
        //Generate the nodes and construct the graph
        activity.generateNodes();
        activity.generateGraph();
        //Print all nodes
        activity.printAllNodes();
        //Check whether the flowchart has no start point
        assertFalse(activity.checkAllNodes());
    }

    @MediumTest
    public void test_1314_4() throws Exception {
        //Generate the file's path
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/if_without_true.png";
        //Load the file and process it
        activity.loadImageFile(imagePath);
        activity.processImage();
        //Generate the nodes and construct the graph
        activity.generateNodes();
        activity.generateGraph();
        //Print all nodes
        activity.printAllNodes();
        //Check whether the flowchart has no start point
        assertFalse(activity.checkAllNodes());
    }

    @MediumTest
    public void test_1314_5() throws Exception {
        //Generate the file's path
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/if_without_next.png";
        //Load the file and process it
        activity.loadImageFile(imagePath);
        activity.processImage();
        //Generate the nodes and construct the graph
        activity.generateNodes();
        activity.generateGraph();
        //Print all nodes
        activity.printAllNodes();
        //Check whether the flowchart has no start point
        assertFalse(activity.checkAllNodes());
    }

    @MediumTest
    public void test_1314_6() throws Exception {
        //Generate the file's path
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/while_without_loop.png";
        //Load the file and process it
        activity.loadImageFile(imagePath);
        activity.processImage();
        //Generate the nodes and construct the graph
        activity.generateNodes();
        activity.generateGraph();
        //Print all nodes
        activity.printAllNodes();
        //Check whether the flowchart has no start point
        assertFalse(activity.checkAllNodes());
    }

    @MediumTest
    public void test_1314_7() throws Exception {
        //Generate the file's path
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/while_without_next.png";
        //Load the file and process it
        activity.loadImageFile(imagePath);
        activity.processImage();
        //Generate the nodes and construct the graph
        activity.generateNodes();
        activity.generateGraph();
        //Print all nodes
        activity.printAllNodes();
        //Check whether the flowchart has no start point
        assertFalse(activity.checkAllNodes());
    }
}
