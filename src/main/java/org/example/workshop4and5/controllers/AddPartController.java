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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.workshop4and5.models.InHouse;
import org.example.workshop4and5.models.Inventory;
import org.example.workshop4and5.models.Outsourced;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {
    // Required javafx components/nodes.
    @FXML
    private TextField addPartId, addPartInv, addPartMachineOrCompanyId, addPartMax, addPartMin, addPartName, addPartPrice;
    @FXML
    private RadioButton inHousePartRadio, outsourcedPartRadio;
    @FXML
    private Label addPartMachineOrCompanyIdLbl;
    private ChangeListener<String> addPartMachineOrCompanyIdListener;

    //private class variable inventory to hold all part and product instances.
    private static Inventory inventory= new Inventory();


    // initialize method to establishes all change listeners for text fields and radio buttons.
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addPartId.setText(Integer.toString(inventory.generatePartId()));

        addPartInv.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    addPartInv.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        addPartMin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    addPartMin.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        addPartMax.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    addPartMax.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        this.addPartMachineOrCompanyIdListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    addPartMachineOrCompanyId.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        };
        addPartMachineOrCompanyId.textProperty().addListener(addPartMachineOrCompanyIdListener);
    }

    // Handler for the InHouse Radio Button that changes the label to machineId, and the change listener for the text
    // field to only accept numbers.
    public void setLabelToMachineId(ActionEvent actionEvent) {
        addPartMachineOrCompanyIdLbl.setText("Machine ID");
        addPartMachineOrCompanyId.setText("");
        addPartMachineOrCompanyId.textProperty().removeListener(this.addPartMachineOrCompanyIdListener);
        this.addPartMachineOrCompanyIdListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    addPartMachineOrCompanyId.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        };
        addPartMachineOrCompanyId.textProperty().addListener(this.addPartMachineOrCompanyIdListener);
    }

    // Handler for the Outsourced radion button that changes the label to company name, and the change listener for the
    // text field to accept text.
    public void setLabelToCompanyName(ActionEvent actionEvent) {

        addPartMachineOrCompanyIdLbl.setText("Company Name");
        addPartMachineOrCompanyId.setText("");
        addPartMachineOrCompanyId.textProperty().removeListener(this.addPartMachineOrCompanyIdListener);
        this.addPartMachineOrCompanyIdListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                addPartMachineOrCompanyId.setText(newValue);
            }
        };
        addPartMachineOrCompanyId.textProperty().addListener(this.addPartMachineOrCompanyIdListener);
    }

    // Handler for the Save Part button. It does all the validations before saving the Part. If any validation fails the
    // user is notified using a warning Alert. If successful the new Part is created and saved to the inventory instance
    // and the user is sent back to the main screen view with updated tables. The control is returned back to the
    // MainScreenController.
    public void savePart(ActionEvent actionEvent) throws IOException {
        String name = addPartName.getText();

        if(name.length() == 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Name");
            alert.setHeaderText("Invalid Name");
            alert.setContentText("Name can't be empty.");
            alert.showAndWait();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(addPartPrice.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Price");
            alert.setHeaderText("Invalid Price");
            alert.setContentText("Invalid Price: Price is not double convertible.");
            alert.showAndWait();
            return;
        }

        int min;
        try {
            min = Integer.parseInt(addPartMin.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Min");
            alert.setHeaderText("Invalid Min");
            alert.setContentText("Invalid Min: Min is not integer convertible.");
            alert.showAndWait();
            return;
        }

        int max;
        try {
            max = Integer.parseInt(addPartMax.getText());
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

        int stock;
        try {
            stock = Integer.parseInt(addPartInv.getText());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Inv");
            alert.setHeaderText("Invalid Inv");
            alert.setContentText("Invalid Inv");
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

        int machineId = -1;
        String companyName = "";
        if(inHousePartRadio.isSelected()) {
            try {
                machineId = Integer.parseInt(addPartMachineOrCompanyId.getText());
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Machine ID");
                alert.setHeaderText("Invalid Machine ID");
                alert.setContentText("Machine ID not Integer convertible.");
                alert.showAndWait();
                return;
            }
            InHouse part = new InHouse(Integer.parseInt(addPartId.getText()), name, price, stock, min, max, machineId);
            inventory.addPart(part);

        } else if(outsourcedPartRadio.isSelected()) {
            companyName = addPartMachineOrCompanyId.getText();
            if(companyName.length() == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error: Empty company name");
                alert.setHeaderText("Error: Empty company name");
                alert.setContentText("Company name can't be empty");
                alert.showAndWait();
                return;
            }
            Outsourced part = new Outsourced(Integer.parseInt(addPartId.getText()), name, price, stock, min, max, companyName);
            inventory.addPart(part);
        }
        backMainScreenView(actionEvent);
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

