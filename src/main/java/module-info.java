module com.example.pseudoaleatorios {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pseudoaleatorios to javafx.fxml;
    exports com.example.pseudoaleatorios;
}