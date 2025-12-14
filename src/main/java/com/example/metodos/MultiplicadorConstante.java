package com.example.metodos;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;

import static com.example.pruebas.Aleatoridad.realizarPruebaAleatoriedad;
import static com.example.pruebas.Independencia.realizarPruebaIndependenciaPoker;
import static com.example.pruebas.Uniformidad.realizarPruebaUniformidad;

public class MultiplicadorConstante {
    public static void main(String[] args) {
        int x0 = 9803;
        int a = 6965;
        int n = 10;

        multiplicadorConstante(a, x0, n);
    }

    public static void multiplicadorConstante(int a, int x0, int n) {
        // Validación inicial en ventana en lugar de consola
        if (String.valueOf(x0).length() != 4) {
            JOptionPane.showMessageDialog(null, "La semilla (X0) debe ser de 4 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 1. Preparar estructuras de datos
        StringBuilder salida = new StringBuilder();
        ArrayList<Double> listaRi = new ArrayList<>();

        // Encabezados con formato alineado
        salida.append(String.format("%-4s %-6s %-6s %-12s %-12s %-8s %-8s\n",
                "i", "a", "Xi", "a * Xi", "Producto(8d)", "Centro", "Ri"));
        salida.append("---------------------------------------------------------------------------------\n");

        int xi = x0;

        // 2. Ciclo de Generación
        for (int i = 0; i < n; i++) {
            long producto = (long) a * xi;

            // Convertir a String
            String productoStr = String.valueOf(producto);

            // Rellenar con ceros a la izquierda hasta tener 8 dígitos (lógica estándar)
            // Usamos String.format para ser más eficientes que el while
            String productoFormateado = String.format("%08d", producto);

            // Si el producto supera 8 dígitos (raro si a y x0 son 4 digitos), tomamos los últimos 8 o ajustamos
            if (productoFormateado.length() > 8) {
                productoFormateado = productoFormateado.substring(productoFormateado.length() - 8);
            }

            // Extracción de los 4 dígitos centrales (posiciones 2 a 6 en base 0)
            String digitosCentrales = productoFormateado.substring(2, 6);

            int siguiente = Integer.parseInt(digitosCentrales);
            double ri = siguiente / 10000.0;

            // Guardar en lista para las pruebas
            listaRi.add(ri);

            // Agregar a la tabla visual
            salida.append(String.format("%-4d %-6d %-6d %-12d %-12s %-8s %.4f\n",
                    i, a, xi, producto, productoFormateado, digitosCentrales, ri));

            xi = siguiente;
        }

        // 3. Preparar componente visual (Reutilizable)
        JTextArea textArea = new JTextArea(salida.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(650, 400)); // Un poco más ancho

        // 4. Ciclo del Menú de Pruebas
        Object[] opciones = {"Uniformidad", "Aleatoriedad", "Prueba de Póker", "Salir"};
        int seleccion = -1;

        do {
            seleccion = JOptionPane.showOptionDialog(
                    null,
                    scrollPane,
                    "Resultados - Multiplicador Constante",
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
                    realizarPruebaIndependenciaPoker(listaRi); // Asumiendo que ya tienes este metodo del paso anterior
                    break;
                case 3:
                    System.out.println("Saliendo...");
                    break;
                default:
                    break;
            }
        } while (seleccion != 3 && seleccion != -1);
    }


}
