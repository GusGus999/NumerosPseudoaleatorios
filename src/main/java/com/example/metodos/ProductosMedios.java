
package com.example.metodos;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.example.pruebas.Aleatoridad.realizarPruebaAleatoriedad;
import static com.example.pruebas.Independencia.realizarPruebaIndependenciaPoker;
import static com.example.pruebas.Uniformidad.realizarPruebaUniformidad;


public class ProductosMedios {

    public static void productosMedios(int x0, int x1, int n) {
        // Validación inicial
        if (String.valueOf(x0).length() < 3 || String.valueOf(x1).length() < 3) {
            JOptionPane.showMessageDialog(null,
                    "Advertencia: Las semillas deben tener preferiblemente 4 dígitos.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        // 1. Preparar estructuras
        StringBuilder salida = new StringBuilder();
        ArrayList<Double> listaRi = new ArrayList<>();

        // Encabezados
        salida.append(String.format("%-4s %-6s %-6s %-12s %-12s %-6s %-8s\n",
                "i", "X(i)", "X(i+1)", "Producto", "Prod(8d)", "Centro", "Ri"));
        salida.append("----------------------------------------------------------------------\n");

        int semilla1 = x0;
        int semilla2 = x1;

        // 2. Ciclo de Generación
        for (int i = 0; i < n; i++) {
            long producto = (long) semilla1 * semilla2;

            // Formatear a 8 dígitos rellenando con ceros a la izquierda
            String productoStr = String.format("%08d", producto);

            // Manejo de seguridad por si el producto es muy pequeño o muy grande
            String digitosCentrales;
            if (productoStr.length() >= 6) {
                // Extraer posiciones 2,3,4,5 (centrales de 8 dígitos)
                digitosCentrales = productoStr.substring(2, 6);
            } else {
                // Caso borde (si el número colapsa a 0)
                digitosCentrales = "0000";
            }

            int siguienteX = Integer.parseInt(digitosCentrales);
            double ri = siguienteX / 10000.0;

            // Guardar para estadísticas
            listaRi.add(ri);

            // Agregar a la tabla visual
            salida.append(String.format("%-4d %-6d %-6d %-12d %-12s %-6s %.4f\n",
                    i, semilla1, semilla2, producto, productoStr, digitosCentrales, ri));

            // Actualizar semillas:
            // La semilla1 pasa a ser la vieja semilla2
            // La semilla2 pasa a ser el nuevo número generado
            semilla1 = semilla2;
            semilla2 = siguienteX;
        }

        // 3. Preparar componente visual
        JTextArea textArea = new JTextArea(salida.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new java.awt.Dimension(650, 400));

        // 4. Ciclo del Menú de Pruebas
        Object[] opciones = {"Uniformidad", "Aleatoriedad", "Prueba de Póker", "Salir"};
        int seleccion = -1;

        do {
            seleccion = JOptionPane.showOptionDialog(
                    null,
                    scrollPane,
                    "Resultados - Productos Medios",
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
                    System.out.println("Saliendo...");
                    break;
                default:
                    break;
            }
        } while (seleccion != 3 && seleccion != -1);
    }

}