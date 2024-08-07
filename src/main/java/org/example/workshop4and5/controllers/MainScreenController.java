package org.example.workshop4and5.controllers;


/**********************************************
 Last Name:SAINI
 First Name:HASHMEET SINGH
 This code is the authored by and a property of
 Hashmeet Singh Saini
 Date:April 4th 2024
 **********************************************/


import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.workshop4and5.abstractClasses.Part;
import org.example.workshop4and5.models.Inventory;
import org.example.workshop4and5.models.Product;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    //Required javaFX nodes/components.
    @FXML
    private TableView mainScreenPartsTable, mainScreenProductsTable;

    @FXML
    private TextField partSearch, productSearch;

    //inventory class variable to manage all product and part instances.
    private static Inventory inventory=new Inventory();


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //general screen
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Initializing the table views with the data.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            initPartsTable();
        } catch(Exception e) {
            System.out.println("Error initializing parts table: " + e.toString());
        }
        try {
            initProductsTable();
        } catch(Exception e) {
            System.out.println("Error initializing products table" + e.toString());
        }
    }

    public void onSaveObj(ActionEvent actionEvent){
        System.out.println("Save obj button clicked.");
        String filename = "dataAsObject";
        inventory.saveDataAsObject(filename);

        // Get the absolute path of the saved file
        File file = new File(filename);
        String absolutePath = file.getAbsolutePath();
        System.out.println("Data saved in file: " + absolutePath);
    }

    public void onLoadFile(ActionEvent actionEvent){
        System.out.println("Load file button clicked.");
        inventory.loadDataFromObject("dataAsObject");
        mainScreenPartsTable.setItems(inventory.getAllParts());
        mainScreenProductsTable.setItems(inventory.getAllProducts());
    }

    public void onSaveDB(ActionEvent actionEvent){
        System.out.println("save db button clicked.");
        inventory.saveDataToDatabase();
    }

    public void onLoadDB(ActionEvent actionEvent){
        System.out.println("load db button clicked.");
        inventory.loadDataFromDatabase();
        mainScreenPartsTable.setItems(inventory.getAllParts());
        mainScreenProductsTable.setItems(inventory.getAllProducts());
    }

    // Handler for exit button to exit application when clicked.
    public void onExitBtn(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Parts section
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Populating the parts table with necessary columns and data from the inventory instance.
    public void initPartsTable()throws IOException {

        mainScreenPartsTable.setEditable(true);

        TableColumn partIdCol= new TableColumn<>("Part Id");
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn partNameCol= new TableColumn<>("Part Name");
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn invCol= new TableColumn<>("Inventory Level");
        invCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn priceCol= new TableColumn<>("Price/Cost per Unit");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainScreenPartsTable.getColumns().addAll(partIdCol, partNameCol, invCol, priceCol);
        mainScreenPartsTable.setItems(inventory.getAllParts());
    }

    // Handler for the Add part button that opens the add-part-view when clicked. From here the AddPartController
    // prevails.
    public void openAddPartView(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/workshop4and5/add-part-view.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 375, 425);
        stage.setTitle("Add Part");
        stage.setScene(scene);
        stage.show();
    }

    // Handler for the Modify Part button that opens the modify-part-view when clicked. From here the
    // ModifyPartController prevails. Throws alert to user if no part is selected by the user from the parts table.
    public void openModifyPartView(ActionEvent actionEvent) throws IOException {
        Part part = (Part)mainScreenPartsTable.getSelectionModel().getSelectedItem();

        if(part == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No part selected");
            alert.setHeaderText("No part selected");
            alert.setContentText("Please select a part from the Parts Table to modify.");
            alert.showAndWait();
        } else {
            ModifyPartController.setPart(part);
            Parent root = FXMLLoader.load(getClass()
                    .getResource("/org/example/workshop4and5/modify-part-view.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 375, 425);
            stage.setTitle("Modify Part");
            stage.setScene(scene);
            stage.show();
        }
    }

    // Handler for the Delete Part button that uses confirmation alert to confirm if the user really wants to delete the
    // selected part. If yes the part is deleted from the inventory. It also throws a warning alert if no part is
    // selected from the parts view before click the delete part button.
    public void onDeletePartBtn(ActionEvent actionEvent) {
        Part part = (Part) mainScreenPartsTable.getSelectionModel().getSelectedItem();

        if (part == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No part selected");
            alert.setHeaderText("No part selected");
            alert.setContentText("Please select a part from the Parts Table to delete.");
            alert.showAndWait();
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Delete");
            alert.setHeaderText("Confirm delete part: " + part.getName() + ".");
            alert.setContentText("Are you sure you want to delete part: " + part.getName() + "? This part will be " +
                    "deleted forever.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                inventory.deletePart(part);
                mainScreenPartsTable.setItems(inventory.getAllParts());
            }
        }
    }

    // Handler for the Search Part text box. If empty, sets part table to show all data. First checks if content is
    // integer convertible, if yes then tries to find part by id. Otherwise, by name. If nothing is found it throws a
    // warning telling user that that nothing could be found.
    public void onPartSearch(ActionEvent actionEvent) {
        String searchText = this.partSearch.getText();

        if(searchText.isEmpty()) {
            this.mainScreenPartsTable.setItems(inventory.getAllParts());
        }

        ObservableList<Part> resultParts = inventory.searchPartByName(searchText);
        try {
            int id = Integer.parseInt(searchText);
            Part part = inventory.searchPartByID(id);
            resultParts.add(part);
        } catch(NumberFormatException exception) {
            System.out.println("Non Fatal Error: " + searchText + " is not integer convertible.");
        }

        if(resultParts.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: no parts with name " + searchText +" could be found.");
            alert.setHeaderText("Error: no results for " + searchText + " could be found.");
            alert.setContentText("Error: no results for " + searchText +" could be found.");
            alert.showAndWait();
            return;
        }
        this.mainScreenPartsTable.setItems(resultParts);
        this.partSearch.setText("");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Products Section
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Populating the products table with necessary columns and data from the inventory instance.
    public void initProductsTable()throws IOException {
        mainScreenProductsTable.setEditable(true);

        TableColumn productIdCol= new TableColumn<>("Product Id");
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn productNameCol= new TableColumn<>("Product Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn invCol= new TableColumn<>("Inventory Level");
        invCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn priceCol= new TableColumn<>("Price/Cost per Unit");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainScreenProductsTable.getColumns().addAll(productIdCol, productNameCol, invCol, priceCol);
        mainScreenProductsTable.setItems(inventory.getAllProducts());
    }

    // Handler for the Add product button that opens the add-product-view when clicked. From here the
    // AddProductController prevails.
    public void openAddProductView(ActionEvent actionEvent) throws IOException {
        System.out.println("Opening AddProductForm");
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/workshop4and5/add-product-view.fxml"));
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 770, 400);
        stage.setTitle("Add Product");
        stage.setScene(scene);
        stage.show();
    }

    // Handler for the Modify Product button that opens the modify-product-view when clicked. From here the
    // ModifyProductController prevails. Throws alert to user if no product is selected by the user from the products
    // table.
    public void openModifyProductView(ActionEvent actionEvent) throws IOException {
        Product product = (Product)mainScreenProductsTable.getSelectionModel().getSelectedItem();

        if(product == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No product selected");
            alert.setHeaderText("No product selected");
            alert.setContentText("Please select a product from the Products Table to modify.");
            alert.showAndWait();
        } else {
            ModifyProductController.setProduct(product);
            Parent root = FXMLLoader.load(getClass().getResource("/org/example/workshop4and5/modify-product-view.fxml"));
            Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 770, 400);
            stage.setTitle("Modify Product");
            stage.setScene(scene);
            stage.show();
        }
    }

    // Handler for the Delete Product button that checks if the selected product has any associated parts. If yes
    // deleting the product isn't allowed else it sends out a confirmation alert to confirm if the user really wants to
    // delete the selected product. If yes the product is deleted from the inventory. It also throws a warning alert if
    // no part is selected from the parts view before click the delete part button.
    public void onDeleteProductBtn(ActionEvent actionEvent) {
        Product product = (Product) mainScreenProductsTable.getSelectionModel().getSelectedItem();

        if (product == null) {
            System.out.println("No product selected.");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No product selected");
            alert.setHeaderText("No product selected");
            alert.setContentText("Please select a product from the Products Table to delete.");
            alert.showAndWait();
            return;
        } else {
            if (product.getAllAssociatedParts().size() > 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error: Cannot delete a product");
                alert.setHeaderText("Error: Cannot delete a product with associated parts.");
                alert.setContentText("Error: Cannot delete a product with associated parts. Please remove all parts  " +
                        "for " + product.getName() + " before deleting.");
                alert.showAndWait();
                return;
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Delete");
                alert.setHeaderText("Confirm delete part: " + product.getName() + ".");
                alert.setContentText("Are you sure you want to delete part: " + product.getName() + "? This part " +
                        "will be deleted forever.");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    inventory.deleteProduct(product);
                    mainScreenProductsTable.setItems(inventory.getAllProducts());
                }
            }
        }
    }

    // Handler for the Search Product text box. If empty, sets product table to show all data. First checks if content
    // is integer convertible, if yes then tries to find product by id. Otherwise, by name. If nothing is found it
    // throws a warning telling user that that nothing could be found.
    public void onProductSearch(ActionEvent actionEvent) {
        String searchText = this.productSearch.getText();

        if(searchText.isEmpty()) {
            this.mainScreenProductsTable.setItems(inventory.getAllProducts());
        }

        ObservableList<Product> resultProducts = inventory.searchProductByName(searchText);
        try {
            int id = Integer.parseInt(searchText);
            Product product = inventory.searchProductByID(id);
            resultProducts.add(product);
        } catch(NumberFormatException exception) {
        }

        if(resultProducts.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: no products with name/id " + searchText +" could be found.");
            alert.setHeaderText("Error: no products with name/id " + searchText + " could be found.");
            alert.setContentText("Error: no products with name/id " + searchText +" could be found.");
            alert.showAndWait();
            return;
        }
        this.mainScreenProductsTable.setItems(resultProducts);
        this.productSearch.setText("");
    }

}

