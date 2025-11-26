package com.example.VariablesAleatorias;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.lang.Math.log;
import static java.lang.Math.exp;
import static java.lang.Math.pow;

public class MetodoConvolucion {

    public static void ejecutar() {

        int k = 0;
        double lambda = 0.0;
        int n = 0;

        // ============================
        // 1. PEDIR PARÁMETROS
        // ============================
        while (true) {
            try {
                String inputK = JOptionPane.showInputDialog(
                        "--- Método de Convolución (Erlang) ---\n\n" +
                                "1. Ingrese k (entero, número de convoluciones):"
                );
                if (inputK == null) { cancelar(); return; }
                k = Integer.parseInt(inputK);

                if (k <= 0) {
                    error("k debe ser un entero positivo.");
                    continue;
                }

                String inputLambda = JOptionPane.showInputDialog(
                        "2. Ingrese lambda (λ):"
                );
                if (inputLambda == null) { cancelar(); return; }
                lambda = Double.parseDouble(inputLambda);

                if (lambda <= 0) {
                    error("lambda debe ser > 0.");
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                error("Ingrese solo números válidos.");
            }
        }

        // ============================
        // 2. N
        // ============================
        String inputN = JOptionPane.showInputDialog("3. Ingrese N (cantidad de valores):");
        if (inputN == null) { cancelar(); return; }

        try {
            n = Integer.parseInt(inputN);
            if (n <= 0) { error("N debe ser un entero positivo."); return; }
        } catch (Exception e) {
            error("N inválido.");
            return;
        }

        // ============================
        // 3. GENERAR VALORES ERLANG
        // ============================
        List<Double> listaXi = new ArrayList<>();

        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Resultados del Método de Convolución (Erlang)</h2>");
        html.append(String.format("<b>X = suma de %d exponentiales (λ=%.3f)</b><br><br>", k, lambda));

        html.append("<table border='1'><tr>"
                + "<th>i</th>"
                + "<th>Xi (Erlang)</th>"
                + "<th>Detalle</th>"
                + "</tr>");

        for (int i = 1; i <= n; i++) {

            double suma = 0.0;
            StringBuilder detalle = new StringBuilder();

            for (int j = 1; j <= k; j++) {
                double u = CongruencialLineal.siguienteRi();
                double expVal = (-1.0 / lambda) * log(u);

                suma += expVal;
                detalle.append(String.format("Exp%d=%.4f ", j, expVal));
            }

            listaXi.add(suma);

            html.append(String.format(
                    "<tr><td>%d</td><td><b>%.5f</b></td><td>%s</td></tr>",
                    i, suma, detalle
            ));
        }

        html.append("</table></body></html>");

        JOptionPane.showMessageDialog(null, html.toString(),
                "Resultados - Convolución", JOptionPane.PLAIN_MESSAGE);

        // ============================
        // 4. MENÚ DE PRUEBAS
        // ============================
        String[] opciones = {"Chi-Cuadrada", "K-S", "Salir"};
        int sel;

        do {
            sel = JOptionPane.showOptionDialog(
                    null,
                    "Seleccione una prueba estadística:",
                    "Pruebas Estadísticas",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (sel == 0) realizarChiCuadrada(listaXi, k, lambda);
            if (sel == 1) realizarKS(listaXi, lambda);

        } while (sel != 2 && sel != -1);
    }

    // ======================================================================
    // ==========  CHI-CUADRADA   ===========================================
    // ======================================================================
    private static void realizarChiCuadrada(List<Double> datos, int k, double lambda) {

        int n = datos.size();
        int numInt = (int) Math.sqrt(n);
        if (numInt < 6) numInt = 6;

        double min = Collections.min(datos);
        double max = Collections.max(datos);
        double ancho = (max - min) / numInt;

        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Prueba Chi-Cuadrada (Erlang)</h2>");
        html.append(String.format("<b>H0:</b> Los datos siguen Erlang(k=%d, λ=%.3f)<br><br>", k, lambda));

        html.append("<table border='1' cellspacing='0' cellpadding='4'>");
        html.append("<tr><th>Intervalo</th><th>Rango</th><th>FO</th><th>FE</th><th>Chi Parcial</th></tr>");

        double chi = 0;

        for (int i = 0; i < numInt; i++) {
            double limInf = min + i * ancho;
            double limSup = min + (i + 1) * ancho;

            int fo = 0;
            for (double x : datos) {
                if (i == numInt - 1) {
                    if (x >= limInf && x <= limSup) fo++;
                } else {
                    if (x >= limInf && x < limSup) fo++;
                }
            }

            double prob = cdfErlang(limSup, k, lambda) - cdfErlang(limInf, k, lambda);
            double fe = prob * n;
            double chiParcial = (fe > 0) ? pow(fo - fe, 2) / fe : 0;

            chi += chiParcial;

            html.append(String.format(
                    "<tr><td>%d</td><td>[%.3f , %.3f]</td><td>%d</td><td>%.3f</td><td>%.4f</td></tr>",
                    (i + 1), limInf, limSup, fo, fe, chiParcial
            ));
        }

        html.append("</table><br>");

        double chiCrit = obtenerChiCritico(numInt - 1);

        html.append(String.format("<b>Chi Calculada:</b> %.4f<br>", chi));
        html.append(String.format("<b>Valor Crítico (α=0.05):</b> %.4f<br><br>", chiCrit));

        if (chi < chiCrit) {
            html.append("<b style='color:green;'>RESULTADO: NO SE RECHAZA H0 (Pasa)</b>");
        } else {
            html.append("<b style='color:red;'>RESULTADO: SE RECHAZA H0 (No pasa)</b>");
        }

        html.append("</body></html>");

        JOptionPane.showMessageDialog(null, html.toString(),
                "Prueba Chi-Cuadrada", JOptionPane.PLAIN_MESSAGE);
    }


    // ======================================================================
    // ==========  KOLMOGOROV–SMIRNOV  ======================================
    // ======================================================================
    private static void realizarKS(List<Double> datosOriginales, double lambda) {

        List<Double> datos = new ArrayList<>(datosOriginales);
        Collections.sort(datos);
        int n = datos.size();

        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Prueba Kolmogorov-Smirnov</h2>");
        html.append("<b>H0:</b> Los datos siguen Exponencial(λ)<br><br>");

        html.append("<table border='1' cellspacing='0' cellpadding='4'>");
        html.append("<tr><th>i</th><th>Xi</th><th>|Dif|</th></tr>");

        double maxDif = 0;

        for (int i = 0; i < n; i++) {
            double xi = datos.get(i);

            double fObs = (double) (i + 1) / n;
            double fEsp = 1 - Math.exp(-lambda * xi);
            double dif = Math.abs(fObs - fEsp);

            if (dif > maxDif) maxDif = dif;

            // Mostrar solo extremos como quieres
            if (i < 15 || i > n - 5) {
                html.append(String.format(
                        "<tr><td>%d</td><td>%.4f</td><td>%.4f</td></tr>",
                        (i + 1), xi, dif
                ));
            } else if (i == 15) {
                html.append("<tr><td colspan='3'>... datos ocultos ...</td></tr>");
            }
        }

        html.append("</table><br>");

        double valorCritico = 1.36 / Math.sqrt(n);

        html.append(String.format("<b>D Máx:</b> %.4f<br>", maxDif));
        html.append(String.format("<b>Valor Crítico (0.05):</b> %.4f<br><br>", valorCritico));

        if (maxDif < valorCritico) {
            html.append("<b style='color:green;'>RESULTADO: NO SE RECHAZA H0 (Pasa)</b>");
        } else {
            html.append("<b style='color:red;'>RESULTADO: SE RECHAZA H0 (No pasa)</b>");
        }

        html.append("</body></html>");

        JOptionPane.showMessageDialog(null, html.toString(),
                "Kolmogorov-Smirnov", JOptionPane.PLAIN_MESSAGE);
    }



    // ======================================================================
    // ==========  CDF ERLANG  ==============================================
    // ======================================================================
    // F(x) = 1 - exp(-λx) * Σ ( (λx)^i / i! )
    private static double cdfErlang(double x, int k, double lambda) {
        if (x < 0) return 0;

        double sum = 0;
        for (int i = 0; i < k; i++) {
            sum += pow(lambda * x, i) / factorial(i);
        }
        return 1 - exp(-lambda * x) * sum;
    }

    private static double factorial(int n) {
        double f = 1;
        for (int i = 2; i <= n; i++) f *= i;
        return f;
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

    private static void mostrar(String txt, String titulo) {
        JTextArea area = new JTextArea(txt);
        area.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(area), titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private static double obtenerChiCritico(int gl) {
        double[] tabla = {0, 3.84, 5.99, 7.81, 9.49, 11.07, 12.59, 14.07, 15.51};
        if (gl < tabla.length) return tabla[gl];
        return gl + 1.645 * Math.sqrt(2 * gl);
    }
}
