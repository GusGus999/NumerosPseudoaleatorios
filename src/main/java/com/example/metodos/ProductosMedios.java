// Declaración del paquete que coincide con tu estructura de carpetas
package com.example.metodos;

import javax.swing.*;
import java.awt.*;

/**
 * Implementación del método de Productos Medios para generar números pseudoaleatorios.
 * La lógica se puede iniciar llamando al método iniciar().
 *
 * @author Gemini & BrayanGGX
 */
public class ProductosMedios {

    /**
     * Método principal que sirve como punto de entrada para probar esta clase de forma independiente.
     */
    public static void main(String[] args) {
        // Crea una instancia de la clase y llama al método que inicia el proceso.
        ProductosMedios generador = new ProductosMedios();
        generador.iniciar();
    }

    /**
     * Inicia el proceso de solicitud de datos al usuario y generación de números.
     * Este método no es estático, por lo que puede ser llamado desde un objeto.
     * Ejemplo: new ProductosMedios().iniciar();
     */
    public void iniciar() {
        long x0, x1;
        int D, iteraciones;

        try {
            // --- Paso 1: Obtener y validar las semillas y las iteraciones ---
            String seed1Str = pedirDato("Introduce la primera semilla (x0):", "Método de Productos Medios");
            if (seed1Str == null) return; // El usuario canceló
            x0 = Long.parseLong(seed1Str);
            if (x0 <= 0) throw new NumberFormatException("La semilla debe ser un número positivo.");
            D = seed1Str.length();

            String seed2Str = pedirDato("Introduce la segunda semilla (x1):\n(Debe tener " + D + " dígitos)", "Método de Productos Medios");
            if (seed2Str == null) return; // El usuario canceló
            if (seed2Str.length() != D) throw new NumberFormatException("La segunda semilla debe tener " + D + " dígitos.");
            x1 = Long.parseLong(seed2Str);

            String iteracionesStr = pedirDato("Introduce el número de valores a generar (N):", "Método de Productos Medios");
            if (iteracionesStr == null) return; // El usuario canceló
            iteraciones = Integer.parseInt(iteracionesStr);
            if (iteraciones <= 0) throw new NumberFormatException("El número de iteraciones debe ser mayor que cero.");

        } catch (NumberFormatException e) {
            // Si ocurre cualquier error en la conversión o validación, muestra el mensaje y termina.
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error de entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- Paso 2: Generar los números y construir la tabla de resultados ---
        generarYMostrarResultados(x0, x1, D, iteraciones);
    }

    /**
     * Muestra un diálogo para pedir un dato al usuario.
     * @param mensaje El mensaje a mostrar en el diálogo.
     * @param titulo El título de la ventana del diálogo.
     * @return El String introducido por el usuario, o null si cancela.
     */
    private String pedirDato(String mensaje, String titulo) {
        return JOptionPane.showInputDialog(null, mensaje, titulo, JOptionPane.QUESTION_MESSAGE);
    }


    /**
     * Realiza el algoritmo de productos medios y muestra los resultados en una ventana.
     * @param x0 Semilla inicial 1.
     * @param x1 Semilla inicial 2.
     * @param D Número de dígitos de las semillas.
     * @param N Número de iteraciones a realizar.
     */
    private void generarYMostrarResultados(long x0, long x1, int D, int N) {
        StringBuilder resultados = new StringBuilder();
        resultados.append(String.format("%-5s | %-10s | %-10s | %-18s | %-10s | %-10s\n",
                "y(i)", "n1", "n2", "Resultado (n1*n2)", "x(i)", "r(i)"));
        resultados.append(new String(new char[80]).replace('\0', '-')).append("\n");

        long current_x0 = x0;
        long current_x1 = x1;
        double divisor = Math.pow(10, D);

        for (int i = 0; i < N; i++) {
            long producto = current_x0 * current_x1;
            String productoStr = String.valueOf(producto);

            int longitudDeseada = 2 * D;
            while (productoStr.length() < longitudDeseada) {
                productoStr = "0" + productoStr;
            }

            int startIndex = (productoStr.length() - D) / 2;
            String x_str = productoStr.substring(startIndex, startIndex + D);
            long nuevo_x = Long.parseLong(x_str);
            double r_i = nuevo_x / divisor;

            resultados.append(String.format("%-5s | %-10d | %-10d | %-18d | %-10d | %-10.4f\n",
                    "y" + i, current_x0, current_x1, producto, nuevo_x, r_i));

            current_x0 = current_x1;
            current_x1 = nuevo_x;
        }

        // Muestra los resultados en un JTextArea con Scroll
        JTextArea textArea = new JTextArea(resultados.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 400));

        JOptionPane.showMessageDialog(null, scrollPane, "Resultados del Método de Productos Medios", JOptionPane.INFORMATION_MESSAGE);
    }
}