package com.example.shoaib.lecode_alpha_release.Shape;

import org.opencv.core.Point;

import java.util.ArrayList;

/**
 * Created by ChaoLin on 3/10/2015.
 */
public class Circle extends Shape {
    private double radius;
    private Point center;
    private ArrayList<Point> range;

    public Circle(double radius, Point center){
        this.radius = radius;
        this.center = center;
        Point upperLeft = new Point(center.x-radius, center.y-radius);
        Point upperRight = new Point(center.x+radius, center.y-radius);
        Point bottomLeft = new Point(center.x-radius, center.y+radius);
        Point bottomRight = new Point(center.x+radius, center.y+radius);
        range = new ArrayList<>();
        range.add(upperLeft);
        range.add(upperRight);
        range.add(bottomLeft);
        range.add(bottomRight);
    }

    public double getRadius(){
        return radius;
    }

    public Point getCenter(){
        return center;
    }

    @Override
    public ArrayList<Point> getRange() { return range; }

    @Override
    public String getType() {
        return "circle";
    }
}