package com.example.VariablesAleatorias;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TransformadaInversaUniforme {

    public static void ejecutarUniforme() {
        try {
            // 1. Pedir Limites A (Mínimo) y B (Máximo)
            String strA = JOptionPane.showInputDialog("Ingrese el límite inferior (a):");
            if (strA == null) return;
            int a = Integer.parseInt(strA);

            String strB = JOptionPane.showInputDialog("Ingrese el límite superior (b):");
            if (strB == null) return;
            int b = Integer.parseInt(strB);

            if (a >= b) {
                JOptionPane.showMessageDialog(null, "El límite inferior (a) debe ser menor que (b).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Pedir cantidad N
            String strN = JOptionPane.showInputDialog("¿Cuántas variables desea generar?");
            if (strN == null) return;
            int n = Integer.parseInt(strN);

            // --- LISTAS PARA GUARDAR DATOS ---
            List<Double> listaRi = new ArrayList<>(); // Para Poker
            List<Integer> listaXi = new ArrayList<>(); // Para Chi y KS (Enteros)

            StringBuilder sb = new StringBuilder();
            sb.append("Calculando X = ").append(a).append(" + INT( (").append(b).append("-").append(a).append("+1) * R )\n\n");
            sb.append("i\tRi (GCL)\tXi (Unif)\n");
            sb.append("------------------------------------------\n");

            // Rango total (b - a + 1)
            int rango = b - a + 1;

            // 3. Generación
            for (int i = 1; i <= n; i++) {
                double ri = CongruencialLineal.siguienteRi();

                // Fórmula Uniforme Discreta: a + (b-a+1)*R
                int xi = a + (int) (rango * ri);

                listaRi.add(ri);
                listaXi.add(xi);

                sb.append(i).append("\t")
                        .append(String.format("%.4f", ri)).append("\t")
                        .append(xi).append("\n"); // Xi es entero, no lleva decimales
            }

            // Mostrar Generación
            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Resultados Uniforme Discreta", JOptionPane.INFORMATION_MESSAGE);

            // 4. Menú de Pruebas
            String[] opciones = {"Prueba Chi-Cuadrada", "Prueba K-S", "Prueba de Póker", "Salir"};
            int seleccion;
            do {
                seleccion = JOptionPane.showOptionDialog(
                        null,
                        "Seleccione una prueba para validar Uniformidad en Xi:",
                        "Pruebas Estadísticas",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                );

                switch (seleccion) {
                    case 0: // Chi-Cuadrada
                        realizarChiCuadrada(listaXi, a, b);
                        break;
                    case 1: // K-S
                        realizarKS(listaXi, a, b);
                        break;
                    case 2: // Póker
                        realizarPoker(listaRi);
                        break;
                    case 3: // Salir
                        break;
                }
            } while (seleccion != 3 && seleccion != -1);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==========================================================================
    // PRUEBA 1: CHI-CUADRADA (Bondad de Ajuste para Uniforme)
    // ==========================================================================
    private static void realizarChiCuadrada(List<Integer> datos, int a, int b) {
        int n = datos.size();
        int k = (int) Math.sqrt(n);
        if (k < 5) k = 5;

        // Convertimos a doubles para facilitar cálculos de intervalos
        double min = a;
        double max = b + 1; // +1 porque en discreta el límite superior se incluye entero
        double ancho = (max - min) / k;

        StringBuilder sb = new StringBuilder();
        sb.append("--- PRUEBA CHI-CUADRADA (Uniforme) ---\n");
        sb.append("H0: Los datos se distribuyen uniformemente entre ").append(a).append(" y ").append(b).append("\n\n");
        sb.append(String.format("%-10s %-15s %-10s %-10s %-10s\n", "Int", "Rango", "FO", "FE", "Chi-Calc"));
        sb.append("------------------------------------------------------------\n");

        double chiCalculada = 0;

        for (int i = 0; i < k; i++) {
            double limInf = min + (i * ancho);
            double limSup = min + ((i + 1) * ancho);

            // Frecuencia Observada
            int fo = 0;
            for (int x : datos) {
                // Ajuste para discreta: consideramos si el entero cae en el rango continuo
                if (x >= limInf && x < limSup) fo++;
                    // Caso borde para el último valor exacto
                else if (i == k-1 && x >= limInf && x <= b) fo++;
            }

            // Frecuencia Esperada: En uniforme es constante = n / k
            // Ojo: Matemáticamente es Proporción del rango * n
            double fe = (double) n / k;

            double chiParcial = Math.pow(fo - fe, 2) / fe;
            chiCalculada += chiParcial;

            sb.append(String.format("%-10d [%.2f-%.2f)   %-10d %-10.2f %-10.4f\n",
                    (i+1), limInf, limSup, fo, fe, chiParcial));
        }

        double valorCritico = obtenerChiCritico(k - 1);
        sb.append("\nChi-Calculada: ").append(String.format("%.4f", chiCalculada)).append("\n");
        sb.append("Valor Crítico (0.05, GL=").append(k-1).append("): ").append(valorCritico).append("\n\n");

        if (chiCalculada < valorCritico) sb.append("RESULTADO: NO SE RECHAZA H0 (Pasa la prueba).");
        else sb.append("RESULTADO: SE RECHAZA H0 (No pasa la prueba).");

        mostrarResultado(sb.toString(), "Chi-Cuadrada Uniforme");
    }

    // ==========================================================================
    // PRUEBA 2: KOLMOGOROV-SMIRNOV (K-S para Uniforme)
    // ==========================================================================
    private static void realizarKS(List<Integer> datosOriginales, int a, int b) {
        List<Integer> datos = new ArrayList<>(datosOriginales);
        Collections.sort(datos);
        int n = datos.size();
        double rangoTotal = b - a + 1.0;

        StringBuilder sb = new StringBuilder();
        sb.append("--- PRUEBA KOLMOGOROV-SMIRNOV ---\n");
        sb.append("H0: Distribución Uniforme Discreta.\n\n");
        sb.append(String.format("%-5s %-10s %-10s %-10s %-10s\n", "i", "Xi", "F_Obs", "F_Esp", "|Dif|"));
        sb.append("----------------------------------------------------------\n");

        double maxDiferencia = 0;

        for (int i = 0; i < n; i++) {
            int xi = datos.get(i);

            double fObs = (double) (i + 1) / n;

            // F_Esperada (Uniforme): (x - a + 1) / (b - a + 1)
            double fEsp = (xi - a + 1) / rangoTotal;

            // Ajuste visual: F_Esp no puede pasar de 1.0
            if(fEsp > 1.0) fEsp = 1.0;

            double dif = Math.abs(fObs - fEsp);
            if (dif > maxDiferencia) maxDiferencia = dif;

            if (i < 15 || i > n - 5) {
                sb.append(String.format("%-5d %-10d %-10.4f %-10.4f %-10.4f\n",
                        (i+1), xi, fObs, fEsp, dif));
            }
        }

        double valorCritico = 1.36 / Math.sqrt(n);

        sb.append("\nDMáx Calculada: ").append(String.format("%.4f", maxDiferencia)).append("\n");
        sb.append("Valor Crítico (0.05, n=").append(n).append("): ").append(String.format("%.4f", valorCritico)).append("\n\n");

        if (maxDiferencia < valorCritico) sb.append("RESULTADO: NO SE RECHAZA H0 (Pasa la prueba).");
        else sb.append("RESULTADO: SE RECHAZA H0 (No pasa la prueba).");

        mostrarResultado(sb.toString(), "K-S Uniforme");
    }

    // ==========================================================================
    // PRUEBA 3: PÓKER (Sobre Ri - Idéntico a la clase anterior)
    // ==========================================================================
    private static void realizarPoker(List<Double> listaRi) {
        int[] fo = new int[7];
        String[] categorias = {"Todos Dif.", "Un Par", "Dos Pares", "Tercia", "Full House", "Póker", "Quintilla"};
        double[] probs = {0.3024, 0.5040, 0.1080, 0.0720, 0.0090, 0.0045, 0.0001};

        for (double r : listaRi) {
            String s = String.format("%.5f", r).substring(2);
            if (s.length() < 5) s = String.format("%-5s", s).replace(' ', '0');
            s = s.substring(0, 5);

            fo[clasificarMano(s)]++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("--- PRUEBA DE PÓKER (Sobre Ri) ---\n\n");
        sb.append(String.format("%-15s %-10s %-10s %-10s\n", "Categoría", "FO", "FE", "Chi-Calc"));
        sb.append("--------------------------------------------------\n");

        double chiCalculada = 0;
        int n = listaRi.size();

        for (int i = 0; i < 7; i++) {
            double fe = probs[i] * n;
            double chiParcial = (fe > 0) ? Math.pow(fo[i] - fe, 2) / fe : 0;
            chiCalculada += chiParcial;
            sb.append(String.format("%-15s %-10d %-10.2f %-10.4f\n", categorias[i], fo[i], fe, chiParcial));
        }

        double valorCritico = 12.59;
        sb.append("\nChi-Calculada: ").append(String.format("%.4f", chiCalculada)).append("\n");
        sb.append("Valor Crítico (GL=6): ").append(valorCritico).append("\n");

        if (chiCalculada < valorCritico) sb.append("RESULTADO: Pasa la prueba (Independencia).");
        else sb.append("RESULTADO: No pasa la prueba.");

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

    private static void mostrarResultado(String texto, String titulo) {
        JTextArea area = new JTextArea(texto);
        area.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(area), titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private static double obtenerChiCritico(int gl) {
        double[] tabla = {0, 3.84, 5.99, 7.81, 9.49, 11.07, 12.59, 14.07, 15.51, 16.92, 18.31, 19.68, 21.03};
        if (gl < tabla.length) return tabla[gl];
        return gl + 1.645 * Math.sqrt(2 * gl);
    }
}