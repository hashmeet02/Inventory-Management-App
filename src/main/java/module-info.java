module org.example.workshop4and5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.workshop4and5 to javafx.fxml;
    opens org.example.workshop4and5.controllers to javafx.fxml;
    opens org.example.workshop4and5.abstractClasses to javafx.fxml;
    opens org.example.workshop4and5.models to javafx.fxml;
    exports org.example.workshop4and5;
    exports org.example.workshop4and5.controllers;
    exports org.example.workshop4and5.abstractClasses;
    exports org.example.workshop4and5.models;
}