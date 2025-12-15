package com.example.metodos;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.example.pruebas.Aleatoridad.realizarPruebaAleatoriedad;
import static com.example.pruebas.Independencia.realizarPruebaIndependenciaPoker;
import static com.example.pruebas.Uniformidad.realizarPruebaUniformidad;

public class CongurencialMultiplicativo {
    public static void congruencialMultiplicativo(int x0, int a, int m) {
        StringBuilder salida = new StringBuilder();
        ArrayList<Double> listaRi = new ArrayList<>();

        salida.append("Parámetros: X0=" + x0 + ", a=" + a + ", m=" + m + "\n");
        salida.append(String.format("%-6s %-10s %-15s %-15s %-10s\n",
                "i", "Xi", "a * Xi", "Xi+1 (Mod)", "Ri"));
        salida.append("------------------------------------------------------------------\n");

        int xi = x0;
        int i = 0;
        int limiteSeguridad = 50000;

        do {
            long producto = (long) a * xi;
            int xiSiguiente = (int) (producto % m);
            double ri = (double) xiSiguiente / (m - 1);

            listaRi.add(ri);

            salida.append(String.format("%-6d %-10d %-15d %-15d %.4f\n",
                    i, xi, producto, xiSiguiente, ri));

            xi = xiSiguiente;
            i++;

            // Condición de parada: Fin del ciclo (Periodo)
            if (xi == x0) {
                salida.append("\n[FIN] Periodo completado. Volvimos a la semilla " + x0 + ".\n");
                salida.append("Total de números generados (Periodo): " + i + "\n");
                break;
            }

            if (i >= limiteSeguridad) {
                salida.append("\n[!] Alerta: Límite de seguridad visual alcanzado.\n");
                break;
            }

        } while (true);

        // --- Componente Visual ---
        JTextArea textArea = new JTextArea(salida.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(650, 400));

        Object[] opciones = {"Uniformidad", "Aleatoriedad", "Prueba de Póker", "Salir"};
        int seleccion = -1;

        do {
            seleccion = JOptionPane.showOptionDialog(
                    null, scrollPane, "Resultados - Periodo Completo",
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

    // --- Función Auxiliar: Algoritmo de Euclides para MCD ---
    public static int calcularMCD(int a, int b) {
        while (b != 0) {
            int temporal = b;
            b = a % b;
            a = temporal;
        }
        return a;
    }

    public static void main(String[] args) {
        try {
            // 1. Pedir Semilla (X0)
            String inputX0 = JOptionPane.showInputDialog(null,
                    "Ingrese la semilla inicial (X0):\n(Debe ser impar)",
                    "Paso 1: Semilla", JOptionPane.QUESTION_MESSAGE);
            if (inputX0 == null) return;
            int x0 = Integer.parseInt(inputX0);

            // Validar que X0 sea impar (Recomendación fuerte)
            if (x0 % 2 == 0) {
                JOptionPane.showMessageDialog(null,
                        "Advertencia: X0 es par. El periodo será muy corto.",
                        "Aviso", JOptionPane.WARNING_MESSAGE);
            }

            // 2. Pedir Multiplicador (a)
            String inputA = JOptionPane.showInputDialog(null,
                    "Ingrese el multiplicador (a):",
                    "Paso 2: Multiplicador", JOptionPane.QUESTION_MESSAGE);
            if (inputA == null) return;
            int a = Integer.parseInt(inputA);

            // 3. Pedir 'd' para calcular m = 2^d
            int d = 0;
            boolean dValido = false;

            while (!dValido) {
                String inputD = JOptionPane.showInputDialog(null,
                        "Ingrese el valor de 'd' (para m = 2^d):\nNota: d debe ser mayor a 3.",
                        "Paso 3: Exponente", JOptionPane.QUESTION_MESSAGE);
                if (inputD == null) return;

                d = Integer.parseInt(inputD);

                if (d > 3) {
                    dValido = true;
                } else {
                    JOptionPane.showMessageDialog(null,
                            "Error: El valor de 'd' debe ser estrictamente mayor a 3.",
                            "Valor inválido", JOptionPane.ERROR_MESSAGE);
                }
            }

            // Calcular m y verificar primos relativos
            int m = (int) Math.pow(2, d);
            int mcd = calcularMCD(a, m);

            if (mcd != 1) {
                JOptionPane.showMessageDialog(null,
                        "ERROR CRÍTICO:\n" +
                                "El multiplicador 'a' (" + a + ") y el módulo 'm' (" + m + ") NO son primos relativos.\n" +
                                "Su Máximo Común Divisor es: " + mcd + ".\n\n" +
                                "El generador no es válido.",
                        "Falla de Primos Relativos", JOptionPane.ERROR_MESSAGE);
                return; // Detener el programa
            }

            // Mostrar confirmación antes de ejecutar
            JOptionPane.showMessageDialog(null,
                    "Datos Validados:\n" +
                            "m = 2^" + d + " = " + m + "\n" +
                            "MCD(a, m) = 1 (Son primos relativos)\n\n" +
                            "Ejecutando generador...",
                    "Validación Exitosa", JOptionPane.INFORMATION_MESSAGE);

            // Ejecutar algoritmo
            congruencialMultiplicativo(x0, a, m);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese solo números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
