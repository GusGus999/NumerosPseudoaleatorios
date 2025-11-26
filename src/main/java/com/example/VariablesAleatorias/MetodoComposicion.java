package com.example.VariablesAleatorias;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.lang.Math.log;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.abs;

// NOTA IMPORTANTE: Se asume que existe la clase CongruencialLineal
// con el método estático public static double siguienteRi()
// que devuelve un número aleatorio uniforme U(0, 1).

public class MetodoComposicion {

    public static void ejecutar() {

        int n = 0;
        double P1 = 0.0, P2 = 0.0;
        double LAMBDA1 = 0.0; // Solo se usa si el componente 1 es Exp
        double LAMBDA2 = 0.0; // Solo se usa si el componente 2 es Exp
        String tipoComponente1 = "";
        String tipoComponente2 = "";

        // ============================
        // 0. SELECCIÓN DE DISTRIBUCIONES
        // ============================
        String[] opcionesComp = {"Exponencial", "Uniforme (0,1)"};

        // Componente 1
        int sel1 = JOptionPane.showOptionDialog(
                null, "--- Método de Composición ---\n\n" +
                        "1. Seleccione la distribución para P1:",
                "Distribución Componente 1", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opcionesComp, opcionesComp[0]);
        if (sel1 == -1) { cancelar(); return; }
        tipoComponente1 = (sel1 == 0) ? "Exp" : "Unif";

        // Componente 2
        int sel2 = JOptionPane.showOptionDialog(
                null, "2. Seleccione la distribución para P2:",
                "Distribución Componente 2", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opcionesComp, opcionesComp[0]);
        if (sel2 == -1) { cancelar(); return; }
        tipoComponente2 = (sel2 == 0) ? "Exp" : "Unif";

        // ============================
        // 1. PEDIR PARÁMETROS
        // ============================
        while (true) {
            try {
                // P1
                String input = JOptionPane.showInputDialog("3. Ingrese P1:");
                if (input == null) { cancelar(); return; }
                P1 = Double.parseDouble(input);

                // Lambda 1 (Si es Exponencial)
                if (tipoComponente1.equals("Exp")) {
                    input = JOptionPane.showInputDialog(String.format("4. Ingrese Lambda 1 (Para P1 = %.2f):", P1));
                    if (input == null) { cancelar(); return; }
                    LAMBDA1 = Double.parseDouble(input);
                    if (LAMBDA1 <= 0) { error("Lambda debe ser > 0"); continue; }
                } else {
                    LAMBDA1 = 1.0; // Marcador, no usado
                }

                // Lambda 2 (Si es Exponencial)
                if (tipoComponente2.equals("Exp")) {
                    input = JOptionPane.showInputDialog("5. Ingrese Lambda 2 (Para P2):");
                    if (input == null) { cancelar(); return; }
                    LAMBDA2 = Double.parseDouble(input);
                    if (LAMBDA2 <= 0) { error("Lambda debe ser > 0"); continue; }
                } else {
                    LAMBDA2 = 1.0; // Marcador, no usado
                }

                // P2
                input = JOptionPane.showInputDialog("6. Ingrese P2 (P1 + P2 debe ser 1):");
                if (input == null) { cancelar(); return; }
                P2 = Double.parseDouble(input);

                // Validar suma
                if (abs(P1 + P2 - 1.0) > 1e-6) {
                    error(String.format("La suma es %.2f. Debe ser 1.0", P1 + P2));
                    continue;
                }
                break;

            } catch (NumberFormatException e) {
                error("Ingrese solo números válidos.");
            }
        }

        // ============================
        // 2. PEDIR N
        // ============================
        String inputN = JOptionPane.showInputDialog("7. Ingrese N (cantidad de valores):");
        if (inputN == null) { cancelar(); return; }

        try {
            n = Integer.parseInt(inputN);
        } catch (Exception e) {
            error("N inválido.");
            return;
        }

        // ============================
        // 3. GENERAR VALORES (COMPOSICIÓN)
        // ============================
        List<Double> listaXi = new ArrayList<>();

        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Resultados del Método de Composición</h2>");
        String desc1 = tipoComponente1.equals("Exp") ? String.format("Exp(%.2f)", LAMBDA1) : "Unif(0,1)";
        String desc2 = tipoComponente2.equals("Exp") ? String.format("Exp(%.2f)", LAMBDA2) : "Unif(0,1)";
        html.append(String.format("<b>f(x) = %.2f·%s + %.2f·%s</b><br><br>", P1, desc1, P2, desc2));

        html.append("<table border='1'><tr>"
                + "<th>i</th>"
                + "<th>R1 (Sel)</th>"
                + "<th>R2 (Trans)</th>"
                + "<th>Xi Generado</th>"
                + "<th>Origen</th>"
                + "</tr>");

        for (int i = 1; i <= n; i++) {

            // Se asume que CongruencialLineal.siguienteRi() existe y provee U(0, 1)
            double u = CongruencialLineal.siguienteRi(); // Selección (R1)
            double v = CongruencialLineal.siguienteRi(); // Transformación (R2)

            double x = 0.0;
            String origen = "";

            // Lógica de selección
            if (u <= P1) {
                // Componente 1
                if (tipoComponente1.equals("Exp")) {
                    // Exponencial: X = (-1/λ) * ln(V)
                    origen = "Exp 1 (λ=" + String.format("%.2f", LAMBDA1) + ")";
                    x = (-1.0 / LAMBDA1) * log(v);
                } else {
                    // Uniforme U(0, 1): X = V
                    origen = "Uniforme U(0,1) 1";
                    x = v;
                }
            } else {
                // Componente 2
                if (tipoComponente2.equals("Exp")) {
                    // Exponencial: X = (-1/λ) * ln(V)
                    origen = "Exp 2 (λ=" + String.format("%.2f", LAMBDA2) + ")";
                    x = (-1.0 / LAMBDA2) * log(v);
                } else {
                    // Uniforme U(0, 1): X = V
                    origen = "Uniforme U(0,1) 2";
                    x = v;
                }
            }

            listaXi.add(x);

            html.append(String.format(
                    "<tr><td>%d</td><td>%.4f</td><td>%.4f</td><td><b>%.5f</b></td><td>%s</td></tr>",
                    i, u, v, x, origen
            ));
        }

        html.append("</table></body></html>");

        // Usamos mostrarHTML
        mostrarHTML(html.toString(), "Resultados - Composición");

        // ============================
        // 4. MENÚ DE PRUEBAS
        // ============================
        String[] opciones = {"Chi-Cuadrada", "K-S", "Salir"};
        int sel;

        do {
            sel = JOptionPane.showOptionDialog(
                    null,
                    "Seleccione una prueba estadística para los datos generados:",
                    "Pruebas Estadísticas (Composición)",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            // Se pasan los tipos de distribución a las pruebas
            if (sel == 0) realizarChiCuadrada(listaXi, P1, P2, LAMBDA1, LAMBDA2, tipoComponente1, tipoComponente2);
            if (sel == 1) realizarKS(listaXi, P1, P2, LAMBDA1, LAMBDA2, tipoComponente1, tipoComponente2);

        } while (sel != 2 && sel != -1);
    }

    // ======================================================================
    // ==========  CHI-CUADRADA (Corregida con Interfaz de Tabla) ==========
    // ======================================================================
    private static void realizarChiCuadrada(List<Double> datos, double p1, double p2, double l1, double l2,
                                            String tipoC1, String tipoC2) {

        int n = datos.size();
        // Regla de Sturges para numInt, mínimo 5
        int numInt = (int) Math.max(5, Math.ceil(1 + 3.322 * Math.log10(n)));

        double min = Collections.min(datos);
        double max = Collections.max(datos);

        // Ajuste de límites para Uniforme(0,1)
        if (tipoC1.equals("Unif") || tipoC2.equals("Unif")) {
            if (min > 0.0) min = 0.0;
            if (max < 1.0) max = 1.0;
        }

        if (abs(max - min) < 1e-6) {
            error("No es posible realizar Chi-Cuadrada: Rango de datos nulo.");
            return;
        }

        double ancho = (max - min) / numInt;

        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Prueba Chi-Cuadrada (Método de Composición)</h2>");
        html.append("<b>H0:</b> Los datos siguen la distribución de mezcla especificada.<br><br>");

        html.append("<table border='1'><tr>"
                + "<th>i</th>"
                + "<th>Rango [Li, Ls]</th>"
                + "<th>FO (Observada)</th>"
                + "<th>FE (Esperada)</th>"
                + "<th>(FO - FE)² / FE</th>"
                + "</tr>");

        double chi = 0;

        for (int i = 0; i < numInt; i++) {
            double limInf = min + i * ancho;
            double limSup = min + (i + 1) * ancho;

            // 1. Contar Observados (FO)
            int fo = 0;
            for (double x : datos) {
                if (i == numInt - 1) {
                    if (x >= limInf && x <= limSup) fo++;
                } else {
                    if (x >= limInf && x < limSup) fo++;
                }
            }

            // 2. Calcular Esperados (FE)
            double prob = cdfComposicion(limSup, p1, p2, l1, l2, tipoC1, tipoC2) -
                    cdfComposicion(limInf, p1, p2, l1, l2, tipoC1, tipoC2);
            double fe = prob * n;

            // 3. Cálculo de Chi Parcial
            double chiParcial = 0;
            if (fe > 0) {
                chiParcial = pow(fo - fe, 2) / fe;
                chi += chiParcial;
            } else if (fo > 0) {
                chiParcial = 9999.0; // Valor grande para indicar error
            }

            html.append(String.format(
                    "<tr><td>%d</td><td>[%.4f - %.4f]</td><td>%d</td><td>%.4f</td><td>%.4f</td></tr>",
                    (i + 1), limInf, limSup, fo, fe, chiParcial
            ));
        }

        int gradosLibertad = numInt - 1;
        if (gradosLibertad < 1) gradosLibertad = 1;

        double chiCrit = obtenerChiCritico(gradosLibertad);

        // Resumen de Resultados
        html.append("</table>");
        html.append("<hr>");
        html.append(String.format("<b>Chi Calculada:</b> %.4f<br>", chi));
        html.append(String.format("<b>Grados de Libertad (gl):</b> %d<br>", gradosLibertad));
        html.append(String.format("<b>Valor Crítico (α=0.05):</b> %.4f<br><br>", chiCrit));

        html.append("<b>RESULTADO:</b> ");

        if (chi < chiCrit) {
            html.append("<b><font color='green'>NO se rechaza H0.</font></b> (Los datos se ajustan a la distribución)");
        } else {
            html.append("<b><font color='red'>SE rechaza H0.</font></b> (Los datos NO se ajustan a la distribución)");
        }

        html.append("</body></html>");

        mostrarHTML(html.toString(), "Prueba Chi-Cuadrada (Composición)");
    }

    // ======================================================================
    // ==========  KOLMOGOROV–SMIRNOV (Adaptada a Composición) ==============
    // ======================================================================
    private static void realizarKS(List<Double> datosOriginal, double p1, double p2, double l1, double l2,
                                   String tipoC1, String tipoC2) {

        List<Double> datos = new ArrayList<>(datosOriginal);
        Collections.sort(datos);

        int n = datos.size();

        StringBuilder sb = new StringBuilder();
        sb.append("--- PRUEBA K-S (Composición) ---\n");
        sb.append("H0: Los datos siguen la distribución especificada.\n\n");

        double dMax = 0;

        for (int i = 0; i < n; i++) {
            double x = datos.get(i);

            // Probabilidad acumulada observada y esperada
            double fObs = (double) (i + 1) / n;
            double fEsp = cdfComposicion(x, p1, p2, l1, l2, tipoC1, tipoC2);

            double d = Math.abs(fObs - fEsp);
            if (d > dMax) dMax = d;

            // Mostrar solo primeros y últimos datos para no saturar
            if (i < 10 || i > n - 5) {
                sb.append(String.format("%d  Xi=%.4f  Fobs=%.4f  Fesp=%.4f  Dif=%.4f\n",
                        i + 1, x, fObs, fEsp, d));
            } else if (i == 10) {
                sb.append("... (datos intermedios ocultos) ...\n");
            }
        }

        // Valor crítico para α=0.05 y n>35
        double dCrit = 1.36 / Math.sqrt(n);

        sb.append("\nDmax calculado: ").append(String.format("%.5f", dMax));
        sb.append("\nD crítico (α=0.05): ").append(String.format("%.5f", dCrit));
        sb.append("\n\nRESULTADO: ");

        if (dMax < dCrit) sb.append("NO se rechaza H0 (Pasa)");
        else sb.append("SE rechaza H0 (No pasa)");

        mostrar(sb.toString(), "K-S (Composición)");
    }

    // ======================================================================
    // ==========  CDF COMPOSICIÓN (Adaptada)  ==============================
    // ======================================================================
    private static double cdfComposicion(double x, double p1, double p2, double l1, double l2,
                                         String tipoC1, String tipoC2) {
        if (x < 0) return 0;

        double F1 = calcularCDFComponente(x, l1, tipoC1);
        double F2 = calcularCDFComponente(x, l2, tipoC2);

        return (p1 * F1) + (p2 * F2);
    }

    private static double calcularCDFComponente(double x, double lambda, String tipo) {
        if (tipo.equals("Exp")) {
            // CDF Exponencial: F(x) = 1 - e^(-λx)
            return 1 - exp(-lambda * x);
        } else {
            // CDF Uniforme U(0, 1): F(x) = x para 0 <= x < 1
            if (x < 0) return 0;
            if (x >= 1) return 1.0;
            return x;
        }
    }


    // ======================================================================
    // UTILIDADES
    // ======================================================================
    private static void error(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private static void cancelar() {
        JOptionPane.showMessageDialog(null, "Operación cancelada.");
    }

    // Muestra texto plano (usado para K-S)
    private static void mostrar(String txt, String titulo) {
        JTextArea area = new JTextArea(txt);
        area.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(area), titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    // Muestra HTML (usado para Chi-Cuadrada y Generación)
    private static void mostrarHTML(String html, String titulo) {
        JOptionPane.showMessageDialog(null, html, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private static double obtenerChiCritico(int gl) {
        // Tabla para un α=0.05
        double[] tabla = {0, 3.84, 5.99, 7.81, 9.49, 11.07, 12.59, 14.07, 15.51};
        if (gl < tabla.length) return tabla[gl];
        // Aproximación de Wilson-Hilferty
        return gl * pow(1 - (2.0 / (9 * gl)) + (1.645 * Math.sqrt(2.0 / (9 * gl))), 3);
    }
}