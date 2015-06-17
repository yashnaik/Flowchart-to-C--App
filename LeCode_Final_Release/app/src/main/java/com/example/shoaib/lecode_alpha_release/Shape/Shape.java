package com.example.shoaib.lecode_alpha_release.Shape;

import org.opencv.core.Point;

import java.util.ArrayList;

/**
 * Created by ChaoLin on 3/10/2015.
 */
public abstract class Shape {
    private String content = "";
    public abstract ArrayList<Point> getRange();
    public abstract Point getCenter();
    public abstract String getType();
    public void writeContent(String content){
        this.content = content;
    }
    public String getContent(){ return content; }
}

