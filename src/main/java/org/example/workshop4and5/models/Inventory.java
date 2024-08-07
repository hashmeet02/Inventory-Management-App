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
import javafx.scene.control.Alert;
import org.example.workshop4and5.abstractClasses.Part;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class Inventory {
    private static ObservableList<Part> allParts= FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts=FXCollections.observableArrayList();

    public void addPart(Part newPart){
        allParts.add(newPart);
    }

    public void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    public Part searchPartByID(int partId){
        for(Part part: getAllParts()){
            if (part.getId()==partId){
                return part;
            }
        }

        //might change this
        Alert alert= new Alert(Alert.AlertType.ERROR);
        alert.setTitle("error");
        alert.setHeaderText("No item found");
        alert.show();
        return null;
    }

    public Product searchProductByID(int productId){
        for(Product product: getAllProducts()){
            if(product.getId()==productId){
                return product;
            }
        }
        //might change this
        Alert alert= new Alert(Alert.AlertType.ERROR);
        alert.setTitle("error");
        alert.setHeaderText("No item found");
        alert.show();
        return null;
    }

    public ObservableList<Part> searchPartByName( String name){
        ObservableList<Part> result = FXCollections.observableArrayList();
        for(Part part: getAllParts()){

            if(part.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(part);
            }
        }
        return result;
    }

    public ObservableList<Product> searchProductByName( String name){
        ObservableList<Product> result = FXCollections.observableArrayList();
        for(Product product: getAllProducts()){

            if(product.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(product);
            }
        }
        return result;
    }

    public void updatePart(int index, Part selectedPart){
        Part part = allParts.get(index);
        part.setId(selectedPart.getId());
        part.setName(selectedPart.getName());
        part.setPrice(selectedPart.getPrice());
        part.setStock(selectedPart.getStock());
        part.setMin(selectedPart.getMin());
        part.setMax(selectedPart.getMax());
        if(part instanceof InHouse) {
            ((InHouse)part).setMachineId(((InHouse)selectedPart).getMachineId());
        } else if(part instanceof Outsourced) {
            ((Outsourced)part).setCompanyName(((Outsourced)part).getCompanyName());
        }
    }

    public void updateProduct(int index, Product selectedProduct){
        Product product = allProducts.get(index);
        product.setId(selectedProduct.getId());
        product.setName(selectedProduct.getName());
        product.setMin(selectedProduct.getMin());
        product.setMax(selectedProduct.getMax());
        product.setPrice(selectedProduct.getPrice());
        product.setStock(selectedProduct.getStock());
    }

    public boolean deletePart(Part selectedPart){
        for(Part part: allParts) {
            if (part.getId() == selectedPart.getId() &&
                    part.getName().equals(selectedPart.getName()) &&
                    part.getPrice() == selectedPart.getPrice() &&
                    part.getStock() == selectedPart.getStock() &&
                    part.getMin() == selectedPart.getMin() &&
                    part.getMax() == selectedPart.getMax()) {
                if(selectedPart instanceof InHouse &&
                        ((InHouse) part).getMachineId() == ((InHouse) selectedPart).getMachineId()) {
                    return allParts.remove(part);
                } else if(selectedPart instanceof Outsourced &&
                        ((Outsourced)part).getCompanyName().equals(((Outsourced) selectedPart).getCompanyName())) {
                    return allParts.remove(part);
                }
            }
        }
        System.out.println("No matching part was found");
        return false;
    }

    public boolean deleteProduct(Product selectedProduct){
        for(Product product: allProducts) {
            if (product.getId() == selectedProduct.getId() &&
                    product.getName().equals(selectedProduct.getName()) &&
                    product.getPrice() == selectedProduct.getPrice() &&
                    product.getStock() == selectedProduct.getStock() &&
                    product.getMin() == selectedProduct.getMin() &&
                    product.getMax() == selectedProduct.getMax()) {
                return allProducts.remove(product);
            }
        }
        System.out.println("No matching part was found");
        return false;
    }

    public ObservableList<Part> getAllParts(){
        return allParts;
    }

    public ObservableList<Product> getAllProducts(){
        return allProducts;
    }

    public static void saveDataAsObject(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            // Convert ObservableList to ArrayList before writing
            ArrayList<Part> parts = new ArrayList<>(allParts);
            ArrayList<Product> products = new ArrayList<>(allProducts);

            oos.writeObject(parts);
            oos.writeObject(products);
            System.out.println("Data saved as object successfully.");
        } catch (IOException e) {
            System.err.println("Error saving data as object: " + e.getMessage());
        }
    }

    public static void loadDataFromObject(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            ArrayList<Part> parts = (ArrayList<Part>) ois.readObject();
            ArrayList<Product> products = (ArrayList<Product>) ois.readObject();

            allParts = FXCollections.observableArrayList(parts);
            allProducts = FXCollections.observableArrayList(products);

            System.out.println("Data loaded from object successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data from object: " + e.getMessage());
        }
    }

    public void saveDataToDatabase() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:D:\\SENECA\\SEM5\\APD_545\\week12\\workshop4and5\\src\\main\\java\\org\\example\\workshop4and5\\data\\inventory.db");
             Statement stm = conn.createStatement();) {
            String queryProducts = "CREATE TABLE IF NOT EXISTS products(" +
                    "id INTEGER PRIMARY KEY," +
                    "name VARCHAR(50)," +
                    "price REAL," +
                    "stock INTEGER," +
                    "min INTEGER, " +
                    "max INTEGER " +
                    ");";
            boolean result = stm.execute(queryProducts);
            System.out.println("Products table initialized.");

            String queryParts = "CREATE TABLE IF NOT EXISTS parts(" +
                    "id INTEGER PRIMARY KEY, " +
                    "name VARCHAR(50)," +
                    "price REAL," +
                    "stock INTEGER," +
                    "min INTEGER, " +
                    "max INTEGER, " +
                    "type VARCHAR(10)" +
                    ");";
            result = stm.execute(queryParts);
            System.out.println("Parts table initialized");

            String queryProductParts = "CREATE TABLE IF NOT EXISTS product_parts(" +
                    "product_id INTEGER," +
                    "part_id INTEGER," +
                    "PRIMARY KEY (product_id, part_id)," +
                    "FOREIGN KEY (product_id) REFERENCES products(id)," +
                    "FOREIGN KEY (part_id) REFERENCES parts(id));";
            result = stm.execute(queryProductParts);
            System.out.println("product parts table initialized");

            String queryInHouse = "CREATE TABLE IF NOT EXISTS inHouse(" +
                    "part_id INTEGER PRIMARY KEY," +
                    "machine_id INTEGER," +
                    "FOREIGN KEY (part_id) REFERENCES parts(id)" +
                    ")";
            result = stm.execute(queryInHouse);
            System.out.println("InHouse table initialized");

            String queryOutsourced = "CREATE TABLE IF NOT EXISTS outsourced(" +
                    "part_id INTEGER PRIMARY KEY," +
                    "company_name VARCHAR(50)," +
                    "FOREIGN KEY (part_id) REFERENCES parts(id)" +
                    ")";
            result = stm.execute(queryOutsourced);
            System.out.println("Outsourced table initialized");



            insertProducts(conn);
            insertParts(conn);
            insertInHouse(conn);
            insertOutsourced(conn);
            insertProductParts(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void insertProductParts(Connection conn) throws SQLException {
        String insertQuery = "INSERT OR REPLACE INTO product_parts (product_id, part_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            for (Product product : allProducts) {
                for (Part part : product.getAllAssociatedParts()) {
                    stmt.setInt(1, product.getId());
                    stmt.setInt(2, part.getId());
                    stmt.executeUpdate();
                }
            }
        }
    }
    private void insertProducts(Connection conn) throws SQLException {
        String insertQuery = "INSERT OR REPLACE INTO products (id, name, price, stock, min, max) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            for (Product product : allProducts) {
                stmt.setInt(1, product.getId());
                stmt.setString(2, product.getName());
                stmt.setDouble(3, product.getPrice());
                stmt.setInt(4, product.getStock());
                stmt.setInt(5, product.getMin());
                stmt.setInt(6, product.getMax());
                stmt.executeUpdate();
            }
        }
    }

    private void insertParts(Connection conn) throws SQLException {
        String insertQuery = "INSERT OR REPLACE INTO parts (id, name, price, stock, min, max, type) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            for (Part part : allParts) {
                stmt.setInt(1, part.getId());
                stmt.setString(2, part.getName());
                stmt.setDouble(3, part.getPrice());
                stmt.setInt(4, part.getStock());
                stmt.setInt(5, part.getMin());
                stmt.setInt(6, part.getMax());
                if (part instanceof InHouse) {
                    stmt.setString(7, "inhouse");
                } else if (part instanceof Outsourced) {
                    stmt.setString(7, "outsourced");
                }
                stmt.executeUpdate();
            }
        }
    }
    private void insertInHouse(Connection conn) throws SQLException {
        String insertQuery = "INSERT OR REPLACE INTO inHouse (part_id, machine_id) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            for (Part part : allParts) {
                if (part instanceof InHouse) {
                    stmt.setInt(1, part.getId());
                    stmt.setInt(2, ((InHouse) part).getMachineId());
                    stmt.executeUpdate();
                }
            }
        }
    }

    private void insertOutsourced(Connection conn) throws SQLException {
        String insertQuery = "INSERT OR REPLACE INTO outsourced (part_id, company_name) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
            for (Part part : allParts) {
                if (part instanceof Outsourced) {
                    stmt.setInt(1, part.getId());
                    stmt.setString(2, ((Outsourced) part).getCompanyName());
                    stmt.executeUpdate();
                }
            }
        }
    }

    public void loadDataFromDatabase() {
        // Clear existing data
        allProducts.clear();
        allParts.clear();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:D:\\SENECA\\SEM5\\APD_545\\week12\\workshop4and5\\src\\main\\java\\org\\example\\workshop4and5\\data\\inventory.db");
             Statement stm = conn.createStatement()) {

            // Load data from products table
            ResultSet productResultSet = stm.executeQuery("SELECT * FROM products");
            while (productResultSet.next()) {
                int id = productResultSet.getInt("id");
                String name = productResultSet.getString("name");
                double price = productResultSet.getDouble("price");
                int stock = productResultSet.getInt("stock");
                int min = productResultSet.getInt("min");
                int max = productResultSet.getInt("max");
                Product product = new Product(id, name, price, stock, min, max);
                allProducts.add(product);
            }

            // Load data from parts table with JOIN to inHouse and outsourced tables
            String partQuery = "SELECT p.id, p.name, p.price, p.stock, p.min, p.max, p.type, " +
                    "i.machine_id AS inhouse_machine_id, o.company_name AS outsourced_company_name " +
                    "FROM parts p LEFT JOIN inHouse i ON p.id = i.part_id " +
                    "LEFT JOIN outsourced o ON p.id = o.part_id";
            ResultSet partResultSet = stm.executeQuery(partQuery);
            while (partResultSet.next()) {
                int id = partResultSet.getInt("id");
                String name = partResultSet.getString("name");
                double price = partResultSet.getDouble("price");
                int stock = partResultSet.getInt("stock");
                int min = partResultSet.getInt("min");
                int max = partResultSet.getInt("max");
                String type = partResultSet.getString("type");

                Part part;
                if ("inhouse".equals(type)) {
                    int machineId = partResultSet.getInt("inhouse_machine_id");
                    part = new InHouse(id, name, price, stock, min, max, machineId);
                } else {
                    String companyName = partResultSet.getString("outsourced_company_name");
                    part = new Outsourced(id, name, price, stock, min, max, companyName);
                }
                allParts.add(part);
            }

            // Load data from product_parts table using JOIN
            String productPartQuery = "SELECT product_id, part_id FROM product_parts";
            try (ResultSet productPartResultSet = stm.executeQuery(productPartQuery)) {
                while (productPartResultSet.next()) {
                    int productId = productPartResultSet.getInt("product_id");
                    int partId = productPartResultSet.getInt("part_id");
                    Product product = allProducts.stream().filter(p -> p.getId() == productId).findFirst().orElse(null);
                    Part part = allParts.stream().filter(p -> p.getId() == partId).findFirst().orElse(null);
                    if (product != null && part != null) {
                        product.addAssociatedPart(part);
                    }
                }
            }

            System.out.println("Data loaded from database successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int generatePartId() {
        int id = 0;
        for (Part part : allParts) {
            if(id <= part.getId()) {
                id = part.getId();
            }
        }
        return ++id;
    }

    public int generateProductId() {
        int id = 0;
        for (Product product : allProducts) {
            if(id <= product.getId()) {
                id = product.getId();
            }
        }
        return ++id;
    }
}
