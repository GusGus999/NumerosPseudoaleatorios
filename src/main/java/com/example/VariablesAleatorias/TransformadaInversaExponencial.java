package com.example.VariablesAleatorias;

import javax.swing.*;
import java.awt.Font; // Importante para la alineación
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
public class TransformadaInversaExponencial {
    public static void ejecutarExponencial() {
        try {
            // 1. Pedir datos
            String strLambda = JOptionPane.showInputDialog("Ingrese el valor de Lambda (λ):");
            if (strLambda == null) return;
            double lambda = Double.parseDouble(strLambda);

            if (lambda <= 0) {
                JOptionPane.showMessageDialog(null, "Lambda debe ser mayor que 0.", "Error Lógico", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String strN = JOptionPane.showInputDialog("¿Cuántas variables desea generar?");
            if (strN == null) return;
            int n = Integer.parseInt(strN);

            if (n <= 0) {
                JOptionPane.showMessageDialog(null, "La cantidad N debe ser mayor a 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // --- LISTAS PARA GUARDAR DATOS ---
            List<Double> listaRi = new ArrayList<>(); // Para prueba de Póker
            List<Double> listaXi = new ArrayList<>(); // Para Chi-Cuadrada y K-S
            StringBuilder sb = new StringBuilder();
            sb.append("Calculando X = -ln(1 - R) / ").append(lambda).append("\n\n");
            sb.append(String.format("%-5s %-12s %-12s\n", "i", "Ri (GCL)", "Xi (Exp)"));
            sb.append("------------------------------------------\n");
            // 2. Generación
            for (int i = 1; i <= n; i++) {
                // NOTA: Requiere la clase CongruencialLineal.
                // Si deseas probar sin ella, usa: double ri = Math.random();
                double ri = CongruencialLineal.siguienteRi();

                // Fórmula Transformada Inversa Exponencial
                double xi = -Math.log(1 - ri) / lambda;

                // Guardar en memoria
                listaRi.add(ri);
                listaXi.add(xi);

                sb.append(String.format("%-5d %-12.4f %-12.4f\n", i, ri, xi));
            }
            // Mostrar Generación
            mostrarResultado(sb.toString(), "Resultados Generación Exponencial");
            // 3. Menú de Pruebas
            String[] opciones = {"Prueba Chi-Cuadrada", "Prueba K-S", "Prueba de Póker", "Salir"};
            int seleccion;
            do {
                seleccion = JOptionPane.showOptionDialog(
                        null,
                        "Seleccione una prueba estadística para validar los datos:",
                        "Pruebas Estadísticas",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                );
                switch (seleccion) {
                    case 0: // Chi-Cuadrada
                        realizarChiCuadrada(listaXi, lambda);
                        break;
                    case 1: // K-S
                        realizarKS(listaXi, lambda);
                        break;
                    case 2: // Póker
                        realizarPoker(listaRi);
                        break;
                    case 3: // Salir
                        break;
                }
            } while (seleccion != 3 && seleccion != -1);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese números válidos.", "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }
    // ==========================================================================
    // PRUEBA 1: CHI-CUADRADA (Bondad de Ajuste sobre Xi)
    // ==========================================================================
    private static void realizarChiCuadrada(List<Double> datos, double lambda) {
        int n = datos.size();
        int k = (int) Math.sqrt(n); // Regla de la raíz cuadrada
        if (k < 5) k = 5; // Mínimo 5 intervalos para validez estadística

        double min = Collections.min(datos);
        double max = Collections.max(datos);
        double ancho = (max - min) / k;

        StringBuilder sb = new StringBuilder();
        sb.append("--- PRUEBA CHI-CUADRADA ---\n");
        sb.append("H0: Los datos siguen una distribución Exponencial (λ=").append(lambda).append(")\n\n");
        sb.append(String.format("%-5s %-18s %-8s %-8s %-10s\n", "Int", "Rango", "FO", "FE", "Chi-Calc"));
        sb.append("------------------------------------------------------------------\n");

        double chiCalculada = 0;

        for (int i = 0; i < k; i++) {
            double limInf = min + (i * ancho);
            double limSup = min + ((i + 1) * ancho);

            // Frecuencia Observada
            int fo = 0;
            for (double x : datos) {
                if (i == k - 1) { // Último intervalo incluye límite superior por precisión decimal
                    if (x >= limInf && x <= (max + 0.0001)) fo++;
                } else {
                    if (x >= limInf && x < limSup) fo++;
                }
            }

            // Frecuencia Esperada (Usando FDA Exponencial: 1 - e^(-λx))
            // Probabilidad del intervalo = F(limSup) - F(limInf)
            double prob = (1 - Math.exp(-lambda * limSup)) - (1 - Math.exp(-lambda * limInf));

            // Ajuste fino para que la suma de probabilidades sea 1.0 en el último intervalo
            if (i == k - 1) prob = 1.0 - (1 - Math.exp(-lambda * limInf));

            double fe = prob * n;

            // Evitar división por cero si FE es extremadamente pequeña
            double chiParcial = (fe > 0) ? Math.pow(fo - fe, 2) / fe : 0.0;
            chiCalculada += chiParcial;

            sb.append(String.format("%-5d [%6.2f-%6.2f] %-8d %-8.2f %-10.4f\n",
                    (i + 1), limInf, limSup, fo, fe, chiParcial));
        }

        // Valor crítico aproximado (Alpha=0.05, GL = k-1)
        double valorCritico = obtenerChiCritico(k - 1);

        sb.append("\nChi-Calculada: ").append(String.format("%.4f", chiCalculada)).append("\n");
        sb.append("Valor Crítico (0.05, GL=").append(k - 1).append("): ").append(valorCritico).append("\n\n");

        if (chiCalculada < valorCritico) sb.append("RESULTADO: NO SE RECHAZA H0 (Pasa la prueba).");
        else sb.append("RESULTADO: SE RECHAZA H0 (No pasa la prueba).");

        mostrarResultado(sb.toString(), "Chi-Cuadrada Exponencial");
    }

    // ==========================================================================
    // PRUEBA 2: KOLMOGOROV-SMIRNOV (K-S) (Sobre Xi)
    // ==========================================================================
    private static void realizarKS(List<Double> datosOriginales, double lambda) {
        // Necesitamos ordenar una copia de la lista para no alterar la original (por si se usa en otras pruebas)
        List<Double> datos = new ArrayList<>(datosOriginales);
        Collections.sort(datos);
        int n = datos.size();

        StringBuilder sb = new StringBuilder();
        sb.append("--- PRUEBA KOLMOGOROV-SMIRNOV ---\n");
        sb.append("H0: Los datos siguen una distribución Exponencial.\n\n");
        sb.append(String.format("%-5s %-10s %-10s %-10s %-10s\n", "i", "Xi", "F_Obs", "F_Esp", "|Dif|"));
        sb.append("------------------------------------------------------------\n");

        double maxDiferencia = 0;

        for (int i = 0; i < n; i++) {
            double xi = datos.get(i);

            // Probabilidad Observada (i / n)
            double fObs = (double) (i + 1) / n;

            // Probabilidad Esperada (Teórica): 1 - e^(-λx)
            double fEsp = 1 - Math.exp(-lambda * xi);

            double dif = Math.abs(fObs - fEsp);
            if (dif > maxDiferencia) maxDiferencia = dif;

            // Mostramos solo los primeros 15 y últimos 5 para no saturar si n es grande
            if (i < 15 || i > n - 5) {
                sb.append(String.format("%-5d %-10.4f %-10.4f %-10.4f %-10.4f\n",
                        (i + 1), xi, fObs, fEsp, dif));
            } else if (i == 15) {
                sb.append("...\t...\t...\t...\t...\n");
            }
        }

        // Valor Crítico aproximado para K-S (Alpha = 0.05) para n > 35
        double valorCritico = 1.36 / Math.sqrt(n);

        sb.append("\nDMáx Calculada: ").append(String.format("%.4f", maxDiferencia)).append("\n");
        sb.append("Valor Crítico (0.05, n=").append(n).append("): ").append(String.format("%.4f", valorCritico)).append("\n\n");

        if (maxDiferencia < valorCritico) sb.append("RESULTADO: NO SE RECHAZA H0 (Pasa la prueba).");
        else sb.append("RESULTADO: SE RECHAZA H0 (No pasa la prueba).");

        mostrarResultado(sb.toString(), "K-S Exponencial");
    }

    // ==========================================================================
    // PRUEBA 3: PÓKER (Sobre Ri - Independencia)
    // ==========================================================================
    private static void realizarPoker(List<Double> listaRi) {
        int[] fo = new int[7];
        String[] categorias = {"Todos Dif.", "Un Par", "Dos Pares", "Tercia", "Full House", "Póker", "Quintilla"};
        // Probabilidades teóricas (para 5 decimales)
        double[] probs = {0.3024, 0.5040, 0.1080, 0.0720, 0.0090, 0.0045, 0.0001};

        for (double r : listaRi) {
            String s = String.format("%.5f", r).substring(2); // quitar "0."
            if (s.length() < 5) s = String.format("%-5s", s).replace(' ', '0'); // Rellenar ceros a la derecha
            s = s.substring(0, 5); // Asegurar 5 dígitos

            fo[clasificarMano(s)]++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("--- PRUEBA DE PÓKER (5 Decimales) ---\n");
        sb.append("H0: Los números R son independientes.\n\n");
        sb.append(String.format("%-15s %-10s %-10s %-10s\n", "Categoría", "FO", "FE", "Chi-Calc"));
        sb.append("--------------------------------------------------\n");

        double chiCalculada = 0;
        int n = listaRi.size();

        for (int i = 0; i < 7; i++) {
            double fe = probs[i] * n;
            // Evitar dividir por 0 si FE es muy pequeña
            double chiParcial = (fe > 0) ? Math.pow(fo[i] - fe, 2) / fe : 0;
            chiCalculada += chiParcial;

            sb.append(String.format("%-15s %-10d %-10.2f %-10.4f\n",
                    categorias[i], fo[i], fe, chiParcial));
        }

        // Grados de libertad = Categorías - 1 = 6
        double valorCritico = 12.59; // Chi cuadrada tabla 0.05, GL=6

        sb.append("\nChi-Calculada: ").append(String.format("%.4f", chiCalculada)).append("\n");
        sb.append("Valor Crítico (0.05, GL=6): ").append(valorCritico).append("\n\n");

        if (chiCalculada < valorCritico) sb.append("RESULTADO: NO SE RECHAZA H0 (Son Independientes).");
        else sb.append("RESULTADO: SE RECHAZA H0 (No parecen independientes).");

        mostrarResultado(sb.toString(), "Prueba de Póker");
    }

    private static int clasificarMano(String s) {
        Map<Character, Integer> conteo = new HashMap<>();
        for (char c : s.toCharArray()) conteo.put(c, conteo.getOrDefault(c, 0) + 1);

        int pares = 0, tercias = 0, pokers = 0, quintillas = 0;
        for (int val : conteo.values()) {
            if (val == 5) quintillas++;
            if (val == 4) pokers++;
            if (val == 3) tercias++;
            if (val == 2) pares++;
        }

        if (quintillas == 1) return 6;
        if (pokers == 1) return 5;
        if (tercias == 1 && pares == 1) return 4;
        if (tercias == 1) return 3;
        if (pares == 2) return 2;
        if (pares == 1) return 1;
        return 0;
    }

    // UTILERÍAS DE VISUALIZACIÓN MEJORADA
    private static void mostrarResultado(String texto, String titulo) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        // IMPORTANTE: Fuente monoespaciada para alinear columnas
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(600, 400));

        JOptionPane.showMessageDialog(null, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private static double obtenerChiCritico(int gl) {
        // Tabla simplificada para Alpha = 0.05
        double[] tabla = {0, 3.84, 5.99, 7.81, 9.49, 11.07, 12.59, 14.07, 15.51, 16.92, 18.31, 19.68, 21.03};
        if (gl < tabla.length) return tabla[gl];
        return gl + 1.645 * Math.sqrt(2 * gl); // Aproximación para GL altos
    }
}