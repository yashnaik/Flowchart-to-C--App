package com.example.shoaib.lecode_alpha_release.Shape;

import org.opencv.core.Point;

import java.util.ArrayList;

/**
 * Created by ChaoLin on 3/10/2015.
 */
public class Diamond extends Polygon {
    private ArrayList<Point> points;
    private ArrayList<Point> range;
    private Point center;

    public Diamond(ArrayList<Point> points){
        this.points = points;
        //Set the range
        Point point = points.get(0);
        double minX=point.x, minY=point.y, maxX=point.x, maxY=point.y;
        for(Point p: points){
            minX = Math.min(p.x, minX);
            maxX = Math.max(p.x, maxX);
            minY = Math.min(p.y, minY);
            maxY = Math.max(p.y, maxY);
        }
        Point upperLeft = new Point(minX, minY);
        Point upperRight = new Point(maxX, minY);
        Point bottomLeft = new Point(minX, maxY);
        Point bottomRight = new Point(maxX, maxY);
        range = new ArrayList<>();
        range.add(upperLeft);
        range.add(upperRight);
        range.add(bottomLeft);
        range.add(bottomRight);
        //Set the center
        double centerX = upperLeft.x + 0.5*(bottomRight.x - upperLeft.x);
        double centerY = upperLeft.y + 0.5*(bottomRight.y - upperLeft.y);
        center = new Point(centerX, centerY);
    }

    @Override
    public ArrayList<Point> getRange() { return range; }

    @Override
    public String getType() {
        return "diamond";
    }

    @Override
    public ArrayList<Point> getPoints() {
        return points;
    }

    @Override
    public Point getCenter() {
        return center;
    }
}