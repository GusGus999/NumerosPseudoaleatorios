module com.example.pseudoaleatorios {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires POOLibrary;
    //requires com.example.pseudoaleatorios;

    // AÑADE ESTA LÍNEA PARA EL MODELO
    opens com.example.modelo to javafx.base;

    // Y ASEGÚRATE DE TENER ESTA PARA EL CONTROLADOR
    opens com.example.pseudoaleatorios to javafx.fxml;

    // Es posible que necesites exportar el paquete del controlador si no lo tienes
    exports com.example.pseudoaleatorios;
}