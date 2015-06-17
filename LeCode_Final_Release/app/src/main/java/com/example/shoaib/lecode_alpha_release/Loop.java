package com.example.shoaib.lecode_alpha_release;

/**
 * Created by shoaib on 3/12/2015.
 */
public class Loop extends Code {  //Class for "loop" objects
    private Code True; //Pointer to True part of the loop

    public Code getTrue(){
        return True;
    }

    public void setTrue(Code True){
        this.True = True;
    }
}
