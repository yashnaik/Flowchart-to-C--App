package com.example.shoaib.lecode_alpha_release;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by shoaib on 3/12/2015.
 */
public class GenerateCode {   //Class that will generate code

    File myFile; //File to store Code

    public ArrayList<String> Name;      //Array list to store the names of the nodes
    public ArrayList<String> Prev;      //Array list to store the name of the previous node
    public ArrayList<String> Next;      //Array list to store the name of the next node
    public ArrayList<String> True;      //Array list to store the name of the node for True condition
    public ArrayList<String> False;     //Array list to store the name of the node for False condition
    public ArrayList<String> Expression;//Array list to store the expression contained within the node
    public ArrayList<String> nType;     //Array list to store the type of the node
    public ArrayList<Integer> Processed;//Array list to see if the node was processed
    public ArrayList<String> Variables; //Array list to store the names of the variables in the program

    public String Generated_Code; //String to store the Generated code


    private int indent; //Variable to keep track of tab indents

    public GenerateCode(){ //Constructor
        Generated_Code = new String();
        indent = 0; //Indent should be zero in the beginning
    }

    public void setDataStructure(ArrayList<String> Name,  ArrayList<String> Prev,
                                 ArrayList<String> Next,  ArrayList<String> True,
                                 ArrayList<String> False, ArrayList<String> Expression,
                                 ArrayList<String> nType){ //Setter function for setting the Data structure for the flowchart

        this.Name = Name;                 //Setting Name for the node
        this.Prev = Prev;                 //Setting Prev node
        this.Next = Next;                 //Setting Next node
        this.True = True;                 //Setting True node
        this.False = False;               //Setting False node
        this.Expression = Expression;     //Setting Expression for the node
        this.nType = nType;               //Setting the type of the node
        Processed = new ArrayList<Integer>(); //Creating the Array list
        Variables = new ArrayList<String>();  //Creating the Array list
        for(int i = 0; i < Name.size(); i++){ //Initalizing Pocessed value to zero for all the nodes i.e. they are not processed
            Processed.add(0);
        }
    }

    public int checkIfExists(ArrayList<String> Source, String check_Var){ //Function to check if a Variable exists in a given Array list
        for(int i = 0; i < Source.size(); i++){
            if(Source.get(i).equals(check_Var)){ //Function returns 1 if variable is found
                return 1;
            }
        }
        return -1; //Function returns -1 if variable is not found
    }
    public void populateVariables(){ //Function to detect variables in a given flowchart so that they can be later declared in the program

        for(int i = 0; i < Expression.size(); i++){ //Go through each expression contained in the flowchart to detect variables

            String tokens[] = Expression.get(i).split("[=> ]+"); //Split expression string based on spaces to obtain variables and store all those in tokens array

            Log.d("Expression iteration " + Integer.toString(i), "So the number of tokens is " + Integer.toString(tokens.length));

            for(int k = 0; k < tokens.length; k++) {//run loop for each token
                Log.d("K is ", Integer.toString(k));
                Log.d("Token is ", tokens[k]+ "END");

                if (!tokens[k].equals("NULL") && tokens[k].matches("[a-zA-Z]+")){ //if the token is an alphabet and is also not null
                    Log.d("We are", " in");
                    String token_String = tokens[k];
                    if (Variables.size() > 0) { //if Variables array list is not empty, check if the encounted variable already exists in the variable list or not
                        int exists = checkIfExists(Variables, token_String);
                        if(exists == -1){//If variable does not exist in the Array list, then add it to the list
                            Variables.add(token_String);
                        }

                    }
                    else {//Variables array list is empty
                        Log.d("Again and Again", "\n");
                        Variables.add(token_String); //Add the variable to the array list
                    }
                }
            }
        }
        Log.d("We exited populate\n", " \n");
    }

    public String generate_code(){ //Function to generate code
        //String Holla = new String("Variable Holla");
        //return Holla;
        int loc_start = getlocation(Name, "start"); //get location of the start node
        display();
        populateVariables(); //Create and fill the Variables Array List
        GeneratetheCode(loc_start, "None"); //Generate the code starting from start location
        return Generated_Code;
        //return "okay the code is generated\n#include \"iostream.h\"\nreturn 0;";


    }

    public void display(){ //Function to display the Array lists for all the nodes for debugging purposes
        for(int i = 0; i < Name.size(); i++){ //Run for the number of nodes

            Log.d("Array Location " + Integer.toString(i), "Name: " + Name.get(i) + " Prev: " +
                    Prev.get(i) + " Next: " + Next.get(i) + " True: " + True.get(i) + " False: "
                    + False.get(i) + " Expression: " + Expression.get(i) + " NType: " + nType.get(i)
                    + "\n");
        }
    }

    public int getlocation(ArrayList<String> names, String name){ //Function to get the location of a variable in the array list
        for(int i = 0; i < names.size(); i++){//Run the loop for the size of array list
            if(names.get(i).equals(name)){//If variable is found
                return i;//Return position
            }
        }

        return -1; //Return negative value, if the element was not found.
    }

    public void print(String print_text){//Function to print/append any String to the Generated code string while maintaining proper indentation
        for(int i = 0; i < indent; i++){//Run the loop for the value of indent
            Generated_Code = Generated_Code + "\t";//Add those numbers of tabs as many as indents are there.
        }
        Generated_Code = Generated_Code + print_text; //Finally append the text to the generated code string
    }

    public void Generate_Code_for_Type(int loc, String branch){ //Function to generate code depending on the type of the node
        if(nType.get(loc).equals("start")){ //If the type of the node is start
            print("//This Code is generated by LeCode App\n"); //Initial comments of the code generated by the app
            print("#include \"iostream\" \n"); //Mandatory include files being added to the generated code
            print("using namespace std;\n");
            print("int main() {\n\n"); //Main function for the generated code is being printed
            indent++;//increase indent as main function has now begun
            for(int i = 0; i < Variables.size(); i++){ //Print all  the variables in the beginning of  the generated code
                print("int " + Variables.get(i) + ";\n");
            }
            print("\n"); //New line
        }
        else if(nType.get(loc).equals("end")){ //If the type of the node is end

            print("return 0;\n"); //print return 0;
            indent--; //Reduce indent as the generated code is exiting fom the main function
            print("}\n"); //Ending braces of the program
        }
        else if(nType.get(loc).equals("If") && branch.equals("True")){//If the type of the node is if and the true part of the code is required

            String print_string = "if ( " + Expression.get(loc) + " ) {\n";//if statement with the expression
            print(print_string);
            indent++;//increase indent as if loop braces have started
        }
        else if(nType.get(loc).equals("If") && branch.equals("False")){//If the type of the node is if and the false part of the code is required

            indent--; //Reduce indent as the true part of the if is over
            print("}\n");  //Braces to end the true part
            print("else {\n"); //Print else part for the generated code
            indent++; //Increase indent as false part has started
        }
        else if(nType.get(loc).equals("While")){//If the type of the node is while and the true part of the code is required

            print("while ( " + Expression.get(loc) + " ) {\n"); //while statement with the expression
            indent++;//Increase indent as true part execution has started
        }
        else if(nType.get(loc).equals("code")){ //If its a code structure

            print(Expression.get(loc) + ";\n"); //Simply print it.

        }
    }

    public void GeneratetheCode(int loc, String branch) { //Function to generate the code from a particular starting node
        // cout<<"GenerateCode called for "<<n[loc].name<<endl;
        if (Processed.get(loc) == 1) { //If the node is already processed, do not generate code for it again.
            return;
        } else if (nType.get(loc).equals("start") || nType.get(loc).equals("end") ||
                nType.get(loc).equals("code")) { //If node type is: start, enf or code
            Processed.set(loc, 1); //Set that node to as processed
            Generate_Code_for_Type(loc, "None"); //Generate code for that node with the option none as these nodes do not have a "true" or "false" associated with them
            int next_loc = getlocation(Name, Next.get(loc)); //get next location for  generating the code based on the next node which is connected to the current node
            //cout<<"So next_loc is "<<next_loc<<endl;
            if (next_loc != -1) { //If next node does exist
                GeneratetheCode(next_loc, "None"); //Generate the code for next node
            } else {
                return;//return if no next node exist
            }
        } else if (nType.get(loc).equals("If")) { //if node is if

            Processed.set(loc, 1); //set node as processed
            //cout<<"So we did come in If\n";
            Generate_Code_for_Type(loc, "True"); //generate the code for If but first for the true part

            int next_loc = getlocation(Name, True.get(loc)); //get the node connected to the true part of the if
            //cout<<"IF's next_loc is "<<next_loc<<endl;
            if (next_loc != -1) { //If true part of the if exists
                GeneratetheCode(next_loc, "None"); //Generate the code for the true part
            } else {
                return; //return if it does not have a true part
            }

            if (False.get(loc).equals("NULL") != true) { //if false part exists for that node

                Generate_Code_for_Type(loc, "False"); //Generate the code for the false part

                next_loc = getlocation(Name, False.get(loc)); //get the next node connected to the false part of id
                if (next_loc != -1) { //If a node exists connected to the false part of if
                    GeneratetheCode(next_loc, "None"); //Generate the code for it
                } else {
                    return; //Return if no node exists which is connected to the false part of if
                }
                indent--; //Reduce indent as false part of if is over

                print("}\n\n"); //Place braces as false part of if is over

            }

            next_loc = getlocation(Name, Next.get(loc)); //Get location for the next node
            GeneratetheCode(next_loc, "None"); //Generate the code for next node
        } else if (nType.get(loc).equals("While")) { //If node type is while

            Processed.set(loc, 1); //Set the node to as processed
            Generate_Code_for_Type(loc, "True"); //Generate the code for the true part of while

            int next_loc = getlocation(Name, True.get(loc)); //get the node location which is connected to the true part of while

            if (next_loc != -1) { //If next node exists, generate the code for it
                GeneratetheCode(next_loc, "None");
            } else {
                return; //If no node exists,  return
            }

            indent--; //Reduce indent as whole loop is over
            print("}\n\n");//Place braces as while loop is over

            next_loc = getlocation(Name, Next.get(loc)); //get location for the next node

            if (next_loc != -1) {  //If next node exists, generate the code for it
                GeneratetheCode(next_loc, "None");
            } else {
                return; //Return, if no next node exists.
            }
        }

    }
}

