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

        int xi = x0;
        int i = 0;
        int limiteSeguridad = m + 100; // Un poco más que m para detectar el cierre del ciclo

        // Set para control estricto (aunque Hull-Dobell garantiza m, visualizamos el cierre)
        boolean cicloCerrado = false;

        do {
            // 1. Calcular la ecuación lineal
            // Usamos long para evitar desbordamiento antes del módulo
            long lineal = (long) a * xi + c;

            // 2. Aplicar módulo
            int xiSiguiente = (int) (lineal % m);

            // 3. Calcular Ri
            double ri = (double) xiSiguiente / m; // Normalización estándar [0, 1)

            listaRi.add(ri);

            salida.append(String.format("%-6d %-10d %-20d %-15d %.4f\n",
                    i, xi, lineal, xiSiguiente, ri));

            // Actualizar para siguiente iteración
            xi = xiSiguiente;
            i++;

            // --- VALIDACIÓN DE PERIODO ---
            // En el metodo Mixto con Hull-Dobell, el ciclo se cierra cuando volvemos a X0.
            if (xi == x0) {
                salida.append("\nCiclo completado. Se alcanzó la semilla original " + x0 + ".\n");
                salida.append("Total de números generados: " + i + " (Igual a m: " + m + ")\n");
                cicloCerrado = true;
                break;
            }

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

    // 1. Máximo Común Divisor (Euclides)
    private static int calcularMCD(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // 2. Obtener factores primos únicos de un número
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

            // X0
            String inX0 = JOptionPane.showInputDialog(null, "Ingrese la semilla (X0):", "Paso 1", JOptionPane.QUESTION_MESSAGE);
            if(inX0==null) return;
            int x0 = Integer.parseInt(inX0);

            // a
            String inA = JOptionPane.showInputDialog(null, "Ingrese el multiplicador (a):", "Paso 2", JOptionPane.QUESTION_MESSAGE);
            if(inA==null) return;
            int a = Integer.parseInt(inA);

            // c
            String inC = JOptionPane.showInputDialog(null, "Ingrese la constante aditiva (c):", "Paso 3", JOptionPane.QUESTION_MESSAGE);
            if(inC==null) return;
            int c = Integer.parseInt(inC);

            // m
            String inM = JOptionPane.showInputDialog(null, "Ingrese el módulo (m): ", "Paso 4", JOptionPane.QUESTION_MESSAGE);
            if(inM==null) return;
            int m = Integer.parseInt(inM);

            // --- VALIDACIONES DEL TEOREMA DE HULL-DOBELL ---

            // 0. Validación básica m > 0
            if (m <= 0) {
                JOptionPane.showMessageDialog(null, "Error: El módulo (m).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 1. c y m deben ser primos relativos (MCD = 1)
            if (calcularMCD(c, m) != 1) {
                JOptionPane.showMessageDialog(null,
                                "'c' y 'm' NO son primos relativos.\n" +
                                "MCD(" + c + ", " + m + ") != 1.\n" +
                                "Esto evitará que se genere un periodo completo.",
                        "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Si q es factor primo de m, entonces a-1 debe ser múltiplo de q
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

            // 3. Si m es múltiplo de 4, entonces a-1 debe ser múltiplo de 4
            // Nota: Interpretamos "4 debe ser múltiplo de a-1" como "(a-1) es divisible por 4" según el teorema estándar.
            if (m % 4 == 0) {
                if (aMenos1 % 4 != 0) {
                    JOptionPane.showMessageDialog(null,
                                    "Como m (" + m + ") es múltiplo de 4,\n" +
                                    "(a-1) = " + aMenos1 + " también debe ser múltiplo de 4.",
                            "Validación Fallida", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Si pasa todas las validaciones:
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
