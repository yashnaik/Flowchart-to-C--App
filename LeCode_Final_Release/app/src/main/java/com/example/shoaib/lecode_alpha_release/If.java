package com.example.shoaib.lecode_alpha_release;

/**
 * Created by shoaib on 3/12/2015.
 */
public class If extends Code { //Class for "if" objects

    Code True;  //Pointer to True part of if statement
    Code False; //Pointer to False part of if statement

    public Code getTrue(){
        return True;
    }

    public void setTrue(Code True){
        this.True = True;
    }

    public Code getFalse(){
        return False;
    }

    public void setFalse(Code False){
        this.False = False;
    }

}
