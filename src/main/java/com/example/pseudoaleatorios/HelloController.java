package com.example.pseudoaleatorios; // Asegúrate que este sea tu paquete

import com.example.modelo.DatoGenerado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class HelloController {

    // --- Vinculación con elementos del Menú ---
    @FXML private MenuItem mi_cuadradosMedios;
    @FXML private MenuItem mi_multiplicadorConstante;
    @FXML private MenuItem mi_productosMedios;
    // ... (puedes agregar los otros menu items si los necesitas)

    // --- Vinculación con la Tabla y sus Columnas ---
    @FXML private TableView<DatoGenerado> tableView;
    @FXML private TableColumn<DatoGenerado, Integer> tc_n;
    @FXML private TableColumn<DatoGenerado, String> tc_yn;
    @FXML private TableColumn<DatoGenerado, String> tc_xn;
    @FXML private TableColumn<DatoGenerado, Double> tc_rn;

    // --- Vinculación con los Campos de Texto y Botón ---
    @FXML private TextField tf_x0;
    @FXML private TextField tf_a;
    @FXML private TextField tf_b;
    @FXML private TextField tf_c;
    @FXML private TextField tf_d;
    @FXML private TextField tf_m;
    @FXML private TextField tf_n;
    @FXML private Button btn_generar;

    // Variable para saber qué generador está seleccionado
    private String generadorSeleccionado = "";

    @FXML
    public void initialize() {
        // 1. Configurar las columnas de la tabla para que sepan qué dato mostrar
        tc_n.setCellValueFactory(new PropertyValueFactory<>("n"));
        tc_yn.setCellValueFactory(new PropertyValueFactory<>("yn"));
        tc_xn.setCellValueFactory(new PropertyValueFactory<>("xn"));
        tc_rn.setCellValueFactory(new PropertyValueFactory<>("rn"));

        // 2. Asignar acciones a los elementos del menú
        mi_multiplicadorConstante.setOnAction(event -> prepararInterfaz("Multiplicador Constante"));
        mi_cuadradosMedios.setOnAction(event -> prepararInterfaz("Cuadrados Medios"));
        mi_productosMedios.setOnAction(event -> prepararInterfaz("Productos Medios"));
        // ... (asigna acciones para los otros métodos de la misma forma)

        // 3. Asignar la acción principal al botón "Generar"
        btn_generar.setOnAction(event -> generarNumeros());
    }

    // Preparar la interfaz
    private void prepararInterfaz(String generador) {
        generadorSeleccionado = generador;
        tableView.getItems().clear(); // Limpia la tabla de resultados anteriores

        // Primero, deshabilita todos los campos
        tf_x0.setDisable(true);
        tf_a.setDisable(true);
        tf_b.setDisable(true);
        tf_c.setDisable(true);
        tf_d.setDisable(true);
        tf_m.setDisable(true);
        tf_n.setDisable(true);

        // habilita solo los necesarios para cada caso
        switch (generador) {
            case "Multiplicador Constante":
                tf_x0.setDisable(false);
                tf_a.setDisable(false);
                tf_n.setDisable(false);
                break;
            case "Cuadrados Medios":
                tf_x0.setDisable(false);
                break;
            case "Productos Medios":
                // Por ejemplo, si productos medios necesita dos semillas
                tf_x0.setDisable(false); // Semilla 1
                tf_a.setDisable(false);  // Semilla 2 (usamos el campo 'a' para la segunda semilla)
                break;
            // ... (añade más casos para los otros generadores)
        }
    }

    /**
     * Se ejecuta al presionar el botón "Generar".
     * Lee los datos, elige el algoritmo correcto y puebla la tabla.
     */
    private void generarNumeros() {
        try {
            switch (generadorSeleccionado) {
                case "Multiplicador Constante":
                    int x0_mc = Integer.parseInt(tf_x0.getText());
                    int a_mc = Integer.parseInt(tf_a.getText());
                    int n_mc = Integer.parseInt(tf_n.getText());
                    if (String.valueOf(x0_mc).length() != 4 || String.valueOf(a_mc).length() != 4) {
                        mostrarAlerta("Error de Entrada", "La semilla (x0) y la constante (a) deben tener 4 dígitos.");
                        return;
                    }
                    tableView.setItems(generarMultiplicadorConstante(a_mc, x0_mc, n_mc));
                    break;

                case "Cuadrados Medios":
                    // Lógica para Cuadrados Medios
                    break;

                case "Productos Medios":
                    // Lógica para Productos Medios
                    break;

                default:
                    mostrarAlerta("Advertencia", "Por favor, seleccione un método generador del menú.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Entrada", "Por favor, ingrese solo números válidos en los campos.");
        }
    }

    /**
     * Lógica del generador Multiplicador Constante.
     * @return Una ObservableList con los datos listos para la tabla.
     */
    private ObservableList<DatoGenerado> generarMultiplicadorConstante(int a, int x0, int n) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();
        int xi = x0;

        for (int i = 0; i < n; i++) {
            long producto = (long) a * xi;
            String productoStr = String.format("%08d", producto); // Asegura 8 dígitos rellenando con ceros

            int inicio = (productoStr.length() - 4) / 2;
            String digitosCentrales = productoStr.substring(inicio, inicio + 4);

            int siguiente = Integer.parseInt(digitosCentrales);
            double ri = (double) siguiente / 10000;

            datos.add(new DatoGenerado(i, productoStr, digitosCentrales, ri));
            xi = siguiente;
        }
        return datos;
    }

    /**
     * Muestra una ventana de alerta al usuario.
     */
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}