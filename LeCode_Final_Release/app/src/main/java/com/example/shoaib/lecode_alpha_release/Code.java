package com.example.shoaib.lecode_alpha_release;

/**
 * Created by shoaib on 3/12/2015.
 */
public class Code {
    //Base class to represent code objects
    private Code prev; //Pointer to object from which this code gets instantiated
    private Code next; //Pointer to object which has to exectue after this code object
    private String name; //Name of the Code object
    private String expression; //Expression that the Code object holds

    public Code getPrev(){
        return prev;
    }

    public Code getNext(){
        return next;
    }

    public void setNext(Code next){
        this.next = next;
    }

}
