package com.example.metodos;

import javax.swing.*;
import java.awt.*;
import java.util.*;

import static com.example.pruebas.Aleatoridad.realizarPruebaAleatoriedad;
import static com.example.pruebas.Independencia.realizarPruebaIndependenciaPoker;
import static com.example.pruebas.Uniformidad.realizarPruebaUniformidad;

public class CongruencialLineal {

    public static void congruencialLineal(int x0, int a, int c, int m) {

        StringBuilder salida = new StringBuilder();
        ArrayList<Double> listaRi = new ArrayList<>();

        salida.append("Parámetros: X0=" + x0 + ", a=" + a + ", c=" + c + ", m=" + m + "\n");
        salida.append("Condiciones de Hull-Dobell verificadas: Periodo Completo garantizado (N = " + m + ").\n\n");

        salida.append(String.format("%-6s %-10s %-20s %-15s %-10s\n",
                "i", "Xi", "a*Xi + c", "Xi+1 (Mod)", "Ri"));
        salida.append("----------------------------------------------------------------------\n");

        int xi = x0; // Empezamos con el 32 (o lo que ingrese el usuario)
        int i = 0;
        int limiteSeguridad = m + 100;

        do {
            // 1. Calcular la ecuación lineal
            long lineal = (long) a * xi + c;

            // 2. Aplicar módulo
            int xiSiguiente = (int) (lineal % m);

            // 3. Calcular Ri
            double ri = (double) xiSiguiente / m;

            listaRi.add(ri);

            salida.append(String.format("%-6d %-10d %-20d %-15d %.4f\n",
                    i, xi, lineal, xiSiguiente, ri));

            // Actualizar para siguiente iteración
            xi = xiSiguiente;
            i++;

            // --- CORRECCIÓN SIMPLE ---
            // Como Hull-Dobell garantiza el periodo completo, sabemos que el ciclo
            // termina exactamente cuando hemos generado 'm' números.
            if (i == m) {
                salida.append("\nCiclo completado. Se han generado " + i + " números (Periodo Completo).\n");
                // Visualmente indicamos que se alcanzó la semilla original para el reporte
                salida.append("El siguiente valor sería la semilla original: " + x0 + "\n");
                break;
            }

            // Seguridad extra por si acaso
            if (i > limiteSeguridad) {
                salida.append("\nAlerta: Se excedió el límite de seguridad.\n");
                break;
            }

        } while (true);

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
                    null, scrollPane, "Resultados - Congruencial Lineal",
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

    // --- MÉTODOS DE VALIDACIÓN MATEMÁTICA ---

    private static int calcularMCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public static ArrayList<Integer> obtenerFactoresPrimos(int n) {
        ArrayList<Integer> factores = new ArrayList<>();
        int d = 2;
        int temp = n;
        while (d * d <= temp) {
            if (temp % d == 0) {
                factores.add(d);
                while (temp % d == 0) {
                    temp /= d;
                }
            }
            d++;
        }
        if (temp > 1) {
            factores.add(temp);
        }
        return factores;
    }

    public static void main(String[] args) {
        try {
            // --- ENTRADA DE DATOS ---
            String inX0 = JOptionPane.showInputDialog(null, "Ingrese la semilla (X0):", "Paso 1", JOptionPane.QUESTION_MESSAGE);
            if(inX0==null) return;
            int x0 = Integer.parseInt(inX0);

            String inA = JOptionPane.showInputDialog(null, "Ingrese el multiplicador (a):", "Paso 2", JOptionPane.QUESTION_MESSAGE);
            if(inA==null) return;
            int a = Integer.parseInt(inA);

            String inC = JOptionPane.showInputDialog(null, "Ingrese la constante aditiva (c):", "Paso 3", JOptionPane.QUESTION_MESSAGE);
            if(inC==null) return;
            int c = Integer.parseInt(inC);

            String inM = JOptionPane.showInputDialog(null, "Ingrese el módulo (m): ", "Paso 4", JOptionPane.QUESTION_MESSAGE);
            if(inM==null) return;
            int m = Integer.parseInt(inM);

            // --- VALIDACIONES HULL-DOBELL ---

            if (m <= 0) {
                JOptionPane.showMessageDialog(null, "Error: El módulo (m) debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (calcularMCD(c, m) != 1) {
                JOptionPane.showMessageDialog(null,
                        "'c' y 'm' NO son primos relativos.\n" +
                                "MCD(" + c + ", " + m + ") != 1.\n" +
                                "Esto evitará que se genere un periodo completo.",
                        "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<Integer> factoresPrimosM = obtenerFactoresPrimos(m);
            int aMenos1 = a - 1;

            for (int primo : factoresPrimosM) {
                if (aMenos1 % primo != 0) {
                    JOptionPane.showMessageDialog(null,
                            "El número " + primo + " es factor primo de m (" + m + "),\n" +
                                    "pero (a-1) = " + aMenos1 + " NO es divisible entre " + primo + ".",
                            "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            if (m % 4 == 0) {
                if (aMenos1 % 4 != 0) {
                    JOptionPane.showMessageDialog(null,
                            "Como m (" + m + ") es múltiplo de 4,\n" +
                                    "(a-1) = " + aMenos1 + " también debe ser múltiplo de 4.",
                            "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            JOptionPane.showMessageDialog(null,
                    "Todas las condiciones se cumplen\n" +
                            "El generador tendrá un periodo completo de tamaño " + m + ".",
                    "Validación Exitosa", JOptionPane.INFORMATION_MESSAGE);

            congruencialLineal(x0, a, c, m);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese solo números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}