package com.example.VariablesAleatorias;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.lang.Math.log;
import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.abs;

public class MetodoComposicion {

    public static void ejecutar() {

        int n = 0;
        double P1 = 0.0, P2 = 0.0, LAMBDA1 = 0.0, LAMBDA2 = 0.0;

        // ============================
        // 1. PEDIR PARÁMETROS
        // ============================
        while (true) {
            try {
                // P1
                String input = JOptionPane.showInputDialog("--- Método de Composición ---\n\n1. Ingrese P1:");
                if (input == null) { cancelar(); return; }
                P1 = Double.parseDouble(input);

                // Lambda 1
                input = JOptionPane.showInputDialog("2. Ingrese Lambda 1:");
                if (input == null) { cancelar(); return; }
                LAMBDA1 = Double.parseDouble(input);
                if (LAMBDA1 <= 0) { error("Lambda debe ser > 0"); continue; }

                // Lambda 2
                input = JOptionPane.showInputDialog("3. Ingrese Lambda 2:");
                if (input == null) { cancelar(); return; }
                LAMBDA2 = Double.parseDouble(input);
                if (LAMBDA2 <= 0) { error("Lambda debe ser > 0"); continue; }

                // P2
                input = JOptionPane.showInputDialog("4. Ingrese P2 (P1 + P2 debe ser 1):");
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
        String inputN = JOptionPane.showInputDialog("5. Ingrese N (cantidad de valores):");
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
        html.append(String.format("<b>f(x) = %.2f·Exp(%.2f) + %.2f·Exp(%.2f)</b><br><br>", P1, LAMBDA1, P2, LAMBDA2));

        html.append("<table border='1'><tr>"
                + "<th>i</th>"
                + "<th>R1 (Sel)</th>"
                + "<th>R2 (Trans)</th>"
                + "<th>Xi Generado</th>"
                + "<th>Origen</th>"
                + "</tr>");

        for (int i = 1; i <= n; i++) {

            double u = CongruencialLineal.siguienteRi(); // Selección
            double v = CongruencialLineal.siguienteRi(); // Transformación

            double x = 0.0;
            String origen = "";
            double lambda_usada = 0.0;

            // Lógica de selección
            if (u <= P1) {
                lambda_usada = LAMBDA1;
                origen = "Exp 1";
            } else {
                lambda_usada = LAMBDA2;
                origen = "Exp 2";
            }

            // Transformada inversa
            x = (-1.0 / lambda_usada) * log(v);
            listaXi.add(x);

            html.append(String.format(
                    "<tr><td>%d</td><td>%.4f</td><td>%.4f</td><td><b>%.5f</b></td><td>%s</td></tr>",
                    i, u, v, x, origen
            ));
        }

        html.append("</table></body></html>");

        JOptionPane.showMessageDialog(null, html.toString(),
                "Resultados - Composición", JOptionPane.PLAIN_MESSAGE);

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

            if (sel == 0) realizarChiCuadrada(listaXi, P1, P2, LAMBDA1, LAMBDA2);
            if (sel == 1) realizarKS(listaXi, P1, P2, LAMBDA1, LAMBDA2);

        } while (sel != 2 && sel != -1);
    }

    // ======================================================================
    // ==========  CHI-CUADRADA (Adaptada a Composición) ====================
    // ======================================================================
    private static void realizarChiCuadrada(List<Double> datos, double p1, double p2, double l1, double l2) {

        int n = datos.size();
        int numInt = (int) Math.sqrt(n);
        if (numInt < 5) numInt = 5; // Mínimo 5 intervalos

        double min = Collections.min(datos);
        double max = Collections.max(datos);
        double ancho = (max - min) / numInt;

        StringBuilder sb = new StringBuilder();
        sb.append("--- PRUEBA CHI-CUADRADA (Composición) ---\n");
        sb.append("H0: Los datos siguen la distribución especificada.\n\n");
        sb.append(String.format("%-5s %-18s %-8s %-10s %-10s\n", "Int", "Rango", "FO", "FE", "Chi Parc"));

        double chi = 0;

        for (int i = 0; i < numInt; i++) {
            double limInf = min + i * ancho;
            double limSup = min + (i + 1) * ancho;

            // Contar Observados (FO)
            int fo = 0;
            for (double x : datos) {
                if (i == numInt - 1) { // Último intervalo incluye límite superior
                    if (x >= limInf && x <= limSup) fo++;
                } else {
                    if (x >= limInf && x < limSup) fo++;
                }
            }

            // Calcular Esperados (FE) usando la CDF de Composición
            double prob = cdfComposicion(limSup, p1, p2, l1, l2) - cdfComposicion(limInf, p1, p2, l1, l2);
            double fe = prob * n;

            double chiParcial = (fe > 0) ? pow(fo - fe, 2) / fe : 0;
            chi += chiParcial;

            sb.append(String.format("%-5d [%.4f - %.4f] %-8d %-10.4f %-10.4f\n",
                    (i + 1), limInf, limSup, fo, fe, chiParcial));
        }

        // Grados de libertad: Intervalos - 1 - parámetros estimados (aquí asumimos gl = k - 1)
        double chiCrit = obtenerChiCritico(numInt - 1);

        sb.append("\nChi calculada: ").append(String.format("%.4f", chi));
        sb.append("\nValor crítico (α=0.05, gl=").append(numInt - 1).append("): ").append(chiCrit);
        sb.append("\n\nRESULTADO: ");

        if (chi < chiCrit) sb.append("NO se rechaza H0 (Pasa)");
        else sb.append("SE rechaza H0 (No pasa)");

        mostrar(sb.toString(), "Chi-Cuadrada (Composición)");
    }

    // ======================================================================
    // ==========  KOLMOGOROV–SMIRNOV (Adaptada a Composición) ==============
    // ======================================================================
    private static void realizarKS(List<Double> datosOriginal, double p1, double p2, double l1, double l2) {

        List<Double> datos = new ArrayList<>(datosOriginal);
        Collections.sort(datos); // Ordenar es vital para KS

        int n = datos.size();

        StringBuilder sb = new StringBuilder();
        sb.append("--- PRUEBA K-S (Composición) ---\n");
        sb.append("H0: Los datos siguen la distribución especificada.\n\n");

        double dMax = 0;

        for (int i = 0; i < n; i++) {
            double x = datos.get(i);

            // Probabilidad acumulada observada y esperada
            double fObs = (double) (i + 1) / n;
            double fEsp = cdfComposicion(x, p1, p2, l1, l2);

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

        double dCrit = 1.36 / Math.sqrt(n);

        sb.append("\nDmax calculado: ").append(String.format("%.5f", dMax));
        sb.append("\nD crítico (α=0.05): ").append(String.format("%.5f", dCrit));
        sb.append("\n\nRESULTADO: ");

        if (dMax < dCrit) sb.append("NO se rechaza H0 (Pasa)");
        else sb.append("SE rechaza H0 (No pasa)");

        mostrar(sb.toString(), "K-S (Composición)");
    }

    // ======================================================================
    // ==========  CDF COMPOSICIÓN  =========================================
    // ======================================================================
    // Función de distribución acumulada para la mezcla de dos exponenciales
    // F(x) = P1 * (1 - e^(-λ1*x)) + P2 * (1 - e^(-λ2*x))
    private static double cdfComposicion(double x, double p1, double p2, double l1, double l2) {
        if (x < 0) return 0;
        double term1 = 1 - exp(-l1 * x);
        double term2 = 1 - exp(-l2 * x);
        return (p1 * term1) + (p2 * term2);
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
        // Aproximación de Wilson-Hilferty para gl grandes
        return gl * pow(1 - (2.0 / (9 * gl)) + (1.645 * Math.sqrt(2.0 / (9 * gl))), 3);
    }
}