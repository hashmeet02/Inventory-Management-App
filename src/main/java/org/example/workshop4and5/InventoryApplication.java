/**********************************************
 Last Name:SAINI
 First Name:HASHMEET SINGH
 This code is the authored by and a property of
 Hashmeet Singh Saini
 Date:April 4th 2024
 **********************************************/

package org.example.workshop4and5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.workshop4and5.abstractClasses.Part;
import org.example.workshop4and5.models.InHouse;
import org.example.workshop4and5.models.Inventory;
import org.example.workshop4and5.models.Outsourced;
import org.example.workshop4and5.models.Product;

import java.io.IOException;

public class InventoryApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("main-screen-view.fxml"));
        primaryStage.setTitle("Inventory Manager!");
        primaryStage.setScene(new Scene(root, 913, 438));
        primaryStage.setResizable(false);
        primaryStage.show();

    }

    private static Inventory inventory= new Inventory();

    public static void main(String[] args) {
        //Launching the application. Code heads to the MainScreenController.
        inventory.saveDataToDatabase();
        launch();
    }

}