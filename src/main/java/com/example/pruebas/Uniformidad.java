package com.example.pruebas;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static com.example.tablas.TablaChiCuadrada.obtenerValorCritico;

public class Uniformidad {
    public static void realizarPruebaUniformidad(ArrayList<Double> datos) {
        int n = datos.size();

        // 1. Definir número de intervalos (m).
        // Regla común: m = raíz cuadrada de n.
        // Si n es muy pequeño (ej. 10), forzamos mínimo 5 intervalos para que la prueba tenga sentido.
        int m = (int) Math.sqrt(n);
        if (m < 5) m = 5;

        double anchoIntervalo = 1.0 / m;
        int[] frecuenciaObservada = new int[m];
        double frecuenciaEsperada = (double) n / m; // En uniformidad, se espera lo mismo en todos

        // 2. Contar frecuencias observadas (En qué "cubeta" cae cada número)
        for (double ri : datos) {
            int indice = (int) (ri / anchoIntervalo);
            if (indice >= m) indice = m - 1; // Para el caso raro de que ri sea exactamente 1.0
            frecuenciaObservada[indice]++;
        }

        // 3. Construir la tabla de resultados y calcular Chi-Cuadrada
        StringBuilder reporte = new StringBuilder();
        reporte.append("--- PRUEBA DE UNIFORMIDAD (CHI-CUADRADA) ---\n");
        reporte.append(String.format("Datos (n): %d | Intervalos (m): %d | Esp. (E): %.2f\n\n", n, m, frecuenciaEsperada));
        reporte.append(String.format("%-15s %-10s %-10s %-15s\n", "Intervalo", "Obs(O)", "Esp(E)", "(O-E)²/E"));
        reporte.append("----------------------------------------------------\n");

        double chiCuadradaCalculada = 0.0;

        for (int i = 0; i < m; i++) {
            double limiteInf = i * anchoIntervalo;
            double limiteSup = (i + 1) * anchoIntervalo;

            // Fórmula: (O - E)² / E
            double termino = Math.pow(frecuenciaObservada[i] - frecuenciaEsperada, 2) / frecuenciaEsperada;
            chiCuadradaCalculada += termino;

            String intervaloStr = String.format("[%.2f - %.2f)", limiteInf, limiteSup);
            reporte.append(String.format("%-15s %-10d %-10.2f %-15.4f\n",
                    intervaloStr, frecuenciaObservada[i], frecuenciaEsperada, termino));
        }

        reporte.append("----------------------------------------------------\n");
        reporte.append(String.format("Chi-Cuadrada Calculada: %.4f\n", chiCuadradaCalculada));

        // 4. Obtener valor crítico de tabla (Grados de libertad = m - 1)
        int gradosLibertad = m - 1;
        double valorCritico = obtenerValorCritico(gradosLibertad);

        reporte.append(String.format("Valor Crítico (Tabla, gl=%d): %.4f\n\n", gradosLibertad, valorCritico));

        // 5. Conclusión
        if (chiCuadradaCalculada < valorCritico) {
            reporte.append("CONCLUSIÓN: NO SE RECHAZA H0.\nLos números PARECEN UNIFORMES.");
        } else {
            reporte.append("CONCLUSIÓN: SE RECHAZA H0.\nLos números NO SON UNIFORMES.");
        }

        // 6. Mostrar resultado
        JTextArea textArea = new JTextArea(reporte.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(null, new JScrollPane(textArea), "Resultados Chi-Cuadrada", JOptionPane.INFORMATION_MESSAGE);
    }


}
