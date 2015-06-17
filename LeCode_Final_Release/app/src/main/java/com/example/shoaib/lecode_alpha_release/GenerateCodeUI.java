package com.example.shoaib.lecode_alpha_release;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoaib.lecode_alpha_release.Shape.Circle;
import com.example.shoaib.lecode_alpha_release.Shape.Polygon;

import org.opencv.core.Point;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by shoaib on 3/13/2015.
 * Edit by shoaib, ChaoLin and Yash
 */
public class GenerateCodeUI extends ActionBarActivity {

    //Attributes about image process
    private ImageProcess ip;
    private String imageFilePath;
    private ArrayList<Node> nodes;
    private ArrayList<Circle> circles;
    private ArrayList<ArrayList<Polygon>> polygons;
    private TextView theCode;

    //private TextView theCode;
    private String Generated_code;
    private File myCppFile;//The C++ file
    private File directory;
    private File Folder; //Variable to create a new folder
    private String File_name, File_extension; //Variable to store File name and the extension for
    private int File_Count; //Variable to keep track of the file number
    private FileOutputStream fouts; //Object to open txt file
    private OutputStreamWriter fout; //Object to write data in txt file
    public GenerateCode mGenerateCode;

    public ArrayList<String> Name;
    public ArrayList<String> Prev;
    public ArrayList<String> Next;
    public ArrayList<String> True;
    public ArrayList<String> False;
    public ArrayList<String> Expression;
    public ArrayList<String> nType;

    Intent myEmailClient;




    private String temp_code = //Temporary display string for Alpha Release
            "#include <iostream>\n" +
                    "#include <stdio.h>\n\n" +
                    "int main(){\n" +
                    "\n" +
                    "int x = 2;\n" +
                    "int y = 3;\n" +
                    "int z = x + y;\n" +
                    "std::cout<<z\n" +
                    "return 0;\n\n" +
                    "}\n" ;

    public GenerateCodeUI(){
        mGenerateCode = new GenerateCode();
        Name = new ArrayList();
        Prev = new ArrayList();
        Next = new ArrayList();
        True = new ArrayList();
        False = new ArrayList();
        Expression = new ArrayList();
        nType = new ArrayList();
    }

    public void display(){
        for(int i = 0; i < Name.size(); i++){

            Log.d("Array Location " + Integer.toString(i), "Name: " + Name.get(i) + " Prev: " +
                    Prev.get(i) + " Next: " + Next.get(i) + " True: " + True.get(i) + " False: "
                    + False.get(i) + " Expression: " + Expression.get(i) + " NType: " + nType.get(i)
                    + "\n");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_code_ui);
        //Get the textView;
        theCode = (TextView) findViewById(R.id.Code);
        Log.d("Crashed 1", "starting\n");
        Initalize_File_parameters();
    }



    public void Initalize_File_parameters() { //Function to initialize file parameters
        File_name = "Generated_Code_";
        File_extension = ".cpp"; //Extension for C++ files
        Folder = new File(Environment.getExternalStorageDirectory(), "LeCode");
        Folder.mkdirs();//Creating the folder
    }

    public void setGenerated_Code(String Gen_Code){
        Generated_code = new String (Gen_Code);
        //this.Generated_Code = Generated_Code;
    }

    public void Prepare_Email(){
        myEmailClient = new Intent(Intent.ACTION_SEND); //Object to send an email
        String subject; //Subject for the email
        String Email_Content;
        Email_Content= new String("Please find attached the C++ code" +
                " for the flowchart");//Text for Email content
        //Subject for Email
        subject = "Generated Code for Flowchart from Team LeCode";

        myEmailClient.putExtra(Intent.EXTRA_SUBJECT, subject); //Adding subject to the object
        myEmailClient.setType("text/plain"); //Will be in txt
        myEmailClient.putExtra(Intent.EXTRA_TEXT, Email_Content);
        myEmailClient.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myCppFile));
        //Attaching C++ file

    }

    public void Email_the_Code(View view) { //Function to Email Code
        Prepare_Email();
        startActivity(myEmailClient); //Sending the Email

    }

    public Intent getMyEmailClient(){
        return myEmailClient;
    }

    public void WriteToFile(){ //Function to write generated code to file

        directory = Environment.getExternalStorageDirectory(); //getting the directory for
        // external storage

        try {
            myCppFile = new File(directory + "/LeCode/", File_name + File_Count + File_extension);
            myCppFile.createNewFile(); //txt file created

            fouts = new FileOutputStream(myCppFile); // object created for txt file
            fout = new OutputStreamWriter(fouts); // object for writing in the txt file

            fout.write(Generated_code); //writing generated code to file
            fout.close(); //close the txt file
            File_Count++; //Incrementing file count
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void processImagePressed(View view){
        loadImageFile(); //What should be use for final release
        //For beta release, we not use the photo taken, but a sample image
        //Because the app would crush down if you just take a random photo or a flowchart with poor brightness
        //imageFilePath = fakeImage();
        //Load the image to ImageProcess object and process the image
        //loadImageFile(imageFilePath);
        processImage();
        textDetection();
        //Generate the nodes and construct the graph
        generateNodes();
        generateGraph();
        //Print all nodes
        printAllNodes();
        //Check wrong drawings and syntax problems
        if(!checkAllNodes()) return;
        //store the data in nodes to the data structure of arraylists
        generateDataStructure();
        //Pass the graph to object GenerateCode to generate the code
        setGenerateCode();
        String Generated_Code = getGenerateCode();
        Log.v("Generated Code:", Generated_Code);
        //Write the generated code to file;
        WriteToFile();
    }

    public void loadImageFile(){
        //Load the image file path from previous UI, and load the image by this
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            Log.v("error", "no image file exists");
        }else{
            imageFilePath = extras.getString("File Path");
            Log.v("Image Path: ", imageFilePath);
            ip = new ImageProcess(imageFilePath);
        }
    }

    public void loadImageFile(String file){
        imageFilePath = file;
        ip = new ImageProcess(imageFilePath);
    }

    public ImageProcess getImageProcess(){
        return ip;
    }

    public void processImage(){
        //Detect the lines first
        //ArrayList<Line> lines = ip.detectLines();
        //Toast.makeText(getApplicationContext(), "number of lines: " + lines.size(), Toast.LENGTH_LONG).show();
        //Then detect the circles
        circles = ip.detectCircles();
        //Toast.makeText(getApplicationContext(), "number of circles: " + circles.size(), Toast.LENGTH_LONG).show();
        //Then detect the polygons
        polygons = ip.detectPolygons();
        //Toast.makeText(getApplicationContext(), "number of rectangles" + polygons.get(0).size(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "number of diamonds"+ polygons.get(1).size(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "number of hexagons" + polygons.get(2).size(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getApplicationContext(), "number of custom shapes" + polygons.get(3).size(), Toast.LENGTH_LONG).show();
        //Write all result to a new image file
        ip.generateResultImage();
    }

    public void generateNodes(){
        //For each existing shape in our image, create a node for it
        nodes = new ArrayList<Node>();
        int name = 0;
        for(int i = 0; i < circles.size(); i++){
            nodes.add(new Node(circles.get(i), "C"+name++));
        }
        for(int i = 0; i < polygons.size(); i++){
            for(int j = 0; j < polygons.get(i).size(); j++){
                Point p1 = polygons.get(i).get(j).getRange().get(0);
                Point p2 = polygons.get(i).get(j).getRange().get(3);
                //It may detect some letter as shape, so eliminate these wrong shape
                if((p2.x - p1.x)*(p2.y - p1.y) >= 800){
                    nodes.add(new Node(polygons.get(i).get(j), "N"+name++));
                }
            }
        }

        //Sort the nodes by its center coordinate
        Collections.sort(nodes, new Comparator<Node>() {

            @Override
            public int compare(Node n1, Node n2) {
                Point p1 = n1.getCenter();
                Point p2 = n2.getCenter();
                if (p1.y < (p2.y - 30.0)) {
                    return -1;
                } else if (p1.y <= (p2.y + 30.0) && p1.y >= (p2.y - 30.0)) {
                    if (p1.x < p2.x - 10.0) {
                        return -1;
                    } else {
                        return 1;
                    }
                } else {
                    return 1;
                }
            }
        });
    }

    public void generateGraph(){
        //From the start circle, construct a graph for the flowchart
        int size = nodes.size();
        //Set the first circle to Start and last circle to End;
        if(size > 1){
            if(nodes.get(0).getType().equals("start")){
                nodes.get(0).setName("start");
            }else{
                Log.v("Error: ", " Start not circle");
            }
            for(int i = 0; i<size; i++){
                if(i == 0){
                    continue;
                }
                Log.v("Size:" + size, "i="+i);
                if(nodes.get(i).getType().equals("start")){
                    nodes.get(i).setName("end");
                    nodes.get(i).setType("end");
                }
            }
        }


        //Add nodes to tree level by level
        ArrayList<ArrayList<Node>> tree = new ArrayList<ArrayList<Node>>();
        ArrayList<Node> level = new ArrayList<Node>();
        Node prev = null;
        for(int i = 0; i < nodes.size(); i++){
            Node curr = nodes.get(i);
            if(i == 0){
                level.add(curr);
            }else{
                //If current node in the same level as previous node, then add current node to current level
                if(ifSameLevel(prev, curr)){
                    level.add(curr);
                }
                //If current node not in the same level as previous node, then add current level to tree and create a new level
                else{
                    tree.add(level);
                    level = new ArrayList<Node>();
                    level.add(curr);
                }
            }
            prev = curr;
        }
        tree.add(level);

        for(int i = 0; i<tree.size()-1; i++){
            for(int j = 0; j<tree.get(i).size(); j++){
                Node curr = tree.get(i).get(j);
                //If it is an circle, set the one right below circle to next
                //And set circle to be the below one's prev
                if(curr.getType().equals("start")){
                    for(int k = 0; k<tree.get(i+1).size(); k++){
                        Node next = tree.get(i+1).get(k);
                        if(next.getCenter().x <= curr.getCenter().x+50.0 && next.getCenter().x >= curr.getCenter().x - 50.0){
                            curr.setNext(next);
                            next.setPrev(curr);
                        }
                    }
                }
                //If it is a rectangle, set the one right below to next
                //And set rectangle to be the below one's prev
                else if(curr.getType().equals("code")){
                    for(int k = 0; k<tree.get(i+1).size(); k++){
                        Node next = tree.get(i+1).get(k);
                        if(next.getCenter().x <= curr.getCenter().x+50.0 && next.getCenter().x >= curr.getCenter().x - 50.0){
                            curr.setNext(next);
                            next.setPrev(curr);
                        }
                    }
                }
                //If it is a diamond, set the one right below to next
                //Set the one left to the below one to true
                //Set the one right to the below one to false
                //Set the diamond to the the prev of the three
                else if(curr.getType().equals("If")){
                    for(int k = 0; k<tree.get(i+1).size(); k++){
                        Node next = tree.get(i+1).get(k);
                        if(next.getCenter().x <= curr.getCenter().x+50.0 && next.getCenter().x >= curr.getCenter().x - 50.0){
                            if(k > 0){
                                Node ntrue = tree.get(i+1).get(k-1);
                                curr.setTrue(ntrue);
                                ntrue.setPrev(curr);
                            }
                            curr.setNext(next);
                            next.setPrev(curr);
                            if(k < tree.get(i+1).size() -1){
                                Node nfalse = tree.get(i+1).get(k+1);
                                curr.setFalse(nfalse);
                                nfalse.setPrev(curr);
                            }

                        }
                    }
                }


                //If it is a while, set the one right below to next
                //Set the one left to the below one to true
                //Set the diamond to the the prev of the three
                else if(curr.getType().equals("While")){
                    for(int k = 0; k<tree.get(i+1).size(); k++){
                        Node next = tree.get(i+1).get(k);
                        if(next.getCenter().x <= curr.getCenter().x+50.0 && next.getCenter().x >= curr.getCenter().x - 50.0){
                            if(k > 0){
                                Node ntrue = tree.get(i+1).get(k-1);
                                curr.setTrue(ntrue);
                                ntrue.setPrev(curr);
                            }
                            curr.setNext(next);
                            next.setPrev(curr);
                        }
                    }
                }
            }
        }
    }

    public void printAllNodes(){
        //A method used while test generateNodes() and generateGraph()
        for(int i = 0; i < nodes.size(); i++){
            Node curr =nodes.get(i);
            Log.v("Node" + curr.getCenter().x + " " + curr.getCenter().y,
                    "Name: "+ curr.getName() + " Type: "+curr.getType() + " Expression: " + curr.getExpression()
                            +"Prev: "+ (curr.hasPrev()? curr.getPrev().getName() : "Null")
                            +"Next: "+ (curr.hasNext()? curr.getNext().getName() : "Null")
                            +"True: "+ (curr.hasTrue()? curr.getTrue().getName() : "Null")
                            +"False: "+ (curr.hasFalse()? curr.getFalse().getName() : "Null")
            );

            //+ "Next: "+curr.getNext() == null? "Null" : curr.getNext().getName());
        }
    }

    //Decide if two nodes at the same level
    public boolean ifSameLevel(Node n1, Node n2){
        if(n1.getCenter().y <=  n2. getCenter().y + 50.0 && n1.getCenter().y >= n2.getCenter().y - 50.0){
            return true;
        }
        return false;
    }

    public void generateDataStructure(){
       /*Name = new ArrayList<String>();
        Expression = new ArrayList<String>();
        nType = new ArrayList<String>();
        Prev = new ArrayList<String>();
        Next = new ArrayList<String>();
        True = new ArrayList<String>();
        False = new ArrayList<String>();*/
        //Add each attributes to its according array lists
        for(int i = 0; i<nodes.size(); i++){
            Node curr = nodes.get(i);
            Name.add(curr.getName());
            Expression.add(curr.getExpression());
            nType.add(curr.getType());
            Prev.add((curr.hasPrev()? curr.getPrev().getName() : "Null"));
            Next.add((curr.hasNext()? curr.getNext().getName() : "Null"));
            True.add((curr.hasTrue()? curr.getTrue().getName() : "Null"));
            False.add((curr.hasFalse()? curr.getFalse().getName() : "Null"));
        }
    }

    public void setGenerateCode(){
        //Give the arraylist which store the data to GenerateCode object, and Generate the code
        mGenerateCode.setDataStructure(Name, Prev, Next, True, False, Expression, nType);
        Generated_code = mGenerateCode.generate_code();
        theCode.setText(Generated_code);
    }

    public String getGenerateCode(){
        return Generated_code;
    }

    public String fakeImage(){
        String extStorageDir = Environment.getExternalStorageDirectory().toString();
        try{
            //Store test_4_1_2.png to external storage, let it be the fake image file
            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.full_program_2);
            File file = new File(extStorageDir+"/LeCoder_Image", "while_node.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        }catch(Exception e){
            Log.v("Error", "Can not save files on ex");
        }
        String imagePath = Environment.getExternalStorageDirectory()+"/LeCoder_Image/while_node.jpg";
        return imagePath;
    }
    public void textDetection() {


        ArrayList<Polygon> rectangles = ip.getPolygons().get(0);//Get the list of rectangles in the image
        ArrayList<Polygon> diamonds = ip.getPolygons().get(1);//Get the list of diamonds in the image
        ArrayList<Polygon> hexagons = ip.getPolygons().get(2);//get the list of hexagon in the image
        for(int i = 0; i<rectangles.size(); i++){
            Point p1 = rectangles.get(i).getRange().get(0); //Get the upper-left point
            Point p2 = rectangles.get(i).getRange().get(3);  //Get the bottom-right point
            String s = p1.x + "," + p1.y + "," + p2.x + "," + p2.y;
            Log.v("height + width ", s);
              charRecognition2 cr = new charRecognition2();
            String content = cr.OcrConvert(p1.x,p1.y,p2.x,p2.y,1,imageFilePath);//Call OcrConvert for every rectangle in the Array list and pass window co-ordinates
            Log.v("Content is ", content);
            ip.getPolygons().get(0).get(i).writeContent(content);//Store the received string to the arraylist
        }
        for(int i = 0; i<diamonds.size(); i++){
            Point p1 = diamonds.get(i).getRange().get(0); //Get the upper-left point
            Point p2 = diamonds.get(i).getRange().get(3); //Get the bottom-right point
            String s = p1.x + "," + p1.y + "," + p2.x + "," + p2.y;
            charRecognition2 cr = new charRecognition2();
            Log.v("Point is", s);
            String content = cr.OcrConvert(p1.x,p1.y,p2.x,p2.y,2,imageFilePath);//Call OcrConvert for every diamond in the Array list and pass window co-ordinates
            Log.v("Content is is ", content);
            ip.getPolygons().get(1).get(i).writeContent(content); //Store the received string to the arraylist

        }
        for(int i = 0; i<hexagons.size(); i++){
            Point p1 = hexagons.get(i).getRange().get(0); //Get the upper-left point
            Point p2 = hexagons.get(i).getRange().get(3); //Get the bottom-right point
            String s = p1.x + "," + p1.y + "," + p2.x + "," + p2.y;
            charRecognition2 cr = new charRecognition2();
            Log.v("Point is", s);
            String content = cr.OcrConvert(p1.x,p1.y,p2.x,p2.y,3,imageFilePath);//Call OcrConvert for every hexagon in the Array list and pass window co-ordinates
            Log.v("Content is is ", content);
            ip.getPolygons().get(2).get(i).writeContent(content); //Store the received string to the arraylist

        }
    }

    //Check if there is wrong drawings or syntax errors
    public boolean checkAllNodes(){
        //A method used while test generateNodes() and generateGraph()
        for(int i = 0; i < nodes.size(); i++){
            Node curr =nodes.get(i);
            //The first node shoule always be starting point
            if(i == 0 && !curr.getType().equals("start")){
                Toast.makeText(getApplicationContext(), "Please put your starting point at top", Toast.LENGTH_LONG).show();
                return false;
            }
            //Check the start point, which should just have next
            if(curr.getType().equals("start")){
                if(curr.hasPrev()){
                    Toast.makeText(getApplicationContext(), "There should be no shapes above starting point", Toast.LENGTH_LONG).show();
                    return false;
                }
                if(!curr.hasNext()){
                    Toast.makeText(getApplicationContext(), "There should be something below starting point", Toast.LENGTH_LONG).show();
                    return false;
                }

            }
            //Check the end point, which should just have prev
            else if(curr.getType().equals("end")){
                if(!curr.hasPrev()){
                    Toast.makeText(getApplicationContext(), "There should be something above end point", Toast.LENGTH_LONG).show();
                    return false;
                }
                if(curr.hasNext()){
                    Toast.makeText(getApplicationContext(), "There should be no shape below end point", Toast.LENGTH_LONG).show();
                    return false;
                }

            }
            //Check the code node (rectangle), which should at least have prev
            else if(curr.getType().equals("code")){
                if(!curr.hasPrev()){
                    Toast.makeText(getApplicationContext(), "There should be something above your code statement (rectangle)", Toast.LENGTH_LONG).show();
                    return false;
                }

            }
            //Check the if node (diamond), which should have prev, next, true, false
            else if(curr.getType().equals("If")){
                if(!curr.hasPrev()){
                    Toast.makeText(getApplicationContext(), "There should be something above your if shape (diamond)", Toast.LENGTH_LONG).show();
                    return false;
                }
                if(!curr.hasNext()){
                    Toast.makeText(getApplicationContext(), "There should be something below your if shape (diamond)", Toast.LENGTH_LONG).show();
                    return false;
                }
                if(!curr.hasTrue()){
                    Toast.makeText(getApplicationContext(), "There should be something left below to your if shape (diamond)", Toast.LENGTH_LONG).show();
                    return false;
                }
                if(!curr.hasFalse()){
                    Toast.makeText(getApplicationContext(), "There should be something right below to your if shape (diamond)", Toast.LENGTH_LONG).show();
                    return false;
                }

            }
            //Check the while node (hexagon), which should have prev, next, true
            else if(curr.getType().equals("While")){
                if(!curr.hasPrev()){
                    Toast.makeText(getApplicationContext(), "There should be something above your while shape (hexagon)", Toast.LENGTH_LONG).show();
                    return false;
                }
                if(!curr.hasNext()){
                    Toast.makeText(getApplicationContext(), "There should be something below your while shape (hexagon)", Toast.LENGTH_LONG).show();
                    return false;
                }
                if(!curr.hasTrue()){
                    Toast.makeText(getApplicationContext(), "There should be something left below to your while shape (hexagon)", Toast.LENGTH_LONG).show();
                    return false;
                }
            }
        }
        return true;
    }

    public void captureAgainPressed(View view){
        Intent intent = new Intent(GenerateCodeUI.this, StartScreenUI.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}