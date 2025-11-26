package com.example.VariablesAleatorias;

import javax.swing.JOptionPane;
import static java.lang.Math.log;

public class MetodoComposicion {

    public static void main(String[] args) {


        int n;
        double P1, P2, LAMBDA1, LAMBDA2;

        // --- 1. SOLICITAR PARÁMETROS EN VENTANAS (INPUT) ---
        try {
            // P1
            P1 = Double.parseDouble(JOptionPane.showInputDialog(
                    "--- Método de Composición (f(x) = P1*Exp(λ1) + P2*Exp(λ2)) ---\n\n" +
                            "1. Ingrese la probabilidad P1 para la primera componente:"
            ));

            // LAMBDA 1
            LAMBDA1 = Double.parseDouble(JOptionPane.showInputDialog(
                    "2. Ingrese la Tasa (Lambda 1) para la primera Exponencial (Exp(λ1)): "
            ));

            // LAMBDA 2
            LAMBDA2 = Double.parseDouble(JOptionPane.showInputDialog(
                    "3. Ingrese la Tasa (Lambda 2) para la segunda Exponencial (Exp(λ2)): "
            ));

            // P2
            P2 = Double.parseDouble(JOptionPane.showInputDialog(
                    "4. Ingrese la probabilidad P2 para la segunda componente (¡P1 + P2 debe sumar 1!):"
            ));

            // N (ÚLTIMO VALOR)
            n = Integer.parseInt(JOptionPane.showInputDialog(
                    "5. Ingrese el número de valores de X a generar (N):"
            ));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese solo números válidos.", "Error de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- 2. VALIDACIÓN ---
        if (Math.abs(P1 + P2 - 1.0) > 1e-6) {
            JOptionPane.showMessageDialog(null,
                    String.format("Error: P1 (%.4f) + P2 (%.4f) = %.4f.\nLa suma debe ser 1.0", P1, P2, P1 + P2),
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- 3. EJECUTAR SIMULACIÓN Y CONSTRUIR MENSAJE DE SALIDA ---
        StringBuilder resultadoHTML = new StringBuilder();

        resultadoHTML.append("<html><body>");
        resultadoHTML.append("<h2>Resultados del Método de Composición</h2>");
        resultadoHTML.append(String.format("<b>Fórmula:</b> f(x) = %.2f·Exp(%.2f) + %.2f·Exp(%.2f)<br><br>", P1, LAMBDA1, P2, LAMBDA2));

        // ENCABEZADOS DE LA TABLA: i, X Generado y Origen/Método
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

            // Selección de componente (Usa R1, un número Uniforme, para decidir el método)
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
            // Muestra: Iteración (i), el valor final (X) y el origen/método (la Exponencial seleccionada)
            resultadoHTML.append(String.format(
                    "<tr><td>%d</td><td><b>%.4f</b></td><td>%s</td></tr>",
                    i, x, componenteUsado
            ));
        }

        resultadoHTML.append("</table></body></html>");

        // --- 4. MOSTRAR EL RESULTADO COMPLETO EN VENTANA ---
        JOptionPane.showMessageDialog(null, resultadoHTML.toString(), "Resultados de Simulación (Método de Composición)", JOptionPane.PLAIN_MESSAGE);
    }
}