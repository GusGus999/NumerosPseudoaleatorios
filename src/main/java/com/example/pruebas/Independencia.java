package com.example.pruebas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.example.tablas.TablaChiCuadrada.obtenerValorCritico;

public class Independencia {
    public static void realizarPruebaIndependenciaPoker(ArrayList<Double> datos) {
        int n = datos.size();

        // Probabilidades teóricas para 5 decimales (Suman 1.0)
        double[] probEsperada = {0.3024, 0.5040, 0.1080, 0.0720, 0.0090, 0.0045, 0.0001};
        String[] nombresManos = {"Todos Diferentes", "Un Par", "Dos Pares", "Tercia", "Full House", "Póker", "Quintilla"};
        int[] conteoObservado = new int[7]; // Indices 0 al 6 corresponden a las manos de arriba

        // 1. Clasificar cada número
        for (double num : datos) {
            // Convertimos a String y tomamos 5 decimales.
            // Ej: 0.123456 -> "12345"
            String numStr = String.format("%.5f", num).replace(",", ".").substring(2);
            if (numStr.length() > 5) numStr = numStr.substring(0, 5); // Asegurar 5 dígitos

            int tipoMano = evaluarMano(numStr);
            conteoObservado[tipoMano]++;
        }

        // 2. Calcular Chi-Cuadrada
        StringBuilder reporte = new StringBuilder();
        reporte.append("--- PRUEBA DE PÓKER (INDEPENDENCIA) ---\n");
        reporte.append(String.format("Datos analizados (n): %d\n\n", n));
        reporte.append(String.format("%-18s %-10s %-10s %-10s\n", "Mano", "Obs(O)", "Esp(E)", "(O-E)²/E"));
        reporte.append("-------------------------------------------------------\n");

        double chiCuadradaCalculada = 0.0;

        for (int i = 0; i < 7; i++) {
            double esperado = probEsperada[i] * n;
            double termino = 0.0;
            // Evitar división por cero si n es muy pequeño
            if (esperado > 0) {
                termino = Math.pow(conteoObservado[i] - esperado, 2) / esperado;
            }

            chiCuadradaCalculada += termino;

            reporte.append(String.format("%-18s %-10d %-10.2f %-10.4f\n",
                    nombresManos[i], conteoObservado[i], esperado, termino));
        }

        reporte.append("-------------------------------------------------------\n");
        reporte.append(String.format("Chi-Cuadrada Calculada: %.4f\n", chiCuadradaCalculada));

        // 3. Obtener valor crítico
        // Grados de libertad = Categorías - 1 = 7 - 1 = 6
        int gradosLibertad = 6;
        double valorCritico = obtenerValorCritico(gradosLibertad);

        reporte.append(String.format("Valor Crítico (gl=%d):   %.4f\n\n", gradosLibertad, valorCritico));

        // 4. Conclusión
        if (chiCuadradaCalculada < valorCritico) {
            reporte.append("CONCLUSIÓN: NO SE RECHAZA H0.\nLos números tienen un comportamiento de independencia ACEPTABLE.");
        } else {
            reporte.append("CONCLUSIÓN: SE RECHAZA H0.\nLos dígitos NO parecen independientes (Patrón detectado).");
        }

        // Advertencia para muestras pequeñas
        if (n < 50) {
            reporte.append("\n\n(NOTA: Para mayor fiabilidad, use n >= 50)");
        }

        JTextArea textArea = new JTextArea(reporte.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Resultados Prueba de Póker", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Analiza 5 dígitos y determina qué mano de póker forman.
     * Retorna índice: 0=TD, 1=1P, 2=2P, 3=T, 4=FH, 5=P, 6=Q
     */
    private static int evaluarMano(String s) {
        int[] counts = new int[10]; // Conteo para dígitos 0-9
        for (char c : s.toCharArray()) {
            counts[c - '0']++;
        }

        boolean par = false;
        boolean tercia = false;
        boolean poker = false;
        boolean quintilla = false;
        int paresCount = 0;

        for (int c : counts) {
            if (c == 5) quintilla = true;
            if (c == 4) poker = true;
            if (c == 3) tercia = true;
            if (c == 2) {
                par = true;
                paresCount++;
            }
        }

        if (quintilla) return 6; // Quintilla
        if (poker) return 5;     // Póker
        if (tercia && par) return 4; // Full House
        if (tercia) return 3;    // Tercia
        if (paresCount == 2) return 2; // Dos Pares
        if (par) return 1;       // Un Par
        return 0;                // Todos Diferentes
    }
}
