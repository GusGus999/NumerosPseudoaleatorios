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
        }
    }

    private void generarNumeros() {
        try {
            switch (generadorSeleccionado) {
                case "Cuadrados Medios":
                    int x0_cm = Integer.parseInt(tf_x0.getText());
                    int n_cm = Integer.parseInt(tf_n.getText());
                    if (String.valueOf(x0_cm).length() != 4 || String.valueOf(n_cm).length() > 1) {
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

                    // Instanciar la clase con la lógica del método
                    ProductosMedios generadorPM = new ProductosMedios();

                    // Generar los resultados. Esto puede lanzar una IllegalArgumentException
                    ObservableList<ProductosMedios.ResultadoProductosMedios> resultadosPM = generadorPM.generar(semilla1_pm, semilla2_pm, n_pm);

                    // Convertir los resultados al formato genérico que usa la tabla (DatoGenerado)
                    ObservableList<DatoGenerado> datosParaTabla = FXCollections.observableArrayList();
                    for (ProductosMedios.ResultadoProductosMedios res : resultadosPM) {
                        datosParaTabla.add(new DatoGenerado(
                                res.iteracion(),
                                res.producto(),
                                String.valueOf(res.xn()),
                                res.rn()
                        ));
                    }

                    // Mostrar los datos en la tabla
                    tableView.setItems(datosParaTabla);
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

                case "Congruencial Aditivo":
                    String semillasTexto = tf_x0.getText();  // ejemplo: "65,89,98,3,69"
                    int m_ca = Integer.parseInt(tf_m.getText());
                    int n_ca = Integer.parseInt(tf_n.getText());
                    int cantidadTotal = 15; // cantidad fija de números a generar

                    // Validar datos
                    if (!validarCongruencialAditivo(semillasTexto, tf_m.getText(), tf_n.getText())) {
                        return;
                    }

                    // Generar y mostrar resultados
                    tableView.setItems(congruencialAditivo(semillasTexto, m_ca, n_ca, cantidadTotal));
                    break;

                case "Congruencial Lineal":
                    try {
                        int a_cl = Integer.parseInt(tf_a.getText());
                        int c_cl = Integer.parseInt(tf_c.getText());
                        int m_cl = Integer.parseInt(tf_m.getText());
                        int x0_cl = Integer.parseInt(tf_x0.getText());
                        int n_cl = Integer.parseInt(tf_n.getText());

                        // --- Validaciones de entrada ---
                        if (a_cl <= 0 || c_cl < 0 || m_cl <= 0 || x0_cl < 0 || n_cl <= 0) {
                            mostrarAlerta("Error de Entrada", "Todos los valores deben ser positivos y mayores que cero.");
                            return;
                        }

                        // --- Validaciones teóricas ---
                        if (!sonPrimosRelativos(c_cl, m_cl)) {
                            mostrarAlerta("Error de Validación", "c y m no son primos relativos.");
                            return;
                        }

                        if (!aMenosUnoMultiploFactoresPrimosDeM(a_cl, m_cl)) {
                            mostrarAlerta("Error de Validación", "a - 1 no es múltiplo de todos los factores primos de m.");
                            return;
                        }

                        if (m_cl % 4 == 0 && (a_cl - 1) % 4 != 0) {
                            mostrarAlerta("Error de Validación", "Si m es múltiplo de 4, (a - 1) debe ser divisible entre 4.");
                            return;
                        }

                        // --- Generar los números pseudoaleatorios ---
                        tableView.setItems(congruencialLineal(a_cl, c_cl, m_cl, x0_cl, n_cl));

                    } catch (NumberFormatException e) {
                        mostrarAlerta("Error de Entrada", "Por favor ingrese solo números válidos en los campos.");
                    }
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

            datos.add(new DatoGenerado(i, yiCuadradoStr, String.valueOf(xi), ri));
            yi = xi;
        }
        return datos;
    }


    // --- Método Congruencial Lineal ---
    private ObservableList<DatoGenerado> congruencialLineal(int a, int c, int m, int x0, int n) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();

        int xi = x0;
        for (int i = 0; i < n; i++) {
            double ri = (double) xi / m;
            datos.add(new DatoGenerado(i, String.valueOf(xi), "", ri)); // No hay producto intermedio como en otros métodos
            xi = (a * xi + c) % m;
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

            datos.add(new DatoGenerado(i, productoStr, digitosCentrales, ri));
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

            datos.add(new DatoGenerado(i, "", String.valueOf(xi), ri));
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

            datos.add(new DatoGenerado(i, String.valueOf(xi), String.valueOf(siguiente), ri));

            xi = siguiente; // Actualizar xi para la siguiente iteración
        }
        return datos;
    }

    // --- VALIDACIÓN DEL MÉTODO CONGRUENCIAL ADITIVO ---
    private boolean validarCongruencialAditivo(String semillasTexto, String tf_m, String tf_n) {
        try {
            // Validar m
            int m = Integer.parseInt(tf_m);
            if (m <= 0) {
                mostrarAlerta("Error de Entrada", "El valor de m debe ser mayor que 0.");
                return false;
            }

            // Validar n
            int n = Integer.parseInt(tf_n);
            if (n <= 1) {
                mostrarAlerta("Error de Entrada", "Debe haber al menos 2 semillas iniciales.");
                return false;
            }

            // Validar semillas (esperadas separadas por comas)
            String[] partes = semillasTexto.split(",");
            if (partes.length < n) {
                mostrarAlerta("Error de Entrada", "Debe ingresar al menos " + n + " semillas separadas por comas.");
                return false;
            }

            for (String s : partes) {
                int valor = Integer.parseInt(s.trim());
                if (valor < 0 || valor >= m) {
                    mostrarAlerta("Error de Entrada", "Cada semilla debe estar entre 0 y " + (m - 1));
                    return false;
                }
            }

            return true; // ✅ todo bien

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de Entrada", "Ingrese solo números válidos para m, n y las semillas.");
            return false;
        }
    }

    // --- MÉTODO CONGRUENCIAL ADITIVO ---
    private ObservableList<DatoGenerado> congruencialAditivo(String semillasTexto, int m, int n, int cantidadTotal) {
        ObservableList<DatoGenerado> datos = FXCollections.observableArrayList();

        // Convertir las semillas desde el texto (ej: "65,89,98,3,69")
        String[] partes = semillasTexto.split(",");
        int[] xi = new int[cantidadTotal];

        // Cargar las semillas iniciales
        for (int i = 0; i < n; i++) {
            xi[i] = Integer.parseInt(partes[i].trim());
        }

        int totalGenerados = cantidadTotal - n;

        // Generar nuevos xi
        for (int i = n, iter = 0; i < cantidadTotal; i++, iter++) {
            int nuevoXi = (xi[i - n] + xi[i - 1]) % m;
            xi[i] = nuevoXi;
            double ri = (double) nuevoXi / (m - 1);

            // Guardar en la tabla (iter empieza en 0 para x₆)
            datos.add(new DatoGenerado(iter + 1, (xi[i - n] + " + " + xi[i - 1] + " mod " + m), String.valueOf(nuevoXi), ri));
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

    private boolean aMenosUnoMultiploFactoresPrimosDeM(int a, int m) {
        int[] primos = factoresPrimos(m);
        for (int p : primos) {
            if ((a - 1) % p != 0) {
                return false;
            }
        }
        return true;
    }

    private int[] factoresPrimos(int num) {
        java.util.Set<Integer> factores = new java.util.HashSet<>();
        int n = num;
        for (int i = 2; i <= n / i; i++) {
            while (n % i == 0) {
                factores.add(i);
                n /= i;
            }
        }
        if (n > 1) {
            factores.add(n);
        }
        int[] res = new int[factores.size()];
        int idx = 0;
        for (int f : factores) {
            res[idx++] = f;
        }
        return res;
    }


}