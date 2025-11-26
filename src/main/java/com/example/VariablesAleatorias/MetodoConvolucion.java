package com.example.VariablesAleatorias;

import javax.swing.JOptionPane;
import static java.lang.Math.log;

public class MetodoConvolucion {

    public static void ejecutar() {

        int k = 0;
        double lambda = 0.0;
        int n = 0;

        // --- 1. VALIDAR PARÁMETROS ---
        while (true) {
            try {
                // k
                String inputK = JOptionPane.showInputDialog(
                        "--- Método de Convolución (Erlang) ---\n\n" +
                                "1. Ingrese k (parámetro entero, número de convoluciones):"
                );
                if (inputK == null) { mostrarCancelacionYSalir(); return; }
                k = Integer.parseInt(inputK);

                if (k <= 0) {
                    JOptionPane.showMessageDialog(null,
                            "Error: k debe ser un entero positivo.",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // lambda
                String inputLambda = JOptionPane.showInputDialog(
                        "2. Ingrese lambda (λ) para la distribución exponencial:"
                );
                if (inputLambda == null) { mostrarCancelacionYSalir(); return; }
                lambda = Double.parseDouble(inputLambda);

                if (lambda <= 0) {
                    JOptionPane.showMessageDialog(null,
                            "Error: lambda debe ser > 0.",
                            "Error de Validación",
                            JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                break;  // parámetros válidos

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Error: ingrese solo números válidos.",
                        "Error de Entrada",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

        // --- 2. PEDIR N ---
        String inputN = JOptionPane.showInputDialog(
                "3. Ingrese el número de valores a generar (N):"
        );
        if (inputN == null) { mostrarCancelacionYSalir(); return; }

        try {
            n = Integer.parseInt(inputN);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Error: N inválido. Regresando al menú.",
                    "Error Fatal",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // --- 3. GENERAR VALORES ERLANG ---
        StringBuilder html = new StringBuilder();

        html.append("<html><body>");
        html.append("<h2>Resultados del Método de Convolución</h2>");
        html.append(String.format("<b>Fórmula:</b> X = suma de %d variables Exp(λ=%.3f)<br><br>", k, lambda));

        html.append("<table border='1'><tr>"
                + "<th>i</th>"
                + "<th>X Generado</th>"
                + "<th>Detalle (sumatoria)</th>"
                + "</tr>");

        for (int i = 1; i <= n; i++) {

            double suma = 0.0;
            StringBuilder detalle = new StringBuilder();

            for (int j = 1; j <= k; j++) {
                double u = CongruencialLineal.siguienteRi();
                double exp = (-1.0 / lambda) * log(u);

                suma += exp;

                detalle.append(String.format("Exp%d=%.4f ", j, exp));
            }

            html.append(String.format(
                    "<tr><td>%d</td><td><b>%.5f</b></td><td>%s</td></tr>",
                    i, suma, detalle
            ));
        }

        html.append("</table></body></html>");

        // --- 4. MOSTRAR RESULTADOS ---
        JOptionPane.showMessageDialog(null,
                html.toString(),
                "Resultados del Método de Convolución (Erlang)",
                JOptionPane.PLAIN_MESSAGE);
    }

    private static void mostrarCancelacionYSalir() {
        JOptionPane.showMessageDialog(null,
                "Operación cancelada. Regresando al menú principal.",
                "Cancelado",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
