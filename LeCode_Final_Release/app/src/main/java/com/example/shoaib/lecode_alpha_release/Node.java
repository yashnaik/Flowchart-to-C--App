package com.example.shoaib.lecode_alpha_release;

import com.example.shoaib.lecode_alpha_release.Shape.Shape;

import org.opencv.core.Point;

/**
 * Created by ChaoLin on 4/16/2015.
 */
public class Node {
    private Point center;
    private String name;
    private String type;
    private String expression;
    private Node prev;
    private Node next;
    private Node ntrue;
    private Node nfalse;

    public Node(Shape shape, String name){
        this.center = shape.getCenter();
        this.name = name;
        if(shape.getType().equals("circle")){
            this.setType("start");
        }else if (shape.getType().equals("rectangle")){
            this.setType("code");
        }else if (shape.getType().equals("diamond")){
            this.setType("If");
        }else if(shape.getType().equals("hexagon")){
            this.setType("While");
        }else{
            this.setType("Nothing");
        }
        this.expression =  shape.getContent();
        prev = null;
        next = null;
        ntrue = null;
        nfalse = null;
    }

    //Getter of center
    public Point getCenter(){
        return center;
    }

    //Setter and getter of name
    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    //Setter and Getter of Type
    public void setType(String type){this.type = type; }

    public String getType(){
        return type;
    }

    //Getter of expression
    public String getExpression(){
        return expression;
    }

    //Setter and getter of prev
    public void setPrev(Node n){
        prev = n;
    }

    public Node getPrev(){
        return prev;
    }

    public boolean hasPrev(){return prev != null;}

    //Setter and getter of next
    public void setNext(Node n){
        next = n;
    }

    public Node getNext(){
        return next;
    }

    public boolean hasNext(){return next != null;}

    //Setter and getter of ntrue
    public void setTrue(Node n){
        ntrue = n;
    }

    public Node getTrue(){
        return ntrue;
    }

    public boolean hasTrue(){return ntrue != null;}

    //Setter and getter of nfalse
    public void setFalse(Node n){
        nfalse = n;
    }

    public Node getFalse(){
        return nfalse;
    }

    public boolean hasFalse(){return nfalse != null;}
}
