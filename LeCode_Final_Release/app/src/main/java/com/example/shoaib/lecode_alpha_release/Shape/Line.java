package com.example.shoaib.lecode_alpha_release.Shape;

import org.opencv.core.Point;

/**
 * Created by ChaoLin on 3/10/2015.
 */
public class Line {
    private Point thisEnd;
    private Point thatEnd;

    public Line(Point thisEnd, Point thatEnd){
        this.thisEnd = thisEnd;
        this.thatEnd = thatEnd;
    }

    public Point getThisEnd(){
        return thisEnd;
    }

    public Point getThatEnd(){
        return thatEnd;
    }

    public Point getAnotherEnd(Point end){
        if(end == thisEnd){
            return thatEnd;
        }else{
            return thisEnd;
        }
    }
}
