/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orelk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import prog24178.labs.objects.CookieInventoryItem;
import prog24178.labs.objects.Cookies;

/**
 * Assign 5 class
 *
 * @author kriss
 */

public class Assign5 extends Application {
    
    //creates an alert object that is used to warn the user of various
    //improper inputs
    Alert alert = new Alert(Alert.AlertType.WARNING);
    
    //creates an ArrayList cookieArray
    ArrayList<String> cookieArray = new ArrayList<>();
    
    //creates an Observable list to wrap around cookieArray array
    ObservableList<String> cookieInventory;
    
    //creates an ArrayList cookieIdArray to keep track of cookie IDs
    ArrayList<Integer> cookieIdArray = new ArrayList<>();
    
    //instantiates the CookieInventoryFile 
    CookieInventoryFile inventory = new CookieInventoryFile();
    
    Label selectCookieLabel = new Label("Select Cookie:");
    ComboBox<String> selectCookie = new ComboBox<>();
    
    Label qtySoldLabel = new Label("Enter Quantity Sold:");
    TextField qtySoldTextField = new TextField();
    Button qtySoldButton = new Button("_Sell");
    
    Label qtyBakedLabel = new Label("Enter Quantity Baked:");
    TextField qtyBakedTextField = new TextField();
    Button qtyBakedButton = new Button("_Add");
    
    Button exitButton = new Button("E_xit");
    
    @Override
    public void start(Stage primaryStage) {    
        
        //Load file "cookies.txt" into program, or if it doesn't exist
        //throw an exception. Program will work but the initial inventory
        //will remain empty
        try{
            File cookieFile = new File("cookies.txt");
            inventory.loadFromFile(cookieFile);
        }catch(Exception ex){
            if(ex instanceof FileNotFoundException){
                System.out.println("Error: cannot find file");
            }
            else if(ex instanceof IOException){
                ex.printStackTrace();
            }
        }
        
        //keep track of names and Id's in cookieArray and cookieIdArray
        for(Cookies t: Cookies.values()){
            cookieArray.add(t.getName());
            cookieIdArray.add(t.getId());
        }
        
        //Places all the cookie names available in inventory into a drop-down
        //menu format
        cookieInventory = (FXCollections.observableArrayList(cookieArray));
        selectCookie.getItems().addAll(cookieInventory);
        selectCookie.setValue(cookieArray.get(0));
        
        qtySoldButton.setOnAction(e->{
            
            //checks if the user entered a correct value into text field for
            //cookie to sell
            if(!isValidEntry(qtySoldTextField.getText())){
                
            }
            
            //if the user entered a correct input into the text field, check to
            //see if the inventory has enough stock for that particular cookie
            //if not, tell the user how much the inventory actually has. If
            //there is enough, sell that amount and update the inventory. If
            //all the cookies are sold out, remove that cookie object from 
            //inventory
            else{
                int amountToSell = Integer.parseInt(qtySoldTextField.getText());
                int cookie = cookieSelected();
                int amountInventoryContains = 0;
                String cookieName = selectCookie.getValue();
                if(inventory.find(cookie) != null){
                    amountInventoryContains = inventory.find(cookie).getQuantity();
                    if(amountInventoryContains > amountToSell){
                        inventory.find(cookie).setQuantity
                            (amountInventoryContains - amountToSell);
                        qtySoldTextField.clear();
                    }
                    else if(amountInventoryContains == amountToSell){
                        inventory.remove(
                            inventory.indexOf(inventory.find(cookie)));
                        qtySoldTextField.clear();
                    }
                    else{
                        
                        String title = "Not Enough Cookies In Inventory";
                        
                        String message = "The store only has " + 
                            amountInventoryContains + " " + cookieName + " cookies";

                        alertCreator(title, message);
                    }
                    
                }else{
                    String message = "Sorry, there are no " + selectCookie.getValue() +
                        " cookies available to sell";
                    alertCreator("Insufficient Inventory", message);
                }
            }
        });
        
        qtyBakedButton.setOnAction(e->{
            
            //checks to see if user entered the right type of input into the
            //add baked text field
            if(!isValidEntry(qtyBakedTextField.getText())){
                
            }
            
            //if the user input is correct, add the amount of cookies the user
            //has input into the inventory. If there are no cookies of that type
            //yet, create a new CookieInventoryItem and add it to the inventory
            //array. If there are cookies already in the inventory, add the 
            //baked quantity to the current quantity
            else{
                int amountToAdd = Integer.parseInt(qtyBakedTextField.getText());
                int cookie = cookieSelected();
                int amountInventoryContains = 0;
                
                if(inventory.find(cookie) != null){
                    amountInventoryContains = inventory.find(cookie).getQuantity();
                    inventory.find(cookie).setQuantity(
                        amountInventoryContains + amountToAdd);
                    qtyBakedTextField.clear();
                }else{
                    inventory.add(new CookieInventoryItem(cookie, amountToAdd));
                    qtyBakedTextField.clear();
                }
                
            }
        });
        
        //if the user presses the exit button, save all of the inventory
        //information onto a file given here. If the file doesn't exist
        //create a new file. If the file does exist, overwrite the information
        exitButton.setOnAction(e -> {
            try{
                File file = new File("cookies.txt");
                inventory.writeToFile(file);
                System.exit(0);
            }catch(IOException ex){
                ex.printStackTrace();
            }
        });
        
        HBox paneForList = new HBox(10);
        paneForList.getChildren().addAll(selectCookieLabel, selectCookie);
        
        VBox paneForQtySold = new VBox();
        paneForQtySold.getChildren().addAll(qtySoldLabel, qtySoldTextField, qtySoldButton);
        qtySoldButton.setMaxWidth(Double.MAX_VALUE);
        paneForQtySold.setId("add-sell");
        
        VBox paneForQtyBaked = new VBox();
        paneForQtyBaked.getChildren().addAll(qtyBakedLabel, qtyBakedTextField, qtyBakedButton);
        qtyBakedButton.setMaxWidth(Double.MAX_VALUE);
        paneForQtyBaked.setId("add-sell");
        
        HBox paneForAddSell = new HBox();
        paneForAddSell.getChildren().addAll(paneForQtySold, paneForQtyBaked);
        paneForAddSell.getStyleClass().add("hbox");
        
        HBox paneForButton = new HBox();
        Button reportButton = new Button("_Inventory Report");
        paneForButton.getChildren().addAll(reportButton, exitButton);
        paneForButton.getStyleClass().add("hbox");
        
        VBox pane = new VBox(15);
        pane.getChildren().addAll(paneForList, paneForAddSell, paneForButton);
        
        Scene scene = new Scene(pane, 450, 275);
        scene.getStylesheets().add("orelk/css/css.css");
        
        primaryStage.setTitle("Cookie Inventory");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setResizable(false);
        
        BorderPane reportPane = new BorderPane();
        Button returnButton = new Button("_Return");
        reportPane.setTop(returnButton);
        VBox infoPane = new VBox(5);
        ScrollPane wrapInfoPane = new ScrollPane(infoPane);
        
        Scene scene2 = new Scene(reportPane, 450, 275);
        scene2.getStylesheets().add("orelk/css/css.css");
        
        //report button will place a new scene onto the stage with information
        //of the current inventory items
        reportButton.setOnAction(e-> {
            StringBuilder infoString = new StringBuilder();
            for(CookieInventoryItem item: inventory){
                infoString.append(String.format("%-30s%8s%5d%n",
                    item.cookie.getName().trim(), "Amount:",item.getQuantity()));
            }
            infoPane.getChildren().add(new Label(infoString.toString()));
            reportPane.setCenter(wrapInfoPane);
            primaryStage.setScene(scene2);
        });
        
        //when the user goes back to the main page, the information is erased
        //from the scene so that each time the user goes back, updated 
        //information can be written
        returnButton.setOnAction(e -> {
            infoPane.getChildren().removeIf(Label.class::isInstance);
            primaryStage.setScene(scene);
        });
    }
    
    /**
    * Returns the id of the cookie selected in the drop-down menu
    * 
    * @return the number of the cookie id selected
    * 
    */
    public int cookieSelected(){
        int idOfCookie = cookieIdArray.get
            (cookieArray.indexOf(selectCookie.getValue()));
        return idOfCookie;
    }
    
    /**
    * Checks to see if the user entered a proper value into the text box
    * 
    * @param text the string that is being checked for validity
    * @return Boolean value of the status of user's entered text value
    * 
    */
    public boolean isValidEntry(String text){
        if(text.isEmpty()){
            if(qtySoldButton.isFocused())
                alertCreator("Data Entry Error", "Please enter the number of "
                    + "cookies you wish to buy");
            else if(qtyBakedButton.isFocused())
                alertCreator("Data Entry Error", "Please enter the number of "
                    + "cookies you wish to add");
            return false;
        }
        for(int i = 0; i < text.length(); i++){
            if(!Character.isDigit(text.charAt(i))){
                alertCreator("Data Entry Error", "You must enter a valid numberic value");
                return false;
            }
        }
        if(Double.parseDouble(text) < 0){
            alertCreator("Data Entry Error", "You must enter a quantity that is greater than 0");
            return false;
        }
        return true;
    }
    
    /**
    * Adds the proper title and content to an alert box to warn the user
    * of some issue with their input
    * 
    * @param title specified title for the alert box
    * @param message specified message for the alert box
    * 
    */
    public void alertCreator(String title, String message){
        if(qtySoldButton.isFocused())
            qtySoldTextField.clear();
        if(qtyBakedButton.isFocused())
            qtyBakedTextField.clear();
        alert.getDialogPane().setPrefWidth(300);
        alert.getDialogPane().setStyle("-fx-font-size:16px");
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
