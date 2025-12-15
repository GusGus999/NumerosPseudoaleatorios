package com.example.metodos;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// Asegúrate de que estas importaciones sigan siendo correctas en tu proyecto
import static com.example.pruebas.Aleatoridad.realizarPruebaAleatoriedad;
import static com.example.pruebas.Independencia.realizarPruebaIndependenciaPoker;
import static com.example.pruebas.Uniformidad.realizarPruebaUniformidad;

public class CongruencialAditivo {

    public static void congruencialAditivo(ArrayList<Integer> semillaSecuencia, int m) {
        StringBuilder salida = new StringBuilder();
        ArrayList<Double> listaRi = new ArrayList<>();
        ArrayList<Integer> secuencia = new ArrayList<>(semillaSecuencia);

        int k = semillaSecuencia.size();

        // Cabecera de la tabla
        salida.append(String.format("%-6s %-10s %-10s %-15s %-10s %-10s\n",
                "i", "X(i-1)", "X(i-k)", "Suma", "Xi (Mod)", "Ri"));
        salida.append("-----------------------------------------------------------------------------------\n");

        // Set para detectar ciclos (Límite intrínseco)
        Set<String> estadosVistos = new HashSet<>();
        estadosVistos.add(generarFirmaEstado(secuencia, k));

        int i = k;
        boolean cicloDetectado = false;
        int limiteSeguridad = 10000; // Evita loop infinito si el periodo es muy grande

        // El ciclo se ejecuta hasta encontrar un patrón repetido (límite intrínseco)
        while (!cicloDetectado && i < limiteSeguridad) {

            // Formula: Xi = (X(i-1) + X(i-k)) mod m
            int x_prev = secuencia.get(i - 1);
            int x_k_atras = secuencia.get(i - k);

            int suma = x_prev + x_k_atras;
            int xi = suma % m;

            double ri = (double) xi / (m - 1); // Normalización

            secuencia.add(xi);
            listaRi.add(ri);

            salida.append(String.format("%-6d %-10d %-10d %-15d %-10d %.4f\n",
                    (i + 1), x_prev, x_k_atras, suma, xi, ri));

            // Verificación de ciclo
            String firmaActual = generarFirmaEstado(secuencia, k);

            if (estadosVistos.contains(firmaActual)) {
                salida.append("\n[!] Límite intrínseco alcanzado: Ciclo detectado.\n");
                cicloDetectado = true;
            } else {
                estadosVistos.add(firmaActual);
            }

            i++;
        }

        if (i >= limiteSeguridad) {
            salida.append("\n[!] Se detuvo por límite de seguridad (10,000 iters).\n");
        }

        // --- Componente Visual ---
        JTextArea textArea = new JTextArea(salida.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(700, 450));

        Object[] opciones = {"Uniformidad", "Aleatoriedad", "Prueba de Póker", "Salir"};
        int seleccion = -1;

        do {
            seleccion = JOptionPane.showOptionDialog(
                    null, scrollPane, "Resultados - Congruencial Aditivo",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, opciones, opciones[0]);

            switch (seleccion) {
                case 0: realizarPruebaUniformidad(listaRi); break;
                case 1: realizarPruebaAleatoriedad(listaRi); break;
                case 2: realizarPruebaIndependenciaPoker(listaRi); break;
                case 3: System.out.println("Saliendo..."); break;
                default: break;
            }
        } while (seleccion != 3 && seleccion != -1);
    }

    private static String generarFirmaEstado(ArrayList<Integer> lista, int k) {
        StringBuilder sb = new StringBuilder();
        int size = lista.size();
        for (int j = size - k; j < size; j++) {
            sb.append(lista.get(j)).append(",");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            // 1. Pedir la cantidad de semillas
            String inputCantidad = JOptionPane.showInputDialog(null,
                    "¿Cuántas semillas desea ingresar?",
                    "Configuración - Paso 1",
                    JOptionPane.QUESTION_MESSAGE);

            if (inputCantidad == null) return; // Si cancela
            int cantidadSemillas = Integer.parseInt(inputCantidad);

            if (cantidadSemillas < 2) {
                JOptionPane.showMessageDialog(null, "El método requiere al menos 2 semillas.");
                return;
            }

            // 2. Pedir las semillas una por una
            ArrayList<Integer> semillas = new ArrayList<>();
            for (int i = 0; i < cantidadSemillas; i++) {
                String inputSemilla = JOptionPane.showInputDialog(null,
                        "Ingrese la semilla número " + (i + 1) + ":",
                        "Ingresando semillas (" + (i + 1) + "/" + cantidadSemillas + ")",
                        JOptionPane.QUESTION_MESSAGE);

                if (inputSemilla == null) return; // Si cancela a mitad del proceso
                semillas.add(Integer.parseInt(inputSemilla));
            }

            // 3. Pedir el módulo
            String inputM = JOptionPane.showInputDialog(null,
                    "Ingrese el valor del módulo (m):",
                    "Configuración - Paso Final",
                    JOptionPane.QUESTION_MESSAGE);

            if (inputM == null) return;
            int m = Integer.parseInt(inputM);

            // 4. Ejecutar algoritmo
            congruencialAditivo(semillas, m);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Error: Por favor ingrese solo números enteros válidos.",
                    "Error de Formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}