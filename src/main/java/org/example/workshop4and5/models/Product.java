package org.example.workshop4and5.models;

/**********************************************
 Last Name:SAINI
 First Name:HASHMEET SINGH
 This code is the authored by and a property of
 Hashmeet Singh Saini
 Date:April 4th 2024
 **********************************************/

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.workshop4and5.abstractClasses.Part;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient ObservableList<Part> associatedParts= FXCollections.observableArrayList();
    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    public Product(int id,String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    //code this
    public void addAssociatedPart(Part part){
        this.associatedParts.add(part);
    }

    //code this
    public boolean deleteAssociatedPart(Part _part){
        for(Part part : associatedParts) {
            if(part.getId() == _part.getId() &&
                    part.getName() == _part.getName() &&
                    part.getMax() == _part.getMax() &&
                    part.getMin() == _part.getMin() &&
                    part.getPrice() == _part.getPrice() &&
                    part.getStock() == _part.getStock()) {
                if(_part instanceof InHouse &&
                        ((InHouse) part).getMachineId() == ((InHouse) _part).getMachineId()) {
                    return associatedParts.remove(part);
                } else if(_part instanceof Outsourced &&
                        ((Outsourced)part).getCompanyName().equals(((Outsourced) _part).getCompanyName())) {
                    return associatedParts.remove(part);
                }
            }
        }
        System.out.println("No matching part was found");
        return false;
    }


    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        // Convert associatedParts to ArrayList before writing
        ArrayList<Part> parts = new ArrayList<>(associatedParts);
        out.writeObject(parts);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        // Read ArrayList and convert to ObservableList after deserialization
        ArrayList<Part> parts = (ArrayList<Part>) in.readObject();
        associatedParts = FXCollections.observableArrayList(parts);
    }

    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

}
