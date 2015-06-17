package com.example.shoaib.lecode_alpha_release;

import android.os.Environment;
import android.util.Log;

import com.example.shoaib.lecode_alpha_release.Shape.Circle;
import com.example.shoaib.lecode_alpha_release.Shape.CustomShape;
import com.example.shoaib.lecode_alpha_release.Shape.Diamond;
import com.example.shoaib.lecode_alpha_release.Shape.Hexagon;
import com.example.shoaib.lecode_alpha_release.Shape.Line;
import com.example.shoaib.lecode_alpha_release.Shape.Polygon;
import com.example.shoaib.lecode_alpha_release.Shape.Rectangle;
import com.example.shoaib.lecode_alpha_release.Shape.Shape;

import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.highgui.Highgui.imread;
/**
 * Created by ChaoLin on 3/9/2015.
 */
public class ImageProcess {
    private Mat imageSrc;
    private Mat imageGray;
    private Mat imageColor;
    private ArrayList<Line> lines;
    private ArrayList<Circle> circles;
    private ArrayList<ArrayList<Polygon>> polygons;
    private ArrayList<Polygon> rectangles;
    private ArrayList<Polygon> diamonds;
    private ArrayList<Polygon> hexagons;
    private ArrayList<Polygon> customshapes;

    public ImageProcess(String image){
        //Check if OpenCV could work properly

        Log.d("Entered ImageProcess", "\n");
        if (!OpenCVLoader.initDebug()) {
            Log.v("Opencv not work", " ");
        }
        Log.d("OpenCv is working !!", "\n");
        //Read image from source file on external storage
        Log.d("IP ", "Step 1\n");
        imageSrc = imread(image);
        Log.v("height + width ", imageSrc.height()+ " "+ imageSrc.width());
        Log.d("IP ", "Step 2\n");
        if(imageSrc.empty()){
            Log.v("", "No Source");
        }
        Log.d("IP ", "Step 3\n");
        imageGray = new Mat();
        Log.d("IP ", "Step 4\n");
        imageColor = new Mat();
        //Gray Scale the source image
        Imgproc.cvtColor(imageSrc, imageGray, Imgproc.COLOR_RGB2GRAY);
        //Use the morphology close to process the gray scaled image
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
        Mat temp = new Mat();
        //Scaled down the image, use morphology transformation to close the image, and then scaled the image to normal size.
        Imgproc.resize(imageGray, temp, new Size(imageGray.cols() / 4, imageGray.rows() / 4));
        Imgproc.morphologyEx(temp, temp, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.resize(temp, temp, new Size(imageGray.cols(), imageGray.rows()));
        //Normalize the image
        Core.divide(imageGray, temp, temp, 1, CvType.CV_32F);
        Core.normalize(temp, imageGray, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
        //Threshold the image using THRESH_BINARY_INV and THRESH_OTSU
        Imgproc.threshold(imageGray, imageGray, -1, 255,
                Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
        //Generate a colored image from the canny gray scaled image
        Imgproc.cvtColor(imageGray, imageColor, Imgproc.COLOR_GRAY2RGB);
    }

    public ArrayList<Line> detectLines(){
        Imgproc.cvtColor(imageSrc, imageGray, Imgproc.COLOR_RGB2GRAY);
        //Imgproc.Canny(imageGray, imageGray, 80, 100);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
        Mat temp = new Mat();

        Imgproc.resize(imageGray, temp, new Size(imageGray.cols() / 4, imageGray.rows() / 4));
        Imgproc.morphologyEx(imageGray, imageGray, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.resize(temp, temp, new Size(imageGray.cols(), imageGray.rows()));

        Core.divide(imageGray, temp, temp, 1, CvType.CV_32F); // temp will now have type CV_32F
        Core.normalize(temp, imageGray, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);

        Imgproc.threshold(imageGray, imageGray, -1, 255,
                Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);

        Mat cvLines = new Mat();
        Imgproc.HoughLinesP(imageGray, cvLines, 1, Math.PI / 180, 100, 100, 50);
        lines = new ArrayList<Line>();
        for(int i = 0; i<cvLines.cols(); i++){
            double[] vector = cvLines.get(0, i);
            double x1 = vector[0];
            double y1 = vector[1];
            double x2 = vector[2];
            double y2 = vector[3];
            Point start = new Point(x1, y1);
            Point end = new Point(x2, y2);
            //Log.v("("+x1+","+y1+") ", "("+x2+","+y2+")");
            lines.add(new Line(start, end));

            Core.line(imageColor, start, end, new Scalar(0, 0, 255), 1);
        }
        Log.v("lines num: ", ""+lines.size());
        return lines;
    }

    public ArrayList<Circle> detectCircles(){
        circles = new ArrayList<Circle>();
        //Gray Scale the source image
        Imgproc.cvtColor(imageSrc, imageGray, Imgproc.COLOR_RGB2GRAY);
        //Use Gaussian filter to blur the gray scaled image
        Imgproc.GaussianBlur(imageGray, imageGray, new Size(3, 3), 2, 2);
        //Create a empty mat to store the circle data
        Mat cvCircles = new Mat();
        //Use HoughCircle to find the circles
        //Set minimum center distance to 100 to eliminate duplicate circles
        //Set minimum radius to 50 to eliminate small noise
        Imgproc.HoughCircles(imageGray, cvCircles, Imgproc.CV_HOUGH_GRADIENT, 1, 100, 100, 50, 50, 600);
        //Read detect circles from mat, their radius and center point, and stored these data in Circle data structure
        for(int i = 0; i<cvCircles.cols(); i++){
            double[] vector = cvCircles.get(0, i);
            double x = vector[0];
            double y = vector[1];
            double radius = vector[2];
            Point center = new Point(x, y);
            //Log.v("("+x+","+y+") ", " "+radius);
            circles.add(new Circle(radius, center));
            //Draw the circle on colored image
            Core.circle(imageColor, center, (int) radius, new Scalar(0, 255, 255), 2);
        }
        Log.v("circles num: ", ""+circles.size());
        return circles;
    }

    public ArrayList<ArrayList<Polygon>> detectPolygons(){
        polygons = new ArrayList<ArrayList<Polygon>>();
        rectangles = new ArrayList<Polygon>();
        diamonds = new ArrayList<Polygon>();
        hexagons = new ArrayList<Polygon>();
        customshapes = new ArrayList<Polygon>();
        //Gray scale the source image
        Imgproc.cvtColor(imageSrc, imageGray, Imgproc.COLOR_RGB2GRAY);
        //Create a kernel
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
        Mat temp = new Mat();
        //Scaled down the image, use morphology transformation to close the image, and then scaled the image to normal size.
        Imgproc.resize(imageGray, temp, new Size(imageGray.cols() / 4, imageGray.rows() / 4));
        Imgproc.morphologyEx(temp, temp, Imgproc.MORPH_CLOSE, kernel);
        Imgproc.resize(temp, temp, new Size(imageGray.cols(), imageGray.rows()));
        //Normalize the image
        Core.divide(imageGray, temp, temp, 1, CvType.CV_32F);
        Core.normalize(temp, imageGray, 0, 255, Core.NORM_MINMAX, CvType.CV_8U);
        //Threshold the image using THRESH_BINARY_INV and THRESH_OTSU
        Imgproc.threshold(imageGray, imageGray, -1, 255,
                Imgproc.THRESH_BINARY_INV + Imgproc.THRESH_OTSU);
        //Create a list of MatOfPoint to store the contours data
        List<MatOfPoint> contours=new ArrayList<MatOfPoint>();
        //Use findContours to find the contours in the gray scale image
        Imgproc.findContours(imageGray, contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
        for(int i = 0; i<contours.size(); i++){
            MatOfPoint approx = new MatOfPoint();
            MatOfPoint2f temp1 = new MatOfPoint2f();
            MatOfPoint2f temp2 = new MatOfPoint2f();
            //Approximate the polygons
            contours.get(i).convertTo(temp1, CvType.CV_32FC2);
            Imgproc.approxPolyDP(temp1, temp2, 15, true);
            temp2.convertTo(approx, CvType.CV_32S);
            //If the contour has four corners, it is a rectangle or a diamond
            if( approx.toArray().length == 4 && Math.abs(Imgproc.contourArea(approx)) > 200&& Imgproc.isContourConvex(approx) ){
                //Calculate the max cosine of the shape
                double maxCosine = 0;
                for( int j = 2; j < 5; j++ ){
                    // find the maximum cosine of the angle between joint edges
                    double cosine = Math.abs(angle(approx.toArray()[j%4], approx.toArray()[j-2], approx.toArray()[j-1]));
                    maxCosine = Math.max(maxCosine, cosine);
                }
                //Imgproc.drawContours(imageColor, contours, i, new Scalar(255, 0, 255), 3);
                //If the max consine is less than 0.25 (between 75 and 105), it should be a rectangle
                //Otherwise, it should be a diamond
                if( maxCosine < 0.25 ){
                    //Read the data and stored them in Rectangle data structure
                    Point p0 = new Point(approx.toArray()[0].x,approx.toArray()[0].y);
                    Point p1 = new Point(approx.toArray()[1].x,approx.toArray()[1].y);
                    Point p2 = new Point(approx.toArray()[2].x,approx.toArray()[2].y);
                    Point p3 = new Point(approx.toArray()[3].x,approx.toArray()[3].y);
                    ArrayList<Point> points = new ArrayList<Point>();
                    points.add(p0);points.add(p1);points.add(p2);points.add(p3);
                    rectangles.add(new Rectangle(points));
                    //Draw the rectangle
                    //Core.line(imageColor, p0, p1, new Scalar(255, 0, 255), 2);
                    //Core.line(imageColor, p1, p2, new Scalar(255, 0, 255), 2);
                    //Core.line(imageColor, p2, p3, new Scalar(255, 0, 255), 2);
                    //Core.line(imageColor, p3, p0, new Scalar(255, 0, 255), 2);
                }else{
                    //Read the data and stored them in Diamond data structure
                    Point p0 = new Point(approx.toArray()[0].x,approx.toArray()[0].y);
                    Point p1 = new Point(approx.toArray()[1].x,approx.toArray()[1].y);
                    Point p2 = new Point(approx.toArray()[2].x,approx.toArray()[2].y);
                    Point p3 = new Point(approx.toArray()[3].x,approx.toArray()[3].y);
                    ArrayList<Point> points = new ArrayList<Point>();
                    points.add(p0);points.add(p1);points.add(p2);points.add(p3);
                    diamonds.add(new Diamond(points));
                    //Draw the diamond
                    //Core.line(imageColor, p0, p1, new Scalar(255, 0, 255), 2);
                    //Core.line(imageColor, p1, p2, new Scalar(255, 0, 255), 2);
                    //Core.line(imageColor, p2, p3, new Scalar(255, 0, 255), 2);
                    //Core.line(imageColor, p3, p0, new Scalar(255, 0, 255), 2);
                }
            }
            //If the contour has six corners, it is a hexagon
            else if(approx.toArray().length == 6 && Math.abs(Imgproc.contourArea(approx)) > 200&& Imgproc.isContourConvex(approx)){
                //Read the data and stored them in Hexagon data structure
                Point p0 = new Point(approx.toArray()[0].x,approx.toArray()[0].y);
                Point p1 = new Point(approx.toArray()[1].x,approx.toArray()[1].y);
                Point p2 = new Point(approx.toArray()[2].x,approx.toArray()[2].y);
                Point p3 = new Point(approx.toArray()[3].x,approx.toArray()[3].y);
                Point p4 = new Point(approx.toArray()[4].x,approx.toArray()[4].y);
                Point p5 = new Point(approx.toArray()[5].x,approx.toArray()[5].y);
                ArrayList<Point> points = new ArrayList<Point>();
                points.add(p0);points.add(p1);points.add(p2);points.add(p3);points.add(p4);points.add(p5);
                hexagons.add(new Hexagon(points));
                //Draw the hexagon
                //Core.line(imageColor, p0, p1, new Scalar(255, 0, 255), 2);
                //Core.line(imageColor, p1, p2, new Scalar(255, 0, 255), 2);
                //Core.line(imageColor, p2, p3, new Scalar(255, 0, 255), 2);
                //Core.line(imageColor, p3, p4, new Scalar(255, 0, 255), 2);
                //Core.line(imageColor, p4, p5, new Scalar(255, 0, 255), 2);
                //Core.line(imageColor, p5, p0, new Scalar(255, 0, 255), 2);
            }

            //If the contour has five corners, it is a hexagon
            else if(approx.toArray().length == 5 && Math.abs(Imgproc.contourArea(approx)) > 200&& Imgproc.isContourConvex(approx)){
                //Read the data and stored them in CustomSHap data structure
                Point p0 = new Point(approx.toArray()[0].x,approx.toArray()[0].y);
                Point p1 = new Point(approx.toArray()[1].x,approx.toArray()[1].y);
                Point p2 = new Point(approx.toArray()[2].x,approx.toArray()[2].y);
                Point p3 = new Point(approx.toArray()[3].x,approx.toArray()[3].y);
                Point p4 = new Point(approx.toArray()[4].x,approx.toArray()[4].y);
                ArrayList<Point> points = new ArrayList<Point>();
                points.add(p0);points.add(p1);points.add(p2);points.add(p3);points.add(p4);
                customshapes.add(new CustomShape(points));
            }

        }

        //Check if there exists duplicate detected rectangles, if there exists, just keep 1
        int sizeRec = rectangles.size();
        if(sizeRec > 0){
            //Use a boolean array to store where the rectangle is duplicated
            boolean[] duplicateRec = new boolean[sizeRec];
            for(int i = 0; i<sizeRec; i++){
                Polygon rec1 = rectangles.get(i);
                for(int j = i+1; j<sizeRec; j++){
                    Polygon rec2 = rectangles.get(j);
                    //Get the range of each rectangles
                    ArrayList<Point> range1 = rec1.getRange();
                    ArrayList<Point> range2 = rec2.getRange();
                    Log.v("Rectangle1: ("+range1.get(0).x +" "+ range1.get(0).y+")", "("+range1.get(3).x+ " " + range1.get(3).y + ")");
                    Log.v("Rectangle2: ("+range2.get(0).x +" "+ range2.get(0).y+")", "("+range2.get(3).x+ " " + range2.get(3).y + ")");
                    //Compare the upper-left corners and bottom-right corners of the two rectangles
                    if(checkDuplicate(range1.get(0), range2.get(0)) && checkDuplicate(range1.get(3), range2.get(3))){
                        duplicateRec[j] = true;
                    }
                }
            }
            //Delete the duplicated ones
            for(int i = sizeRec -1; i>=0; i--){
                if(duplicateRec[i] == true){
                    rectangles.remove(i);
                }
            }
            //Draw the rectangle
            for(int i = 0; i<rectangles.size(); i++){
                ArrayList<Point> range = rectangles.get(i).getPoints();
                Core.line(imageColor, range.get(0), range.get(1), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(1), range.get(2), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(2), range.get(3), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(3), range.get(0), new Scalar(255, 0, 0), 3);
            }
        }

        //Check if there exists duplicate detected diamonds, if there exists, just keep 1
        int sizeDia = diamonds.size();
        if(sizeDia > 0){
            //Use a boolean array to store where the diamond is duplicated
            boolean[] duplicateDia = new boolean[sizeDia];
            for(int i = 0; i<sizeDia; i++){
                Polygon dia1 = diamonds.get(i);
                for(int j = i+1; j<sizeDia; j++){
                    Polygon dia2 = diamonds.get(j);
                    //Get the range of each diamonds
                    ArrayList<Point> range1 = dia1.getRange();
                    ArrayList<Point> range2 = dia2.getRange();
                    Log.v("Diamond 1 ("+range1.get(0).x +" "+ range1.get(0).y+")", "("+range1.get(3).x+ " " + range1.get(3).y + ")");
                    Log.v("Diamond 2 ("+range2.get(0).x +" "+ range2.get(0).y+")", "("+range2.get(3).x+ " " + range2.get(3).y + ")");
                    //Compare the upper-left corners and bottom-right corners of the two diamonds
                    if(checkDuplicate(range1.get(0), range2.get(0)) && checkDuplicate(range1.get(3), range2.get(3))){
                        duplicateDia[j] = true;
                    }
                }
            }
            //Delete the duplicated ones
            for(int i = sizeDia -1; i>=0; i--){
                if(duplicateDia[i] == true){
                    diamonds.remove(i);
                }
            }
            //Draw the diamonds
            for(int i = 0; i<diamonds.size(); i++){
                ArrayList<Point> range = diamonds.get(i).getPoints();
                Core.line(imageColor, range.get(0), range.get(1), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(1), range.get(2), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(2), range.get(3), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(3), range.get(0), new Scalar(255, 0, 0), 3);
            }
        }

        //Check if there exists duplicate detected hexagons, if there exists, just keep 1
        int sizeHex = hexagons.size();
        if(sizeHex > 0){
            //Use a boolean array to store where the hexagons is duplicated
            boolean[] duplicateHex = new boolean[sizeHex];
            for(int i = 0; i<sizeHex; i++){
                Polygon hex1 = hexagons.get(i);
                for(int j = i+1; j<sizeHex; j++){
                    Polygon hex2 = hexagons.get(j);
                    //Get the range of each hexagons
                    ArrayList<Point> range1 = hex1.getRange();
                    ArrayList<Point> range2 = hex2.getRange();
                    Log.v("Hexagon 1 ("+range1.get(0).x +" "+ range1.get(0).y+")", "("+range1.get(3).x+ " " + range1.get(3).y + ")");
                    Log.v("Hexagon 2 ("+range2.get(0).x +" "+ range2.get(0).y+")", "("+range2.get(3).x+ " " + range2.get(3).y + ")");
                    //Compare the upper-left corners and bottom-right corners of the two hexagons
                    if(checkDuplicate(range1.get(0), range2.get(0)) && checkDuplicate(range1.get(3), range2.get(3))){
                        duplicateHex[j] = true;
                    }
                }
            }
            //Delete the duplicated ones
            for(int i = sizeHex -1; i>=0; i--){
                if(duplicateHex[i] == true){
                    hexagons.remove(i);
                }
            }
            //Draw the hexagons
            for(int i = 0; i<hexagons.size(); i++){
                ArrayList<Point> range = hexagons.get(i).getPoints();
                Core.line(imageColor, range.get(0), range.get(1), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(1), range.get(2), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(2), range.get(3), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(3), range.get(4), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(4), range.get(5), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(5), range.get(0), new Scalar(255, 0, 0), 3);
            }
        }

        //Check if there exists duplicate detected custom shapes, if there exists, just keep 1
        int sizeCus = customshapes.size();
        if(sizeCus > 0){
            //Use a boolean array to store where the custom shapes is duplicated
            boolean[] duplicateCus = new boolean[sizeCus];
            for(int i = 0; i<sizeCus; i++){
                Polygon cus1 = customshapes.get(i);
                for(int j = i+1; j<sizeCus; j++){
                    Polygon cus2 = customshapes.get(j);
                    //Get the range of each custom shape
                    ArrayList<Point> range1 = cus1.getRange();
                    ArrayList<Point> range2 = cus2.getRange();
                    Log.v("CustomShape 1 ("+range1.get(0).x +" "+ range1.get(0).y+")", "("+range1.get(3).x+ " " + range1.get(3).y + ")");
                    Log.v("CustomShape 2 ("+range2.get(0).x +" "+ range2.get(0).y+")", "("+range2.get(3).x+ " " + range2.get(3).y + ")");
                    //Compare the upper-left corners and bottom-right corners of the two custom shapes
                    if(checkDuplicate(range1.get(0), range2.get(0)) && checkDuplicate(range1.get(3), range2.get(3))){
                        duplicateCus[j] = true;
                    }
                }
            }
            //Delete the duplicated ones
            for(int i = sizeCus -1; i>=0; i--){
                if(duplicateCus[i] == true){
                    customshapes.remove(i);
                }
            }
            //Draw the custom shapes
            for(int i = 0; i<customshapes.size(); i++){
                ArrayList<Point> range = customshapes.get(i).getPoints();
                Core.line(imageColor, range.get(0), range.get(1), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(1), range.get(2), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(2), range.get(3), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(3), range.get(4), new Scalar(255, 0, 0), 3);
                Core.line(imageColor, range.get(4), range.get(0), new Scalar(255, 0, 0), 3);
            }
        }

        //Add rectangles and diamond to polugons
        //All rectangles stored in the 1st list in polygons
        //All diamond stored in the 2nd list in polygons
        polygons.add(rectangles);
        polygons.add(diamonds);
        polygons.add(hexagons);
        polygons.add(customshapes);
        return polygons;
    }

    //Check if two point is duplicated by compare their x and y values
    //If p1.x <= p2.x+20 or p1.x >= p2.x -20 while p1.y <= p2.y+20 or p1.y >= p2.y - 20
    //Then we say these two points duplicate
    public boolean checkDuplicate(Point p1, Point p2){
        if(p1.x >= p2.x - 20.0 && p1.x <= p2.x + 20.0){
            if(p1.y >= p2.y - 20.0 && p1.y <= p2.y + 20.0){
                return true;
            }
        }
        return false;
    }

    public double angle( Point pt1, Point pt2, Point pt0 ) {
        //Read the x value and y value of each point, calculate the consine value of the angle
        double dx1 = pt1.x - pt0.x;
        double dy1 = pt1.y - pt0.y;
        double dx2 = pt2.x - pt0.x;
        double dy2 = pt2.y - pt0.y;
        return (dx1*dx2 + dy1*dy2)/Math.sqrt((dx1*dx1 + dy1*dy1)*(dx2*dx2 + dy2*dy2) + 1e-10);
    }

    public void generateResultImage(){
        /*
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File imageFile = new File(Environment.getExternalStorageDirectory()+File.separator+"LeCoder_Image"+File.separator
                +"FoundStuff"+timeStamp+".jpg");
        */
        File imageFile = new File(Environment.getExternalStorageDirectory()+File.separator+"LeCoder_Image"+File.separator
                +"ProcessedImage.jpg");
        Highgui.imwrite(imageFile.getAbsolutePath(), imageColor);
    }

    public ArrayList<Line> getLines(){
        return lines;
    }

    public ArrayList<Circle> getCircles(){
        return circles;
    }

    public ArrayList<ArrayList<Polygon>> getPolygons(){
        return polygons;
    }

    public int connectedLines(Shape shape){
        int connectedLines = 0;
        //Get the two corners of the shape, top-left and bottom-right
        //Set the upper and bottom y range, left anf right x range of the shape
        double leftX = shape.getRange().get(0).x - 10.0;
        double rightX = shape.getRange().get(3).x + 10.0;
        double upperY = shape.getRange().get(0).y -10.0;
        double bottomY = shape.getRange().get(3).y + 10.0;
        for(Line l : lines){
            //Get the two end points of the line
            Point start = l.getThisEnd();
            Point end = l.getThatEnd();
            //check if one end is in the range while the other end is out side the range
            if(insideRange(start, leftX, rightX, upperY, bottomY)){
                if(!insideRange(end, leftX, rightX, upperY, bottomY)) {
                    connectedLines++;
                }
            }else{
                if(insideRange(end, leftX, rightX, upperY, bottomY)){
                    connectedLines++;
                }
            }
        }
        return connectedLines;
    }

    public boolean insideRange(Point p, double x1, double x2, double y1, double y2){
        //If point is satisfy x1 <= p.x <= x2 while y1 <= p.y <= y2, then it is in the range
        if(p.x>=x1 && p.x<=x2 && p.y>=y1 && p.y<=y2){
            return true;
        }
        return false;
    }
}