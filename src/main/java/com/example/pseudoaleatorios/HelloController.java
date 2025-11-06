package com.example.pseudoaleatorios; // Asegúrate que este sea tu paquete

import com.example.modelo.DatoGenerado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.metodos.ProductosMedios;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    // --- Vinculación con elementos del Menú ---
    @FXML private MenuItem mi_cuadradosMedios;
    @FXML private MenuItem mi_multiplicadorConstante;
    @FXML private MenuItem mi_productosMedios;
    @FXML private MenuItem mi_aditivo;
    @FXML private MenuItem mi_multiplicativo;
    @FXML private MenuItem mi_lineal;
    @FXML private MenuItem mi_cuadratico;
    @FXML private MenuItem mi_uniformidad;


    // --- Vinculación con la Tabla y sus Columnas ---
    @FXML private TableView<DatoGenerado> tableView;
    @FXML private TableColumn<DatoGenerado, Integer> tc_n;
    @FXML private TableColumn<DatoGenerado, String> tc_yn;
    @FXML private TableColumn<DatoGenerado, String> tc_xn;
    @FXML private TableColumn<DatoGenerado, String> tc_rn;

    // --- Vinculación con los Campos de Texto y Botón ---
    @FXML private TextField tf_x0;
    @FXML private TextField tf_a;
    @FXML private TextField tf_b;
    @FXML private TextField tf_c;
    @FXML private TextField tf_d;
    @FXML private TextField tf_m;
    @FXML private TextField tf_n;

    @FXML private Label lbl_intervalo;
    @FXML private Label lbl_frecuencia;
    @FXML private Label lbl_estadistico;

    @FXML private Button btn_generar;

    // Variable para saber qué generador está seleccionado
    private String generadorSeleccionado = "";
    // Lista para guardar los numeros generados
    //private List<Double> numerosGenerados;

    @FXML
    public void initialize() {
        tc_n.setCellValueFactory(new PropertyValueFactory<>("n"));
        tc_yn.setCellValueFactory(new PropertyValueFactory<>("yn"));
        tc_xn.setCellValueFactory(new PropertyValueFactory<>("xn"));
        tc_rn.setCellValueFactory(new PropertyValueFactory<>("rn"));

        // Asignar acciones a los elementos del menú
        mi_multiplicadorConstante.setOnAction(event -> prepararInterfaz("Multiplicador Constante"));
        mi_cuadradosMedios.setOnAction(event -> prepararInterfaz("Cuadrados Medios"));
        mi_productosMedios.setOnAction(event -> prepararInterfaz("Productos Medios"));
        mi_aditivo.setOnAction(event -> prepararInterfaz("Congruencial Aditivo"));
        mi_multiplicativo.setOnAction(event -> prepararInterfaz("Congruencial Multiplicativo"));
        mi_lineal.setOnAction(event -> prepararInterfaz("Congruencial Lineal"));
        mi_cuadratico.setOnAction(event -> prepararInterfaz("Congruencial Cuadratico"));
        mi_uniformidad.setOnAction(event -> prepararInterfaz("Uniformidad"));

        // Asignar la acción principal al botón "Generar"
        btn_generar.setOnAction(event -> generarNumeros());
    }

    // Preparar la interfaz
    private void prepararInterfaz(String generador) {
        generadorSeleccionado = generador;
        tableView.getItems().clear(); // Limpia la tabla de resultados anteriores

        // Deshabilita todos los campos
        tf_x0.setDisable(true);
        tf_a.setDisable(true);
        tf_b.setDisable(true);
        tf_c.setDisable(true);
        tf_d.setDisable(true);
        tf_m.setDisable(true);
        tf_n.setDisable(true);

        // habilita solo los necesarios para cada caso
        switch (generador) {
            case "Cuadrados Medios":
                tf_x0.setDisable(false);
                tf_n.setDisable(false);
                break;
            case "Multiplicador Constante":
                tf_x0.setDisable(false);
                tf_a.setDisable(false);
                tf_n.setDisable(false);
                break;
            case "Productos Medios":
                tf_x0.setDisable(false); // Semilla 1
                tf_a.setDisable(false);  // Semilla 2
                tf_n.setDisable(false);
                break;
            case "Congruencial Aditivo":
                tf_x0.setDisable(false); // Semilla 1
                tf_a.setDisable(false);  // Semilla 2
                tf_b.setDisable(false);  // Semilla 3
                tf_c.setDisable(false);  // Semilla 4
                tf_d.setDisable(false);  // Semilla 5
                tf_m.setDisable(false);  // Módulo
                tf_n.setDisable(false);
                break;
            case "Congruencial Multiplicativo":
                tf_x0.setDisable(false);
                tf_a.setDisable(false);
                tf_n.setDisable(false);
                tf_d.setDisable(false);
                break;
            case "Congruencial Lineal":
                tf_x0.setDisable(false); // Semilla (X₀)
                tf_a.setDisable(false);  // Constante multiplicativa (a)
                tf_c.setDisable(false);  // Constante aditiva (c)
                tf_m.setDisable(false);  // Módulo (m)
                tf_n.setDisable(false);
                break;
            case "Congruencial Cuadratico":
                tf_x0.setDisable(false);
                tf_a.setDisable(false);
                tf_b.setDisable(false);
                tf_c.setDisable(false);
                tf_d.setDisable(false);
                break;
            case "Uniformidad":
                lbl_intervalo.setText("k: ");
                lbl_frecuencia.setText("Frecuencia: ");
                lbl_estadistico.setText("Valor critico: ");
                break;

        }
    }

    private void generarNumeros() {
        try {
            switch (generadorSeleccionado) {
                case "Cuadrados Medios":
                    int x0_cm = Integer.parseInt(tf_x0.getText());
                    int n_cm = Integer.parseInt(tf_n.getText());
                    if (String.valueOf(x0_cm).length() != 4) {
                        mostrarAlerta("Error de Entrada", "La semilla (x0) debe tener 4 dígitos.");
                        return;
                    }
                    tableView.setItems(cuadradosMedios(x0_cm, n_cm));
                    break;

                case "Multiplicador Constante":
                    int x0_mc = Integer.parseInt(tf_x0.getText());
                    int a_mc = Integer.parseInt(tf_a.getText());
                    int n_mc = Integer.parseInt(tf_n.getText());
                    if (String.valueOf(x0_mc).length() != 4 || String.valueOf(a_mc).length() != 4) {
                        mostrarAlerta("Error de Entrada", "La semilla (x0) y la constante (a) deben tener 4 dígitos.");
                        return;
                    }
                    tableView.setItems(multiplicadorConstante(a_mc, x0_mc, n_mc));
                    break;

                // Dentro del switch (generadorSeleccionado)
                case "Productos Medios":
                    // Leer las dos semillas y el número de iteraciones
                    int semilla1_pm = Integer.parseInt(tf_x0.getText());
                    int semilla2_pm = Integer.parseInt(tf_a.getText());
                    int n_pm = Integer.parseInt(tf_n.getText());

                    if (String.valueOf(semilla1_pm).length() != 4 || String.valueOf(semilla2_pm).length() != 4) {
                        mostrarAlerta("Error de Entrada", "La semilla (x0) y la constante (a) deben tener 4 dígitos.");
                        return;
                    }

                    tableView.setItems(productosMedios(semilla1_pm, semilla2_pm,n_pm));
                    break;

                case "Congruencial Aditivo":
                    List<Integer> semillas = new ArrayList<>();
                    semillas.add(Integer.parseInt(tf_x0.getText()));
                    semillas.add(Integer.parseInt(tf_a.getText()));
                    semillas.add(Integer.parseInt(tf_b.getText()));
                    semillas.add(Integer.parseInt(tf_c.getText()));
                    semillas.add(Integer.parseInt(tf_d.getText()));
                    int m_ca = Integer.parseInt(tf_m.getText());
                    int n_ca = Integer.parseInt(tf_n.getText());

                    // Validación: n debe ser mayor o igual al número de semillas
                    if (n_ca < semillas.size()) {
                        mostrarAlerta("Error de Validación", "La cantidad de números a generar (n) no puede ser menor que la cantidad de semillas (" + semillas.size() + ").");
                        return;
                    }

                    tableView.setItems(congruencialAditivo(semillas, m_ca, n_ca +5));
                    break;

                case "Congruencial Multiplicativo":
                    if (tf_x0.getText().trim().isEmpty() || tf_a.getText().trim().isEmpty() ||
                            tf_n.getText().trim().isEmpty() || tf_d.getText().trim().isEmpty()) {
                        mostrarAlerta("Atencion", "Por favor, rellena todos los campos antes de continuar");
                        return;
                    }

                    int x0_cmm = Integer.parseInt(tf_x0.getText());
                    int n_cmm = Integer.parseInt(tf_n.getText());
                    int d_cmm = Integer.parseInt(tf_d.getText());
                    int t_cmm = Integer.parseInt(tf_a.getText());

                    int a_cmm = 8 * t_cmm - 3;
                    int m_cmm = (int) Math.pow(2, d_cmm);

                    // Verificar que a y m sean primos relativos
                    if (!sonPrimosRelativos(a_cmm, m_cmm))
                        mostrarAlerta("Error de Entrada", "a y m no son primos relativos");
                    tableView.setItems(congruencialMultiplicativo(a_cmm, n_cmm, x0_cmm, m_cmm));
                    break;

                case "Congruencial Lineal":
                    int x0_cl = Integer.parseInt(tf_x0.getText());
                    int a_cl = Integer.parseInt(tf_a.getText());
                    int c_cl = Integer.parseInt(tf_c.getText());
                    int m_cl = Integer.parseInt(tf_m.getText());
                    int n_cl = Integer.parseInt(tf_n.getText());

                    // Validación: c y m deben ser primos relativos
                    if (mcd(c_cl, m_cl) != 1) {
                        mostrarAlerta("Error de Validación", "La constante aditiva 'c' (" + c_cl + ") y el módulo 'm' (" + m_cl + ") deben ser primos relativos.");
                        return; // Detiene la ejecución si no cumplen
                    }

                    tableView.setItems(congruencialLineal(x0_cl, a_cl, c_cl, m_cl, n_cl));
                    break;

                case "Congruencial Cuadratico":
                    if (tf_x0.getText().trim().isEmpty() || tf_a.getText().trim().isEmpty() ||
                            tf_b.getText().trim().isEmpty() || tf_c.getText().trim().isEmpty() ||
                            tf_d.getText().trim().isEmpty()) {
                        mostrarAlerta("Atencion", "Por favor, rellena todos los campos antes de continuar");
                        return;
                    }

                    int x0_cc = Integer.parseInt(tf_x0.getText());
                    int a_cc = Integer.parseInt(tf_a.getText());
                    int b_cc = Integer.parseInt(tf_b.getText());
                    int c_cc = Integer.parseInt(tf_c.getText());
                    int d_cc = Integer.parseInt(tf_d.getText());
                    int m_cc = (int) Math.pow(2,10);
                    tf_m.setText(String.valueOf(m_cc));

                    tableView.setItems(congruencialCuadratico(x0_cc, a_cc, b_cc, c_cc,d_cc-1, m_cc));
                    break;
                case "Uniformidad":
                    lbl_intervalo.setText("k: ");
                    lbl_frecuencia.setText("Frecuencia: ");
                    lbl_estadistico.setText("Valor critico: ");
                    break;

                default:
                    mostrarAlerta("Advertencia", "Por favor, seleccione un método generador del menú.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Entrada", "Por favor, ingrese solo números válidos en los campos.");

        } catch (IllegalArgumentException e) {
            mostrarAlerta("Error de Validación", e.getMessage());
        }
    }

    // --- Cuadrados Medios ---
    public ObservableList<DatoGenerado> cuadradosMedios(int x0, int n) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();

        int yi = x0; // Asignamos la semilla a nuestra variable iterativa

        for (int i = 0; i < n; i++) {
            long yiCuadrado = (long) Math.pow(yi, 2);
            String yiCuadradoStr = String.format("%08d", yiCuadrado);
            int xi = Integer.parseInt(yiCuadradoStr.substring(2, 6));
            double ri = xi / 10000.0;

            datos.add(new DatoGenerado(i, yiCuadradoStr, String.valueOf(xi), formatear(ri)));
            yi = xi;
        }
        return datos;
    }


    // --- Metodo de multiplicador constante ---
    private ObservableList<DatoGenerado> multiplicadorConstante(int a, int x0, int n) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();
        int xi = x0;

        for (int i = 0; i < n; i++) {
            long producto = (long) a * xi;
            String productoStr = String.format("%08d", producto); // Asegura 8 dígitos rellenando con ceros

            int inicio = (productoStr.length() - 4) / 2;
            String digitosCentrales = productoStr.substring(inicio, inicio + 4);

            int siguiente = Integer.parseInt(digitosCentrales);
            double ri = (double) siguiente / 10000;

            datos.add(new DatoGenerado(i, productoStr, digitosCentrales, formatear(ri)));
            xi = siguiente;
        }
        return datos;
    }

    // --- Productos Medios ---
    private ObservableList<DatoGenerado> productosMedios(int x0, int x1, int n) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();
        int xi = x0;
        int xi1 = x1;

        for (int i = 0; i < n; i++) {
            long producto = (long) xi * xi1; // Multiplicar las dos semillas
            String productoStr = String.format("%08d", producto); // Asegurar 8 dígitos

            // Extraer los 4 dígitos centrales
            int inicio = (productoStr.length() - 4) / 2;
            String digitosCentrales = productoStr.substring(inicio, inicio + 4);

            int siguiente = Integer.parseInt(digitosCentrales);
            double ri = (double) siguiente / 10000;

            // Añadir el resultado a la lista (mostrando el producto de xi * xi+1)
            datos.add(new DatoGenerado(i, productoStr, digitosCentrales, formatear(ri)));

            // Actualizar las semillas para la siguiente iteración
            xi = xi1;
            xi1 = siguiente;
        }
        return datos;
    }

    // --- Congruencial aditivo
    public ObservableList<DatoGenerado> congruencialAditivo(List<Integer> semillas, int m, int n) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();
        List<Integer> secuenciaXi = new ArrayList<>(semillas);

        // Luego, generamos los números restantes a partir de las semillas
        for (int i = semillas.size(); i < n; i++) {
            // Se aplica la fórmula específica: X_i = (X_{i-1} + X_{i-5}) mod m
            int nuevoXi = (secuenciaXi.get(i - 1) + secuenciaXi.get(i - 5)) % m;
            secuenciaXi.add(nuevoXi); // Se añade el nuevo valor a la secuencia para cálculos futuros

            double ri = (double) nuevoXi / m;
            String operacion = String.format("(%d + %d) mod %d", secuenciaXi.get(i-5), secuenciaXi.get(i-1), m);
            datos.add(new DatoGenerado(i + 1, operacion, String.valueOf(nuevoXi), formatear(ri)));
        }

    return datos;
    }

    // --- Congruencial multiplicativo ---
    public ObservableList<DatoGenerado> congruencialMultiplicativo (int a, int n, int x0, int m) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();
        int xi = x0;

        for (int i = 1; i <= n; i++) {
            // Xi = (a * Xi-1) mod m
            xi = (a * xi) % m;
            double ri = (double) xi / m;

            datos.add(new DatoGenerado(i, "", String.valueOf(xi), formatear(ri)));
        }
        return datos;
    }

    // Congruencial Lineal
    public ObservableList<DatoGenerado> congruencialLineal(int x0, int a, int c, int m, int n) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();
        int xi = x0;

        for (int i = 1; i <= n; i++) {
            // Se calcula el siguiente valor de la secuencia
            int siguiente_xi = (a * xi + c) % m;

            // Se calcula el número pseudoaleatorio r_i con el valor actual de xi
            double ri = (double) xi / m;

            // Se añade el resultado a la lista (usando el valor xi de esta iteración)
            // Para la columna 'Yn' (operación) podemos mostrar la fórmula para claridad
            String operacion = String.format("(%d * %d + %d) mod %d", a, xi, c, m);
            datos.add(new DatoGenerado(i, "", String.valueOf(siguiente_xi), formatear(ri)));

            // Se actualiza xi para la siguiente iteración
            xi = siguiente_xi;
        }
    return datos;
    }


    // --- Metodo congruencial cuadratico ---
    public ObservableList<DatoGenerado> congruencialCuadratico(int x0, int a, int b, int c, int n, int m) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();

        int xi = x0; // Asignamos la semilla a nuestra variable iterativa

        for (int i = 0; i < n; i++) {
            long operacion = (long) a * xi * xi + (long) b * xi + c;
            int siguiente = (int) (operacion % m);
            double ri = (double) siguiente / m;

            //numerosGenerados.add(ri);
            datos.add(new DatoGenerado(i, String.valueOf(xi), String.valueOf(siguiente), formatear(ri)));

            xi = siguiente; // Actualizar xi para la siguiente iteración
        }
        return datos;
    }

    // --- Pruebas Estadisticas ---
    private void uniformidad(){

    }

    // --- Alertas ---
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // --- Metodos auxiliares ---
    public static boolean sonPrimosRelativos(int a, int b) {
        return mcd(a, b) == 1;
    }

    public static int mcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static String formatear(double numero) {
        return String.format("%.4f", numero);
    }

    public static String formatear(long numero) {
        return String.valueOf(numero);
    }

    public static String formatear(int numero) {
        return String.valueOf(numero);
    }
}