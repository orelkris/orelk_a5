/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orelk;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import prog24178.labs.objects.CookieInventoryItem;
import prog24178.labs.objects.Cookies;

/*
 * Cookie InventoryFile class: Lab 5 Summer 2016
 * Kristina Orel
 */
public class CookieInventoryFile extends ArrayList<CookieInventoryItem>{
    
    /**
    * Constructs a CookieInventoryFile instance
    */
    public CookieInventoryFile(){
        
    }
    
    /**
    * Constructs a CookieInventoryFile instance
    * 
    * @param file loads file into program
    * if file does not exist no file information is loaded into program
    */
    public CookieInventoryFile(File file) throws IOException{
        loadFromFile(file);
    }
    
    public CookieInventoryItem find(int id){
        for(CookieInventoryItem c: this){
            if(c.cookie.getId() == id){
                return c;
            }
        }
        return null;
    }
    
    /**
    * Loads file into program
    * 
    * @param file the passed value to open file to load information from 
    * if the file exists the file is overwritten and if file does not exist
    * a new file is created
    */
    public void loadFromFile(File file) throws IOException{
        if(file.exists()){
            try(
                Scanner input = new Scanner(file);
            ){
                int fileLine = 0;
                while(input.hasNextLine()){
                    String readLine = input.nextLine();
                    fileLine++;
                    Scanner inputChecker = new Scanner(readLine);
                    inputChecker.useDelimiter("\\|");
                    Integer id = (inputChecker.hasNextInt()) ? 
                        inputChecker.nextInt() : null;
                    Integer quantity = (inputChecker.hasNextInt()) ? 
                        inputChecker.nextInt() : null;
                    try{
                        if(id != null && quantity != null){
                            Cookies cookie = Cookies.getCookie(id);
                            if(this.find(id) == null)
                                this.add(new CookieInventoryItem(cookie, quantity));
                            else
                                this.find(id).setQuantity(
                                    this.find(id).getQuantity() + quantity);
                        }
                        else{
                            System.out.println("Error: Incorrect data type. " 
                                + "Check file line " +  fileLine);
                        }
                        
                    }catch(IllegalArgumentException ex){
                        System.out.println("Error: " + ex.getMessage() + 
                            " Check file line " + fileLine);  
                    }
                }   
            }
            
        }else{
            throw new FileNotFoundException("There is no such file");
        }
        
    }
    
    /**
    * Writes program information onto file
    * 
    * @param file passed into program for writing information to 
    */
    public void writeToFile(File file) throws IOException{
            try(PrintWriter output = new PrintWriter(new BufferedWriter(
                new FileWriter(file)))){
                for(CookieInventoryItem c: this){
                    output.println(c.toFileString());
                }
            }
    }
}