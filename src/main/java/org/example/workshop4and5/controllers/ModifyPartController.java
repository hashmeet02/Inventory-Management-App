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
import org.example.workshop4and5.abstractClasses.Part;
import org.example.workshop4and5.models.InHouse;
import org.example.workshop4and5.models.Inventory;
import org.example.workshop4and5.models.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPartController implements Initializable {
    // Required javafx components/nodes.
    @FXML
    private TextField modifyPartId, modifyPartName, modifyPartInv, modifyPartPrice, modifyPartMin, modifyPartMax,
            modifyPartMachineOrCompanyId;
    @FXML
    private RadioButton modifyPartInHouseRadio, modifyPartOutsourcedRadio;
    @FXML
    private Label modifyPartMachineOrCompanyIdLbl;

    //private class variables for storing data objects.
    public static Part part;
    public static Part originalPart;
    private static Inventory inventory= new Inventory();
    private ChangeListener<String> modifyPartMachineOrCompanyIdListener;

    // initialize method to establishes all change listeners for text fields and radio buttons.It also populates the
    // contents of the all the fields according the selected part to be modified. So the user sees the original data.
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(part instanceof InHouse) {
            System.out.println("This is an InHouse Part");
            originalPart = new InHouse(part.getId(), part.getName(), part.getPrice(), part.getStock(), part.getMin(), part.getMax(), ((InHouse) part).getMachineId());
            modifyPartInHouseRadio.setSelected(true);

        } else if(part instanceof Outsourced) {
            System.out.println("This is an Outsourced Part");
            originalPart = new Outsourced(part.getId(), part.getName(), part.getPrice(), part.getStock(), part.getMin(), part.getMax(), ((Outsourced) part).getCompanyName());
            modifyPartOutsourcedRadio.setSelected(true);
        }

        modifyPartInv.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    modifyPartInv.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        modifyPartMin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    modifyPartMin.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        modifyPartMax.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    modifyPartMax.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        this.modifyPartMachineOrCompanyIdListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    modifyPartMachineOrCompanyId.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        };
        modifyPartMachineOrCompanyId.textProperty().addListener(modifyPartMachineOrCompanyIdListener);


        modifyPartId.setText(Integer.toString(part.getId()));
        modifyPartName.setText(part.getName());
        modifyPartPrice.setText(Double.toString(part.getPrice()));
        modifyPartMax.setText(Integer.toString(part.getMax()));
        modifyPartMin.setText(Integer.toString(part.getMin()));
        modifyPartInv.setText(Integer.toString(part.getStock()));
        if(part instanceof InHouse) {
            setLabelToMachineId(new ActionEvent());
            modifyPartMachineOrCompanyId.setText(Integer.toString(((InHouse) part).getMachineId()));
        } else if(part instanceof Outsourced) {
            setLabelToCompanyName(new ActionEvent());
            modifyPartMachineOrCompanyId.setText(((Outsourced) part).getCompanyName());
        }
    }

    // Handler for the InHouse Radio Button that changes the label to machineId, and the change listener for the text
    // field to only accept numbers.
    public void setLabelToMachineId(ActionEvent actionEvent) {
        System.out.println("setLabelToMachineID");
        modifyPartMachineOrCompanyIdLbl.setText("Machine ID");
        modifyPartMachineOrCompanyId.setText("");
        modifyPartMachineOrCompanyId.textProperty().removeListener(this.modifyPartMachineOrCompanyIdListener);
        this.modifyPartMachineOrCompanyIdListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("\\d*")) {
                    modifyPartMachineOrCompanyId.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        };
        modifyPartMachineOrCompanyId.textProperty().addListener(this.modifyPartMachineOrCompanyIdListener);
    }

    // Handler for the Outsourced radion button that changes the label to company name, and the change listener for the
    // text field to accept text.
    public void setLabelToCompanyName(ActionEvent actionEvent) {
        System.out.println("setLabelToCompanyName");
        modifyPartMachineOrCompanyIdLbl.setText("Company Name");
        modifyPartMachineOrCompanyId.setText("");
        modifyPartMachineOrCompanyId.textProperty().removeListener(this.modifyPartMachineOrCompanyIdListener);
        this.modifyPartMachineOrCompanyIdListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                modifyPartMachineOrCompanyId.setText(newValue);
            }
        };
        modifyPartMachineOrCompanyId.textProperty().addListener(this.modifyPartMachineOrCompanyIdListener);
    }

    // Handler for the Save Part button. It does all the validations before saving the Part. If any validation fails the
    // user is notified using a warning Alert. If successful the Part is modified and saved to the inventory instance
    // and the user is sent back to the main screen view with updated tables. The control is returned back to the
    // MainScreenController.
    public void savePart(ActionEvent actionEvent) throws IOException {
        System.out.println("savePart()");
        String name = modifyPartName.getText();

        if(name.length() == 0) {
            System.out.println("Error Invalid Name: " + name);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Name");
            alert.setHeaderText("Invalid Name");
            alert.setContentText("Name can't be empty.");
            alert.showAndWait();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(modifyPartPrice.getText());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing Price to Integer: " + modifyPartPrice.getText());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Price");
            alert.setHeaderText("Invalid Price");
            alert.setContentText("Invalid Price: Price is not double convertible.");
            alert.showAndWait();
            return;
        }

        Integer min;
        try {
            min = Integer.parseInt(modifyPartMin.getText());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing Min to Integer: " + modifyPartMin.getText());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Min");
            alert.setHeaderText("Invalid Min");
            alert.setContentText("Invalid Min: Min is not integer convertible.");
            alert.showAndWait();
            return;
        }

        Integer max;
        try {
            max = Integer.parseInt(modifyPartMax.getText());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing Max to Integer: " + modifyPartMax.getText());
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Max");
            alert.setHeaderText("Invalid Max");
            alert.setContentText("Invalid Max: Max is not integer convertible.");
            alert.showAndWait();
            return;
        }

        if(max < min) {
            System.out.println("Error: Max must be greater than min");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error: Max must be greater than min");
            alert.setHeaderText("Error: Max must be greater than min");
            alert.setContentText("Error: Max must be greater than min");
            alert.showAndWait();
            return;
        }

        Integer stock;
        try {
            stock = Integer.parseInt(modifyPartInv.getText());
        } catch (NumberFormatException e) {
            System.out.println("Error parsing Inventory/Stock to Integer: " + modifyPartMax.getText());
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
        if(modifyPartInHouseRadio.isSelected()) {
            try {
                machineId = Integer.parseInt(modifyPartMachineOrCompanyId.getText());
            } catch (NumberFormatException e) {
                System.out.println("Error parsing machineId to Integer: " + modifyPartMachineOrCompanyId.getText());
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Machine ID");
                alert.setHeaderText("Invalid Machine ID");
                alert.setContentText("Machine ID not Integer convertible.");
                alert.showAndWait();
                return;
            }
        } else if(modifyPartOutsourcedRadio.isSelected()) {
            companyName = modifyPartMachineOrCompanyId.getText();
            if(companyName.length() == 0) {
                System.out.println("Error: Empty company name");
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error: Empty company name");
                alert.setHeaderText("Error: Empty company name");
                alert.setContentText("Company name can't be empty");
                alert.showAndWait();
                return;
            }
        }
        inventory.deletePart(originalPart);
        part.setName(name);
        part.setPrice(price);
        part.setStock(stock);
        part.setMin(min);
        part.setMax(max);
        if(part instanceof InHouse) {
            ((InHouse)part).setMachineId(machineId);
        } else if(part instanceof Outsourced) {
            ((Outsourced)part).setCompanyName(companyName);
        }
        inventory.addPart(part);
        backMainScreenView(actionEvent);
    }

    // Handler for the cancel button to discard all changes and return to the main screen view. From here the control is
    // returned back to the MainScreenController.
    public void backMainScreenView(ActionEvent actionEvent) throws IOException {
        if(modifyPartOutsourcedRadio.isSelected()) {
            System.out.println("Outsourced");
        }
        if(modifyPartInHouseRadio.isSelected()) {
            System.out.println("In House");
        }

        Parent root = FXMLLoader.load(getClass().getResource("/org/example/workshop4and5/main-screen-view.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 913, 438);
        stage.setTitle("Inventory Manager!");
        stage.setScene(scene);
        stage.show();
    }

    public static void setPart(Part _part) {
        part = _part;
    }


}