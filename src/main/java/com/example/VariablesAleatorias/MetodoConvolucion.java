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
                                "1. Ingrese k (entero, número de convoluciones, k ≥ 1):"
                );
                if (inputK == null) { cancelar(); return; }
                k = Integer.parseInt(inputK);

                if (k <= 0) {
                    error("k debe ser un entero positivo.");
                    continue;
                }

                String inputLambda = JOptionPane.showInputDialog(
                        "2. Ingrese lambda (λ > 0):"
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
        html.append(String.format("<b>Distribución Erlang:</b> k = %d, λ = %.3f<br><br>", k, lambda));

        html.append("<table border='1'><tr>"
                + "<th>i</th>"
                + "<th>Xi (Erlang)</th>"
                + "<th>Detalle (Suma de Exp)</th>"
                + "</tr>");

        // Fórmulas simplificada para Erlang: X = (-1/λ) * log(Producto(Ri))
        for (int i = 1; i <= n; i++) {

            double productoRi = 1.0;
            StringBuilder detalle = new StringBuilder();

            // Generación por producto (equivale a la suma de k logaritmos)
            for (int j = 1; j <= k; j++) {
                double u = CongruencialLineal.siguienteRi();
                productoRi *= u;
                // Para mostrar el detalle de la suma individual (esto es más lento):
                detalle.append(String.format("Exp%d=%.4f; ", j, (-1.0 / lambda) * log(u)));
            }

            // Generación final eficiente
            double x = (-1.0 / lambda) * log(productoRi);

            listaXi.add(x);

            html.append(String.format(
                    "<tr><td>%d</td><td><b>%.5f</b></td><td>%s</td></tr>",
                    i, x, detalle.toString()
            ));
        }

        html.append("</table></body></html>");

        mostrarHTML(html.toString(), "Resultados - Convolución (Erlang)");

        // ============================
        // 4. MENÚ DE PRUEBAS
        // ============================
        String[] opciones = {"Chi-Cuadrada", "K-S", "Salir"};
        int sel;

        do {
            sel = JOptionPane.showOptionDialog(
                    null,
                    "Seleccione una prueba estadística para los datos generados:",
                    "Pruebas Estadísticas",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            // Pasamos k y lambda a las pruebas
            if (sel == 0) realizarChiCuadrada(listaXi, k, lambda);
            if (sel == 1) realizarKS(listaXi, k, lambda);

        } while (sel != 2 && sel != -1);
    }

    // ------------------------------------------------------------------

    // ======================================================================
    // ==========  CHI-CUADRADA (Corregida con CDF Erlang y HTML)  ==========
    // ======================================================================
    private static void realizarChiCuadrada(List<Double> datos, int k, double lambda) {

        int n = datos.size();
        // Regla de Sturges (o similar) para numInt, mínimo 5
        int numInt = (int) Math.max(5, Math.ceil(1 + 3.322 * Math.log10(n)));

        double min = Collections.min(datos);
        double max = Collections.max(datos);

        // Si el rango es cero (p. ej., n es muy pequeño), evitar división por cero
        if (abs(max - min) < 1e-6) {
            error("No es posible realizar Chi-Cuadrada: Rango de datos nulo.");
            return;
        }

        double ancho = (max - min) / numInt;

        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Prueba Chi-Cuadrada (Convolución - Erlang)</h2>");
        html.append(String.format("<b>H0:</b> Los datos siguen Erlang(k=%d, λ=%.3f)<br><br>", k, lambda));

        html.append("<table border='1'><tr>"
                + "<th>i</th>"
                + "<th>Rango [Li, Ls]</th>"
                + "<th>FO (Observada)</th>"
                + "<th>FE (Esperada)</th>"
                + "<th>(FO - FE)² / FE</th>"
                + "</tr>");

        double chiCalculada = 0;

        for (int i = 0; i < numInt; i++) {
            double limInf = min + i * ancho;
            double limSup = min + (i + 1) * ancho;

            // 1. Frecuencia Observada (FO)
            int fo = 0;
            for (double x : datos) {
                if (i == numInt - 1) { // Último intervalo incluye límite superior
                    if (x >= limInf && x <= limSup) fo++;
                } else {
                    if (x >= limInf && x < limSup) fo++;
                }
            }

            // 2. Frecuencia Esperada (FE) usando CDF de ERLANG
            double prob = cdfErlang(limSup, k, lambda) - cdfErlang(limInf, k, lambda);
            double fe = prob * n;

            // 3. Cálculo de Chi Parcial
            double chiParcial = 0;
            if (fe > 0) {
                chiParcial = pow(fo - fe, 2) / fe;
                chiCalculada += chiParcial;
            } else if (fo > 0) {
                chiParcial = 9999.0; // Valor grande para indicar una gran desviación
            }

            html.append(String.format(
                    "<tr><td>%d</td><td>[%.4f - %.4f]</td><td>%d</td><td>%.4f</td><td>%.4f</td></tr>",
                    (i + 1), limInf, limSup, fo, fe, chiParcial
            ));
        }

        // Grados de libertad (gl = numInt - 1 - m, donde m=2 si se estiman k y lambda, m=0 si son dados)
        // Usaremos gl = numInt - 1 si los parámetros son dados.
        int gradosLibertad = numInt - 1;
        if (gradosLibertad < 1) gradosLibertad = 1;

        double chiCritico = obtenerChiCritico(gradosLibertad);

        // Resumen de Resultados
        html.append("</table>");
        html.append("<hr>");
        html.append(String.format("<b>Chi Calculada:</b> %.4f<br>", chiCalculada));
        html.append(String.format("<b>Grados de Libertad (gl):</b> %d<br>", gradosLibertad));
        html.append(String.format("<b>Valor Crítico (α=0.05):</b> %.4f<br><br>", chiCritico));

        html.append("<b>RESULTADO:</b> ");

        if (chiCalculada < chiCritico) {
            html.append("<b><font color='green'>NO se rechaza H0.</font></b> (Los datos se ajustan a la distribución Erlang)");
        } else {
            html.append("<b><font color='red'>SE rechaza H0.</font></b> (Los datos NO se ajustan a la distribución Erlang)");
        }

        html.append("</body></html>");

        mostrarHTML(html.toString(), "Prueba Chi-Cuadrada (Erlang)");
    }


    // ------------------------------------------------------------------

    // ======================================================================
    // ==========  KOLMOGOROV–SMIRNOV (Adaptada a Erlang)  ==================
    // ======================================================================
    private static void realizarKS(List<Double> datosOriginales, int k, double lambda) {

        List<Double> datos = new ArrayList<>(datosOriginales);
        Collections.sort(datos);
        int n = datos.size();

        StringBuilder html = new StringBuilder();
        html.append("<html><body>");
        html.append("<h2>Prueba Kolmogorov-Smirnov (Erlang)</h2>");
        html.append(String.format("<b>H0:</b> Los datos siguen Erlang(k=%d, λ=%.3f)<br><br>", k, lambda));

        html.append("<table border='1' cellspacing='0' cellpadding='4'>");
        html.append("<tr><th>i</th><th>Xi</th><th>Fobs</th><th>Fesp</th><th>|Diferencia|</th></tr>");

        double maxDif = 0;

        for (int i = 0; i < n; i++) {
            double xi = datos.get(i);

            double fObs = (double) (i + 1) / n;
            double fEsp = cdfErlang(xi, k, lambda); // Usamos la CDF de Erlang
            double dif = Math.abs(fObs - fEsp);

            if (dif > maxDif) maxDif = dif;

            // Mostrar solo extremos
            if (i < 15 || i > n - 5) {
                html.append(String.format(
                        "<tr><td>%d</td><td>%.4f</td><td>%.4f</td><td>%.4f</td><td>%.4f</td></tr>",
                        (i + 1), xi, fObs, fEsp, dif
                ));
            } else if (i == 15) {
                html.append("<tr><td colspan='5'>... datos intermedios ocultos ...</td></tr>");
            }
        }

        html.append("</table><br>");

        // Valor crítico para α=0.05
        double valorCritico = 1.36 / Math.sqrt(n);

        html.append(String.format("<b>D Máx:</b> %.4f<br>", maxDif));
        html.append(String.format("<b>Valor Crítico (α=0.05):</b> %.4f<br><br>", valorCritico));

        if (maxDif < valorCritico) {
            html.append("<b style='color:green;'>RESULTADO: NO SE RECHAZA H0 (Pasa la prueba)</b>");
        } else {
            html.append("<b style='color:red;'>RESULTADO: SE RECHAZA H0 (No pasa la prueba)</b>");
        }

        html.append("</body></html>");

        mostrarHTML(html.toString(), "Kolmogorov-Smirnov (Erlang)");
    }


    // ------------------------------------------------------------------

    // ======================================================================
    // ==========  CDF ERLANG  ==============================================
    // ======================================================================
    // F(x; k, λ) = 1 - exp(-λx) * SUM ( (λx)^i / i! ) for i=0 to k-1
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

    // Muestra texto plano (usado por K-S que llama a mostrar, aunque K-S se ha pasado a HTML)
    private static void mostrar(String txt, String titulo) {
        JTextArea area = new JTextArea(txt);
        area.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(area), titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    // Muestra HTML (para resultados tabulados)
    private static void mostrarHTML(String html, String titulo) {
        JOptionPane.showMessageDialog(null, html, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private static double obtenerChiCritico(int gl) {
        double[] tabla = {0, 3.84, 5.99, 7.81, 9.49, 11.07, 12.59, 14.07, 15.51};
        if (gl < tabla.length) return tabla[gl];
        // Aproximación simplificada de Wilson-Hilferty para gl grandes (solo para fines de simulación)
        return gl + 1.645 * Math.sqrt(2 * gl);
    }
}