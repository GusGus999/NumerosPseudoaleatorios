package com.example.pseudoaleatorios; // Asegúrate que este sea tu paquete

import com.example.modelo.DatoGenerado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.example.metodos.ProductosMedios;
public class HelloController {
    // --- Vinculación con elementos del Menú ---
    @FXML private MenuItem mi_cuadradosMedios;
    @FXML private MenuItem mi_multiplicadorConstante;
    @FXML private MenuItem mi_productosMedios;
    @FXML private MenuItem mi_aditivo;
    @FXML private MenuItem mi_multiplicativo;
    @FXML private MenuItem mi_lineal;
    @FXML private MenuItem mi_cuadratico;
    @FXML private MenuItem mi_transformadaInversa;
    @FXML private MenuItem mi_convolucion;
    @FXML private MenuItem mi_composicion;

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
        mi_transformadaInversa.setOnAction(event -> prepararInterfaz("Transformada Inversa"));
        mi_convolucion.setOnAction(event -> prepararInterfaz("Convolucion"));
        mi_composicion.setOnAction(event -> prepararInterfaz("Composicion"));
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
                break;
            case "Congruencial Multiplicativo":
                tf_x0.setDisable(false);
                tf_a.setDisable(false);
                tf_n.setDisable(false);
                tf_d.setDisable(false);
                break;
            case "Congruencial Lineal":
                break;
            case "Congruencial Cuadratico":
                tf_x0.setDisable(false);
                tf_a.setDisable(false);
                tf_b.setDisable(false);
                tf_c.setDisable(false);
                tf_d.setDisable(false);
                break;
            case "Convolucion":
                tf_x0.setDisable(false); // Habilita campo para Media
                tf_x0.setPromptText("Media (μ)");
                tf_a.setDisable(false);  // Habilita campo para Desviación
                tf_a.setPromptText("Desviación (σ)");
                tf_n.setDisable(false);  // Habilita campo para N
                break;
            case "Transformada Inversa":
                tf_a.setDisable(false); // Habilita campo para Lambda
                tf_a.setPromptText("Lambda (λ)");
                tf_n.setDisable(false); // Habilita campo para N
                break;
            // ... dentro del switch (generador) ...

            case "Composicion":
                // Usaremos Distribución Triangular como ejemplo de Composición
                tf_a.setDisable(false);
                tf_a.setPromptText("Mínimo (a)");

                tf_b.setDisable(false);
                tf_b.setPromptText("Máximo (b)");

                tf_c.setDisable(false);
                tf_c.setPromptText("Moda (c)");

                tf_n.setDisable(false);
                break;
        }
    }

    private void generarNumeros() {
        try {
            switch (generadorSeleccionado) {
                case "Cuadrados Medios":
                    int x0_cm = Integer.parseInt(tf_x0.getText());
                    int n_cm = Integer.parseInt(tf_n.getText());
                    if (String.valueOf(x0_cm).length() != 4 || String.valueOf(n_cm).length() > 3) {
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
                    long semilla1_pm = Long.parseLong(tf_x0.getText());
                    long semilla2_pm = Long.parseLong(tf_a.getText());
                    int n_pm = Integer.parseInt(tf_n.getText());

                    break;

                case "Congruencial Multiplicativo":
                    int x0_cmm = Integer.parseInt(tf_x0.getText());
                    int a_cmm = Integer.parseInt(tf_a.getText());
                    int n_cmm = Integer.parseInt(tf_n.getText());
                    int d_cmm = Integer.parseInt(tf_d.getText());
                    int m_cmm = (int) Math.pow(2, d_cmm);

                    if (String.valueOf(x0_cmm).length() > 1 || String.valueOf(a_cmm).length() > 1 || String.valueOf(n_cmm).length() > 1 ||String.valueOf(d_cmm).length() > 1) {
                        mostrarAlerta("Error de Entrada", "La semilla (x0) y la constante (a) deben tener 4 dígitos.");
                        return;
                    }

                    // Verificar que a y m sean primos relativos
                    if (!sonPrimosRelativos(a_cmm, m_cmm)) {
                        mostrarAlerta("Error de Entrada", "a y m no son primos relativos");
                    }

                    tableView.setItems(congruencialMultiplicativo(a_cmm, n_cmm, x0_cmm, m_cmm));
                    break;

                case "Congruencial Cuadratico":
                    int x0_cc = Integer.parseInt(tf_x0.getText());
                    int a_cc = Integer.parseInt(tf_a.getText());
                    int b_cc = Integer.parseInt(tf_b.getText());
                    int c_cc = Integer.parseInt(tf_c.getText());
                    int d_cc = Integer.parseInt(tf_d.getText());
                    int m_cc = (int) Math.pow(2,10);
                    tf_m.setText(String.valueOf(m_cc));

                    if (String.valueOf(x0_cc).length() > 1 || String.valueOf(a_cc).length() > 1 || String.valueOf(b_cc).length() > 1 ||String.valueOf(c_cc).length() > 1 || String.valueOf(d_cc).length() > 1) {
                        mostrarAlerta("Error de Entrada", "La semilla (x0) y la constante (a) deben tener 4 dígitos.");
                        return;
                    }

                    tableView.setItems(congruencialCuadratico(x0_cc, a_cc, b_cc, c_cc,d_cc-1, m_cc));
                    break;
                case "Transformada Inversa":
                    // Usamos tf_a para Lambda y tf_n para la cantidad
                    double lambda = Double.parseDouble(tf_a.getText());
                    int n_ti = Integer.parseInt(tf_n.getText());

                    if (lambda <= 0) {
                        mostrarAlerta("Error", "Lambda debe ser mayor a 0");
                        return;
                    }
                    tableView.setItems(metodoTransformadaInversa(lambda, n_ti));
                    break;
                case "Convolucion":
                    // Usamos tf_x0 para la Media y tf_a para la Desviación Estándar
                    double media = Double.parseDouble(tf_x0.getText());
                    double desviacion = Double.parseDouble(tf_a.getText());
                    int n_conv = Integer.parseInt(tf_n.getText());

                    if (desviacion < 0) {
                        mostrarAlerta("Error", "La desviación estándar no puede ser negativa");
                        return;
                    }
                    tableView.setItems(metodoConvolucion(media, desviacion, n_conv));
                    break;
                // ... dentro del switch (generadorSeleccionado) ...

                case "Composicion":
                    // Leemos los 3 parámetros de la Triangular
                    double min_comp = Double.parseDouble(tf_a.getText());
                    double max_comp = Double.parseDouble(tf_b.getText());
                    double moda_comp = Double.parseDouble(tf_c.getText());
                    int n_comp = Integer.parseInt(tf_n.getText());

                    // Validaciones lógicas de un triángulo
                    if (min_comp >= max_comp) {
                        mostrarAlerta("Error", "El Mínimo (a) debe ser menor que el Máximo (b).");
                        return;
                    }
                    if (moda_comp < min_comp || moda_comp > max_comp) {
                        mostrarAlerta("Error", "La Moda (c) debe estar entre a y b.");
                        return;
                    }

                    tableView.setItems(metodoComposicion(min_comp, max_comp, moda_comp, n_comp));
                    break;
                default:
                    mostrarAlerta("Advertencia", "Por favor, seleccione un método generador del menú.");
            }
        } catch (NumberFormatException e) { // <-- ESTE ES EL CATCH QUE YA TENÍAS
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

            datos.add(new DatoGenerado(i, yiCuadradoStr, String.valueOf(xi), String.valueOf(ri)));
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

            datos.add(new DatoGenerado(i, productoStr, digitosCentrales, String.valueOf(ri)));
            xi = siguiente;
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

            datos.add(new DatoGenerado(i, "", String.valueOf(xi), String.valueOf(ri)));
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

            datos.add(new DatoGenerado(i, String.valueOf(xi), String.valueOf(siguiente), String.valueOf(ri)));

            xi = siguiente; // Actualizar xi para la siguiente iteración
        }
        return datos;
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
    // Nuevo metodo para generar variables aleatorias
    public ObservableList<DatoGenerado> generarExponencial(ObservableList<DatoGenerado> datosUniforme, double lambda) {
        ObservableList<DatoGenerado> datosExponencial = FXCollections.observableArrayList();

        for (DatoGenerado dato : datosUniforme) {
            // 1. Obtener el número pseudoaleatorio R_i
            double ri = Double.parseDouble(dato.getRn());

            // 2. Aplicar la Transformada Inversa
            double xi = (-1.0 / lambda) * Math.log(1.0 - ri);

            // 3. Crear un nuevo objeto DatoGenerado para mostrar el resultado
            // Reutilizamos los campos, por ejemplo, guardando el R_i en 'yn'
            // y el X_i (variable exponencial) en 'rn'
            datosExponencial.add(new DatoGenerado(
                    dato.getN(),
                    String.format("%.4f", ri), // R_i original (uniforme)
                    "", // Campo no usado
                    String.format("%.4f", xi) // X_i transformado (exponencial)
            ));
        }
        return datosExponencial;
    }

    // --- Método Transformada Inversa (Exponencial) ---
    public ObservableList<DatoGenerado> metodoTransformadaInversa(double lambda, int n) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();

        for (int i = 0; i < n; i++) {
            // 1. Generamos R (uniforme 0-1) usando la clase Math de Java
            double ri = Math.random();

            // 2. Aplicamos la fórmula de la inversa para exponencial
            // X = -1/lambda * ln(1 - R)
            double xi = (-1.0 / lambda) * Math.log(1.0 - ri);

            // Guardamos en la tabla:
            // n: índice
            // yn: vacío o fórmula
            // xn: El resultado final (Variable Aleatoria Exponencial)
            // rn: El número aleatorio uniforme usado (R)
            datos.add(new DatoGenerado(
                    i + 1,
                    "",
                    String.format("%.4f", xi), // Resultado final (X)
                    String.format("%.4f", ri)  // R generado
            ));
        }
        return datos;
    }

    // --- Método de Convolución (Normal) ---
    public ObservableList<DatoGenerado> metodoConvolucion(double media, double desviacion, int n) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();

        for (int i = 0; i < n; i++) {
            double suma = 0;

            // 1. Sumar 12 números aleatorios uniformes (0-1)
            for (int k = 0; k < 12; k++) {
                suma += Math.random();
            }

            // 2. Calcular Z (Normal Estándar) -> Z = Suma - 6
            double z = suma - 6.0;

            // 3. Ajustar a la Media y Desviación -> X = Media + Desv * Z
            double xi = media + (desviacion * z);

            // Guardamos en la tabla
            datos.add(new DatoGenerado(
                    i + 1,
                    String.format("Z: %.4f", z), // Mostramos Z como dato intermedio
                    String.format("%.4f", xi),   // Resultado final (X)
                    String.format("%.2f", suma)  // La suma de los 12
            ));
        }
        return datos;
    }
    // --- Método de Composición (Distribución Triangular) ---
    public ObservableList<DatoGenerado> metodoComposicion(double a, double b, double c, int n) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();

        for (int i = 0; i < n; i++) {
            // Generamos dos números uniformes
            double u1 = Math.random();
            double u2 = Math.random();

            // Calculamos la proporción del área izquierda (probabilidad de usar el lado izquierdo)
            // Si c == a (triángulo rectángulo izquierdo), area1 es 0.
            // Si c == b (triángulo rectángulo derecho), area1 es 1.
            double probIzquierda = (c - a) / (b - a);

            double xi;
            String formulaUsada; // Para saber qué parte del triángulo usamos

            if (u1 <= probIzquierda) {
                // Caso 1: Usamos la parte izquierda del triángulo (a -> c)
                // Fórmula: a + sqrt(u2) * (c - a)
                xi = a + Math.sqrt(u2) * (c - a);
                formulaUsada = "Izquierda (a->c)";
            } else {
                // Caso 2: Usamos la parte derecha del triángulo (c -> b)
                // Fórmula: b - sqrt(1 - u2) * (b - c)
                xi = b - Math.sqrt(1.0 - u2) * (b - c);
                formulaUsada = "Derecha (c->b)";
            }

            // Guardamos en la tabla
            // Yn: Mostramos qué lado del triángulo se eligió
            // Xn: Valor generado
            // Rn: Los randoms usados (u1, u2)
            datos.add(new DatoGenerado(
                    i + 1,
                    formulaUsada,
                    String.format("%.4f", xi),
                    String.format("u1:%.2f u2:%.2f", u1, u2)
            ));
        }
        return datos;
    }
}