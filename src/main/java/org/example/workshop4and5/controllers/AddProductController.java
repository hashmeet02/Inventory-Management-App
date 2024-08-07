package org.example.workshop4and5.controllers;


/**********************************************
 Last Name:SAINI
 First Name:HASHMEET SINGH
 This code is the authored by and a property of
 Hashmeet Singh Saini
 Date:April 4th 2024
 **********************************************/


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.workshop4and5.abstractClasses.Part;
import org.example.workshop4and5.models.Inventory;
import org.example.workshop4and5.models.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {

    // Required javafx components/nodes.
    @FXML
    private TextField addProductId, addProductName, addProductPrice, addProductInv,
            addProductMax, addProductMin, addProductSearch;
    @FXML
    private TableView addProductTable, assocProductTable;

    //private class variables to store the data objects being altered. .
    private ObservableList<Part> associatedParts= FXCollections.observableArrayList();
    private static Inventory inventory= new Inventory();

    // initialize method to establishes all change listeners for text fields and radio buttons. It also populates
    // the all and associated parts tables with the data.
    public void initialize(URL url, ResourceBundle resourceBundle){
        addProductId.setText(Integer.toString(inventory.generateProductId()));

        addProductInv.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    addProductInv.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        addProductMin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    addProductMin.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        addProductMax.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    addProductMax.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        TableColumn productIdCol = new TableColumn("Product ID");
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn productNameCol = new TableColumn("Product Name");
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn invCol = new TableColumn("Inventory Level");
        invCol.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn priceCol = new TableColumn("Price/Cost per Unit");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProductTable.getColumns().addAll(productIdCol, productNameCol, invCol, priceCol);
        addProductTable.setItems(inventory.getAllParts());

        TableColumn productIDCol2 = new TableColumn("Part ID");
        productIDCol2.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn productNameCol2 = new TableColumn("Part Name");
        productNameCol2.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn inveCol2 = new TableColumn("Inventory Level");
        inveCol2.setCellValueFactory(new PropertyValueFactory<>("stock"));

        TableColumn priceCol2 = new TableColumn("Price/Cost per Unit");
        priceCol2.setCellValueFactory(new PropertyValueFactory<>("price"));

        assocProductTable.getColumns().addAll(productIDCol2, productNameCol2, inveCol2, priceCol2);
        assocProductTable.setItems(associatedParts);

    }

    // Handler for the Search Part text box. If empty, sets part table to show all data. First checks if content is
    // integer convertible, if yes then tries to find part by id. Otherwise, by name. If nothing is found it throws a
    // warning telling user that that nothing could be found.
    public void onPartSearch(ActionEvent actionEvent) {
        String searchText = this.addProductSearch.getText();
        System.out.println("Searching Product: " + searchText);

        if(searchText.isEmpty()) {
            this.addProductTable.setItems(inventory.getAllParts());
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
            alert.setTitle("Error: no parts with name/id " + searchText +" could be found.");
            alert.setHeaderText("Error: no parts with name/id " + searchText + " could be found.");
            alert.setContentText("Error: no parts with name/id " + searchText +" could be found.");
            alert.showAndWait();
            return;
        }
        this.addProductTable.setItems(resultParts);
        this.addProductSearch.setText("");
    }

    // Handler for the Save Product button. It does all the validations before saving the Product. If any validation
    // fails the user is notified using a warning Alert. If successful the new Product is created and saved to the
    // inventory instance and the user is sent back to the main screen view with updated tables. The control is returned
    // back to the MainScreenController.
    public void saveProduct(ActionEvent actionEvent) throws IOException {
        System.out.println("savePart()");
        String name = addProductName.getText();

        if(name.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Name");
            alert.setHeaderText("Invalid Name");
            alert.setContentText("Name can't be empty.");
            alert.showAndWait();
            return;
        }

        Double price;
        try {
            price = Double.parseDouble(addProductPrice.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Price");
            alert.setHeaderText("Invalid Price");
            alert.setContentText("Invalid Price: Price is not double convertible.");
            alert.showAndWait();
            return;
        }

        Integer min;
        try {
            min = Integer.parseInt(addProductMin.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Min");
            alert.setHeaderText("Invalid Min");
            alert.setContentText("Invalid Min: Min is not integer convertible.");
            alert.showAndWait();
            return;
        }

        Integer max;
        try {
            max = Integer.parseInt(addProductMax.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Max");
            alert.setHeaderText("Invalid Max");
            alert.setContentText("Invalid Max: Max is not integer convertible.");
            alert.showAndWait();
            return;
        }

        if(max < min) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: Max must be greater than min");
            alert.setHeaderText("Error: Max must be greater than min");
            alert.setContentText("Error: Max must be greater than min");
            alert.showAndWait();
            return;
        }

        Integer stock;
        try {
            stock = Integer.parseInt(addProductInv.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Inv");
            alert.setHeaderText("Invalid Inv");
            alert.setContentText("Inv is no Integer convertible");
            alert.showAndWait();
            return;
        }

        if(stock> max || stock < min) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: Inv must be between min and max");
            alert.setHeaderText("Error: Inv must be between min and max");
            alert.setContentText("Inv level must be greater than or equal to min " +
                    "and less than or equal to max ");
            alert.showAndWait();
            return;
        }

        if(associatedParts.size()>0){
            double partsTotal=0;
            for(Part part: associatedParts){
                partsTotal+=part.getPrice();
            }
            if(partsTotal>price){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error: Parts cost more than product");
                alert.setHeaderText("Error: Parts cost more than product");
                alert.setContentText("The accumulated cost of associated parts can't" +
                        "be greater than the cost of the product itself.");
                alert.showAndWait();
                return;
            }
        }

        if(associatedParts.size()>0){
            Product product = new Product(Integer.parseInt(addProductId.getText()), name, price, stock, min, max);
            for(Part part: associatedParts ){
                product.addAssociatedPart(part);
            }
            inventory.addProduct(product);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No associated parts");
            alert.setHeaderText("No associated Parts selected");
            alert.setContentText("A product must have at least 1 associated part.");
            alert.showAndWait();
            return;
        }
        backMainScreenView(actionEvent);
    }

    // Handler for the Add part button that add the selected part to the associatedParts array of the product. If no
    // part is selected from the parts table then a Warning alert is given to the user.
    public void associatePart(ActionEvent actionEvent) {
        Part part = (Part) addProductTable.getSelectionModel().getSelectedItem();
        if(part == null) {
            System.out.println("Error: no part selected");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No part selected");
            alert.setHeaderText("No part selected");
            alert.setContentText("Please select a part from the Parts Table to modify.");
            alert.showAndWait();
        } else {
            associatedParts.add(part);
            System.out.println("Associated parts list length: " + Integer.toString(associatedParts.size()));
            assocProductTable.setItems(associatedParts);
        }
    }

    // Handler for the Remove Associated part button that removes the selected part from the associatedParts array of
    // the product. If no part is selected from the parts table then a Warning alert is given to the user. Also the
    // removal of the part is only allowed when the Product has more than 1 associated parts. This is because during
    // the product must have at least 1 associated part.
    public void removePartBtn(ActionEvent actionEvent) {
        Part part = (Part) assocProductTable.getSelectionModel().getSelectedItem();
        int index = assocProductTable.getSelectionModel().getSelectedIndex();
        if(part == null) {
            System.out.println("Error: no part selected");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No part selected");
            alert.setHeaderText("No part selected");
            alert.setContentText("No part selected. Please select a part to de-associate from this product.");
            alert.showAndWait();
        } else if (associatedParts.size()<=1) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Product must have 1 or more parts");
            alert.setHeaderText("Product must have 1 or more parts");
            alert.setContentText("Can't remove the selected Product because " +
                    "a product must have at least 1 associated part upon creation.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm De-association");
            alert.setHeaderText("Confirm de-association of part: " + part.getName());
            alert.setContentText("Are you sure you want to de-associate part: " + part.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                associatedParts.remove(index);
                assocProductTable.setItems(associatedParts);
            }
        }
    }

    // Handler for the cancel button to discard all changes and return to the main screen view. From here the control is
    // returned back to the MainScreenController.
    public void backMainScreenView(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/workshop4and5/main-screen-view.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 913, 438);
        stage.setTitle("Inventory Manager!");
        stage.setScene(scene);
        stage.show();
    }
}
