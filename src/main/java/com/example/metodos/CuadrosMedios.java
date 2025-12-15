package com.example.metodos;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.example.pruebas.Aleatoridad.realizarPruebaAleatoriedad;
import static com.example.pruebas.Independencia.realizarPruebaIndependenciaPoker;
import static com.example.pruebas.Uniformidad.realizarPruebaUniformidad;

public class CuadrosMedios {
    public static void cuadradosMedios(int y0, int n) {
        StringBuilder salida = new StringBuilder();
        ArrayList<Double> listaRi = new ArrayList<>();

        salida.append(String.format("%-4s %-8s %-12s %-25s %-8s %-8s\n",
                "i", "Yi", "Yi²", "Yi²(Ceros izq)", "Xi", "Ri"));
        salida.append("---------------------------------------------------------------------------------\n");

        int yi = y0;

        for (int i = 0; i < n; i++) {
            long yiCuadrado = (long) Math.pow(yi, 2);
            String yiCuadradoStr = String.format("%08d", yiCuadrado);

            int xi = 0;
            if (yiCuadradoStr.length() >= 6) {
                xi = Integer.parseInt(yiCuadradoStr.substring(2, 6));
            } else {
                xi = Integer.parseInt(yiCuadradoStr);
            }

            double ri = xi / 10000.0;
            listaRi.add(ri);

            salida.append(String.format("%-4d %-8d %-12d %-25s %-8d %.4f\n",
                    i, yi, yiCuadrado, yiCuadradoStr, xi, ri));

            yi = xi;
        }

        // Preparamos el componente visual
        JTextArea textArea = new JTextArea(salida.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        // IMPORTANTE: Creamos el ScrollPane una sola vez para reutilizarlo
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(600, 400));

        // Agregamos una opción extra "Salir" al final
        Object[] opciones = {"Uniformidad", "Aleatoriedad", "Prueba de Póker", "Salir"};

        int seleccion = -1;

        do {
            // Mostramos el diálogo dentro del ciclo
            seleccion = JOptionPane.showOptionDialog(
                    null,
                    scrollPane, // Reutilizamos la tabla ya generada
                    "Resultados - Seleccione prueba estadística",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    opciones,
                    opciones[0]);

            switch (seleccion) {
                case 0:
                    realizarPruebaUniformidad(listaRi);
                    break;
                case 1:
                    realizarPruebaAleatoriedad(listaRi);
                    break;
                case 2:
                    realizarPruebaIndependenciaPoker(listaRi);
                    break;
                case 3:
                    System.out.println("Saliendo del menú de pruebas...");
                    break;
                default:
                    // Caso si cierran con la X (-1)
                    break;
            }
        } while (seleccion != 3 && seleccion != -1);
    }

    public static void main(String[] args) {
        try {
            // 1. Pedir la semilla (y0)
            String inputY0 = JOptionPane.showInputDialog(null,
                    "Ingrese la semilla inicial (y0):\n(Se recomienda de 4 dígitos)",
                    "Entrada de Datos",
                    JOptionPane.QUESTION_MESSAGE);

            // Si el usuario da click en Cancelar, inputY0 será null
            if (inputY0 == null) return;

            int semilla = Integer.parseInt(inputY0);

            // 2. Pedir la cantidad de iteraciones (n)
            String inputN = JOptionPane.showInputDialog(null,
                    "Ingrese la cantidad de números a generar (n):",
                    "Entrada de Datos",
                    JOptionPane.QUESTION_MESSAGE);

            if (inputN == null) return;

            int n = Integer.parseInt(inputN);

            // 3. Llamar al metodo con los datos ingresados
            cuadradosMedios(semilla, n);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Error: Por favor ingrese solo números enteros válidos.",
                    "Error de formato",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


}