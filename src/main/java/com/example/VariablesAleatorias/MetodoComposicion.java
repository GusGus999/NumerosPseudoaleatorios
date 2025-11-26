package com.example.VariablesAleatorias;

import javax.swing.JOptionPane;
import static java.lang.Math.log;

public class MetodoComposicion {

    public static void ejecutar() {

        // Declaración e inicialización de variables
        int n = 0;
        double P1 = 0.0, P2 = 0.0, LAMBDA1 = 0.0, LAMBDA2 = 0.0;
        boolean parametrosProbabilidadValidos = false;

        // --- 1. SOLICITAR Y VALIDAR PARÁMETROS EN CICLO ---
        // Este ciclo se repite hasta que P1 + P2 = 1 O el usuario CANCELA
        while (!parametrosProbabilidadValidos) {
            String input; // Variable para almacenar la entrada del usuario

            try {
                // --- 1. P1 ---
                input = JOptionPane.showInputDialog(
                        "--- Método de Composición (f(x) = P1*Exp(λ1) + P2*Exp(λ2)) ---\n\n" +
                                "1. Ingrese la probabilidad P1 para la primera componente:"
                );
                if (input == null) { mostrarCancelacionYSalir(); return; } // CANCELAR: Muestra mensaje y sale
                P1 = Double.parseDouble(input);

                // --- 2. LAMBDA 1 ---
                input = JOptionPane.showInputDialog(
                        "2. Ingrese la Tasa (Lambda 1) para la primera Exponencial (Exp(λ1)): "
                );
                if (input == null) { mostrarCancelacionYSalir(); return; } // CANCELAR: Muestra mensaje y sale
                LAMBDA1 = Double.parseDouble(input);

                // --- 3. LAMBDA 2 ---
                input = JOptionPane.showInputDialog(
                        "3. Ingrese la Tasa (Lambda 2) para la segunda Exponencial (Exp(λ2)): "
                );
                if (input == null) { mostrarCancelacionYSalir(); return; } // CANCELAR: Muestra mensaje y sale
                LAMBDA2 = Double.parseDouble(input);

                // --- 4. P2 ---
                input = JOptionPane.showInputDialog(
                        "4. Ingrese la probabilidad P2 para la segunda componente (¡P1 + P2 debe sumar 1!):"
                );
                if (input == null) { mostrarCancelacionYSalir(); return; } // CANCELAR: Muestra mensaje y sale
                P2 = Double.parseDouble(input);


                // --- VALIDACIÓN DE SUMA ---
                if (Math.abs(P1 + P2 - 1.0) > 1e-6) {
                    // Muestra error y el ciclo while se repite
                    JOptionPane.showMessageDialog(null,
                            String.format("Error: P1 (%.4f) + P2 (%.4f) = %.4f.\nLa suma debe ser 1.0. Intente de nuevo.", P1, P2, P1 + P2),
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // La suma es correcta. Salimos del ciclo de validación.
                    parametrosProbabilidadValidos = true;
                }

            } catch (NumberFormatException e) {
                // Si el usuario ingresa texto (no es un número), muestra error y el ciclo se repite
                JOptionPane.showMessageDialog(null,
                        "Error: Ingrese solo números válidos. Intente de nuevo.",
                        "Error de Entrada",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // --- 2. SOLICITAR N (Solo se pide una vez, después de la validación) ---
        String inputN = JOptionPane.showInputDialog(
                "5. Ingrese el número de valores de X a generar (N):"
        );
        if (inputN == null) { mostrarCancelacionYSalir(); return; } // CANCELAR: Muestra mensaje y sale

        try {
            n = Integer.parseInt(inputN);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Número de valores (N) inválido. Regresando al menú principal.", "Error Fatal", JOptionPane.ERROR_MESSAGE);
            return;
        }


        // --- 3. EJECUTAR SIMULACIÓN Y CONSTRUIR MENSAJE DE SALIDA ---
        StringBuilder resultadoHTML = new StringBuilder();

        resultadoHTML.append("<html><body>");
        resultadoHTML.append("<h2>Resultados del Método de Composición</h2>");
        resultadoHTML.append(String.format("<b>Fórmula:</b> f(x) = %.2f·Exp(%.2f) + %.2f·Exp(%.2f)<br><br>", P1, LAMBDA1, P2, LAMBDA2));

        // ENCABEZADOS DE LA TABLA
        resultadoHTML.append("<table border='1'><tr>"
                + "<th>Iteración (i)</th>"
                + "<th>X Generado (Valor Final)</th>"
                + "<th>Origen (Componente)</th>"
                + "</tr>");

        for (int i = 1; i <= n; i++) {

            double u = CongruencialLineal.siguienteRi(); // R1
            double x = 0.0;
            String componenteUsado = "";
            double lambda_usada = 0.0;

            // Selección de componente
            if (u <= P1) {
                lambda_usada = LAMBDA1;
                componenteUsado = String.format("Exponencial (λ=%.2f)", lambda_usada);
            } else {
                lambda_usada = LAMBDA2;
                componenteUsado = String.format("Exponencial (λ=%.2f)", lambda_usada);
            }

            double v = CongruencialLineal.siguienteRi(); // R2

            // Cálculo de X (Transformada Inversa)
            x = (-1.0 / lambda_usada) * log(v);

            // FILA DE SALIDA
            resultadoHTML.append(String.format(
                    "<tr><td>%d</td><td><b>%.4f</b></td><td>%s</td></tr>",
                    i, x, componenteUsado
            ));
        }

        resultadoHTML.append("</table></body></html>");

        // --- 4. MOSTRAR EL RESULTADO COMPLETO EN VENTANA ---
        JOptionPane.showMessageDialog(null, resultadoHTML.toString(), "Resultados de Simulación (Método de Composición)", JOptionPane.PLAIN_MESSAGE);
    }

    // Método auxiliar para mostrar el mensaje de cancelación
    private static void mostrarCancelacionYSalir() {
        JOptionPane.showMessageDialog(null, "Operación cancelada por el usuario. Regresando al menú principal.", "Cancelado", JOptionPane.INFORMATION_MESSAGE);
    }
}